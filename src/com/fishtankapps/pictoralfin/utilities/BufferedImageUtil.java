package com.fishtankapps.pictoralfin.utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class BufferedImageUtil {
	private BufferedImageUtil(){}
	
	public static BufferedImage copyBufferedImage(BufferedImage toCopy){
		BufferedImage target = new BufferedImage(toCopy.getWidth(), toCopy.getHeight(), toCopy.getType());
		Graphics targetGraphics = target.getGraphics();
		targetGraphics.drawImage(toCopy, 0, 0, null);
		targetGraphics.dispose();
		
		return target;
	}
	public static BufferedImage setBufferedImageType(BufferedImage image, int type){
		if(image.getType() == type)
			return image;
		
		BufferedImage target = new BufferedImage(image.getWidth(), image.getHeight(), type);
		Graphics targetGraphics = target.getGraphics();
		targetGraphics.drawImage(image, 0, 0, null);
		targetGraphics.dispose();	
		
		image = null;
		
		return target;
	}
	public static BufferedImage imageToBufferedImage(Image toConvert){
		BufferedImage returnImage = new BufferedImage(toConvert.getWidth(null), toConvert.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
		Graphics returnImageGraphics = returnImage.getGraphics();
		returnImageGraphics.drawImage(toConvert, 0, 0, null);
		returnImageGraphics.dispose();	
		
		toConvert = null;
		
		return returnImage;
	}
	
	public static BufferedImage resizeBufferedImage(BufferedImage image, int width, int height, int scaleType){
		Image scaled = image.getScaledInstance(width, height, scaleType);
		image = null;
		
		return imageToBufferedImage(scaled);
	}
	
	/**
	 * Syntax:
	 * <p>
	 *     Pixels that are (0,0,0) [Black] will be set to the highlightColor,
	 *     <p>
	 *     Pixels that are anything else will be set to the backgroundColor.
	 */
	public static void applyColorThemeToImage(BufferedImage image, Color backgroundColor, Color highlightColor) {
		int backgroundRGB = backgroundColor.getRGB();
		int highlightRGB  = highlightColor.getRGB();
		
		final int blackRGB = -16777216;
		
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++) {
				if(image.getRGB(x, y) == blackRGB)
					image.setRGB(x, y, highlightRGB);
				else
					image.setRGB(x, y, backgroundRGB);
			}
	}
	public static void applyColorThemeToImage(BufferedImage image, Theme theme) {
		int backgroundRGB = theme.getPrimaryBaseColor().getRGB();
		int highlightRGB  = theme.getPrimaryBaseColor().darker().getRGB();
		
		final int blackRGB = -16777216;
		
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++) {
				if(image.getRGB(x, y) == blackRGB)
					image.setRGB(x, y, highlightRGB);
				else
					image.setRGB(x, y, backgroundRGB);
			}
	}
	
}
