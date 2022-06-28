package source;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TestDialog extends JFrame {

   private static final long serialVersionUID = 1L;

   public TestDialog(String image) {
      setSize(450 , 570);
      // setLocation(1325, 600);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
      Rectangle winSize = ge.getMaximumWindowBounds();
      Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
      int x = (int) rect.getMaxX() - getWidth();
      int y = (int) rect.getMaxY() - getHeight();
      setLocation(x, y);

      //setDefaultCloseOperation(EXIT_ON_CLOSE);
      setAlwaysOnTop(true);
      JPanel p = new JPanel();
      URL url = TestDialog.class.getResource("/loading/loading_.gif");
      JLabel jLabel = new JLabel(new ImageIcon(url));
      p.add(jLabel);

      Timer timer = new Timer(5000, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            URL urlImages = TestDialog.class.getResource("/" + image);
            jLabel.setIcon(new ImageIcon(urlImages));
         }
      });
      timer.start();
      getContentPane().add(p);
   }
}
