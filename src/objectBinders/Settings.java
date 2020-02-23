package objectBinders;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Settings implements Serializable{
	private static final long serialVersionUID = -1163643096786559533L;
	
	private Theme theme;
	private Dimension maxPictureSize;
	
	private Settings() {
		theme = Theme.OCEAN_THEME;
		maxPictureSize = new Dimension(720, 1280);
	}

	public final Theme getTheme() {
		return theme;
	}
	public final void setTheme(Theme theme) {
		this.theme = theme;
	}

	public final Dimension getMaxPictureSize() {
		return maxPictureSize;
	}
	public final void setMaxPictureSize(Dimension maxPictureSize) {
		this.maxPictureSize = maxPictureSize;
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
}
