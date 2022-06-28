package source;

import java.io.IOException;

public class UKGAppService {

	private static Win32IdleTime idleTimeCalculator;
	private static String cmd = "start";
	//The name of the process should contain folder name like %"ukg-app"% in it's command line path
	private static String processName = "ukg-app";

	public static void main(String[] args) {

		if (args.length > 0) {
			cmd = args[0];
		}

		if ("start".equalsIgnoreCase(cmd)) {
			idleTimeCalculator = new Win32IdleTime();
			idleTimeCalculator.start();
		} else if ("stop".equalsIgnoreCase(cmd)) {			
			stopUKGApp();
		} else {

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
