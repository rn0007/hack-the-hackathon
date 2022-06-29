package source;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TestDialog extends JFrame {

   private static final long serialVersionUID = 1L;
   private JButton nextExcerciseButton;
   public static int count = 0;
   public static List<String> imageList = UKGAppService.getImageList();
   Timer timer;
   public static long initTime = 0;

   public TestDialog(String image) {
      count++;

      if(count % 3 == 0) {
         showJoke(image);
         count = 0;
         return;
      }

      setSize(450 , 630);
      setJMenuBar(MenuBarSettings.createMenuBar());
      setIconImage(new ImageIcon(TestDialog.class.getResource("/icon/UKG-Uonly_rgb.png")).getImage());
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
      p.setLayout(new BorderLayout());
      URL url = TestDialog.class.getResource("/loading/loading_.gif");
      JLabel jLabel = new JLabel(new ImageIcon(url));
      p.add(jLabel);

      JPanel bottomPanel = new JPanel();
      JButton next = getNextExcerciseButton(jLabel);		
      bottomPanel.add(next);

      timer = new Timer(5000, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
//            System.out.println("Timer actionPerformed");
            URL urlImages = TestDialog.class.getResource("/" + image);
            jLabel.setIcon(new ImageIcon(urlImages));
            p.add(bottomPanel, BorderLayout.SOUTH);
         }
      });
      timer.start();
      getContentPane().add(p);
   }

   public static int getRandomNumber(){
      Random r = new Random();
      //System.out.println(imageList.size()+" images present");
      return r.nextInt(imageList.size());
   }

   public String getRandomImage(){
      int num = getRandomNumber();
//      System.out.println(num);
//      System.out.println("image : "+ imageList.get(num));
      return imageList.get(num);
   }

   public JButton getNextExcerciseButton(JLabel jLabel) {	

      if (nextExcerciseButton == null) {
         nextExcerciseButton = new JButton();
         nextExcerciseButton.setText("Try Another Excercise");			
         nextExcerciseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {		
               timer.stop();
               long timeDurationForWhichDialogIsOpen = System.currentTimeMillis() - initTime;
               Win32IdleTime.additionallDuration += timeDurationForWhichDialogIsOpen;

               String image = getRandomImage();
//               System.out.println("image"+ image);
               URL urlImages = TestDialog.class.getResource("/" + image);
//               System.out.println("url : "+ urlImages);
               jLabel.setIcon(new ImageIcon(urlImages));
               initTime = System.currentTimeMillis();
            }
         });
      }
      return nextExcerciseButton;
   }	

   private void showJoke(String image) {
      Dimension d = new Dimension();
      d.setSize(450,570);
      setJMenuBar(MenuBarSettings.createMenuBar());
      setIconImage(new ImageIcon(TestDialog.class.getResource("/icon/UKG-Uonly_rgb.png")).getImage());
      //d.setSize(400,400);
      setSize(d);
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
      // p.add(new JLabel(new ImageIcon("iron-man-icegif-5.gif")));
      URL url = TestDialog.class.getResource("/jokes/"+ image);
      p.add(new JLabel(new ImageIcon(url)));
      getContentPane().add(p);
   }
}
