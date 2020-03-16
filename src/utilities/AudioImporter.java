package utilities;

import java.io.File;

import javax.swing.JFileChooser;

import jTimeLine.AudioClip;
import mainFrame.PictoralFin;

public class AudioImporter {
	private PictoralFin pictoralFin;
	
	public AudioImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importAudio() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(pictoralFin.getDataFile().getLastOpenAudioLocation()));
		fileChooser.showOpenDialog(null);
		
		if(fileChooser.getSelectedFile() != null) {
			pictoralFin.getTimeLine().addAudioClip(new AudioClip(fileChooser.getSelectedFile(), pictoralFin.getTimeLine()));
			
			pictoralFin.getDataFile().setLastOpenAudioLocation(fileChooser.getSelectedFile().getAbsolutePath());
		}		
	}
}
