package mainFrame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import JTimeLine.JTimeLine;
import customExceptions.CanceledException;
import jComponents.JFileChooserWithImagePreview;
import jComponents.JProgressDialog;
import utilities.BufferedImageUtil;
import utilities.Utilities;
import videoTools.VideoTools;

import static globalValues.GlobalVariables.*;

public class PictureImporter {
	
	private PictoralFin pictoralFin;
	
	PictureImporter(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void importPictures() {
		File[] files = new JFileChooserWithImagePreview().openFiles();	
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
		if(noneHaveBeenAdded)
			frameTimeLine.removeFrame(0);
			
		JProgressDialog jpb = new JProgressDialog("Importing Pictures " + JProgressDialog.PERCENT, "Importing...", files.length);
		ArrayList<String> failedFiles = new ArrayList<>();
		double width, height, ratio;
		BufferedImage frame;
		
		jpb.setIcon(orca);		
		
		try {
			for(File file : files) {	
				
				try {				
					if(file.getAbsolutePath().endsWith(".pfkp")) {
						
					} else if (file.getAbsolutePath().endsWith(".mp4")) {
						for(BufferedImage bi : VideoTools.mp4ToPictures(file.getAbsolutePath()))
							frameTimeLine.addFrame(bi);
					}else {
						frame = ImageIO.read(file);
						
						width = frame.getWidth() / settings.getMaxPictureSize().getWidth();
						height = frame.getHeight() / settings.getMaxPictureSize().getHeight();
						
						if(frame.getWidth() > settings.getMaxPictureSize().getWidth() && frame.getHeight() > settings.getMaxPictureSize().getHeight()) {
							ratio = (width > height) ? width : height;								
							frame = BufferedImageUtil.resizeBufferedImage(frame, (int) (frame.getWidth() / ratio), (int) (frame.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
						}
						
						if(frame == null)
							throw new Exception("frame = NULL");
						frameTimeLine.addFrame(frame);
					}
				}catch(Exception ignore) {
					Utilities.showMessage(ignore.getMessage(), "ERROR", true);
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
		
		
		pfk.getVideoEditor().refresh();
	}
}
