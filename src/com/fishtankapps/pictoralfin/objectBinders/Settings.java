package com.fishtankapps.pictoralfin.objectBinders;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.UIManager;

import com.fishtankapps.pictoralfin.jComponents.JSettingsEditor;

public class Settings implements Serializable{
	private static final long serialVersionUID = -1163643096786559533L;
	
	public static final Settings DEFAULT_SETTINGS = new Settings();
	
	private Theme theme;
	private Dimension maxPictureSize;
	private ArrayList<String> messagesToNotShow;
	private String lookAndFeel;
	private int audioSampleRate;
	
	private Settings() {
		theme = Theme.OCEAN_THEME;
		maxPictureSize = new Dimension(1280, 720);
		messagesToNotShow = new ArrayList<>();
		lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		audioSampleRate = 48000;
	}

	public final Theme getTheme() {
		return theme;
	}
	public final void setTheme(Theme theme) {
		this.theme = theme;
	}

	public final ArrayList<String> getMessagesToNotShow() {
		return messagesToNotShow;
	}
	/**
	 * 
	 * @param title Pass the title of the dialog! Those are less likely to change.
	 */
	public final void addMessageToNotShow(String title) {
		messagesToNotShow.add(title);
	}
	/**
	 * 
	 * @param title Pass the title of the dialog! Those are less likely to change.
	 */
	public final void removeMessageToNotShow(String title) {
		messagesToNotShow.remove(title);
	}
	
	public final Dimension getMaxPictureSize() {
		return maxPictureSize;
	}
	public final void setMaxPictureSize(Dimension maxPictureSize) {
		this.maxPictureSize = maxPictureSize;
	}
	
	public String getLookAndFeel() {
		return lookAndFeel;
	}
	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}
	
	public int getAudioSampleRate() {
		return audioSampleRate;
	}
	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}
	

	public static Settings openSettings() {
		Settings settings;
		try {
			ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(new File("dataFiles/settings.pfd")));
			settings = (Settings) fileInput.readObject();
			
			fileInput.close();
		} catch (Exception e) {
			System.out.println("CATCH BLOCK: Settings.openSettings()");
			settings = new Settings();
			settings.saveSettings();
		}
		
		return settings;
	}
	public void saveSettings() {
		try {
			File outputFile = new File("dataFiles/settings.pfd");
			
			if(!outputFile.exists())
				outputFile.createNewFile();
			
			ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(outputFile));
			fileOutput.writeObject(this);

			fileOutput.close();
		}catch(Exception e) {
			System.out.println("EMPTY CATCH BLOCK: Settings.saveSettings()\n" + e.getMessage());
		}
	}
	
	public Settings copySettings() {
		Settings copy = new Settings();
		
		copy.theme = theme;
		copy.lookAndFeel = lookAndFeel;
		copy.maxPictureSize = maxPictureSize;
		copy.audioSampleRate = audioSampleRate;				
		copy.messagesToNotShow = messagesToNotShow;	
		
		return copy;
	}	
	public void applySettings(Settings settings) {		
		theme = settings.theme;
		lookAndFeel = settings.lookAndFeel;	
		maxPictureSize = settings.maxPictureSize;
		audioSampleRate = settings.audioSampleRate;
		messagesToNotShow = settings.messagesToNotShow;	
	}
	
	
	public void openSettingsEditor() {
		new JSettingsEditor(this);
	}
}
