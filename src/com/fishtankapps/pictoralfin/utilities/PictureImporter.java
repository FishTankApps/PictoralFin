package com.fishtankapps.pictoralfin.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.fishtankapps.pictoralfin.customExceptions.CanceledException;
import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.jComponents.ImageChooser;
import com.fishtankapps.pictoralfin.jComponents.JProgressDialog;
import com.fishtankapps.pictoralfin.jTimeLine.JTimeLine;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.Frame;

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
			
			width = frame.getWidth() / pictoralFin.getConfiguration().getMaxPictureSize().getWidth();
			height = frame.getHeight() / pictoralFin.getConfiguration().getMaxPictureSize().getHeight();
			
			if(frame.getWidth() > pictoralFin.getConfiguration().getMaxPictureSize().getWidth() && frame.getHeight() > pictoralFin.getConfiguration().getMaxPictureSize().getHeight()) {
				ratio = (width > height) ? width : height;								
				frame = BufferedImageUtil.resizeBufferedImage(frame, (int) (frame.getWidth() / ratio), (int) (frame.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
			}
			
			if(frame == null)
				throw new Exception("frame = NULL");
			pictoralFin.getTimeLine().addFrame(frame);
		} catch (Exception e) {
			System.out.println("Error: PictureImporter.simpleImportPicture()" +
							   "Message: " + e.getMessage());
		}
		
	}
	
	public void importPictures() {
		
		File[] files = ImageChooser.openFiles(pictoralFin);	
		if(files != null)
			importPictures(files);
	}
	public void importPictures(File[] files) {	
		new Thread(new Runnable() {
			public void run() {
				importPictures(files, pictoralFin.getTimeLine());	
			}			
		}).start();
		
	}
	
	private void importPictures(File[] files, JTimeLine frameTimeLine) {
			
		StatusLogger.logStatus("Importing Pictures...");
		
		JProgressDialog jpb = new JProgressDialog("Importing Pictures " + JProgressDialog.PERCENT, "Importing...", files.length);
		ArrayList<String> failedFiles = new ArrayList<>();
		double widthRatio, heightRation, ratio;
		BufferedImage image;
		int fileImportedCount = 0;

		jpb.setIcon(GlobalImageKit.pictoralFinIcon);		
		
		try {
			for(File file : files) {	
				
				try {
					if(file.getName().endsWith(".pff")) {
						ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
						Frame frame = (Frame) input.readObject();
						
						input.close();
						
						frameTimeLine.addFrame(frame);
					} else {
						image = ImageIO.read(file);
						
						widthRatio = ((double) image.getWidth()) / pictoralFin.getConfiguration().getMaxPictureSize().getWidth();
						heightRation = ((double) image.getHeight()) / pictoralFin.getConfiguration().getMaxPictureSize().getHeight();
						
						if(image.getWidth() > pictoralFin.getConfiguration().getMaxPictureSize().getWidth() || image.getHeight() > pictoralFin.getConfiguration().getMaxPictureSize().getHeight()) {
							ratio = (widthRatio > heightRation) ? widthRatio : heightRation;								
							image = BufferedImageUtil.resizeBufferedImage(image, (int) (image.getWidth() / ratio), (int) (image.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
						}
						
						if(image == null)
							throw new Exception("frame = NULL");
						
						frameTimeLine.addFrame(image);		
					}
					
					fileImportedCount++;
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
			
			Utilities.showMessage(failedFiles.size() + " Files failed to import:\n" + fileList + "The others files succesfully where imported", failedFiles.size() + " Import(s) Failed", true);
		}	
		
		StatusLogger.logStatus(fileImportedCount + " Picture(s) Imported!");
	}
}
