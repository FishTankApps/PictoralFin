package com.fishtankapps.pictoralfin.jTimeLine;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.utilities.Utilities;

class FramePopUpMenu{
	
	private JPopupMenu menu;
	private JFrameButton button;
	
	private final ActionListener REMOVE_FRAME = e -> {
		FrameTimeLine frameTimeLine = (FrameTimeLine) button.getParent();
		frameTimeLine.removeFrame(button);
		menu.setVisible(false);
		
		PictoralFin pictoralFin = Utilities.getPictoralFin(frameTimeLine);		
		
		
		pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().dettachSettingsPanel();
		
		pictoralFin.getTimeLine().revalidate();			
		pictoralFin.getTimeLine().repaint();
	};
	private final ActionListener COPY_FRAME = e -> {
		FrameTimeLine frameTimeLine = (FrameTimeLine) button.getParent();
		
		frameTimeLine.addFrame(button.getFrame().clone(), frameTimeLine.getIndexOfJFrameButton(button));
		menu.setVisible(false);
		
		PictoralFin pictoralFin = Utilities.getPictoralFin(frameTimeLine);
		
		pictoralFin.getTimeLine().revalidate();			
		pictoralFin.getTimeLine().repaint();
	};
	
	public FramePopUpMenu(JFrameButton button, int x, int y){		
		this.button = button;		
		menu = new JPopupMenu();
		
		JMenuItem removeFrame = new JMenuItem("Remove");
		JMenuItem copyFrame = new JMenuItem("Copy Frame");
		
		removeFrame.addActionListener(REMOVE_FRAME);
		copyFrame.addActionListener(COPY_FRAME);
		
		menu.add(removeFrame);
		menu.add(copyFrame);
		menu.show(button, x, y);
	}
}
