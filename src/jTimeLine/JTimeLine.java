package jTimeLine;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import mainFrame.PictoralFin;
import objectBinders.Frame;
import utilities.ChainGBC;
 
public class JTimeLine extends JPanel {
	
	public static final boolean NEXT_FRAME = true;
	public static final boolean PREVIOUS_FRAME = false;
	
	private static final long serialVersionUID = -7284429791726637894L;
	
	private FrameTimeLine frameTimeLine;
	private AudioTimeLine audioTimeLine;
	private JScrollPane scrollPane;
	private JPanel timeLinePanel;
	private PictoralFin pictoralFin;

	
	public JTimeLine(PictoralFin pictoralFin) {		
		this.pictoralFin = pictoralFin;
		
		frameTimeLine = new FrameTimeLine(pictoralFin);		
		audioTimeLine = new AudioTimeLine(pictoralFin, this);
		
		timeLinePanel = new JPanel(new GridBagLayout());
		timeLinePanel.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		scrollPane = new JScrollPane(timeLinePanel);		
		scrollPane.getHorizontalScrollBar().setUnitIncrement(50);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		
		timeLinePanel.add(frameTimeLine, new ChainGBC(0, 0).setFill(true).setPadding(0, 0, 10, 10));
		timeLinePanel.add(audioTimeLine, new ChainGBC(0, 1).setFill(true).setPadding(0, 0, 0, 10));
		
		add(scrollPane);
	}

	//----------{ FRAMES }-------------------------------------------------------------------------
	//=====[ Getters ]====================================================	
	public int numberOfFrame() {
		return frameTimeLine.numberOfFrames();
	}	
	public Frame getCurrentFrame() {
		return frameTimeLine.getSelectedFrame();
	}
	public int getVideoDurration() {
		return frameTimeLine.getVideoDurration();
	}
	public int getMilliAtCurrentFrame() {
		return frameTimeLine.getMilliAtCurrentFrame();
	}
	public int getCurrentFrameIndex() {
		return frameTimeLine.getSelectedIndex();
	}
	public Frame getFrameAtMilli(int milli) {
		return frameTimeLine.getFrameAtMilli(milli);
	}
	public int getIndexOfFrameAtMilli(int milli) {
		return frameTimeLine.getIndexOfFrameAtMilli(milli);
	}
    public JFrameButton getCurrentFrameButton() {
		return frameTimeLine.getSelectedFrameButton();
	}
	FrameTimeLine getFrameTimeLine() {
		return frameTimeLine;
	}
    
	//=====[ Setters ]====================================================
    public void setCurrentFrame(Frame frame) {
		frameTimeLine.setSelectedFrame(frame);
	}
    public void setCurrentFrameIndex(int index) {
		if(index != 0 || numberOfFrame() != 0)
			frameTimeLine.setSelectedFrame(index);
	}
	
    //=====[ Add/Remove ]=================================================
    public void addFrame(Frame frame) {
		frameTimeLine.addFrame(frame);
		pictoralFin.updateMemoryUsage();
	}
	public void addFrame(BufferedImage image) {
		frameTimeLine.addFrame(new Frame(image));
		pictoralFin.updateMemoryUsage();
	}
	public void removeFrame(int index) {
		frameTimeLine.removeFrame(index);
	}
	public void addOnFrameSelectionChangeListener(OnFrameSelectionChangedListener listener) {
		frameTimeLine.addOnFrameSelectionChangedListener(listener);
	}	
	public void addOnVideoDurrationChangedListener(OnVideoDurrationChangedListener listener) {
		frameTimeLine.addOnVideoDurrationChangedListener(listener);
	}
	
	//=====[ Interactors ]================================================
	public void updateSizes() {
		frameTimeLine.setHeight(250);
		
		JSplitPane jsp = ((JSplitPane) getParent());
		
		Dimension parentDimension = getParent().getSize();
		Dimension preferedSize = new Dimension(parentDimension.width, 
				parentDimension.height - jsp.getDividerLocation() - scrollPane.getHorizontalScrollBar().getHeight());

		frameTimeLine.setHeight((int) (preferedSize.height * .85));
		scrollPane.setPreferredSize(preferedSize);		
		setPreferredSize(preferedSize);
	
		setMinimumSize(new Dimension(1, 100));
		
		revalidate();
		repaint();
	}	
	public void moveCurrentFrameUpTo(int movement) {
		for(int count = 0; count < Math.abs(movement); count++)
			moveCurrentFrame(movement > 0);
	}
	public boolean moveCurrentFrame(boolean whichDirection) {
			if(whichDirection) {
				if(getCurrentFrameIndex() != numberOfFrame() - 1) 
					setCurrentFrameIndex(getCurrentFrameIndex() + 1);		
				else
					return false;
			} else {
				if(getCurrentFrameIndex() != 0) 
					setCurrentFrameIndex(getCurrentFrameIndex() - 1);	
				else
					return false;
			}
			
			return true;
		}

	
	//--------{ AUDIO }----------------------------------------------------------------------------
	public void addAudioClip(AudioClip audioClip) {
		audioTimeLine.addAudioClip(audioClip);
	}
	public AudioTimeLine getAudioTimeLine() {
		return audioTimeLine;
	}
}
