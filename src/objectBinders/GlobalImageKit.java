package objectBinders;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GlobalImageKit {

	public BufferedImage pictoralFinIcon, pictureIcon, videoIcon;
	
	public GlobalImageKit() throws IOException {
		pictoralFinIcon = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("KineticIcon.jpg"));
		pictureIcon =     ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("BiggerPictureIcon.png"));
		videoIcon =       ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("VideoIcon.jpg"));
	}
	
}
