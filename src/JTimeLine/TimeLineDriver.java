package JTimeLine;
import javax.swing.JFrame;

public class TimeLineDriver {

	JFrame frame;
	TimeLine timeLine;
	
	public static void main(String[] args) {
		new TimeLineDriver();
	}
	
	public TimeLineDriver() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(1000, 750);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		timeLine = new TimeLine();
		frame.add(timeLine);
	}
	
}
