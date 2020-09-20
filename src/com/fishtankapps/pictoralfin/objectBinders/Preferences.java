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

import com.fishtankapps.pictoralfin.jComponents.JPreferencesEditor;

public class Preferences implements Serializable{
	private static final long serialVersionUID = -1163643096786559533L;
	
	public static final Preferences DEFAULT_SETTINGS = new Preferences();
	
	private Theme theme;
	private Dimension maxPictureSize;
	private ArrayList<String> messagesToNotShow;
	private String lookAndFeel, fontPreviewString;
	private int audioSampleRate;
	
	private Preferences() {
		theme = Theme.OCEAN_THEME;
		maxPictureSize = new Dimension(1280, 720);
		messagesToNotShow = new ArrayList<>();
		lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		audioSampleRate = 48000;
		fontPreviewString = "Font Sample";
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
	

	public String getFontPreviewString() {
		return fontPreviewString;
	}
	public void setFontPreviewString(String fontPreviewString) {
		this.fontPreviewString = fontPreviewString;
	}

	public static Preferences openPreferences() {
		Preferences settings;
		try {
			ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(new File("dataFiles/preferences.pfd")));
			settings = (Preferences) fileInput.readObject();
			
			fileInput.close();
		} catch (Exception e) {
			System.out.println("CATCH BLOCK: Settings.openPreferences()");
			settings = new Preferences();
			settings.saveSettings();
		}
		
		return settings;
	}
	public void saveSettings() {
		try {
			File outputFile = new File("dataFiles/preferences.pfd");
			
			if(!outputFile.exists())
				outputFile.createNewFile();
			
			ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(outputFile));
			fileOutput.writeObject(this);

			fileOutput.close();
		}catch(Exception e) {
			System.out.println("EMPTY CATCH BLOCK: Settings.savePreferences()\n" + e.getMessage());
		}
	}
	
	public Preferences copyPreferences() {
		Preferences copy = new Preferences();
		
		copy.theme = theme;
		copy.lookAndFeel = lookAndFeel;
		copy.maxPictureSize = maxPictureSize;
		copy.audioSampleRate = audioSampleRate;				
		copy.messagesToNotShow = messagesToNotShow;	
		copy.fontPreviewString = fontPreviewString;
		
		return copy;
	}	
	public void applyPreferences(Preferences preferences) {		
		theme = preferences.theme;
		lookAndFeel = preferences.lookAndFeel;	
		maxPictureSize = preferences.maxPictureSize;
		audioSampleRate = preferences.audioSampleRate;
		messagesToNotShow = preferences.messagesToNotShow;	
		fontPreviewString = preferences.fontPreviewString;
	}
	
	
	public void openPreferencesEditor() {
		new JPreferencesEditor(this);
	}
}
