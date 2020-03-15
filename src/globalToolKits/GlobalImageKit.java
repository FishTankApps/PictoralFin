package globalToolKits;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GlobalImageKit {

	public BufferedImage pictoralFinIcon, pictureIcon, videoIcon, audioIcon;
	
	public GlobalImageKit() throws IOException {
		pictoralFinIcon = ImageIO.read(GlobalImageKit.class.getResourceAsStream("KineticIcon.jpg"));
		pictureIcon =     ImageIO.read(GlobalImageKit.class.getResourceAsStream("BiggerPictureIcon.png"));
		videoIcon =       ImageIO.read(GlobalImageKit.class.getResourceAsStream("VideoIcon.jpg"));
		audioIcon =       ImageIO.read(GlobalImageKit.class.getResourceAsStream("BiggerAudioIcon.png"));
	}
	
}
