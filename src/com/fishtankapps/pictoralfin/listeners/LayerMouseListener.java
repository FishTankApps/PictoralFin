package com.fishtankapps.pictoralfin.listeners;

import java.awt.image.BufferedImage;

public interface LayerMouseListener {

	public abstract void onMousePressed(int xOnImage, int yOnImage, BufferedImage layer);
	public abstract void onMouseReleased(int xOnImage, int yOnImage, BufferedImage layer);
	
}
