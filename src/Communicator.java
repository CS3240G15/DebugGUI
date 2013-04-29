import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Communicator {

	private static int currentSequenceNumber = 1;
	private static DataInputStream dataInputStream = null;
	private static DataOutputStream dataOutputStream = null;
	private static int transmitCount = 0;
	private static NXTComm nxtComm = null;
	private static NXTInfo nxtInfo = null;

	/**
	 * Consolidates all the functions necessary for sending a message into one
	 * @return transmitMessage()'s value
	 * 		   -3 if there was an error
	 * @param String message
	 */
	public static float sendMessage(String message) {
		try {
			return transmitMessage(convertMessageToByteArray(message));
		} catch (Exception sM) {
			System.out.println(sM.toString());
			return -3;
		}
	}
	
	/**
	 * Creates the Bluetooth connection and opens the input/output data streams.
	 *  Uses the NXT's name and address: 001653124655 for group 15, 0016531386B0 for group 16.
	 * @return Whether we were able to establish the connection to the NXT Brick
	 * @throws Exception
	 */
	public static boolean establishPCConnection() throws Exception {
		nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);

		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", "001653124655");
		dataInputStream = new DataInputStream(nxtComm.getInputStream());
		dataOutputStream = new DataOutputStream(nxtComm.getOutputStream());

		return nxtComm.open(nxtInfo);
	}

	/**
	 * Closes the data streams, and then the bluetooth connection.
	 * @return whether the connection was successfully closed
	 */
	public static boolean destroyConnection() throws Exception {
		if (nxtComm == null)
			return true;

		dataInputStream.close();
		dataOutputStream.close();

		nxtComm.getInputStream().close();
		nxtComm.getOutputStream().close();

		nxtComm.close();
		return true;
	}

	/**
	 * Converts the message to a byte array.
	 *  Checks to make sure the message passed in is valid. 
	 *  Calculates the checksum. Increments the sequence number.
	 *  Assembles the message into a packet of the appropriate format: Sequence number, checksum, data.
	 * @param message
	 * @return The message converted to a byte array
	 * @throws Exception 
	 */
	public static byte[] convertMessageToByteArray(String message)
			throws Exception {
		if (message.equals(null) || message.length() == 0)
			throw new Exception("Message is empty");

		if (currentSequenceNumber > 999)
			currentSequenceNumber = 0;

		int checksum = message.length();
		int seqNumLength = 3;
		checksum += seqNumLength;		
		int checksumLength = 3;
		checksum += checksumLength;		
		int spacesInMessage = 2;
		checksum += spacesInMessage; 

		String convertedMessage = "";
		convertedMessage += String.format("%d%d%d", currentSequenceNumber/100, (currentSequenceNumber%100)/10, (currentSequenceNumber%10));
		convertedMessage += " " + String.format("%d%d%d", checksum/100, (checksum%100)/10, (checksum%10));
		convertedMessage += " " + message;
		currentSequenceNumber++;

		return (convertedMessage.getBytes());
	}
	
	public static String getBacklog() {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Task readInputStreamTask = new Task();
		Future<String> future = exec.submit(readInputStreamTask);

		String ret = null;
		try {
			ret = future.get(5, TimeUnit.SECONDS);

		} catch (Exception e1) {
			System.out.println("response timed out");
			ret = "";
		}
		return ret;
	}

	/**
	 * Transmits the message passed in as a parameter.
	 * 	Creates a separate thread to wait for an ACK.
	 * 	If no ACK is received within 30 seconds, retransmits the message.
	 * @param message
	 * @return -1 if unsuccessful
	 * @return -2 is successful and NOT a sensor reading
	 * @return sensor reading if taking a sensor reading
	 */
	public static float transmitMessage(byte[] message) {
		transmitCount++;
		try {
			dataOutputStream.write(message);
			dataOutputStream.flush();
		} catch (IOException e1) {
			return -1;
		}

		ExecutorService exec = Executors.newSingleThreadExecutor();
		Task readInputStreamTask = new Task();
		Future<String> future = exec.submit(readInputStreamTask);

		String response = null;
		try {
			response = future.get(5, TimeUnit.SECONDS);

		} catch (Exception e1) {
			return -1;
		}
		String[] ackMessage = response.split("\\s+");
		String sequenceNumberString = String.format("%d%d%d", 
			(currentSequenceNumber-1)/100, 
			((currentSequenceNumber-1)%100)/10, 
			((currentSequenceNumber-1)%10));
		try {
			if (transmitCount == 10)
				return -1;
			if (ackMessage.length != 2 && ackMessage.length !=3)
				throw new Exception();
			else if (ackMessage[0].equals(sequenceNumberString)
				&& ackMessage[1].trim().equals(String.valueOf(1))) {
				if (ackMessage.length == 3) {
					return Float.parseFloat(ackMessage[2]);
				}
				else
					return -2;
			} else
				throw new Exception();
		} catch (Exception e) {
			transmitMessage(message);
		}

		exec.shutdownNow();
		return -2;
	}


	static class Task implements Callable<String> {

		@SuppressWarnings("unused")
		public String call() throws Exception {
			byte[] message = new byte[11];
			dataInputStream.read(message);
			if (message == null)
				return null;
			return new String(message);
		}
	}
}