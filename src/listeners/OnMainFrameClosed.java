package listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import mainFrame.PictoralFin;

public class OnMainFrameClosed implements WindowListener{
	
	private PictoralFin pictoralFin;
	
	public OnMainFrameClosed(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void windowClosing(WindowEvent arg0) {
		//dataFile.saveToFile();		
		
		pictoralFin.getSettings().saveSettings();
		pictoralFin.getDataFile().saveDataFile();
		
		try {
			pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().getJDriveExplorer().closeDevices();
		} catch (NullPointerException e) {}
		
		System.exit(0);
	}
	
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}	
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
}
