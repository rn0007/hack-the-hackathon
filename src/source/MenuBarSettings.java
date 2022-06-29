package source;

import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBarSettings {

	public static JMenuBar createMenuBar() {

		JMenuBar menuBar;
		JMenu menu;
		JMenu timeToNudgeMenu;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the File Menu.
		menu = new JMenu("Settings");
		menu.setMnemonic(KeyEvent.VK_S);
		menu.getAccessibleContext().setAccessibleDescription("Settings");

		// a group of JMenuItems
		timeToNudgeMenu = new JMenu("Time to nudge");
		timeToNudgeMenu.setMnemonic(KeyEvent.VK_T);
		timeToNudgeMenu.getAccessibleContext().setAccessibleDescription("Time to nudge");
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.getAccessibleContext().setAccessibleDescription("Go to Help!");

		JMenuItem exitMenu = new JMenuItem("Exit");
		exitMenu.setToolTipText("Exit application");
		exitMenu.addActionListener((event) -> System.exit(0));

		JMenuItem thirtyMinsMenuItem = new JMenuItem("30 Minutes");
		JMenuItem fourtyFiveMinsMenuItem = new JMenuItem("45 Minutes");
		JMenuItem oneHourMenuItem = new JMenuItem("1 Hour");

		thirtyMinsMenuItem.addActionListener((event) -> {
			Win32IdleTime.durationToTrackBeingOnline = 30000;
			System.out.println("MenuBarSettings.createMenuBar()" + Win32IdleTime.durationToTrackBeingOnline);

		});

		fourtyFiveMinsMenuItem.addActionListener((event) -> {
			Win32IdleTime.durationToTrackBeingOnline = 45000;
			System.out.println("MenuBarSettings.createMenuBar()" + Win32IdleTime.durationToTrackBeingOnline);

		});

		oneHourMenuItem.addActionListener((event) -> {
			Win32IdleTime.durationToTrackBeingOnline = 60000;
			System.out.println("MenuBarSettings.createMenuBar()" + Win32IdleTime.durationToTrackBeingOnline);

		});

		timeToNudgeMenu.add(thirtyMinsMenuItem);
		timeToNudgeMenu.add(fourtyFiveMinsMenuItem);
		timeToNudgeMenu.add(oneHourMenuItem);

		menu.add(timeToNudgeMenu);
		menu.add(exitMenu);
		menuBar.add(menu);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);
		return menuBar;

	}
}
