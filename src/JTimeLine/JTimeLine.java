package JTimeLine;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import objectBinders.AudioInfo;
import objectBinders.Frame;

public class JTimeLine extends JPanel {
	
	private static final long serialVersionUID = -7284429791726637894L;
	
	private FrameTimeLine frameTimeLine;
	
	public JTimeLine() {
		frameTimeLine = new FrameTimeLine();
		add(frameTimeLine);
		this.setPreferredSize(new Dimension(1000, 700));
	}
	
	public void addFrame(Frame frame) {
		frameTimeLine.addFrame(frame);
	}
}
