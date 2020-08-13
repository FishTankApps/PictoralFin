package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class LayerButton extends JComponent implements MouseListener {

	private static final long serialVersionUID = -6457692933405341223L;	
	
	static final byte ADD_LAYER_INDEX = -1;
	static final byte LEFT = 0;
	static final byte RIGHT = 1;
	static final byte END = 2;
	static final byte BEGINNING = 3;
	
	private byte layerIndex;
	
	private BufferedImage layer;
	private Theme theme;
	
	private int thumbnailSize = 200;
	
	private boolean selected = false;
	private boolean highlighted = false;
	
	public LayerButton(BufferedImage layer, Theme theme, byte layerIndex, int buttonsize) {
		this.layer = layer;
		this.theme = theme;
		this.layerIndex = layerIndex;
		
		enableInputMethods(true);   
		addMouseListener(this);
		setFocusable(true);
		
		setThumbnailSize(buttonsize);
	}
	
	public byte getLayerIndex() {
		return layerIndex;
	}
	
	public BufferedImage getLayer() {
		return layer;
	}
	
	public void setThumbnailSize(int newSize) {
		thumbnailSize = newSize;
		
		setPreferredSize(new Dimension(newSize,newSize));
		setMinimumSize(new Dimension(newSize,newSize));
		setMaximumSize(new Dimension(newSize,newSize));
		
		repaint();
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public void paint(Graphics g) {		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(thumbnailSize / 40));
		
		g2d.setColor((selected) ? theme.getSecondaryHighlightColor() : theme.getPrimaryHighlightColor());
		
		if(highlighted) 
			g2d.setColor(g2d.getColor().brighter().brighter());		
		
		if(theme.isSharp()) {					
			g2d.drawImage(layer, 0, 0, thumbnailSize, thumbnailSize, null);
			g2d.drawRect(0, 0, thumbnailSize, thumbnailSize);
		} else {	
			Color boarderColor = g2d.getColor();
			g2d.drawImage(layer, 0, 0, thumbnailSize, thumbnailSize, null);	
			
			g2d.setColor(theme.getPrimaryBaseColor());
			g2d.drawRect(0, 0, thumbnailSize, thumbnailSize);	
			
			g2d.setColor(boarderColor);
			g2d.drawRoundRect(0, 0, thumbnailSize, thumbnailSize, thumbnailSize / 7, thumbnailSize / 7);	
		}		
	}
	
	public void mouseClicked(MouseEvent mouseEvent) {
		LayerSelecter layerSelector = (LayerSelecter) getParent().getParent().getParent().getParent();
		layerSelector.onLayerButtonClicked(this);
		
		requestFocus();		
		repaint();
	}
	public void mouseEntered(MouseEvent arg0) {		
		highlighted = true;
		repaint();		
	}
	
	public void mouseExited(MouseEvent arg0) {		
		highlighted = false;
		repaint();		
	}	
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
