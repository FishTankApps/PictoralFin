package listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import mainFrame.PictoralFin;
import projectFileManagement.PictoralFinProjectManager;

public class OnMainFrameClosed implements WindowListener{
	
	private PictoralFin pictoralFin;
	
	public OnMainFrameClosed(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void windowClosing(WindowEvent arg0) {
		//dataFile.saveToFile();	
		
		if(!pictoralFin.getTimeLine().isEmpty()) {
			int choice = JOptionPane.showConfirmDialog(null, "Would you like to save the current project?", "Save", JOptionPane.INFORMATION_MESSAGE);
			
			if(choice == JOptionPane.YES_OPTION) 
				pictoralFin.saveProject();
			else if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
				return;
		}
		
		pictoralFin.close();
		pictoralFin.getSettings().saveSettings();
		pictoralFin.getDataFile().saveDataFile();
		
		try {
			pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().getJDriveExplorer().closeDevices();
		} catch (NullPointerException e) {}
		
	    PictoralFinProjectManager.clearTempFiles();		
		System.exit(0);
	}
	
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}	
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
}
