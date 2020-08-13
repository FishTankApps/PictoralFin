package com.fishtankapps.pictoralfin.utilities;

import java.io.File;
import java.util.List;

import com.fishtankapps.pictoralfin.jComponents.JProgressDialog;
import com.fishtankapps.pictoralfin.jTimeLine.AudioClip;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AudioImporter {
	private PictoralFin pictoralFin;
	
	public AudioImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importAudio() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setInitialDirectory(new File(pictoralFin.getDataFile().getLastOpenAudioLocation()).getParentFile());
		fileChooser.setTitle("Import Audio");
		
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.ogg", "*.flac", "*.m4a"),
				new ExtensionFilter("All Files", "*"));
			
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		
		if(files != null) {
			importAudio(files);			
			pictoralFin.getDataFile().setLastOpenAudioLocation(files.get(0).getAbsolutePath());			
		}		
	}
	
	public void importAudio(List<File> files) {
		new Thread(()->{
			JProgressDialog progressDialog = new JProgressDialog("Importing Audio " + JProgressDialog.VALUE_IN_PARENTHESES, "Importing...", files.size());
			for(File file : files) {
				pictoralFin.getTimeLine().addAudioClip(new AudioClip(file, pictoralFin.getTimeLine()));
				progressDialog.moveForward();
			}	
			
			StatusLogger.logStatus(files.size() + " Audio File(s) Imported!");
		}).start();
	}
}
