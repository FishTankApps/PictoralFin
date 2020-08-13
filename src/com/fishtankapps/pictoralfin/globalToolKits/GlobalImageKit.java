package com.fishtankapps.pictoralfin.globalToolKits;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GlobalImageKit {

	public static BufferedImage pictoralFinIcon, pictureIcon, videoIcon, audioIcon;
	
	public static void initialize() throws IOException {
		pictoralFinIcon = ImageIO.read(GlobalImageKit.class.getResourceAsStream("KineticIcon.png"));
		pictureIcon =     ImageIO.read(GlobalImageKit.class.getResourceAsStream("BiggerPictureIcon.png"));
		videoIcon =       ImageIO.read(GlobalImageKit.class.getResourceAsStream("VideoIcon.png"));
		audioIcon =       ImageIO.read(GlobalImageKit.class.getResourceAsStream("BiggerAudioIcon.png"));
	}
	
	public static BufferedImage readImage(String fileName) {
		try {
			return ImageIO.read(GlobalImageKit.class.getResourceAsStream(fileName));
		} catch (Exception e) {
			return null;
		}
	}
}
