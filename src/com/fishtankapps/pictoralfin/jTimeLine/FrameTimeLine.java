package com.fishtankapps.pictoralfin.jTimeLine;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.globalToolKits.GlobalListenerToolKit;
import com.fishtankapps.pictoralfin.interfaces.Themed;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class FrameTimeLine extends JPanel implements Themed, MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = -8764321615928981018L;
	private JFrameButton selectedJFrameButton;
	private JFrameButton highlightedJFrameButton;
	private JButton addFrames;
	private ArrayList<OnFrameSelectionChangedListener> listenersOFSC;
	private ArrayList<OnVideoDurrationChangedListener> listenersOVDC;
	//private OnComponentDraggedListener dragListener;

	//boolean hasFrames = false;
	Theme theme;
	int frameDimension = 100;
	private long durration = 0;
	private PictoralFin pictoralFin;
	
	public FrameTimeLine(PictoralFin pictoralFin) {	
		this.theme = pictoralFin.getConfiguration().getTheme();
		this.pictoralFin = pictoralFin;
		listenersOFSC = new ArrayList<>();
		listenersOVDC = new ArrayList<>();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		//dragListener = new OnComponentDraggedListener(this);
		
		addFrames = new JButton("Add Frames");
		addFrames.addActionListener(GlobalListenerToolKit.onAddPictureRequest);
		addFrames.setIcon(new ImageIcon(GlobalImageKit.pictureIcon));
		addFrames.setFont(new Font(theme.getPrimaryFont(), Font.ITALIC, 50));
		addFrames.setBackground(theme.getPrimaryHighlightColor());
		add(addFrames);
		
		setBackground(theme.getPrimaryBaseColor());
		setHeight(100);		
		setFocusable(true);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		addOnFrameSelectionChangedListener((oldFrame, newFrame) -> {
				if(newFrame == null)
					pictoralFin.getImageEditor().getLayerSelecter().triggerOnSelectedLayerChangedListeners(null, null);
			});
		
//		listenersOFSC.add((oldFrame, newFrame) -> {
//			if(newFrame != null)
//				newFrame.getFrame().loadImages();
//		});
	}
	
	public void addFrame(Frame frame) {	
		JFrameButton newButton = new JFrameButton(frame, theme);
		
		if(getComponent(0) instanceof JButton) {
			remove(0);
			selectedJFrameButton = newButton;
			newButton.setSelected(true);
			
			for(OnFrameSelectionChangedListener l : listenersOFSC)
				l.frameSelectionChanged(null, selectedJFrameButton);
		}
		
		newButton.setPreferredHeight(frameDimension);
		
		newButton.addMouseListener(this);
		newButton.addMouseMotionListener(this);
		
		add(newButton);
		
		flagDurrationChanged();
		
		revalidate();
		repaint();
	}
	
	public void addFrame(Frame frame, int index) {	
		JFrameButton newButton = new JFrameButton(frame, theme);
		
		if(getComponent(0) instanceof JButton) {
			remove(0);
			selectedJFrameButton = newButton;
			newButton.setSelected(true);
			
			for(OnFrameSelectionChangedListener l : listenersOFSC)
				l.frameSelectionChanged(null, selectedJFrameButton);
		}
		
		newButton.setPreferredHeight(frameDimension);
		
		newButton.addMouseListener(this);
		newButton.addMouseMotionListener(this);
		
		add(newButton, index);
		
		flagDurrationChanged();
		
		revalidate();
		repaint();
	}
	
	public void removeFrame(int index) {
		if (getComponent(index) == selectedJFrameButton){
			if(index < numberOfFrames() - 1) {
				selectedJFrameButton = (JFrameButton) getComponent(index + 1);
				selectedJFrameButton.setSelected(true);
				
				for(OnFrameSelectionChangedListener l : listenersOFSC)
					l.frameSelectionChanged(null, selectedJFrameButton);
				
			} else if(numberOfFrames() != 1) {
				selectedJFrameButton = (JFrameButton) getComponent(index - 1);
				selectedJFrameButton.setSelected(true);
				
				for(OnFrameSelectionChangedListener l : listenersOFSC)
					l.frameSelectionChanged(null, selectedJFrameButton);
			}
		}
		
		remove(index);
		
		flagDurrationChanged();

		if(getComponentCount() == 0) {
			add(addFrames);
			selectedJFrameButton = null;
			
			for(OnFrameSelectionChangedListener l : listenersOFSC)
				l.frameSelectionChanged(null, null);
		}
		revalidate();
		repaint();
	}
	
	public void removeFrame(JFrameButton button) {
		removeFrame(getIndexOfJFrameButton(button));
	}
	
	public int numberOfFrames() {
		if(getComponent(0) instanceof JButton)
			return 0;
		
		return getComponentCount();
	}
	
	protected void addOnFrameSelectionChangedListener(OnFrameSelectionChangedListener listener) {
		listenersOFSC.add(listener);
	}
	protected void addOnVideoDurrationChangedListener(OnVideoDurrationChangedListener listener) {
		listenersOVDC.add(listener);
	}
	
	public Frame getFrameAt(int index) {
		return ((JFrameButton) getComponent(index)).getFrame();
	}
	public Frame getFrameAtMilli(long milli) {
		if(getComponentCount() == 0 || getComponent(0) instanceof JButton)
			return null;
		
		long durration = 0;
		for(Component c : getComponents()) {
			JFrameButton button = (JFrameButton) c;
			durration += button.getFrame().getDuration();
			
			if(durration - button.getFrame().getDuration() <= milli && durration >= milli) {
				return button.getFrame();
			}
		}
		
		return null;
	}
	public int getIndexOfFrameAtMilli(long milli) {
		if(getComponentCount() == 0 || getComponent(0) instanceof JButton)
			return -1;
		
		long durration = 0;
		int index = -1;
		for(Component c : getComponents()) {
			JFrameButton button = (JFrameButton) c;
			durration += button.getFrame().getDuration();
			index++;
			
			if(durration > milli) {
				return index;
			}
		}
		
		return -2;
	}
	public Frame[] getFrames() {
		if(getComponentCount() == 0 || getComponent(0) instanceof JButton)
			return null;
		
		Frame[] frames = new Frame[getComponentCount()];
		int index = 0;
		for(Component c : getComponents()) {
			frames[index++] = ((JFrameButton) c).getFrame();
		}
		
		return frames;
	}
	
	public long getMilliAtCurrentFrame() {
		if(getComponentCount() == 0 || getComponent(0) instanceof JButton)
			return -1;
		
		long durration = 0;
		for(Component c : getComponents()) {
			JFrameButton button = (JFrameButton) c;
			durration += button.getFrame().getDuration();
			
			if(button == getSelectedFrameButton()) {
				return durration;
			}
		}
		
		return -1;
	}
	public long getMilliAtFrame(Frame f) {
		if(getComponentCount() == 0 || getComponent(0) instanceof JButton)
			return -1;
		
		long durration = 0;
		for(Component c : getComponents()) {
			JFrameButton button = (JFrameButton) c;
			durration += button.getFrame().getDuration();
			
			if(button.getFrame() == f) {
				return durration;
			}
		}
		
		return -1;
	}
	
	public Frame getSelectedFrame() {
		if(selectedJFrameButton == null)
			return null;
		
		return selectedJFrameButton.getFrame();
	}
	public int getSelectedIndex() {
		return getIndexOfJFrameButton(selectedJFrameButton);
	}
	
	public boolean isEmpty() {
		return (getComponentCount() == 0 || getComponent(0) instanceof JButton);
	}
	
	public void empty() {
		while(numberOfFrames() > 0) removeFrame(0);
	}
	
	public long getVideoDurration() {
		return durration;
	}
	
	private long getDurrationInMillis() {
		if(getComponentCount() == 0 || getComponent(0) instanceof JButton)
			return -1;
		
		long durration = 0;
		for(Component c : getComponents()) {
			JFrameButton button = (JFrameButton) c;
			durration += button.getFrame().getDuration();	
		}
		
		return durration;
	}

	void flagDurrationChanged() {
		durration = getDurrationInMillis();
		for(OnVideoDurrationChangedListener l : listenersOVDC)
			l.onVideoDurrationChanged(durration);
	}
	
	public JFrameButton getSelectedFrameButton() {
		
		return selectedJFrameButton;
	}
	
	public JFrameButton getHighlightedJFrameButton() {
		return highlightedJFrameButton;
	}
	
	public void setSelectedFrame(Frame frame) {
		if(getComponentCount() == 0)
			return;
		
		for(int count = 0; count < getComponentCount(); count++) {
			if(((JFrameButton) getComponent(count)).getFrame() == frame) {
				setSelectedFrame(count);
				return;
			}
		}
			
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

	public void moveJFrameButton(byte whereTo, JFrameButton button) {
		int buttonIndex = getIndexOfJFrameButton(button);
		
		if(buttonIndex == -1)
			return;
			
		if(whereTo == JFrameButton.LEFT && buttonIndex != 0) {
			this.remove(button);
			this.add(button, buttonIndex - 1); 
		} else if (whereTo == JFrameButton.BEGINNING && buttonIndex != 0) {
			this.remove(button);
			this.add(button, 0); 
		} else if (whereTo == JFrameButton.RIGHT && buttonIndex != getComponentCount() - 1) {
			this.remove(button);
			this.add(button, buttonIndex + 1); 
		} else if (whereTo == JFrameButton.END && buttonIndex != getComponentCount() - 1) {
			this.remove(button);
			this.add(button, getComponentCount()); 
		} else 
			return;
		
		JFrameButton old = selectedJFrameButton;
		
		revalidate();
		
		setSelectedFrame(button.getFrame());		
		
		for(OnFrameSelectionChangedListener l : listenersOFSC)
			l.frameSelectionChanged(button, old);
		
		
		repaint();
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
		selectedJFrameButton = button;
		
		pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().attachSettingsPanel(selectedJFrameButton.generateSettingsPanel());
		
		for(OnFrameSelectionChangedListener l : listenersOFSC)
			l.frameSelectionChanged((selectedJFrameButton != null) ? selectedJFrameButton : null, button);
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
