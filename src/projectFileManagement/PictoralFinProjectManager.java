package projectFileManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import mainFrame.PictoralFin;
import mainFrame.StatusLogger;

public class PictoralFinProjectManager {

	private PictoralFinProjectManager() {}
		
	private static String staticFilePath;
	
	public static void saveProject(PictoralFin pictoralFin, String filePath) {
		staticFilePath = filePath;
		if(staticFilePath == null) {
			JFileChooser jfc = new JFileChooser();
			
			jfc.setCurrentDirectory(new File(pictoralFin.getDataFile().getLastOpenProjectLocation()));
			jfc.setDialogTitle("Save Project As");
			jfc.setApproveButtonText("Save");
			jfc.setAcceptAllFileFilterUsed(true);
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("Project Files", "pfp"));
			
			if(jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
				return;
			
			if(jfc.getSelectedFile().exists()) {
				int choice = JOptionPane.showConfirmDialog(null, "The file \"" + jfc.getSelectedFile().getName() +
						"\" already exists.\nAre you sure you want  to replace this file?",
						"File Already Exists", JOptionPane.INFORMATION_MESSAGE);
				
				if(choice != JOptionPane.YES_OPTION) {
					return;
				}					
			}
			staticFilePath = jfc.getSelectedFile().getPath();	
		}
		String name = new File(staticFilePath).getName();
		String[] splitName = name.split("\\.");
		
		if(splitName != null && splitName.length > 1) {
			if(!splitName[1].equals("pfp")) {
				int choice = JOptionPane.showConfirmDialog(null, "The file \"" + name +
						"\" does not have the correct extension.\nWould you like to rename it to:\n\""+splitName[0]+".pfp\"?",
						"Incorrect Extension", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
				
				if(choice == JOptionPane.OK_OPTION)
					staticFilePath = new File(staticFilePath).getParent() + "\\" + splitName[0] + ".pfp";
			}
		}
		
		new Thread(new Runnable() {
			public void run() {
				saveProjectToFile(pictoralFin, staticFilePath);	
				staticFilePath = null;				
			}			
		}).start();
	}
	
	public static void saveProjectToFile(PictoralFin pictoralFin, String filePath) {				
		try {
			File saveLocation = new File(filePath);		
			ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(saveLocation));
			StatusLogger.logStatus("Creating Project Object...");
			PictoralFinStaticProject project = pictoralFin.getTimeLine().generatePictoralFinStaticProject();
			StatusLogger.logStatus("Writing Project to File...");
			objectOutput.writeObject(project);			
			objectOutput.close();	
			StatusLogger.logStatus("Project Saved!");
			System.gc();
			pictoralFin.getDataFile().setLastOpenProjectLocation(saveLocation.getParent());
			
			pictoralFin.setOpenProjectFile(saveLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void openProject(PictoralFin pictoralFin, String filePath) {
		staticFilePath = filePath;
		if(staticFilePath == null) {
			JFileChooser jfc = new JFileChooser();
			jfc.setCurrentDirectory(new File(pictoralFin.getDataFile().getLastOpenProjectLocation()));
			jfc.setDialogTitle("Open Project");
			jfc.setApproveButtonText("Open");
			jfc.setAcceptAllFileFilterUsed(true);
			
			FileNameExtensionFilter projectFiles = new FileNameExtensionFilter("Project Files", "pfp");
			jfc.addChoosableFileFilter(projectFiles);
			jfc.setFileFilter(projectFiles);
			
			if(jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
				return;
			
			staticFilePath = jfc.getSelectedFile().getAbsolutePath();
		}
		
		new Thread(new Runnable() {
			public void run() {
				openProjectFromFile(pictoralFin, staticFilePath);	
				staticFilePath = null;				
			}			
		}).start();
	}
	
	public static void openProjectFromFile(PictoralFin pictoralFin, String filePath) {	
		try {
			File openLocation = new File(filePath);
			StatusLogger.logStatus("Openning File...");
			ObjectInputStream objectOutput = new ObjectInputStream(new FileInputStream(openLocation));
			pictoralFin.getTimeLine().loadPictoralFinStaticProject((PictoralFinStaticProject) objectOutput.readObject());			
			objectOutput.close();	
			StatusLogger.logStatus("Project Openned!");
			
			pictoralFin.getDataFile().setLastOpenProjectLocation(openLocation.getParent());
			pictoralFin.setOpenProjectFile(openLocation);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		pictoralFin.setOpenProjectFile(null);
	}
}
