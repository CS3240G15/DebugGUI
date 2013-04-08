import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;

public class GUI extends JPanel{
	
	private static JFrame frame;
	private static JLabel varSection, errorSection, uplinkSection, sourcecodeSection, breakpointSection, sensorSection;
	private static NXTConnection connection;
	private static DataOutputStream oHandle;
	private static DataInputStream iHandle;
	
	private static void sensorUpdateLoop() {
		// Establish the connection here, for testing purpose, we will use USB connection
		NXTConnection connection = null;
		if (USBtest){
			connection = USB.waitForConnection();
		} else {
			connection = Bluetooth.waitForConnection();
		}

		// Open two data input and output streams for read and write respectively
	    oHandle = connection.openDataOutputStream();
	    iHandle = connection.openDataInputStream();
	    String input = "",output = "";
	    
	    
	}
	
	private static void makeGUI() {
		frame = new JFrame("Debug GUI");
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.NONE;
		//c.weightx = 0.5;
		//c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.ipady = 100;
		frame.getContentPane().add(makeTabSection(), c);
		
		c.ipady = 0;
		c.gridx = 1;
		c.gridheight = 1;
		frame.getContentPane().add(makeAbortButton(), c);
		
		c.gridx = 2;
		frame.getContentPane().add(makeEStopButton(), c);
		
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		frame.getContentPane().add(makeVarSection(), c);
		
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 2;
		frame.getContentPane().add(makeErrorSection(), c);
		
		frame.setSize(800, 400);
		frame.setVisible(true);
		sensorUpdateLoop();
	}
	
	private static JTabbedPane makeTabSection() {
		JTabbedPane pane = new JTabbedPane();
		JPanel sensorPanel =  createSensorPanel();
		pane.addTab("Sensor", sensorPanel);
		pane.setSelectedIndex(0);
		JPanel debugPanel = createDebugPanel();
		pane.addTab("Debug", debugPanel);
		return pane;
	}
	
	private static JPanel createSensorPanel() {
		JPanel panel = new JPanel();
		//panel.setLayout(new GridLayout(1, 0));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 300;
		sensorSection = new JLabel("Laura Ipsen");
		panel.add(sensorSection, c);
		c.gridx = 1;
		c.ipadx = 100;
		uplinkSection = new JLabel("Uplink info");
		panel.add(uplinkSection, c);
		return panel;
	}
	
	private static JPanel createDebugPanel() {
		JPanel panel = new JPanel();
		//panel.setLayout(new GridLayout(1, 0));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 300;
		sourcecodeSection = new JLabel("Ipsen Lauren");
		panel.add(sourcecodeSection, c);
		c.gridx = 1;
		c.ipadx = 100;
		breakpointSection = new JLabel("Breakpoints & stuff");
		panel.add(breakpointSection, c);
		return panel;
	}
	
	private static JButton makeAbortButton() {
		JButton button = new JButton("Abort");
		return button;
	}
	
	private static JButton makeEStopButton() {
		JButton button = new JButton("E Stop");
		return button;
	}
	
	private static JLabel makeVarSection() {
		varSection = new JLabel("Variables and stuff");
		return varSection;
	}
	
	private static JLabel makeErrorSection() {
		errorSection = new JLabel("Errors... and... like... stuff. Man.");
		return errorSection;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Abort")) {
			oHandle.write(("0110").getBytes());
			oHandle.flush();
		}
		else if (e.getActionCommand().equals("E Stop")) {
			oHandle.write(("0101").getBytes());
			oHandle.flush();
		}
		errorSection.setText(e.getActionCommand());
	}
	
	public static void main(String[] args) {
		makeGUI(); // A somewhat awkward way of getting around having to static EVERYTHING.
	}
}
