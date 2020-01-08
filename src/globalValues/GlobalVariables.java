package globalValues;

import java.awt.image.BufferedImage;

import mainFrame.PictoralFinKinetic;
import objectBinders.DataFile;
import objectBinders.Settings;

public class GlobalVariables {

	public static int framePreviewSize = 100;
	public static int framesPerSecond = 10;
	public static DataFile dataFile;
	public static BufferedImage orca;
	public static PictoralFinKinetic pfk;
	public static boolean currentlyRunning = true;
	public static boolean previewIsPlaying = false;
	public static boolean noneHaveBeenAdded = true;
	public static boolean isMousePressed = false;
	public static Settings settings = null;
	public static Object lastInFocus = null;
	public static int openView = 0;
	
}
