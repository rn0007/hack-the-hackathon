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

   public TestDialog(String image) {
		setSize(450 , 540);
		// setLocation(1325, 600);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle winSize = ge.getMaximumWindowBounds();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) winSize.getMaxX() - getWidth();
		int y = (int) winSize.getMaxY() - getHeight();
		setLocation(x, y);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		JPanel p = new JPanel();
		URL url = TestDialog.class.getResource("/" + image);
		p.add(new JLabel(new ImageIcon(url)));
		getContentPane().add(p);
	}
}
