import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel{
	
	private JFrame frame;
	private JPanel debugPanel, sensorPanel;
	
	private void GUI() {
		frame = new JFrame("Debug GUI");
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		frame.getContentPane().add(makeTabSection(), c);
		
		frame.setSize(600, 800);
		frame.setVisible(true);
	}
	
	private JTabbedPane makeTabSection() {
		JTabbedPane pane = new JTabbedPane();
		JPanel panel =  createSensorPanel();
		pane.addTab("Sensor", panel);
		panel = createDebugPanel();
		pane.addTab("Debug", panel);
		return pane;
	}
	
	private JPanel createSensorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0));
		JLabel sensorInfo = new JLabel("Laura Ipsen");
		panel.add(sensorInfo);
		JLabel uplinkInfo = new JLabel("Uplink info");
		panel.add(uplinkInfo);
		return panel;
	}
	
	private JPanel createDebugPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0));
		JLabel sourceCode = new JLabel("Source Code Placeholder text");
		panel.add(sourceCode);
		JLabel debuggingInfo = new JLabel("Single Steps, Breakpoints, etc.");
		panel.add(debuggingInfo);
		return panel;
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI(); // A somewhat awkward way of getting around having to static EVERYTHING.
	}
}
