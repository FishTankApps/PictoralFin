package JTimeLine;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interfaces.Themed;
import objectBinders.Frame;
import objectBinders.Theme;

public class FrameTimeLine extends JPanel implements Themed {
	
	private static final long serialVersionUID = -8764321615928981018L;
	private JFrameButton previousSelection;
	private Theme theme;

	private int frameDimension = 200;
	
	public FrameTimeLine(Theme theme) {
		this.theme = theme;		
		
		setBackground(theme.getPrimaryBaseColor());
		setHeight(100);
	}
	
	public void addFrame(Frame frame) {
		JFrameButton newButton = new JFrameButton(frame, theme);
		newButton.setPreferredHeight(frameDimension);
		add(newButton);		
	}
		
	public Frame getFrameAt(int index) {
		return ((JFrameButton) getComponent(index)).getFrame();
	}
	public Frame getSelectedFrame() {
		return previousSelection.getFrame();
	}
	public int getSelectedIndex() {
		return 0;
	}
	
	public void setSelectedFrame(int index) {
		if(getComponentCount() == 0)
			return;
		
		if(previousSelection != null) {
			previousSelection.setSelected(false);
			previousSelection.repaint();			
		}
		
		if(index == -1) {
			if(getIndexOfJFrameButton(previousSelection) < getComponentCount() - 1 && previousSelection != null) {
				index = getIndexOfJFrameButton(previousSelection) + 1;				
			} else 
				index = 0;
		}
		
		JFrameButton button = (JFrameButton) getComponent(index);
		
		button.setSelected(true);
		previousSelection = button;
	}
	public int getIndexOfJFrameButton(JFrameButton button) {
		for(int count = 0; count < getComponents().length; count++)
			if(getComponents()[count] == button)
				return count;
		
		return -1;
	}
	
	
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	public void setHeight(int height) {
		frameDimension = height;
		
		 for(Component c : getComponents())
			 if(c instanceof JFrameButton)
				 ((JFrameButton) c).setPreferredHeight(height);		 
		
			
		 
			
		revalidate();
		repaint();
	}
		
	void onJFrameButtonClicked(MouseEvent mouseEvent, JFrameButton button) {
		if(previousSelection != null) {
			previousSelection.setSelected(false);
			previousSelection.repaint();
		}
		
		button.setSelected(true);
		previousSelection = button;
	}

	void onMoveRequest(boolean leftOrRight, JFrameButton button) {
		int index;
		if(leftOrRight) { // LEFT			
			if((index = getIndexOfJFrameButton(button)) != 0) {
				remove(button);
				add(button, index - 1);
				
				JScrollPane sp = (JScrollPane) this.getParent().getParent().getParent();
				sp.getHorizontalScrollBar().setValue(sp.getHorizontalScrollBar().getValue() + 10);
				repaint();
			}
		} else { // RIGHT
			if((index = getIndexOfJFrameButton(button)) != getComponentCount() - 1) {
				remove(button);
				add(button, index + 1);
				
				JScrollPane sp = (JScrollPane) this.getParent().getParent().getParent();
				sp.getHorizontalScrollBar().setValue(sp.getHorizontalScrollBar().getValue() - 10);
				repaint();
			}
		}		
	}
}
