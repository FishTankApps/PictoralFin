package jTimeLine;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class FramePopUpMenu{
	
	private JPopupMenu menu;
	private JFrameButton button;
	
	private final ActionListener REMOVE_FRAME = e -> {
		FrameTimeLine frameTimeLine = (FrameTimeLine) button.getParent();
		frameTimeLine.removeFrame(button);
		menu.setVisible(false);
		
		JTimeLine timeLine = (JTimeLine) frameTimeLine.getParent().getParent().getParent().getParent();
		
		timeLine.revalidate();			
		timeLine.repaint();
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
