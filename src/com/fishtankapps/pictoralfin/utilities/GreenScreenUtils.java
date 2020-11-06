package com.fishtankapps.pictoralfin.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GreenScreenUtils {

	private GreenScreenUtils() {}
	
	public static BufferedImage applyRGBValueFilterToImage(BufferedImage i, Color targetColor, int redUpper, int redLower, 
			int greenUpper, int greenLower, int blueUpper, int blueLower) {
		
		int redTemp, blueTemp, greenTemp, rgbTemp, x, y;
		int targetRed, targetBlue, targetGreen;
		
		int jetBlackRGB = Color.BLACK.getRGB();
		
		targetRed =   targetColor.getRed();
		targetGreen = targetColor.getGreen();
		targetBlue =  targetColor.getBlue();
		
		for(x = 0; x < i.getWidth() ; x++) 
			for(y = 0; y < i.getHeight() ; y++) {
				rgbTemp = i.getRGB(x, y);
				redTemp   = ((rgbTemp >> 16) & 0xFF);
				greenTemp = ((rgbTemp >> 8)  & 0xFF);
				blueTemp  = (rgbTemp         & 0xFF);
				
				
				if(redTemp - targetRed < redUpper)
					if(targetRed - redTemp < redLower)
						if(greenTemp - targetGreen < greenUpper)
							if(targetGreen - greenTemp < greenLower)
								if(blueTemp - targetBlue < blueUpper)
									if(targetBlue - blueTemp < blueLower)
										i.setRGB(x, y, jetBlackRGB);
			}
		
		
		return i;
	}
	
	public static BufferedImage applyRGBValueFilterToClearImage(BufferedImage i, Color targetColor, int redUpper, int redLower, 
			int greenUpper, int greenLower, int blueUpper, int blueLower){
		
		BufferedImage clearImage = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int redTemp, blueTemp, greenTemp, rgbTemp, x, y;
		int targetRed, targetBlue, targetGreen;
		
		int redRGB = Color.RED.getRGB();
		int clearRGB = new Color(0,0,0,0).getRGB();
		
		targetRed =   targetColor.getRed();
		targetGreen = targetColor.getGreen();
		targetBlue =  targetColor.getBlue();
		
		for(x = 0; x < i.getWidth() ; x++) 
			for(y = 0; y < i.getHeight() ; y++) {
				rgbTemp = i.getRGB(x, y);
				redTemp   = ((rgbTemp >> 16) & 0xFF);
				greenTemp = ((rgbTemp >> 8)  & 0xFF);
				blueTemp  = (rgbTemp         & 0xFF);
				
				
				if(redTemp - targetRed < redUpper
						&& targetRed - redTemp < redLower
						&& greenTemp - targetGreen < greenUpper
						&& targetGreen - greenTemp < greenLower
						&& blueTemp - targetBlue < blueUpper
						&& targetBlue - blueTemp < blueLower)
					clearImage.setRGB(x, y, redRGB);				
				else 
					clearImage.setRGB(x, y, clearRGB);
			}
		
		return clearImage;		
	}
	
	public static BufferedImage applyHSBValueFilterToImage(BufferedImage i, Color targetColor, double hueUpper, double hueLower, 
			double satUpper, double satLower, double briUpper, double briLower){
		
		final int HUE = 0, SATURATION = 1, BRIGHTNESS = 2;

		float[] hsbTemp = new float[3];
		float targetHue, targetBrightness, targetSaturation;
		int redTemp, blueTemp, greenTemp, rgbTemp, x, y;		
		
		int jetBlackRGB = Color.BLACK.getRGB();
		
		Color.RGBtoHSB(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), hsbTemp);
		
		targetHue =        hsbTemp[HUE];
		targetSaturation = hsbTemp[SATURATION];
		targetBrightness = hsbTemp[BRIGHTNESS];
		
		for(x = 0; x < i.getWidth() ; x++) 
			for(y = 0; y < i.getHeight() ; y++) {
				rgbTemp = i.getRGB(x, y);
				redTemp   = ((rgbTemp >> 16) & 0xFF);
				greenTemp = ((rgbTemp >> 8)  & 0xFF);
				blueTemp  = (rgbTemp         & 0xFF);
				
				Color.RGBtoHSB(redTemp, greenTemp, blueTemp, hsbTemp);
				
				if(hsbTemp[0] - targetHue < hueUpper
						&& targetHue - hsbTemp[0] < hueLower
						&& hsbTemp[1] - targetSaturation < satUpper
						&& targetSaturation - hsbTemp[1] < satLower
						&& hsbTemp[2] - targetBrightness < briUpper
						&& targetBrightness - hsbTemp[2] < briLower)
					i.setRGB(x, y, jetBlackRGB);			
			}
		
		return i;		
	}
	
	public static BufferedImage applyHSBValueFilterToClearImage(BufferedImage i, Color targetColor, double hueUpper, double hueLower, 
			double satUpper, double satLower, double briUpper, double briLower){
		
		BufferedImage clearImage = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		final int HUE = 0, SATURATION = 1, BRIGHTNESS = 2;

		float[] hsbTemp = new float[3];
		float targetHue, targetBrightness, targetSaturation;
		int redTemp, blueTemp, greenTemp, rgbTemp, x, y;		
		
		int redRGB = Color.RED.getRGB();
		int clearRGB = new Color(0,0,0,0).getRGB();
		
		Color.RGBtoHSB(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), hsbTemp);
		
		targetHue =        hsbTemp[HUE];
		targetSaturation = hsbTemp[SATURATION];
		targetBrightness = hsbTemp[BRIGHTNESS];
		
		for(x = 0; x < i.getWidth() ; x++) 
			for(y = 0; y < i.getHeight() ; y++) {
				rgbTemp = i.getRGB(x, y);
				redTemp   = ((rgbTemp >> 16) & 0xFF);
				greenTemp = ((rgbTemp >> 8)  & 0xFF);
				blueTemp  = (rgbTemp         & 0xFF);
				
				Color.RGBtoHSB(redTemp, greenTemp, blueTemp, hsbTemp);
				
				if(hsbTemp[0] - targetHue < hueUpper
						&& targetHue - hsbTemp[0] < hueLower
						&& hsbTemp[1] - targetSaturation < satUpper
						&& targetSaturation - hsbTemp[1] < satLower
						&& hsbTemp[2] - targetBrightness < briUpper
						&& targetBrightness - hsbTemp[2] < briLower)
					clearImage.setRGB(x, y, redRGB);				
				else 
					clearImage.setRGB(x, y, clearRGB);
			}
		
		return clearImage;		
	}
}
