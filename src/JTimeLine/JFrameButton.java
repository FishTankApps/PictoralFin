package JTimeLine;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

import objectBinders.Frame;

public class JFrameButton extends JButton {

	private static final long serialVersionUID = -6457692933405341223L;
	
	private Frame frame;
	
	public JFrameButton(Frame frame) {
		super();		
		this.frame = frame;
	}
	
	@Override
	public void paint(Graphics g) {		
		this.setPreferredSize(new Dimension(600, 600));
		System.out.println("HERE");
		g.setColor(Color.RED);
		int index;		
		for(index = frame.getNumberOfLayers() - 1; index >= 0; index--) {
			g.drawImage(frame.getLayer(index), (index * 10), (index * 30), 200, 200, null);
			g.drawRect((index * 10), (index * 30), 200, 200);
		}
	}

}
