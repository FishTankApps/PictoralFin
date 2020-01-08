package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mainFrame.PictoralFin;

public class OnAddPictureRequest implements ActionListener { 
	
	private PictoralFin pictoralFin;
	
	public OnAddPictureRequest(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		pictoralFin.getPictureImporter().importPictures();
	}

}
