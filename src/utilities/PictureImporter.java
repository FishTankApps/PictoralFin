package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import customExceptions.CanceledException;
import jComponents.JFileChooserWithImagePreview;
import jComponents.JProgressDialog;
import jTimeLine.JTimeLine;
import mainFrame.PictoralFin;

public class PictureImporter {
	
	private PictoralFin pictoralFin;
	
	public PictureImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void simpleImportPicture(File picture) {
		try {
			double width, height, ratio;
			BufferedImage frame;
			frame = ImageIO.read(picture);
			
			width = frame.getWidth() / pictoralFin.getSettings().getMaxPictureSize().getWidth();
			height = frame.getHeight() / pictoralFin.getSettings().getMaxPictureSize().getHeight();
			
			if(frame.getWidth() > pictoralFin.getSettings().getMaxPictureSize().getWidth() && frame.getHeight() > pictoralFin.getSettings().getMaxPictureSize().getHeight()) {
				ratio = (width > height) ? width : height;								
				frame = BufferedImageUtil.resizeBufferedImage(frame, (int) (frame.getWidth() / ratio), (int) (frame.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
			}
			
			if(frame == null)
				throw new Exception("frame = NULL");
			pictoralFin.getTimeLine().addFrame(frame);
			pictoralFin.updateMemoryUsage();
		} catch (Exception e) {
			System.out.println("Error: PictureImporter.simpleImportPicture()" +
							   "Message: " + e.getMessage());
		}
		
	}
	
	public void importPictures() {
		File[] files = JFileChooserWithImagePreview.openFiles(pictoralFin);	
		if(files != null)
			importPictures(files);
	}
	public void importPictures(File[] files) {	
		new Thread(new Runnable() {
			public void run() {
				importFiles(files, pictoralFin.getTimeLine());	
			}			
		}).start();
		
	}
	
	private void importFiles(File[] files, JTimeLine frameTimeLine) {
			
		JProgressDialog jpb = new JProgressDialog("Importing Pictures " + JProgressDialog.PERCENT, "Importing...", files.length);
		ArrayList<String> failedFiles = new ArrayList<>();
		double widthRatio, heightRation, ratio;
		BufferedImage frame;
		
		jpb.setIcon(pictoralFin.getGlobalImageKit().pictoralFinIcon);		
		
		try {
			for(File file : files) {	
				
				try {				
					if(file.getAbsolutePath().endsWith(".pfkp")) {
						
					} else if (file.getAbsolutePath().endsWith(".mp4")) {
						//for(BufferedImage bi : VideoTools.mp4ToPictures(file.getAbsolutePath()))
						//	frameTimeLine.addFrame(bi);
					} else {
						frame = ImageIO.read(file);
						
						widthRatio = ((double) frame.getWidth()) / pictoralFin.getSettings().getMaxPictureSize().getWidth();
						heightRation = ((double) frame.getHeight()) / pictoralFin.getSettings().getMaxPictureSize().getHeight();
						
						if(frame.getWidth() > pictoralFin.getSettings().getMaxPictureSize().getWidth() || frame.getHeight() > pictoralFin.getSettings().getMaxPictureSize().getHeight()) {
							ratio = (widthRatio > heightRation) ? widthRatio : heightRation;								
							frame = BufferedImageUtil.resizeBufferedImage(frame, (int) (frame.getWidth() / ratio), (int) (frame.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
						}
						
						if(frame == null)
							throw new Exception("frame = NULL");
						frameTimeLine.addFrame(frame);
					}
				}catch(Exception ignore) {
					Utilities.showMessage(ignore.getMessage(), "ERROR", true);
					ignore.printStackTrace();
					failedFiles.add(file.getName());
				}
				
				jpb.moveForward();				
			}
			
		} catch (CanceledException ce) {}		
		
		
		//=======[ DIALOG FOR FAILED FILES ]==========
		if(failedFiles.size() == 1) {
			Utilities.showMessage("A File failed to import:\n" + failedFiles.get(0) + "\nThe others files succesfully where imported", "1 Import Failed", true);
		}else if (failedFiles.size() > 1) {
			String fileList = "";
			for(String fileName: failedFiles)
				fileList += fileName + "\n";
			
			Utilities.showMessage(failedFiles.size() + " Files failed to import:\n" + fileList + "The others files succesfully where imported", failedFiles.size() + " Import Failed", true);
		}	
	}
}
