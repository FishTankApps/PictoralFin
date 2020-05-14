package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import mainFrame.PictoralFin;
import mainFrame.StatusLogger;

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
			ArrayList<BufferedImage> images = VideoUtil.videoToPictures(videoFile.getAbsolutePath());
			numOfFrames = images.size();
			for(BufferedImage frame : images) {
				try {
					StatusLogger.logStatus("Importing Frames... (" + count++ + "/" + numOfFrames + ")");
					if(frame == null)
						continue;
					
					widthRatio = ((double) frame.getWidth()) / pictoralFin.getSettings().getMaxPictureSize().getWidth();
					heightRation = ((double) frame.getHeight()) / pictoralFin.getSettings().getMaxPictureSize().getHeight();
				
					if(frame.getWidth() > pictoralFin.getSettings().getMaxPictureSize().getWidth() || frame.getHeight() > pictoralFin.getSettings().getMaxPictureSize().getHeight()) {
						ratio = (widthRatio > heightRation) ? widthRatio : heightRation;								
						frame = BufferedImageUtil.resizeBufferedImage(frame, (int) (frame.getWidth() / ratio), (int) (frame.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
					}
				
					pictoralFin.getTimeLine().addFrame(frame);
				} catch (Exception e) {
					System.out.println("Empty Catch Block VideoImporter.importVideo(File[]): ");
					e.printStackTrace();
				}				
			}			
		}				
	}
}
