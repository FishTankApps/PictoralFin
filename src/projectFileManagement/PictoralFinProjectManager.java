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

public class PictoralFinProjectManager {

	private PictoralFinProjectManager() {}
		
	public static File saveProject(PictoralFin pictoralFin, String filePath) {
		File saveLocation = null;
		
		if(filePath == null) {
			JFileChooser jfc = new JFileChooser();
			
			jfc.setCurrentDirectory(new File(pictoralFin.getDataFile().getLastOpenProjectLocation()));
			jfc.setDialogTitle("Save Project As");
			jfc.setApproveButtonText("Save");
			jfc.setAcceptAllFileFilterUsed(true);
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("Project Files", "pfp"));
			
			if(jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
				return null;
			
			if(jfc.getSelectedFile().exists()) {
				int choice = JOptionPane.showConfirmDialog(null, "The file \"" + jfc.getSelectedFile().getName() +
						"\" already exists.\nAre you sure you want  to replace this file?",
						"File Already Exists", JOptionPane.INFORMATION_MESSAGE);
				
				if(choice != JOptionPane.YES_OPTION) {
					return null;
				}					
			}
			filePath = jfc.getSelectedFile().getPath();	
		}
		
		saveLocation = new File(filePath);
		
		String[] splitName = saveLocation.getName().split("\\.");
		if(splitName != null && splitName.length > 1) {
			if(!saveLocation.getPath().split("\\.")[1].equals("pfp")) {
				int choice = JOptionPane.showConfirmDialog(null, "The file \"" + saveLocation.getName() +
						"\" does not have the correct extension.\nWould you like to rename it to:\n\""+splitName[0]+".pfp\"?",
						"Incorrect Extension", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION);
				
				if(choice == JOptionPane.OK_OPTION)
					saveLocation = new File(saveLocation.getParent() + "\\" + splitName[0] + ".pfp");
			}
		}
		
		
				
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(saveLocation));
			pictoralFin.setStatus("Creating Project Object...");
			PictoralFinStaticProject project = pictoralFin.getTimeLine().generatePictoralFinStaticProject();
			pictoralFin.setStatus("Writing Project to File...");
			objectOutput.writeObject(project);			
			objectOutput.close();	
			pictoralFin.setStatus("Project Saved!");
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return null;		
	}
	
	public static File openProject(PictoralFin pictoralFin, String filePath) {
		File openLocation = null;
		JFileChooser jfc = new JFileChooser();
		
		jfc.setCurrentDirectory(new File(pictoralFin.getDataFile().getLastOpenProjectLocation()));
		jfc.setDialogTitle("Open Project");
		jfc.setApproveButtonText("Open");
		jfc.setAcceptAllFileFilterUsed(true);
		jfc.addChoosableFileFilter(new FileNameExtensionFilter("Project Files", "pfp"));
		
		jfc.showOpenDialog(null);
		
		openLocation = jfc.getSelectedFile();
		
		try {
			pictoralFin.setStatus("Openning File...");
			ObjectInputStream objectOutput = new ObjectInputStream(new FileInputStream(openLocation));
			pictoralFin.getTimeLine().loadPictoralFinStaticProject((PictoralFinStaticProject) objectOutput.readObject());			
			objectOutput.close();	
			pictoralFin.setStatus("Project Openned!");
			return openLocation;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return null;
	}

	public static void clearTempFiles() {
		File tempFolder = new File("projectTemp");
		for(File tempFile : tempFolder.listFiles())
			tempFile.delete();
	}
}
