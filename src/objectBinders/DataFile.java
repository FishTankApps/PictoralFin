package objectBinders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataFile {
	private String lastOpenPictureLocation;
	private String lastOpenAudioLocation;
	private String lastOpenVideoLocation;
	
	public DataFile() {
		File dataFile = new File("dataFiles/dataFile.pfd");		
		
		try {
			ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(dataFile));
			lastOpenPictureLocation = (String) fileInput.readObject();
			lastOpenAudioLocation = (String) fileInput.readObject();
			lastOpenVideoLocation = (String) fileInput.readObject();
			
			fileInput.close();
		} catch (Exception e) {
			System.out.println("CATCH BLOCK: DataFile.DataFile(File)");
			createNewDataFile();
		}
	}
	private void createNewDataFile() {
		try {
			File outputFile = new File("dataFiles/dataFile.pfd");
			outputFile.createNewFile();
			
			ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(outputFile));
			fileOutput.writeObject(lastOpenPictureLocation = "C:/Users/" + System.getProperty("user.name") + "/Pictures");
			fileOutput.writeObject(lastOpenAudioLocation = "C:/Users/" + System.getProperty("user.name") + "/Music");
			fileOutput.writeObject(lastOpenVideoLocation = "C:/Users/" + System.getProperty("user.name") + "/Videos");
			
			fileOutput.close();
		}catch(Exception e) {
			System.out.println("EMPTY CATCH BLOCK: DataFile.creatNewDataFile(String)");
		}
	}

	public void saveToFile() {
		try {
			ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(new File("dataFiles/dataFile.pfd")));
			fileOutput.writeObject(lastOpenPictureLocation);
			fileOutput.writeObject(lastOpenAudioLocation);
			fileOutput.writeObject(lastOpenVideoLocation);
			
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
}
