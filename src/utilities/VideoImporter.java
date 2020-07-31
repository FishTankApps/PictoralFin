package utilities;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jTimeLine.AudioClip;
import mainFrame.PictoralFin;
import mainFrame.StatusLogger;
import objectBinders.Frame;

public class VideoImporter {
	
	private PictoralFin pictoralFin;
	public VideoImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importVideo() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(new File(pictoralFin.getDataFile().getLastOpenVideoLocation()));
		fileChooser.setDialogTitle("Import Video");
		fileChooser.setApproveButtonText("Import");
		
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Video Files", "mp4", "flv"));
		
				
		
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			new Thread(new Runnable() {
				public void run() {
					importVideo(fileChooser.getSelectedFiles());			
					pictoralFin.getDataFile().setLastOpenVideoLocation(fileChooser.getSelectedFile().getAbsolutePath());					
				}				
			}).start();					
		}		
	}
	
	public void importVideo(File[] files) {	
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
