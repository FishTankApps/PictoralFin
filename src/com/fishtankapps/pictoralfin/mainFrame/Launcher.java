package com.fishtankapps.pictoralfin.mainFrame;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.fishtankapps.pictoralfin.objectBinders.Preferences;
import com.fishtankapps.pictoralfin.utilities.AudioUtil;
import com.fishtankapps.pictoralfin.utilities.FileUtils;
import com.fishtankapps.pictoralfin.utilities.VideoUtil;

import javafx.embed.swing.JFXPanel;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Launcher {

	public static void main(String[] filePaths) {

		try {			
			setUpUIStuff();
			
			setUpPictoralFin(filePaths);
			
			extractFFmpeg();
			
			loadFonts();
			System.out.println("-- Launch Complete --");
			StatusLogger.logStatus("Launch Complete!");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void setUpUIStuff() throws Exception {
		System.out.println("-- Setting Look And Feel --");
		UIManager.setLookAndFeel(Preferences.openPreferences().getLookAndFeel());

		@SuppressWarnings("unused")
		JFXPanel usedToInitializeJFXTools = new JFXPanel();
	}

	public static void extractFFmpeg() {
		//StatusLogger.logStatus("Extracting FFmpeg to temp files...");
		System.out.println("-- Extracting FFmpeg --");
		try {
			File ffmpegFile = FileUtils.createTempFile("ffmpeg", ".exe", "FFmpeg", false);
			
			InputStream ffmpegStream = Launcher.class.getResourceAsStream("ffmpeg.exe");
			byte[] buffer = new byte[ffmpegStream.available()];
			ffmpegStream.read(buffer);

			OutputStream ffmpegOutStream = new FileOutputStream(ffmpegFile);
			ffmpegOutStream.write(buffer);
			
			ffmpegOutStream.close();
			ffmpegStream.close();
			
			
			
			File ffprobeFile = FileUtils.createTempFile("ffprobe", ".exe", "FFmpeg", false);
			
			InputStream ffprobeStream = Launcher.class.getResourceAsStream("ffprobe.exe");
			buffer = new byte[ffprobeStream.available()];
			ffprobeStream.read(buffer);

			OutputStream ffprobeOutStream = new FileOutputStream(ffprobeFile);
			ffprobeOutStream.write(buffer);

			
			ffprobeOutStream.close();
			ffprobeStream.close();
			
			VideoUtil.ffmpegExeicutable  = ffmpegFile;			
			VideoUtil.ffprobeExeicutable = ffprobeFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadFonts() {
		System.out.println("-- Loading Fonts --");
		StatusLogger.logStatus("Loading Fonts...");
		
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Launcher.class.getResourceAsStream("lcd.ttf")));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error loading fonts:\n" + e.getMessage(),
					"Error Loading Fonts", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	public static void setUpPictoralFin(String[] filePaths) {
		System.out.println("-- Setting Up PictoralFin --");
		PictoralFin pictoralFin = new PictoralFin();
		
		AudioUtil.passPictoralFin(pictoralFin);

		System.out.println("-- Launching PictoralFin --");
		pictoralFin.launch();
		
		StatusLogger.logStatus("Importing Selected File(s)...");
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
	}
}
