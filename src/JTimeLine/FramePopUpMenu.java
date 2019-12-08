package JTimeLine;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class FramePopUpMenu{
	
	public FramePopUpMenu(JFrameButton button, int x, int y){				
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem removeFrame = new JMenuItem("Remove");
		removeFrame.addActionListener(e -> {
			FrameTimeLine frameTimeLine = (FrameTimeLine) button.getParent();
			frameTimeLine.remove(button);
			menu.setVisible(false);
			
			JTimeLine timeLine = (JTimeLine) frameTimeLine.getParent().getParent().getParent().getParent();
			
			timeLine.revalidate();			
			timeLine.repaint();
		});

		menu.add(removeFrame);
		menu.show(button, x, y);
	}
}
