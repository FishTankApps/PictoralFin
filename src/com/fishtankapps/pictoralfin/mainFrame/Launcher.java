package com.fishtankapps.pictoralfin.mainFrame;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.fishtankapps.pictoralfin.objectBinders.Settings;
import com.fishtankapps.pictoralfin.utilities.AudioUtil;
import com.xuggle.xuggler.Global;

import javafx.embed.swing.JFXPanel;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class Launcher {
	
	public static void main(String[] filePaths) {

		try {
			System.out.println("-- Setting Look And Feel --");			
			UIManager.setLookAndFeel(Settings.openSettings().getLookAndFeel());
			
			for(com.xuggle.ferry.Logger.Level c : com.xuggle.ferry.Logger.Level.values()) {
				com.xuggle.ferry.Logger.setGlobalIsLogging(c, false);
			}
			Global.setFFmpegLoggingLevel(-1);	
			
			System.out.println("-- Loading Fonts --");
			
			try {
				 GraphicsEnvironment ge = 
				         GraphicsEnvironment.getLocalGraphicsEnvironment();
				     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/lcd.ttf")));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "There was an error loading fonts:\n" +e.getMessage(), "Error Loading Fonts", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			 
			
			System.out.println("-- Setting Up PictoralFin --");
			PictoralFin pictoralFin = new PictoralFin();
			
			@SuppressWarnings("unused")
			JFXPanel usedToInitializeJFXTools = new JFXPanel();
			
			System.out.println("-- Importing Pictures --");
			if (filePaths.length > 0) {
				File file = new File(filePaths[0]);
				if(file.exists()) {
					if(file.getName().contains(".pfp")) {
						pictoralFin.openProject(file.getAbsolutePath());
					}
				}
			}
			
			AudioUtil.passPictoralFin(pictoralFin);
			
			System.out.println("-- Launching PictoralFin --");
			pictoralFin.launch();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
