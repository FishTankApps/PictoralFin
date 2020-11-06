package com.fishtankapps.pictoralfin.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

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
			File[] images = VideoUtil.videoToImageFiles(videoFile.getAbsolutePath());
			numOfFrames = images.length;
			int frameDurration = VideoUtil.getVideoFramesPerSecond(videoFile.getAbsolutePath());
			BufferedImage image;
			
			for(File imageFile : images) {
				try {
					StatusLogger.logStatus("Importing Frames... (" + count++ + "/" + numOfFrames + ")");
					image = ImageIO.read(imageFile);
					
					if(image == null)
						continue;
					
					widthRatio = ((double) image.getWidth()) / pictoralFin.getConfiguration().getMaxPictureSize().getWidth();
					heightRation = ((double) image.getHeight()) / pictoralFin.getConfiguration().getMaxPictureSize().getHeight();
					
					if(image.getWidth() > pictoralFin.getConfiguration().getMaxPictureSize().getWidth() || image.getHeight() > pictoralFin.getConfiguration().getMaxPictureSize().getHeight()) {
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
