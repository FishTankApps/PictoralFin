package utilities;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jTimeLine.AudioClip;
import mainFrame.PictoralFin;

public class AudioImporter {
	private PictoralFin pictoralFin;
	
	public AudioImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importAudio() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(new File(pictoralFin.getDataFile().getLastOpenAudioLocation()));
		fileChooser.setDialogTitle("Import Music");
		fileChooser.setApproveButtonText("Import");
		
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Importable files", "pfkp", "mp3", "wav"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Music Files", "mp3", "wav"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Project files", "pfkp"));
		
				
		
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			importAudio(fileChooser.getSelectedFiles());			
			pictoralFin.getDataFile().setLastOpenAudioLocation(fileChooser.getSelectedFile().getAbsolutePath());			
		}		
	}
	
	public void importAudio(File[] files) {				
		for(File file : files) 
			pictoralFin.getTimeLine().addAudioClip(new AudioClip(file, pictoralFin.getTimeLine()));
				
	}
}
