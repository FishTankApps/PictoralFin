package JTimeLine;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import objectBinders.Frame;
import tools.BufferedImageTools;

public class TimeLineDriver {

	JFrame frame;
	JTimeLine timeLine;
	
	public static void main(String[] args) {
		new TimeLineDriver();
	}
	
	public TimeLineDriver() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(1000, 750);
		frame.setLocationRelativeTo(null);
		
		
		
		timeLine = new JTimeLine();
		frame.add(timeLine);
		
		
		File pictureFolder = new File("C:\\Users\\Robots\\Pictures\\Lego Movies\\Parts of Videos\\Flying Dove");
		
		try {
			Frame f = new Frame(ImageIO.read(pictureFolder.listFiles()[0]));
			f.addLayer(ImageIO.read(pictureFolder.listFiles()[10]));
			f.addLayer(ImageIO.read(pictureFolder.listFiles()[20]));
			f.addLayer(ImageIO.read(pictureFolder.listFiles()[20]));
			timeLine.addFrame(f);
			
			int count = 0;
			for(File picture : pictureFolder.listFiles()) {
				timeLine.addFrame(new Frame(ImageIO.read(picture)));
			}
		}catch (Exception e) {
		
		}
		
		
		frame.setVisible(true);	
	}
	
}
