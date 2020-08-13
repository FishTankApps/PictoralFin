package com.fishtankapps.pictoralfin.listeners;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

public class OnWindowResizedListener implements ComponentListener{

	private PictoralFin pictoralFin;
	
	public OnWindowResizedListener(PictoralFin pf) {pictoralFin = pf;}
	
	public void componentShown(ComponentEvent arg0) {}
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	
	public void componentResized(ComponentEvent arg0) {
		pictoralFin.repaint();
	}
}
