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
	
	public FramePopUpMenu(JFrameButton button, int x, int y){		
		this.button = button;		
		menu = new JPopupMenu();
		
		JMenuItem removeFrame = new JMenuItem("Remove");
		removeFrame.addActionListener(REMOVE_FRAME);
		
		menu.add(removeFrame);
		menu.show(button, x, y);
	}
}
