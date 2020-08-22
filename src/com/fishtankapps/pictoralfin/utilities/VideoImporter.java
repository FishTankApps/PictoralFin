package com.fishtankapps.pictoralfin.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import com.fishtankapps.pictoralfin.jTimeLine.AudioClip;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.Frame;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class VideoImporter {
	
	private PictoralFin pictoralFin;
	public VideoImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importVideo() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setInitialDirectory(new File(pictoralFin.getDataFile().getLastOpenVideoLocation()).getParentFile());
		fileChooser.setTitle("Import Video");
		
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Video Files", "*.mp4", "*.flv", "*.mov", "*.avi", "*.wmv"),
				new ExtensionFilter("All Files", "*"));
		
				
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
		
		if(selectedFiles != null) {
			importVideo(selectedFiles);			
			pictoralFin.getDataFile().setLastOpenVideoLocation(selectedFiles.get(0).getAbsolutePath());				
		}		
	}
	
	public void importVideo(List<File> files) {	
		double widthRatio, heightRation, ratio;
		int count, numOfFrames;
		
		for(File videoFile : files) {		
			count = 1;
			
			// AUDIO:
			StatusLogger.logStatus("Extracting Audio...");
			File audioFile = VideoUtil.extractAudioFromVideo(videoFile);
			
			StatusLogger.logStatus("Importing Audio...");
			if(audioFile != null) {
				AudioClip audioClip = new AudioClip(audioFile, pictoralFin.getTimeLine());
				audioClip.setName(videoFile.getName() + " Audio");
				audioClip.setLength(audioClip.getMaxLength());
				pictoralFin.getTimeLine().addAudioClip(audioClip);	
			}
			
			// VIDEO:
			BufferedImage[] images = VideoUtil.videoToPictures(videoFile.getAbsolutePath());
			numOfFrames = images.length;
			int frameDurration = VideoUtil.getVideoFrameDurration(videoFile.getAbsolutePath());
			
			for(BufferedImage image : images) {
				try {
					StatusLogger.logStatus("Importing Frames... (" + count++ + "/" + numOfFrames + ")");
					if(image == null)
						continue;
					
					widthRatio = ((double) image.getWidth()) / pictoralFin.getSettings().getMaxPictureSize().getWidth();
					heightRation = ((double) image.getHeight()) / pictoralFin.getSettings().getMaxPictureSize().getHeight();
				
					if(image.getWidth() > pictoralFin.getSettings().getMaxPictureSize().getWidth() || image.getHeight() > pictoralFin.getSettings().getMaxPictureSize().getHeight()) {
						ratio = (widthRatio > heightRation) ? widthRatio : heightRation;								
						image = BufferedImageUtil.resizeBufferedImage(image, (int) (image.getWidth() / ratio), (int) (image.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
					}
					
					pictoralFin.getTimeLine().addFrame(new Frame(image, frameDurration));
				} catch (Exception e) {
					System.out.println("Empty Catch Block VideoImporter.importVideo(File[]): ");
					e.printStackTrace();
				}				
			}
			
			
			
			StatusLogger.logStatus("Video Imported!");
		}				
	}
}