import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class GUI_Test extends JPanel {

	public GUI_Test() {
		JTabbedPane tab = new JTabbedPane();
		JPanel panel1 = createSensorPanel("Tab 1 Contains Tooltip and Icon");
		tab.addTab("Sensor Reading", panel1);
		tab.setSelectedIndex(0);
		JPanel panel2 = createDebugPanel("Tab 2 contains icon only");
		tab.addTab("Source Code", panel2);
		setLayout(new GridLayout(1, 1));
		add(tab);
	}

	protected JPanel createSensorPanel(String text) {
		// Getting the pane set up
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		JPanel jplPanel = new JPanel();
		jplPanel.setLayout(new GridBagLayout());
		
		JLabel jlbDisplay = new JLabel(text);
		jlbDisplay.setHorizontalAlignment(JLabel.CENTER);
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridx = 0;
		c.gridwidth = 3;
		c.gridheight = 2;
		jplPanel.add(jlbDisplay, c);

		JButton button = new JButton("Abort");
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 0;
		c.gridheight = 0;
		jplPanel.add(button, c);
		
		button = new JButton("E Stop");
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 0;
		c.gridheight = 0;
		jplPanel.add(button, c);
		return jplPanel;
	}
	
	protected JPanel createDebugPanel(String text) {
		
		JPanel jplPanel = new JPanel();
		JLabel jlbDisplay = new JLabel(text);
		jlbDisplay.setHorizontalAlignment(JLabel.CENTER);
		
		jplPanel.setLayout(new GridLayout(3, 3));
		
		JButton b = new JButton("blah");
		
		jplPanel.add(jlbDisplay);
		jplPanel.add(b);
		return jplPanel;
	}
	
	/*public static void main(String[] args) {
		JFrame frame = new JFrame("Debug GUI");
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(new GUI_Test(),
				BorderLayout.CENTER);
		frame.setSize(400, 125);
		frame.setVisible(true);
	}*/
	
}
