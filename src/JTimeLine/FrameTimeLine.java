package JTimeLine;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import interfaces.Themed;
import objectBinders.Frame;
import objectBinders.Theme;

class FrameTimeLine extends JPanel implements Themed, MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = -8764321615928981018L;
	private JFrameButton selectedJFrameButton;
	private JFrameButton highlightedJFrameButton;
	private ArrayList<OnFrameSelectionChangedListener> listeners;
	private OnComponentDraggedListener dragListener;
	private Theme theme;

	private int frameDimension = 100;
	
	public FrameTimeLine(Theme theme) {		
		this.theme = theme;		
		listeners = new ArrayList<>();
		dragListener = new OnComponentDraggedListener(this);
		
		setBackground(theme.getPrimaryBaseColor());
		setHeight(100);		
		setFocusable(true);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void addFrame(Frame frame) {
		JFrameButton newButton = new JFrameButton(frame, theme);
		newButton.setPreferredHeight(frameDimension);
		
		newButton.addMouseListener(this);
		newButton.addMouseMotionListener(this);
		
		add(newButton);		
	}
	protected void addOnFrameSelectionChangedListener(OnFrameSelectionChangedListener listener) {
		listeners.add(listener);
	}
	
	public Frame getFrameAt(int index) {
		return ((JFrameButton) getComponent(index)).getFrame();
	}
	public Frame getSelectedFrame() {
		return selectedJFrameButton.getFrame();
	}
	public int getSelectedIndex() {
		return 0;
	}
	
	public JFrameButton getHighlightedJFrameButton() {
		return highlightedJFrameButton;
	}
	
	public void setSelectedFrame(int index) {
		if(getComponentCount() == 0)
			return;
		
		if(selectedJFrameButton != null) {
			selectedJFrameButton.setSelected(false);
			selectedJFrameButton.repaint();			
		}
		
		if(index == -1) {
			if(getIndexOfJFrameButton(selectedJFrameButton) < getComponentCount() - 1 && selectedJFrameButton != null) {
				index = getIndexOfJFrameButton(selectedJFrameButton) + 1;				
			} else 
				index = 0;
		}
		
		JFrameButton button = (JFrameButton) getComponent(index);
		
		button.setSelected(true);
		selectedJFrameButton = button;
	}
	public int getIndexOfJFrameButton(JFrameButton button) {
		for(int count = 0; count < getComponentCount(); count++)
			if(getComponents()[count] == button)
				return count;
		
		return -1;
	}
	
	
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	public Theme getTheme() {
		return theme;
	}
	public void setHeight(int height) {
		frameDimension = height;
		
		 for(Component c : getComponents())
			 if(c instanceof JFrameButton)
				 ((JFrameButton) c).setPreferredHeight(height);			
		 
		
		revalidate();
		repaint();
	}
		
	void onJFrameButtonClicked(JFrameButton button) {
		if(selectedJFrameButton != null) {
			selectedJFrameButton.setSelected(false);
			selectedJFrameButton.repaint();
		}		
		
		button.setSelected(true);
		
		if(button != selectedJFrameButton)
			for(OnFrameSelectionChangedListener l : listeners)
				l.frameSelectionChanged((selectedJFrameButton != null) ? selectedJFrameButton.getFrame() : null, button.getFrame());
		
		selectedJFrameButton = button;
	}
	void onJFrameButtonHighlighted(JFrameButton button) {				
		highlightedJFrameButton = button;
	}
	
	void onJFrameButtonUnHighlighted(JFrameButton button) {	
		if(highlightedJFrameButton == button)
			highlightedJFrameButton = null;
	}


	public void mousePressed(MouseEvent event) {
		//System.out.println("PRESSED");
		//dragListener.mousePressed(event);
	}
	public void mouseReleased(MouseEvent event) {
		//System.out.println("RELEASED");
		//dragListener.mouseReleased(event);
	}	
	public void mouseDragged(MouseEvent event) {
		//System.out.println("DRAG");
		//dragListener.mouseDragged(event);
	}


	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}	
	public void mouseMoved(MouseEvent event) {}
}
