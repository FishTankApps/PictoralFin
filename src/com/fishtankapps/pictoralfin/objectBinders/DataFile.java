package com.fishtankapps.pictoralfin.objectBinders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataFile implements Serializable {

	private static final long serialVersionUID = -8264931171924899681L;
	
	private String lastOpenPictureLocation;
	private String lastOpenAudioLocation;
	private String lastOpenVideoLocation;
	private String lastOpenProjectLocation;
	
	private DataFile() {
		lastOpenPictureLocation = "C:/Users/" + System.getProperty("user.name") + "/Pictures/null.jpg";
		lastOpenAudioLocation = "C:/Users/" + System.getProperty("user.name") + "/Music/null.mp3";
		lastOpenVideoLocation = "C:/Users/" + System.getProperty("user.name") + "/Videos/null.mp4";		
		lastOpenProjectLocation = "C:/Users/" + System.getProperty("user.name") + "/Videos/null.pfp";		
	}
	
	public static DataFile openDataFile() {
		DataFile dataFile;

		try {
			ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(new File("./dataFiles/dataFile.pfd")));
			dataFile = (DataFile) fileInput.readObject();
			
			fileInput.close();
		} catch (Exception e) {
			System.out.println("CATCH BLOCK: DataFile.openDataFile()");
			dataFile = new DataFile();
		}
		
		return dataFile;
	}
	

	
	public void saveDataFile() {
		try {
			ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(new File("./dataFiles/dataFile.pfd")));
			fileOutput.writeObject(this);			
			fileOutput.close();
		}catch(Exception e) {
			System.out.println("EMPTY CATCH BLOCK: DataFile.DataFile(String)");
		}
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
}
