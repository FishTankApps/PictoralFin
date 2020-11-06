package com.fishtankapps.pictoralfin.mainFrame;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.fishtankapps.pictoralfin.jComponents.JPictoralFinConfigurationEditor;
import com.fishtankapps.pictoralfin.objectBinders.DataFile;
import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class PictoralFinConfiguration implements Serializable{
	private static final long serialVersionUID = -1163643096786559533L;
	
	public static final PictoralFinConfiguration DEFAULT_SETTINGS = new PictoralFinConfiguration();
	
	private Theme theme;
	private Dimension maxPictureSize;
	private ArrayList<String> messagesToNotShow;
	private String lookAndFeel, fontPreviewString;
	private int audioSampleRate;
	
	private DataFile dataFile;
	
	private PictoralFinConfiguration() {
		theme = Theme.OCEAN_THEME;
		maxPictureSize = new Dimension(1280, 720);
		messagesToNotShow = new ArrayList<>();
		lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		audioSampleRate = 48000;
		fontPreviewString = "Font Sample";
		dataFile = new DataFile();
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

	public DataFile getDataFile() {
		return dataFile;
	}
	public void resetDataFile() {
		dataFile.resetDataFile();
	}
	
	public static PictoralFinConfiguration openConfiguration() {
		PictoralFinConfiguration settings;
		try {
			ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(new File("resources\\pictoralFinConfig.cfg")));			settings = (PictoralFinConfiguration) fileInput.readObject();
			
			fileInput.close();
		} catch (Exception e) {
			System.out.println("PictoralFinConfiguration - Creating New Configuration...");
			settings = new PictoralFinConfiguration();
			settings.saveConfiguration();
		}
		
		return settings;
	}
	public void saveConfiguration() {
		try {
			File outputFile = new File("resources\\pictoralFinConfig.cfg");
			
			if(!outputFile.exists())
				outputFile.createNewFile();
			
			ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(outputFile));
			fileOutput.writeObject(this);

			fileOutput.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error Saving Config.");
			System.out.println("EMPTY CATCH BLOCK: Settings.savePreferences()\n" + e.getMessage());
		}
	}
	
	public PictoralFinConfiguration copyConfiguration() {
		PictoralFinConfiguration copy = new PictoralFinConfiguration();
		
		copy.theme = theme;
		copy.lookAndFeel = lookAndFeel;
		copy.maxPictureSize = maxPictureSize;
		copy.audioSampleRate = audioSampleRate;				
		copy.messagesToNotShow = messagesToNotShow;	
		copy.fontPreviewString = fontPreviewString;
		
		return copy;
	}	
	public void applyConfiguration(PictoralFinConfiguration configuration) {		
		theme = configuration.theme;
		lookAndFeel = configuration.lookAndFeel;	
		maxPictureSize = configuration.maxPictureSize;
		audioSampleRate = configuration.audioSampleRate;
		messagesToNotShow = configuration.messagesToNotShow;	
		fontPreviewString = configuration.fontPreviewString;
	}
	
	
	public void openConfigurationEditor() {
		new JPictoralFinConfigurationEditor(this);
	}
}
