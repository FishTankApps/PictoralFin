package com.fishtankapps.pictoralfin.objectBinders;

import java.io.Serializable;
import java.util.ArrayList;

import com.fishtankapps.pictoralfin.utilities.Constants;

public class DataFile implements Serializable {

	private static final long serialVersionUID = -8264931171924899681L;
	
	private String lastOpenPictureLocation;
	private String lastOpenAudioLocation;
	private String lastOpenVideoLocation;
	private String lastOpenProjectLocation;
	private ArrayList<String> favoriteImageEditorTools;
	
	private int jokeIndex;
	
	public DataFile() {
		resetDataFile();
	}
	
	public void resetDataFile() {
		
		if(Constants.OPERATING_SYSTEM == Constants.OperatingSystem.WINDOWS) {
			lastOpenPictureLocation = "C:/Users/" + System.getProperty("user.name") + "/Pictures/null.jpg";
			lastOpenAudioLocation = "C:/Users/" + System.getProperty("user.name") + "/Music/null.mp3";
			lastOpenVideoLocation = "C:/Users/" + System.getProperty("user.name") + "/Videos/null.mp4";		
			lastOpenProjectLocation = "C:/Users/" + System.getProperty("user.name") + "/Videos/null.pfp";	
		} else if (Constants.OPERATING_SYSTEM == Constants.OperatingSystem.LINUX) {
			lastOpenPictureLocation = "/home/" + System.getProperty("user.name") + "/Pictures/null.jpg";
			lastOpenAudioLocation = "/home/" + System.getProperty("user.name") + "/Music/null.mp3";
			lastOpenVideoLocation = "/home/" + System.getProperty("user.name") + "/Videos/null.mp4";		
			lastOpenProjectLocation = "/home/" + System.getProperty("user.name") + "/Videos/null.pfp";	
		} else if (Constants.OPERATING_SYSTEM == Constants.OperatingSystem.OS_X) {
			lastOpenPictureLocation = "/home/" + System.getProperty("user.name") + "/Pictures/null.jpg";
			lastOpenAudioLocation = "/home/" + System.getProperty("user.name") + "/Music/null.mp3";
			lastOpenVideoLocation = "/home/" + System.getProperty("user.name") + "/Videos/null.mp4";		
			lastOpenProjectLocation = "/home/" + System.getProperty("user.name") + "/Videos/null.pfp";	
		}
		
		jokeIndex = -1;
		favoriteImageEditorTools = new ArrayList<>();
	}
	
	public String getLastOpenedPictureLocation() {
		return lastOpenPictureLocation;
	}
	public void setLastOpenedPictureLocation(String lastOpenLocation) {
		this.lastOpenPictureLocation = lastOpenLocation;
	}
	
	public String getLastOpenAudioLocation() {
		return lastOpenAudioLocation;
	}
	public void setLastOpenAudioLocation(String lastOpenAudioLocation) {
		this.lastOpenAudioLocation = lastOpenAudioLocation;
	}
	
	public String getLastOpenVideoLocation() {
		return lastOpenVideoLocation;
	}
	public void setLastOpenVideoLocation(String lastOpenVideoLocation) {
		this.lastOpenVideoLocation = lastOpenVideoLocation;
	}

	public String getLastOpenProjectLocation() {
		return lastOpenProjectLocation;
	}
	public void setLastOpenProjectLocation(String lastOpenProjectLocation) {
		this.lastOpenProjectLocation = lastOpenProjectLocation;
	}

	public int getJokeIndex() {
		return jokeIndex;
	}
	public void setJokeIndex(int jokeIndex) {
		this.jokeIndex = jokeIndex;
	}

	public void addFavoriteImageEditorTool(String toolName) {
		favoriteImageEditorTools.add(toolName);
	}
	public void removeFavoriteImageEditorTool(String toolName) {
		favoriteImageEditorTools.remove(toolName);
	}
	public boolean isImageEditorToolAFavorite(String toolName) {
		return favoriteImageEditorTools.contains(toolName);
	}
}
