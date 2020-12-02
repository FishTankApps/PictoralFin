package com.fishtankapps.pictoralfin.jComponents;

import java.io.File;
import java.util.List;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.utilities.FileUtils;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImageChooser {	
	
	public static File openFile(PictoralFin pictoralFin) {
		
		File lastOpened = new File(pictoralFin.getConfiguration().getDataFile().getLastOpenedPictureLocation());
		FileChooser fileChooser = new FileChooser();		
		
		fileChooser.setTitle("Import Images");
		fileChooser.setInitialDirectory((lastOpened.isDirectory()) ? lastOpened : lastOpened.getParentFile());
		
		String[] importableImageFiles = FileUtils.getCompatibleImageFiles();
		String[] importableFiles = new String[importableImageFiles.length + 2];
		
		importableFiles[0] = "*.pfkp";
		importableFiles[1] = "*.pff";
		
		for(int index = 0; index < importableImageFiles.length; index++)
			importableFiles[index + 2] = importableImageFiles[index];
		
		
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Importable Files", importableFiles),
				new ExtensionFilter("Image Files", importableImageFiles),
				new ExtensionFilter("Project Files", "*.pfkp", "*.pff"),
				new ExtensionFilter("All Files", "*"));

		File selectedFile = fileChooser.showOpenDialog(null);
		
		if(selectedFile != null)
			pictoralFin.getConfiguration().getDataFile().setLastOpenedPictureLocation(selectedFile.getAbsolutePath());
		
		return selectedFile;
	}
	
	public static File[] openFiles(PictoralFin pictoralFin) {
		File lastOpened = new File(pictoralFin.getConfiguration().getDataFile().getLastOpenedPictureLocation());
		FileChooser fileChooser = new FileChooser();		
		
		fileChooser.setTitle("Import Images");
		fileChooser.setInitialDirectory((lastOpened.isDirectory()) ? lastOpened : lastOpened.getParentFile());
		
		String[] importableImageFiles = FileUtils.getCompatibleImageFiles();
		String[] importableFiles = new String[importableImageFiles.length + 2];
		
		importableFiles[0] = "*.pfkp";
		importableFiles[1] = "*.pff";
		
		for(int index = 0; index < importableImageFiles.length; index++)
			importableFiles[index + 2] = importableImageFiles[index];
		
		
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Importable Files", importableFiles),
				new ExtensionFilter("Image Files", importableImageFiles),
				new ExtensionFilter("Project Files", "*.pfkp", "*.pff"),
				new ExtensionFilter("All Files", "*"));

		List<File> selectedFileList = fileChooser.showOpenMultipleDialog(null);		
		
		if(selectedFileList != null) {
			File[] selectedFileArray = selectedFileList.toArray(new File[selectedFileList.size()]);
			pictoralFin.getConfiguration().getDataFile().setLastOpenedPictureLocation(selectedFileArray[0].getAbsolutePath());
			return selectedFileArray;
		}
		
		return null;		
	}
}
