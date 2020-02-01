package globalToolKits;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class GlobalImageKit {

	public BufferedImage pictoralFinIcon, pictureIcon, videoIcon;
	
	public GlobalImageKit() throws IOException {
		pictoralFinIcon = ImageIO.read(GlobalImageKit.class.getResourceAsStream("KineticIcon.jpg"));
		pictureIcon =     ImageIO.read(GlobalImageKit.class.getResourceAsStream("BiggerPictureIcon.png"));
		videoIcon =       ImageIO.read(GlobalImageKit.class.getResourceAsStream("VideoIcon.jpg"));
	}
	
}
