package com.fishtankapps.pictoralfin.mainFrame;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.utilities.AudioUtil;
import com.fishtankapps.pictoralfin.utilities.Constants;
import com.fishtankapps.pictoralfin.utilities.FileImporter;
import com.fishtankapps.pictoralfin.utilities.JokeFactory;
import com.fishtankapps.pictoralfin.utilities.Utilities;
import com.fishtankapps.pictoralfin.utilities.VideoUtil;

import javafx.embed.swing.JFXPanel;

public class Launcher {

	public static void main(String[] filePaths) {

		try {			
			setUpUIStuff();
			
			loadFonts();
			
			setUpPictoralFin(filePaths);
			
			setUpFFmpegPaths();						
			
			System.out.println("-- Launch Complete --");
			StatusLogger.logPrimaryStatus("Launch Complete!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
			
			Utilities.writeToLogFile("StartUp Error", "--- Error durring start up ---\nExeception Message: " + e.getMessage() + 
					"\nException Localized Message: " + e.getMessage() + "\n\nStack Trace:" + Utilities.stackTraceToString(e));
			
			System.exit(-1);
		}
	}
	
	public static void setUpUIStuff() throws Exception {	
		
		System.out.println("-- Setting Look And Feel --");
		
		
		String lookAndFeel = PictoralFinConfiguration.openConfiguration().getLookAndFeel();
		
		String osName = System.getProperty("os.name");
		System.out.println("OS Name: " + osName + ", Constants.OPERATING_SYSTEM: " + Constants.OPERATING_SYSTEM );
		
		
		//for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
		//	System.out.println("Look And Feel: " + info);
		
		if(lookAndFeel.equals(Constants.LOOK_AND_FEEL_NOT_CHOOSEN)) {		
			
			if(Constants.OPERATING_SYSTEM == Constants.OperatingSystem.WINDOWS)
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			else if(Constants.OPERATING_SYSTEM == Constants.OperatingSystem.LINUX) 
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			else if(Constants.OPERATING_SYSTEM == Constants.OperatingSystem.OS_X) {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WikiTeX");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}			
		
		} else {
			UIManager.setLookAndFeel(lookAndFeel);
		}

		@SuppressWarnings("unused")
		JFXPanel usedToInitializeJFXTools = new JFXPanel();
	}
	
	public static void setUpPictoralFin(String[] filePaths) {
		System.out.println("-- Setting Up PictoralFin --");
		PictoralFin pictoralFin = new PictoralFin();
		
		AudioUtil.passPictoralFin(pictoralFin);
		FileImporter.passPictoralFin(pictoralFin);
		
		System.out.println("-- Launching PictoralFin --");
		pictoralFin.launch();
		
		StatusLogger.logPrimaryStatus("Importing Selected File(s)...");
		System.out.println("-- Importing Files --");
		
		
		if(filePaths != null)
			FileImporter.importFiles(filePaths);
		
		
		if(pictoralFin.getConfiguration().getShowJokeOnStartUp()) {
			new Thread(()->{
				final ImageIcon icon = new ImageIcon(GlobalImageKit.readImage("laughing.png"));
				JOptionPane.showMessageDialog(null, JokeFactory.getJoke(), "Joke", JOptionPane.INFORMATION_MESSAGE, icon);
			}).start();
		}
	}
	
	public static void setUpFFmpegPaths() {
		if(Constants.OPERATING_SYSTEM == Constants.OperatingSystem.WINDOWS) {
			VideoUtil.ffmpegExeicutable = new File("resources/FFmpeg/ffmpeg-Windows.exe");
			VideoUtil.ffprobeExeicutable = new File("resources/FFmpeg/ffprobe-Windows.exe");
		} else if (Constants.OPERATING_SYSTEM == Constants.OperatingSystem.LINUX) {
			VideoUtil.ffmpegExeicutable = new File("resources/FFmpeg/ffmpeg-Deb-Linux");
			VideoUtil.ffprobeExeicutable = new File("resources/FFmpeg/ffprobe-Deb-Linux");
		}  else if (Constants.OPERATING_SYSTEM == Constants.OperatingSystem.OS_X) {
			VideoUtil.ffmpegExeicutable = new File("resources/FFmpeg/ffmpeg-OS-X");
			VideoUtil.ffprobeExeicutable = new File("resources/FFmpeg/ffprobe-OS-X");
		}
	}
	
	public static void loadFonts() {
		System.out.println("-- Loading Fonts --");
		
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Launcher.class.getResourceAsStream("lcd.ttf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Launcher.class.getResourceAsStream("BGOTHL.TTF")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Launcher.class.getResourceAsStream("ITCBLKAD.TTF")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Launcher.class.getResourceAsStream("MAGNETOB.TTF")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Launcher.class.getResourceAsStream("PAPYRUS.TTF")));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error loading fonts:\n" + e.getMessage(),
					"Error Loading Fonts", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
