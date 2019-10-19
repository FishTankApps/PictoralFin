package jComponents;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import interfaces.Undoable;
import objectBinders.AudioInfo;
import objectBinders.Frame;

public class TimeLine extends Undoable<TimeLine>{
	
	private ArrayList<Frame> frames;
	private ArrayList<AudioInfo> tracks;
	private JPanel panel;
	
	
	public TimeLine() {
		frames = new ArrayList<>();
		tracks = new ArrayList<>();
		
		panel = new JPanel();
	}
	
	public void refresh() {
		panel.repaint();
	}
	
	public JPanel getJComponent() {
		return panel;
	}
	
	public boolean exportDataAsMP4() {
		return false;
	}
	
	
	
	
	
	protected void override(TimeLine instance) {
				
	}

	@Override
	protected TimeLine clone() {
		
		return this.clone();
	}

}
