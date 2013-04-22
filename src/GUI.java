import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel{

	static final long serialVersionUID = 1;
	
	private static JFrame frame;
	private static JLabel varSection, errorSection, uplinkSection, sourcecodeSection, breakpointSection, sensorSection;
	
	private static void sensorUpdateLoop() {
		try {
			System.out.println("Now entering the sensorUpdateLoop");

			while (true) {
				String s = "Something's gone wrong";
				s = Float.toString(Communicator.sendMessage("0011 1")) + "\n";
				s += Float.toString(Communicator.sendMessage("0011 2")) + "\n";
				s += Float.toString(Communicator.sendMessage("0011 3")) + "\n";
				s += Float.toString(Communicator.sendMessage("0011 4"));

				sensorSection.setText(s);

				try {
					System.out.println("starting sleep");
					Thread.sleep(500);
					System.out.println("ending sleep");
				} catch (Exception e) {
					errorSection.setText(errorSection.getText() + "\n" + e.toString());
					System.out.println(e.toString());
				}
			}
		} catch (Exception e) {
			errorSection.setText(errorSection.getText() + "\n" + e.toString());
			errorSection.setText(e.toString());
			System.out.println(e.toString());
		}
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
		try {
			Communicator.establishPCConnection();
		} catch (Exception estbPCConn) {
			errorSection.setText(estbPCConn.toString());
			System.out.println(estbPCConn.toString());
		}
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
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 300;
		sensorSection = new JLabel("Sensor Section");
		panel.add(sensorSection, c);
		c.gridx = 1;
		c.ipadx = 100;
		uplinkSection = new JLabel("Uplink info");
		panel.add(uplinkSection, c);
		return panel;
	}

	private static JPanel createDebugPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 300;
		sourcecodeSection = new JLabel("Source Code");
		panel.add(sourcecodeSection, c);
		c.gridx = 1;
		c.ipadx = 100;
		breakpointSection = new JLabel("Breakpoints & stuff");
		panel.add(breakpointSection, c);
		return panel;
	}

	private static JButton makeAbortButton() {
		JButton button = new JButton("Abort");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					Communicator.sendMessage("0110");
			}
		});
		return button;
	}

	private static JButton makeEStopButton() {
		JButton button = new JButton("E Stop");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					Communicator.sendMessage("0101");
			}
		});
		return button;
	}

	private static JLabel makeVarSection() {
		varSection = new JLabel("Variables Section");
		return varSection;
	}

	private static JLabel makeErrorSection() {
		errorSection = new JLabel("Error Section");
		return errorSection;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("Abort")) {
				Communicator.sendMessage("0110");
			}
			else if (e.getActionCommand().equals("E Stop")) {
				Communicator.sendMessage("0101");
			}
			errorSection.setText(e.getActionCommand());
		} catch (Exception b) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		makeGUI();
	}
}
