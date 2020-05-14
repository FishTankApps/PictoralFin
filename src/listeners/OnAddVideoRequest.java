package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mainFrame.PictoralFin;

public class OnAddVideoRequest implements ActionListener {
	private PictoralFin pictoralFin;
	
	public OnAddVideoRequest(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		pictoralFin.getVideoImporter().importVideo();
	}
}
