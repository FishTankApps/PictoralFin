package com.fishtankapps.pictoralfin.utilities;

import java.io.File;
import java.util.List;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class VideoImporter {
	
	private PictoralFin pictoralFin;
	public VideoImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importVideo() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setInitialDirectory(new File(pictoralFin.getConfiguration().getDataFile().getLastOpenVideoLocation()).getParentFile());
		fileChooser.setTitle("Import Video");
		
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Video Files", "*.mp4", "*.flv", "*.mov", "*.avi", "*.wmv"),
				new ExtensionFilter("All Files", "*"));
		
				
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
		
		if(selectedFiles != null) {
			importVideo(selectedFiles);			
			pictoralFin.getConfiguration().getDataFile().setLastOpenVideoLocation(selectedFiles.get(0).getAbsolutePath());				
		}		
	}
	
	public void importVideo(List<File> files) {	
		FileImporter.importFiles(files);				
	}
}
