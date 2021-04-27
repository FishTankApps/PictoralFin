package com.fishtankapps.pictoralfin.utilities;

import java.awt.image.BufferedImage;

public final class Constants {
	private Constants() {}
	
	public enum OperatingSystem {
		OS_X, WINDOWS, LINUX
	}
	
	//----------{ "Settings" }---------------------------------------------------------------------
	public static final boolean DEBUG = true; // Used Globally
	
	public static final int MIN_NUMBER_OF_VIDEO_LINES = 2; // Used in Detecting if a file is video or audio
	public static final int LIGHT_SPOT_EFFECT_WAIT_TIME = 1000; // Used to detect when you are done adjusting the light spot, to prevent a billion "logUndoableChange()" calls.
	
	public static final int ALPHA_IMAGE_TYPE = BufferedImage.TYPE_4BYTE_ABGR; // Used to set the type of all images in memory.
	public static final int OPAQUE_IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;
	
	//----------{ Replacement Values For Understanding }-------------------------------------------
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
	
	public static final int LEFT_MOUSE_BUTTON = 1;
	public static final int RIGHT_MOUSE_BUTTON = 3;
	public static final int SCROLL_WHEEL_BUTTON = 2;
	public static final int LEFT_SCROLL_WHEEL_BUTTON = 4;
	public static final int RIGHT_SCROLL_WHEEL_BUTTON = 5;
	
	//----------{ Information }--------------------------------------------------------------------
	// Reports the OS
	public static final OperatingSystem OPERATING_SYSTEM = (System.getProperty("os.name").contains("Windows")) ? OperatingSystem.WINDOWS : ((System.getProperty("os.name").contains("Linux")) ? OperatingSystem.LINUX : OperatingSystem.OS_X);
	public static final String LOOK_AND_FEEL_NOT_CHOOSEN = "L&F Not Choosen"; // Passed if no L&F has been chosen	
}