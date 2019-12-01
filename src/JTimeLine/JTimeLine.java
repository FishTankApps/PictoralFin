package JTimeLine;

import java.awt.Dimension;

import javax.swing.JPanel;

import objectBinders.Frame;
import objectBinders.Theme;

public class JTimeLine extends JPanel {
	
	private static final long serialVersionUID = -7284429791726637894L;
	
	private FrameTimeLine frameTimeLine;
	
	public JTimeLine(Theme theme) {		
		frameTimeLine = new FrameTimeLine(theme);
		add(frameTimeLine);
		this.setPreferredSize(new Dimension(1000, 700));
	}
	
	public void setHeight(int height) {
		frameTimeLine.setHeight(height);		
		repaint();
	}
	
	public void addFrame(Frame frame) {
		frameTimeLine.addFrame(frame);
	}
}
