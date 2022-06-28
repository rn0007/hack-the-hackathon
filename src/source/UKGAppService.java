package source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UKGAppService {

	private static Win32IdleTime idleTimeCalculator;
	private static String cmd = "start";
	//The name of the process should contain folder name like %"ukg-app"% in it's command line path
	private static String processName = "ukg-app";
	private static List<String> imageList = new ArrayList<>();

	public static List<String> getImageList() {
      return imageList;
   }

   public static void main(String[] args) {

		if (args.length > 0) {
			cmd = args[0];
		}

		if ("start".equalsIgnoreCase(cmd)) {
			loadImages();
			idleTimeCalculator = new Win32IdleTime();
			idleTimeCalculator.start();
		} else if ("stop".equalsIgnoreCase(cmd)) {			
			stopUKGApp();
		} else {

		}
	}
	
	//loads GIFs present in resources folder
	public static void loadImages() {
		//System.out.println("loading Images");
		File resDir = new File("resources");
		File[] files = resDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".gif"));
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			//System.out.println(fileName);
			imageList.add(fileName);
		}
	}
	
	public static void stopUKGApp() {
		String strCmdLine = "wmic process where \"Name='javaw.exe' AND CommandLine like '%%" + processName+ "%%'\" CALL TERMINATE";
		System.out.println("UKG App Task End Command : "+strCmdLine);
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(strCmdLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
