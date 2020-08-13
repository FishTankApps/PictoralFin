package com.fishtankapps.pictoralfin.jComponents.videoEditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;

public class JumpClickSlider extends JSlider {

	private static final long serialVersionUID = 4344085873443773904L;

	public JumpClickSlider(int min, int max, int value) {
		super(min, max, value);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				double percent = event.getPoint().x / ((double) getWidth());
				int range = getMaximum() - getMinimum();
				
				double newVal = range * percent;
				
				int result = (int) (getMinimum() + newVal);
				
				setValue(result);
			}
		});
	}
}
