package com.fishtankapps.pictoralfin.projectFileManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

import javax.swing.JOptionPane;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class PictoralFinProjectManager {

	private PictoralFinProjectManager() {}
		
	private static String staticFilePath;
	
	public static void saveProject(PictoralFin pictoralFin, String filePath) {
		
		Platform.runLater(() -> {
		
				staticFilePath = filePath;
				
				if(staticFilePath == null) {
					FileChooser fileChooser = new FileChooser();
					
					fileChooser.setInitialDirectory(new File(pictoralFin.getConfiguration().getDataFile().getLastOpenProjectLocation()).getParentFile());
					fileChooser.setTitle("Save Project As");
					fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Project Files", "*.pfp"),
							new ExtensionFilter("All Files", "*"));
					
					File selectedFile = fileChooser.showSaveDialog(null);
					
					if(selectedFile == null)
						return;
					
					staticFilePath = selectedFile.getPath();	
				}
				
				String name = new File(staticFilePath).getName();
				String[] splitName = name.split("\\.");
				
				if(splitName != null && splitName.length > 1) {
					if(!splitName[1].equals("pfp")) {
						int choice = JOptionPane.showConfirmDialog(null, "The file \"" + name +
								"\" does not have the correct extension.\nWould you like to rename it to:\n\""+splitName[0]+".pfp\"?",
								"Incorrect Extension", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
						
						if(choice == JOptionPane.OK_OPTION)
							staticFilePath = new File(staticFilePath).getParent() + "/" + splitName[0] + ".pfp";
					}
				}
				
				saveProjectToFile(pictoralFin, staticFilePath);	
				staticFilePath = null;	
			});
	}
	
	public static void saveProjectToFile(PictoralFin pictoralFin, String filePath) {
		try {
			RandomAccessFile saveLocation = new RandomAccessFile(filePath, "rw");		
			ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(saveLocation.getFD()));
			StatusLogger.logPrimaryStatus("Creating Project Object...");
			PictoralFinProject project = pictoralFin.getTimeLine().generatePictoralFinStaticProject();
			StatusLogger.logPrimaryStatus("Writing Project to File...");
			objectOutput.writeObject(project);			
			objectOutput.close();	
			saveLocation.close();
			
			StatusLogger.logPrimaryStatus("Project Saved!");
			System.gc();
			
			pictoralFin.getConfiguration().getDataFile().setLastOpenProjectLocation(filePath);
			
			pictoralFin.setOpenProjectFile(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void openProject(PictoralFin pictoralFin, String filePath) {
		
		Platform.runLater(() -> {		
				staticFilePath = filePath;
				if(filePath == null) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setInitialDirectory(new File(pictoralFin.getConfiguration().getDataFile().getLastOpenProjectLocation()).getParentFile());
					fileChooser.setTitle("Open Project");
					
					fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Project Files", "*.pfp"),
							new ExtensionFilter("All Files", "*"));
					
					File selectedFile = fileChooser.showOpenDialog(null);
					
					if(selectedFile == null)
						return;
					
					staticFilePath = selectedFile.getAbsolutePath();	
				}
				
				openProjectFromFile(pictoralFin, staticFilePath);
			});
		
		
	}
	
	public static void openProjectFromFile(PictoralFin pictoralFin, String filePath) {	
		try {
			RandomAccessFile openLocation = new RandomAccessFile(filePath, "rw");
			StatusLogger.logPrimaryStatus("Openning File...");
			ObjectInputStream objectOutput = new ObjectInputStream(new FileInputStream(openLocation.getFD()));
			pictoralFin.getTimeLine().loadPictoralFinProject((PictoralFinProject) objectOutput.readObject());			
			objectOutput.close();	
			openLocation.close();
			
			StatusLogger.logPrimaryStatus("Project Openned!");
			
			pictoralFin.getConfiguration().getDataFile().setLastOpenProjectLocation(filePath);
			pictoralFin.setOpenProjectFile(new File(filePath));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		pictoralFin.setOpenProjectFile(null);
	}
}
