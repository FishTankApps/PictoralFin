package com.fishtankapps.pictoralfin.utilities;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.fishtankapps.pictoralfin.jComponents.ImageChooser;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

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
		new Thread(() ->
					FileImporter.importFiles(files)
				).start();
		
	}
}
