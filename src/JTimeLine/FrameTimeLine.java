package JTimeLine;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import interfaces.Themed;
import mainFrame.PictoralFin;
import objectBinders.Frame;
import objectBinders.Theme;

class FrameTimeLine extends JPanel implements Themed, MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = -8764321615928981018L;
	private JFrameButton selectedJFrameButton;
	private JFrameButton highlightedJFrameButton;
	private JButton addFrames;
	private ArrayList<OnFrameSelectionChangedListener> listeners;
	//private OnComponentDraggedListener dragListener;

	//boolean hasFrames = false;
	private Theme theme;
	private int frameDimension = 100;
	
	public FrameTimeLine(PictoralFin pictoralFin) {	
		this.theme = pictoralFin.getSettings().getTheme();
		listeners = new ArrayList<>();
		//dragListener = new OnComponentDraggedListener(this);
		
		addFrames = new JButton("Add Frames");
		addFrames.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddPictureRequest);
		addFrames.setIcon(new ImageIcon(pictoralFin.getGlobalImageKit().pictureIcon));
		add(addFrames);
		
		setBackground(theme.getPrimaryBaseColor());
		setHeight(100);		
		setFocusable(true);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void addFrame(Frame frame) {		
		if(getComponent(0) instanceof JButton) 
			remove(0);
		
		
		JFrameButton newButton = new JFrameButton(frame, theme);
		newButton.setPreferredHeight(frameDimension);
		
		newButton.addMouseListener(this);
		newButton.addMouseMotionListener(this);
		
		add(newButton);
		
		revalidate();
		repaint();
	}
	
	public void removeFrame(int index) {
		remove(index);

		if(getComponentCount() == 0)
			add(addFrames);
	}
	
	public void removeFrame(JFrameButton button) {
		remove(button);

		if(getComponentCount() == 0)
			add(addFrames);
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

	
	public void applyTheme(Theme theme) {
		this.theme = theme;
		
	}
}
