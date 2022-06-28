package source;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Utility method to retrieve the idle time on Windows and sample code to test
 * it. JNA shall be present in your classpath for this to work (and compile).
 * 
 * @author gaurav.srivastava
 */
public class Win32IdleTime {

   public static List<String> imageList = UKGAppService.getImageList();
   
   public static long durationToTrackBeingOnline = 20000;
   public static long durationInWhichToCloseTheDialog = 15000;

   public interface Kernel32 extends StdCallLibrary {
      Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

      /**
       * Retrieves the number of milliseconds that have elapsed since the
       * system was started.
       * 
       * @see http://msdn2.microsoft.com/en-us/library/ms724408.aspx
       * @return number of milliseconds that have elapsed since the system was
       *         started.
       */
      public int GetTickCount();
   };

   public interface User32 extends StdCallLibrary {
      User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

      /**
       * Contains the time of the last input.
       * 
       * @see http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputstructures/lastinputinfo.asp
       */
      public static class LASTINPUTINFO extends Structure {
         public int cbSize = 8;

         /// Tick count of when the last input event was received.
         public int dwTime;

         @Override
         protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "cbSize", "dwTime" });
         }
      }

      /**
       * Retrieves the time of the last input event.
       * 
       * @see http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputfunctions/getlastinputinfo.asp
       * @return time of the last input event, in milliseconds
       */
      public boolean GetLastInputInfo(LASTINPUTINFO result);
   };

   /**
    * Get the amount of milliseconds that have elapsed since the last input
    * event (mouse or keyboard)
    * 
    * @return idle time in milliseconds
    */
   public static int getIdleTimeMillisWin32() {
      User32.LASTINPUTINFO lastInputInfo = new User32.LASTINPUTINFO();
      User32.INSTANCE.GetLastInputInfo(lastInputInfo);
      return Kernel32.INSTANCE.GetTickCount() - lastInputInfo.dwTime;
   }

   enum State {
      UNKNOWN, ONLINE, IDLE, AWAY
   };

   public void start(){
      startTimer();
   }



   public static int getRandomNumber(){
      Random r = new Random();
      System.out.println(imageList.size());
      return r.nextInt(imageList.size());
   }

   public static void startTimer() {
      if (!System.getProperty("os.name").contains("Windows")) {
         System.err.println("ERROR: Only implemented on Windows");
         System.exit(1);
      }
      State state = State.UNKNOWN;
      DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
      long startTimeBeingOnline = System.currentTimeMillis();
      System.out.println(
            "Current Date and Time for Online Status tracker " + dateFormat.format(new Date()) + " # " + state);
      boolean onlineTagDisplayed = false;
      boolean hasBeenAwayOnce = false;
      long timeatwhichDialogIsDisplayed = 0;
      List<String> listofStatus = new ArrayList<>();
      TestDialog dialog = null;
      for (;;) {

         if (onlineTagDisplayed) {
            System.out.println("Current Date and Time for Online Status tracker " + dateFormat.format(new Date())
            + " # " + state);
            hasBeenAwayOnce = false;
         }
         if (timeatwhichDialogIsDisplayed != 0
               && System.currentTimeMillis() - timeatwhichDialogIsDisplayed > durationInWhichToCloseTheDialog) {
            if (dialog != null) {
               dialog.dispose();
               timeatwhichDialogIsDisplayed = 0;
            }
         }
         onlineTagDisplayed = false;
         int idleSec = getIdleTimeMillisWin32() / 1000;
         State newState = null;
         if (idleSec < 30) {
            newState = State.ONLINE;
         } else if (idleSec > 60) {
            newState = State.AWAY;
            hasBeenAwayOnce = true;
         } else {
            newState = State.IDLE;
         }
         if (newState != state) {
            if (newState == State.ONLINE && state == State.AWAY) {
               startTimeBeingOnline = System.currentTimeMillis();
               System.out.println("startTimeBeingOnline " + startTimeBeingOnline + " "
                     + dateFormat.format(new Date()) + " # " + newState);

            }
            state = newState;
            System.out.println(dateFormat.format(new Date()) + " # " + state);

         } else {
            if (state.equals(State.ONLINE)) {
               long endTimeBeingOnline = System.currentTimeMillis();
               if (endTimeBeingOnline - startTimeBeingOnline > durationToTrackBeingOnline && !hasBeenAwayOnce) {
                  System.out.println(
                        "You are online from a long time " + dateFormat.format(new Date()) + " # " + state);

                  String appTitle = "UKG Health Assistant";
                  //loadImages();
                  int randomNum = getRandomNumber();
                  String image = imageList.get(randomNum);

                  dialog = new TestDialog(image);
                  dialog.setTitle(appTitle);
                  dialog.show();

                  timeatwhichDialogIsDisplayed = System.currentTimeMillis();
                  onlineTagDisplayed = true;
                  startTimeBeingOnline = System.currentTimeMillis();
                  // Call AWT Code Here
               } else if (endTimeBeingOnline - startTimeBeingOnline > durationToTrackBeingOnline) {
                  System.out.println("endTimeBeingOnline " + endTimeBeingOnline + "startTimeBeingOnline "
                        + startTimeBeingOnline + " " + dateFormat.format(new Date()) + " # " + state);

                  hasBeenAwayOnce = false;               
               }
            }
         }
         try {
            Thread.sleep(1000);
         } catch (Exception ex) {
         }
      }
      
      
   }
   
}
