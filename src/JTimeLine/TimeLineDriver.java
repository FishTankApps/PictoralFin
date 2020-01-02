package JTimeLine;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import objectBinders.Frame;
import objectBinders.Theme;

class TimeLineDriver {

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
			e1.printStackTrace();
		}
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(1000, 750);
		frame.setLocationRelativeTo(null);				
		timeLine = new JTimeLine(Theme.OCEAN_THEME);
		timeLine.addOnFrameSelectionChangeListener((old, neW) -> {System.out.println("SELECTION CHANGED");});
		frame.add(timeLine);		
		
		File pictureFolder = new File("C:\\Users\\Robots\\Pictures\\Others\\Sea Turtles");
		
		try {
//			Frame f = new Frame(5500);
//			Frame f2 = new Frame(ImageIO.read(pictureFolder.listFiles()[0]));
//			f.addLayer(ImageIO.read(pictureFolder.listFiles()[10]));
//			f.addLayer(ImageIO.read(pictureFolder.listFiles()[20]));
//			f.addLayer(ImageIO.read(pictureFolder.listFiles()[30]));			
//			timeLine.addFrame(f);
//			
//			f2.addLayer(ImageIO.read(pictureFolder.listFiles()[10]));
//			f2.addLayer(ImageIO.read(pictureFolder.listFiles()[20]));			
//			timeLine.addFrame(f2);
			
			for(File picture : pictureFolder.listFiles()) {
				timeLine.addFrame(new Frame(ImageIO.read(picture)));
			}
		} catch (Exception e) {
		
		}
		
		
		frame.setVisible(true);	
		
		timeLine.updateSizes();

	}
	
}
