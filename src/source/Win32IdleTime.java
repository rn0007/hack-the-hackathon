package source;

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
   
   public static long durationToTrackBeingOnline = 30000;
   public static long additionallDuration = 0;
   public static long durationInWhichToCloseTheDialog = 20000;
   public static long timeatwhichDialogIsDisplayed = 0;

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
      System.out.println(imageList.size()+" images present");
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
      //long timeatwhichDialogIsDisplayed = 0;
      //long timeatwhichDialogIsClosed = 0;
      List<String> listofStatus = new ArrayList<>();
      TestDialog dialog = null;
      for (;;) {

         if (onlineTagDisplayed) {
            System.out.println("Current Date and Time for Online Status tracker " + dateFormat.format(new Date())
            + " # " + state);
            hasBeenAwayOnce = false;
         }
         System.out.println("dispose timer : system time - timeatwhichDialogIsDisplayed " + (System.currentTimeMillis() - timeatwhichDialogIsDisplayed ));
         System.out.println("dispose timer : durationInWhichToCloseTheDialog " + durationInWhichToCloseTheDialog );
         System.out.println("dispose timer :  additionallDuration "+additionallDuration);
         System.out.println("dispose timer : durationInWhichToCloseTheDialog + additionallDuration   " + (durationInWhichToCloseTheDialog + additionallDuration));
         System.out.println();
         
         if (timeatwhichDialogIsDisplayed != 0
               && System.currentTimeMillis() - timeatwhichDialogIsDisplayed > (durationInWhichToCloseTheDialog + additionallDuration)) {
            if (dialog != null) {
	               dialog.dispose();
	               additionallDuration = 0;
	               timeatwhichDialogIsDisplayed = 0;
	               dialog = null;
	               startTimeBeingOnline = System.currentTimeMillis();
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
               
               System.out.println("online timer : endTimeBeingOnline - startTimeBeingOnline " + ( endTimeBeingOnline - startTimeBeingOnline));
               System.err.println("additionallDuration "+additionallDuration);
               System.out.println("online timer : durationToTrackBeingOnline + additionallDuration " + (durationToTrackBeingOnline + additionallDuration));
               System.out.println("hasBeenAwayOnce "+hasBeenAwayOnce);
               System.out.println();
               
               if (endTimeBeingOnline - startTimeBeingOnline > (durationToTrackBeingOnline + additionallDuration) && !hasBeenAwayOnce) {
                  System.out.println(
                        "You are online from a long time " + dateFormat.format(new Date()) + " # " + state);

                  String appTitle = "UKrewer Wellness Assistant";
                  //loadImages();
                  int randomNum = getRandomNumber();
                  String image = imageList.get(randomNum);
                  
                  if(null == dialog){
                	  System.out.println("dialog opened");
	                  dialog = new TestDialog(image);
	                  additionallDuration = 0;
	                  durationInWhichToCloseTheDialog = 20000;
	                  dialog.setTitle(appTitle);
	                  dialog.show();
	                  timeatwhichDialogIsDisplayed = System.currentTimeMillis();
	                  TestDialog.initTime = timeatwhichDialogIsDisplayed;
                  }
                  
                  onlineTagDisplayed = true;
                  startTimeBeingOnline = System.currentTimeMillis();
                  // Call AWT Code Here
               } else if (endTimeBeingOnline - startTimeBeingOnline > (durationToTrackBeingOnline+additionallDuration)) {
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
