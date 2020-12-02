package com.fishtankapps.pictoralfin.utilities;

import java.io.File;
import java.util.List;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AudioImporter {
	private PictoralFin pictoralFin;
	
	public AudioImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importAudio() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setInitialDirectory(new File(pictoralFin.getConfiguration().getDataFile().getLastOpenAudioLocation()).getParentFile());
		fileChooser.setTitle("Import Audio");
		
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.ogg", "*.flac", "*.m4a"),
				new ExtensionFilter("All Files", "*"));
			
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		
		if(files != null) {
			importAudio(files);			
			pictoralFin.getConfiguration().getDataFile().setLastOpenAudioLocation(files.get(0).getAbsolutePath());			
		}		
	}
	
	public void importAudio(List<File> files) {
		new Thread(()->{
			FileImporter.importFiles(files);
		}).start();
	}
}
