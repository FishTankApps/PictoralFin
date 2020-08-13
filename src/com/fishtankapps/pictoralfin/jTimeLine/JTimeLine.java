package com.fishtankapps.pictoralfin.jTimeLine;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.projectFileManagement.PictoralFinProject;
import com.fishtankapps.pictoralfin.projectFileManagement.PictoralFinProjectSettings_v1;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
 
public class JTimeLine extends JPanel {
	
	public static final boolean NEXT_FRAME = true;
	public static final boolean PREVIOUS_FRAME = false;
	
	private static final long serialVersionUID = -7284429791726637894L;
	
	private FrameTimeLine frameTimeLine;
	private AudioTimeLine audioTimeLine;
	private JScrollPane scrollPane;
	private JPanel timeLinePanel;
	private PictoralFin pictoralFin;
	private JButton zoomIn, zoomOut;
	
	private int size = 150;

	
	public JTimeLine(PictoralFin pictoralFin) {
		super(new GridBagLayout());
		
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
		
		zoomIn = new JButton("+");
		zoomOut = new JButton("-");
		zoomOut.addActionListener(e->{size -= (size > 10) ? 10 : 0; updateSizes();});
		zoomIn.addActionListener( e->{size += 10; updateSizes();});
		
		JLabel label = new JLabel();
		BufferedImage zoomIcon = GlobalImageKit.readImage("ZoomIcon.png");
		BufferedImageUtil.applyColorThemeToImage(zoomIcon, pictoralFin.getSettings().getTheme());
		label.setIcon(new ImageIcon(zoomIcon));
		
		add(scrollPane, new ChainGBC(0, 0).setFill(true).setPadding(0).setWidthAndHeight(1,3));
		add(zoomIn, new ChainGBC(1, 0).setFill(false).setPadding(0).setWidthAndHeight(1,1));
		add(label, new ChainGBC(1, 1).setFill(false, true).setPadding(0).setWidthAndHeight(1,1));
		add(zoomOut, new ChainGBC(1, 2).setFill(false).setPadding(0).setWidthAndHeight(1,1));
		
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
	}

	public boolean isEmpty() {
		return frameTimeLine.isEmpty() && audioTimeLine.isEmpty();
	}
	public void empty() {
		frameTimeLine.empty();
		audioTimeLine.empty();
	}
	public PictoralFinProject generatePictoralFinStaticProject() {
		return new PictoralFinProject(frameTimeLine.getFrames(), audioTimeLine.getAudioClipData(),
				new PictoralFinProjectSettings_v1(pictoralFin));
	}
	
	public void loadPictoralFinStaticProject(PictoralFinProject project) {
		StatusLogger.logStatus("Importing Images...");
		if(project.getFrames() != null)
			for(Frame frame : project.getFrames()) {
				addFrame(frame);
				frame.logUndoableChange();
			}
		
		StatusLogger.logStatus("Importing Audio...");
		if(project.getAudioData() != null)
			for(AudioClipData data : project.getAudioData())
				addAudioClip(new AudioClip(data, this));
		
		StatusLogger.logStatus("Applying Settings...");
		project.getSettings().applySettings(pictoralFin);
		
		revalidate();
		repaint();
	}
	
	//----------{ FRAMES }-------------------------------------------------------------------------
	//=====[ Getters ]====================================================	
	public Frame[] getFrames() {
		return frameTimeLine.getFrames();
	}
	public int numberOfFrame() {
		return frameTimeLine.numberOfFrames();
	}	
	public Frame getCurrentFrame() {
		return frameTimeLine.getSelectedFrame();
	}
	public long getVideoDurration() {
		return frameTimeLine.getVideoDurration();
	}
	public long getMilliAtCurrentFrame() {
		return frameTimeLine.getMilliAtCurrentFrame();
	}
	public int getCurrentFrameIndex() {
		return frameTimeLine.getSelectedIndex();
	}
	public long getMilliAtFrame(Frame f) {
		return frameTimeLine.getMilliAtFrame(f);
	}
	public Frame getFrameAtMilli(long milli) {
		return frameTimeLine.getFrameAtMilli(milli);
	}
	public int getIndexOfFrameAtMilli(long milli) {
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
	}
	public void addFrame(BufferedImage image) {
		frameTimeLine.addFrame(new Frame(image));
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
		JSplitPane jsp = ((JSplitPane) getParent());
		
		Dimension parentDimension = getParent().getSize();
		Dimension preferedSize = new Dimension(parentDimension.width, 
				parentDimension.height - jsp.getDividerLocation() - scrollPane.getHorizontalScrollBar().getHeight());

		frameTimeLine.setHeight(size);
		
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
	public AudioClip[] getAudioClips() {
		return audioTimeLine.getAudioClips();
	}
}
