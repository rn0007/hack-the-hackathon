package source;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestDialog extends JFrame {

   private static final long serialVersionUID = 1L;

   public TestDialog() {
		setSize(500, 400);
		// setLocation(1325, 600);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX() - getWidth();
		int y = (int) rect.getMaxY() - getHeight();
		setLocation(x, y);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		JPanel p = new JPanel();
		// p.add(new JLabel(new ImageIcon("iron-man-icegif-5.gif")));
		URL url = TestDialog.class.getResource("/iron-man-icegif-5.gif");
		p.add(new JLabel(new ImageIcon(url)));
		getContentPane().add(p);
	}
}
