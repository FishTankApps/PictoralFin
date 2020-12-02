package com.fishtankapps.pictoralfin.mainFrame;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.fishtankapps.pictoralfin.utilities.AudioUtil;
import com.fishtankapps.pictoralfin.utilities.Constants;
import com.fishtankapps.pictoralfin.utilities.FileImporter;

import javafx.embed.swing.JFXPanel;

public class Launcher {

	public static void main(String[] filePaths) {

		try {			
			setUpUIStuff();
			
			setUpPictoralFin(filePaths);
			
			loadFonts();
			System.out.println("-- Launch Complete --");
			StatusLogger.logPrimaryStatus("Launch Complete!");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void setUpUIStuff() throws Exception {
		
		
		System.out.println("-- Setting Look And Feel --");
		String lookAndFeel = PictoralFinConfiguration.openConfiguration().getLookAndFeel();
		

		for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			System.out.println("info: " + info);
		
		if(lookAndFeel.equals(Constants.LOOK_AND_FEEL_NOT_CHOOSEN)) {
			String osName = System.getProperty("os.name");
			System.out.println("OS: " + osName);
			
			
			if(osName.contains("Windows"))
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			else if(osName.contains("Linux")) 
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			else if(osName.contains("OS X")) {
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
	
	public static PictoralFin setUpPictoralFin(String[] filePaths) {
		System.out.println("-- Setting Up PictoralFin --");
		PictoralFin pictoralFin = new PictoralFin();
		
		AudioUtil.passPictoralFin(pictoralFin);
		FileImporter.passPictoralFin(pictoralFin);
		
		System.out.println("-- Launching PictoralFin --");
		pictoralFin.launch();
		
		StatusLogger.logPrimaryStatus("Importing Selected File(s)...");
		System.out.println("-- Importing Files --");
		if (filePaths.length > 0) {
			File file = new File(filePaths[0]);

			if (file.exists()) {
				if (file.getName().contains(".pfp")) {
					pictoralFin.openProject(file.getAbsolutePath());
				} else if (file.getName().contains(".pff")) {
					pictoralFin.openFrameFile(file);
				}
			}
		}
		
		return pictoralFin;
	}
	
	public static void loadFonts() {
		System.out.println("-- Loading Fonts --");
		StatusLogger.logPrimaryStatus("Loading Fonts...");
		
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
