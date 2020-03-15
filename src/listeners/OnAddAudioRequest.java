package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mainFrame.PictoralFin;

public class OnAddAudioRequest implements ActionListener {

	private PictoralFin pictoralFin;

	public OnAddAudioRequest(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		pictoralFin.getAudioImporter().importAudio();
	}

}
