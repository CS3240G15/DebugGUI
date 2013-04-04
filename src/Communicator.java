package edu.virginia.RobotController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
/*import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;*/

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class Communicator {

	private static BTConnection connection = null;
	private static int currentSequenceNumber = 1;
	private static DataInputStream inputStream = null;
	private static int transmitCount = 0;

	/**
	 * Calls the blocking method Bluetooth.waitForConnect() to establish a connection
	 * @return the signal strength of the connection
	 * @throws Exception 
	 */
	public static int establishConnection() throws Exception {
		connection = Bluetooth.waitForConnection();
		if (connection == null)
			throw new Exception("Unable to establish bluetooth connection");
		currentSequenceNumber = 1;
		return connection.getSignalStrength();
	}

	/**
	 * Gets the signal strength of the bluetooth connection
	 * @return the signal strength
	 * @throws Exception
	 */
	public static int getSignalStrength() throws Exception {
		//if the connection hasn't been established, try to re-establish it
		if (connection == null) {
			establishConnection();
		}
		return connection.getSignalStrength();
	}

	/**
	 * Closes the streams, and then the bluetooth connection
	 * @return whether the connection was successfully closed
	 */
	public static boolean destroyConnection() throws Exception {
		if (connection == null)
			return true;
		connection.closeStream();
		connection.close();
		int signalStrength = -999;
		try {
			signalStrength = connection.getSignalStrength();
		} catch (Exception e) {
			if (signalStrength != -999)
				return false;
			return true;
		}
		return true;
	}

	/**
	 * Converts the message to a byte array
	 * 	adding the sequence number and checksum at the front
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
		int length = message.length();
		length += String.valueOf(currentSequenceNumber).length();
		length += String.valueOf(length).length();
		length += 2; //accounts for spaces between sequence number and checksum 
						//and checksum and the rest of the message

		StringBuilder builder = new StringBuilder();
		builder.append(String.valueOf(currentSequenceNumber));
		currentSequenceNumber++;
		builder.append(" ").append(String.valueOf(length)).append(" ");
		builder.append(message);

		return (builder.toString().getBytes());
	}

	/**
	 * Transmits the message passed in
	 * 	Creates a separate thread to wait for an ACK
	 * 	If no ACK is received within 30 seconds, retransmit the message
	 * @param message
	 * @return True if the message was successfully transmitted
	 */
	public static boolean transmitMessage(byte[] message) {
		inputStream = connection.openDataInputStream();
		DataOutputStream outputStream = connection.openDataOutputStream();
		transmitCount++;

		try {
			outputStream.write(message);
			outputStream.flush();
		} catch (IOException e1) {
			return false;
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
			}
		}

		String response = null;
		try {
			if (transmitCount == 10)
				return false;

			/*response = future.get(30, TimeUnit.SECONDS);*/
			String[] ackMessage = {"e","e"};// = response.split("\\s+");
			if (ackMessage.length != 2)
				throw new Exception();
			else if (ackMessage[0]
					.equals(String.valueOf(currentSequenceNumber))
					&& ackMessage[1].equals(String.valueOf(1))) {
				return true;
			} else
				throw new Exception();
		} catch (Exception e) {
			transmitMessage(message);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}

}