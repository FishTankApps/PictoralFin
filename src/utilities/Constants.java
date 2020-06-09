package utilities;

import java.awt.image.BufferedImage;

public final class Constants {
	private Constants() {}
	
	public static final boolean DEBUG = true;
	
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
	
	public static final int MAX_SCALE = 300;
	
	public static final int IMAGE_TYPE = BufferedImage.TYPE_3BYTE_BGR;
	
	public static final int DEFAULT_KBS = 128 * 1000;
}