package jComponents.pictureEditor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import listeners.PictureEditorMouseListener;
import objectBinders.Picture;

import static globalValues.GlobalVariables.settings;
import static globalValues.GlobalVariables.pfk;

public class PictureEditor extends JPanel {
	private static final long serialVersionUID = 8201924805919508645L;

	private PreviewSettings previewSettings;
	private Picture picture;
	private int pictureX = 100;
	private int pictureY = 100;
	
	public PictureEditor() {
		this.setLayout(new BorderLayout());
		this.setBackground(settings.getTheme().getPrimaryBaseColor());
		previewSettings = new PreviewSettings();
		
		this.add(previewSettings, BorderLayout.SOUTH);
		
		PictureEditorMouseListener peml = new PictureEditorMouseListener(this);
		this.addMouseListener(peml);
		this.addMouseWheelListener(peml);		
	}
		
	//--------{ OUTSIDE METHOD/TOOLS }-------------------------------------------------------------
	public void refresh() {
		this.setPreferredSize(this.getParent().getSize());	
		previewSettings.refresh();
		picture = pfk.getCurrentFrame();
		centerImage();
	}
	public void centerImage() {
		double heightRatio, widthRatio, ratio;
		int scaledPictureWidth, scaledPictureHeight;
		
		heightRatio = (this.getHeight() - previewSettings.getHeight() - 100) / (double) picture.getHeight();
		widthRatio = (this.getWidth() - 100) / (double) picture.getWidth();
		
		ratio = (heightRatio > widthRatio) ? widthRatio : heightRatio;
		
		previewSettings.setPictureScale(ratio * 100);
		
		scaledPictureWidth  = (int) (picture.getWidth()  * (previewSettings.getPictureScale() / 100.0));
		scaledPictureHeight = (int) (picture.getHeight() * (previewSettings.getPictureScale() / 100.0));
		
		pictureX = ((this.getWidth() -  scaledPictureWidth) / 2);
		pictureY = ((this.getHeight() - previewSettings.getHeight() - scaledPictureHeight) / 2);
		

		repaint();
	}
	public void attachFrameTimeLine() {
		previewSettings.attachFrameTimeLine();
	}
	public void detachFrameTimeLine() {
		previewSettings.detachFrameTimeLine();
	}
	
	//--------{ GETTERS }--------------------------------------------------------------------------
	public int getPictureX() {
		return pictureX;
	}
	public int getPictureY() {
		return pictureY;
	}
	public double getScale() {
		return previewSettings.getPictureScale();
	}
	
	//--------{ SETTERS }--------------------------------------------------------------------------
	public void setScale(double scale) {
		previewSettings.setPictureScale(scale);
	}
	public void setPictureX(int pictureX) {
		this.pictureX = pictureX;
	}
	public void setPictureY(int pictureY) {
		this.pictureY = pictureY;
	}
	
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		BufferedImage bi = picture.getImage(false);
		g.drawImage(bi, pictureX, pictureY, (int) (bi.getWidth() * (previewSettings.getPictureScale()/100)), (int) (bi.getHeight() * (previewSettings.getPictureScale()/100)), null);
	}
}
