import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel {

	public GUI() {
		JTabbedPane jtbExample = new JTabbedPane();
		JPanel jplInnerPanel1 = createInnerPanel("Tab 1 Contains Tooltip and Icon");
		jtbExample.addTab("Sensor Reading", jplInnerPanel1);
		jtbExample.setSelectedIndex(0);
		JPanel jplInnerPanel2 = createInnerPanel("Tab 2 contains icon only");
		jtbExample.addTab("Source Code", jplInnerPanel2);
		setLayout(new GridLayout(1, 1));
		add(jtbExample);
	}

	protected JPanel createInnerPanel(String text) {
		JPanel jplPanel = new JPanel();
		JLabel jlbDisplay = new JLabel(text);
		jlbDisplay.setHorizontalAlignment(JLabel.CENTER);
		jplPanel.setLayout(new GridLayout(1, 1));
		jplPanel.add(jlbDisplay);
		return jplPanel;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Debug GUI");
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(new GUI(),
				BorderLayout.CENTER);
		frame.setSize(400, 125);
		frame.setVisible(true);
	}
	
}
