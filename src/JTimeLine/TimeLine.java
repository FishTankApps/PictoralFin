package JTimeLine;

import java.awt.Container;
import java.awt.Graphics;
import java.util.ArrayList;

import objectBinders.AudioInfo;
import objectBinders.Frame;

public class TimeLine extends Container {
	
	private static final long serialVersionUID = -7284429791726637894L;
	
	private ArrayList<Frame> frames;
	private ArrayList<AudioInfo> tracks;
		
	public TimeLine() {
		frames = new ArrayList<>();
		tracks = new ArrayList<>();	
	}
	
	
	
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		
		
	}
}
