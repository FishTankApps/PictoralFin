package com.fishtankapps.pictoralfin.utilities;

import java.awt.image.BufferedImage;

public final class Constants {
	private Constants() {}
	
	public enum OperatingSystem {
		OS_X, WINDOWS, LINUX
	}
	
	public static final boolean DEBUG = true; // Used Globally
	
	public static final int MIN_NUMBER_OF_VIDEO_LINES = 2; // Used in Detecting if a file is video or audio
	
	public static final String LOOK_AND_FEEL_NOT_CHOOSEN = "L&F Not Choosen";
	
	public static final OperatingSystem OPERATING_SYSTEM = (System.getProperty("os.name").contains("Windows")) ? OperatingSystem.WINDOWS : ((System.getProperty("os.name").contains("Linux")) ? OperatingSystem.LINUX : OperatingSystem.OS_X);
	
	// ----------{ KEYBOARD SHORT CUTS }------------------------------------------------------------
	public static final int CTRL = 2;
	public static final int SHIFT = 1;
	public static final int CTRL_SHIFT = 3;
	public static final int META = 4;
	public static final int META_SHIFT = 5;
	public static final int META_CTRL = 6;
	public static final int META_CTRL_SHIFT = 7;
	public static final int ALT = 8;
	public static final int ALT_SHIFT = 9;
	public static final int CTRL_ALT = 10;
	public static final int CTRL_ALT_SHIFT = 11;
	public static final int META_ALT = 12;
	public static final int META_ALT_SHIRT = 13;
	public static final int META_CTRL_ALT = 14;
	public static final int META_CTRL_ALT_SHIFT = 15;
	public static final int JUST_THE_KEY = 16;
	
	
	public static final int IMAGE_TYPE = BufferedImage.TYPE_4BYTE_ABGR; // Used to set the type of all images in memory.
}