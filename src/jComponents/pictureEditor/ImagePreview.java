package jComponents.pictureEditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import mainFrame.PictoralFin;
import objectBinders.Frame;

public class ImagePreview extends JComponent {

	private static final long serialVersionUID = -9021549379360151600L;
	private static final int PREF_BOARDER = 20;
	
	private Frame selectedFrame = null;
	private BufferedImage pictoralFinIcon;
	
	public ImagePreview(PictoralFin pictoralFin) {
		pictoralFinIcon = pictoralFin.getGlobalImageKit().pictoralFinIcon;
		
		setMinimumSize(new Dimension(800, 100));		
	}
	
	public void setSelectedFrame(Frame frame) {
		this.selectedFrame = frame;
	}
	
	public void paintComponent(Graphics g) {
		if(selectedFrame == null) {
			int displayWidth = getWidth() - PREF_BOARDER * 2;
			int displayHeight = getHeight() - PREF_BOARDER * 2 - 10;
			
			double ratio = (displayHeight / (double) pictoralFinIcon.getHeight()) < (displayWidth / (double) pictoralFinIcon.getWidth()) ? 
					(displayHeight / (double) pictoralFinIcon.getHeight()) : 
					(displayWidth / (double) pictoralFinIcon.getWidth());
			
			int adjustedImageWidth = (int) (pictoralFinIcon.getWidth() * ratio); 
			int adjustedImageHeight = (int) (pictoralFinIcon.getHeight() * ratio);
			
			g.drawImage(pictoralFinIcon, (displayWidth - adjustedImageWidth) / 2 + PREF_BOARDER, (displayHeight - adjustedImageHeight) / 2 + PREF_BOARDER ,
					adjustedImageWidth, adjustedImageHeight, null);
		} else {
			int displayWidth = getWidth() - PREF_BOARDER * 2;
			int displayHeight = getHeight() - PREF_BOARDER * 2 - 10;
			
			BufferedImage image = selectedFrame.getLayer(0);
			
			double ratio = (displayHeight / (double) image.getHeight()) < (displayWidth / (double) image.getWidth()) ? 
					(displayHeight / (double) image.getHeight()) : 
					(displayWidth / (double) image.getWidth());
			
			int adjustedImageWidth = (int) (image.getWidth() * ratio); 
			int adjustedImageHeight = (int) (image.getHeight() * ratio);
			
			g.drawImage(image, (displayWidth - adjustedImageWidth) / 2 + PREF_BOARDER, (displayHeight - adjustedImageHeight) / 2 + PREF_BOARDER ,
					adjustedImageWidth, adjustedImageHeight, null);
		}
	}
	
}
