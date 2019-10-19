package jComponents;

import static globalValues.GlobalVariables.framePreviewSize;
import static globalValues.GlobalVariables.settings;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import listeners.OnFrameSelectedListener;
import objectBinders.Picture;
import tools.BufferedImageTools;

public class FrameTimeLine extends JPanel{

	private static final long serialVersionUID = -1012143077134630885L;
	private ArrayList<Picture> frames;
	private ArrayList<JButton> buttons;
	private JPanel framePanel;
	private OnFrameSelectedListener ofsl;
	private JScrollPane jsp;
	private int indexOfPreviousSelection = 0;
	private int currentFrame = 0;
	private Thread resizeThread;
	
	public FrameTimeLine() {		
		setBackground(settings.getTheme().getPrimaryHighlightColor());
		
		frames = new ArrayList<>();
		buttons = new ArrayList<>();

		ofsl = new OnFrameSelectedListener();
		
		framePanel = new JPanel();	
		framePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		framePanel.setBackground(settings.getTheme().getPrimaryBaseColor());

		
		jsp = new JScrollPane(framePanel);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		this.add(jsp);		
		setPrefSize(600, framePreviewSize);
	}
	public void setPrefSize(int width, int height) {
		Dimension d = new Dimension(width, height);
		jsp.setPreferredSize(d);
		this.setPreferredSize(d);
	}
		
	public void addFrame(BufferedImage bi) {
		this.frames.add(new Picture(bi));
		
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(framePreviewSize, framePreviewSize));
		
		button.addActionListener(ofsl);
		framePanel.add(button);
		buttons.add(button);
	}
	public void removeFrame(int index) {
		this.frames.remove(index);
		buttons.remove(index);
		framePanel.remove(index);
		refresh(false);
	}	
	
	public void setCurrentFrame(int index) {
		indexOfPreviousSelection = currentFrame;
		currentFrame = index;
		paintSelectedButton();
	}
	public Picture getFrame(int index) {
		paintSelectedButton();
		return this.frames.get(index);		
	}
	public ArrayList<Picture> getFrames() {
		return frames;
	}
	
	public int length() {
		return frames.size();
	}
		
	public Picture getCurrentFrame() {
		return frames.get(currentFrame);
	}
	
	public int getButtonIndex(JButton clickedButton) {
		for(JButton button : buttons) 
			if(button == clickedButton)
				return buttons.indexOf(button);
		
		return -1;
	}
	public void refresh(boolean adjustSize) {
		if(resizeThread != null)
			resizeThread.interrupt();		
		
		resizeThread = new Thread(new Runnable() {
			public void run() {
				try {	
					if(adjustSize) {
						setMinimumSize(new Dimension(FrameTimeLine.this.getParent().getWidth(), 75));
						setPrefSize(FrameTimeLine.this.getParent().getWidth(), FrameTimeLine.this.getParent().getHeight());	
					}
					framePreviewSize = (int) (FrameTimeLine.this.getPreferredSize().getHeight() - 20);
					Dimension d = new Dimension(framePreviewSize, framePreviewSize);
					
					for(int index = 0; index < buttons.size(); index++) {		
						Thread.sleep(0,1);
						
						BufferedImage bi = BufferedImageTools.imageToBufferedImage(BufferedImageTools.resizeBufferedImage(frames.get(index).getImage(false), framePreviewSize, framePreviewSize, Image.SCALE_FAST));
						Graphics2D g = bi.createGraphics();
						g.setStroke(new BasicStroke(5));
						g.setColor((buttons.get(index).hasFocus()) ? settings.getTheme().getSecondaryHighlightColor() : settings.getTheme().getPrimaryBaseColor());
						g.drawRoundRect(0, 0, bi.getWidth(), bi.getHeight(), 20, 20);
						g.drawRect(0, 0, bi.getWidth(), bi.getHeight());						
						
						buttons.get(index).setIcon(new ImageIcon(bi));

						buttons.get(index).setPreferredSize(d);
					}	
					
					revalidate();
					repaint();
				}catch(Exception ignore) {}			
			}		
		});
		resizeThread.start();		
	}
	
	public void paintSelectedButton() {
		if(buttons.size() != 0) {
			BufferedImage bi2 = BufferedImageTools.imageToBufferedImage(BufferedImageTools.resizeBufferedImage(frames.get(indexOfPreviousSelection).getImage(false), framePreviewSize, framePreviewSize, Image.SCALE_FAST));
			Graphics2D g2 = bi2.createGraphics();
			g2.setStroke(new BasicStroke(5));
			g2.setColor(settings.getTheme().getPrimaryBaseColor());
			g2.drawRoundRect(0, 0, bi2.getWidth(), bi2.getHeight(), 20, 20);
			g2.drawRect(0, 0, bi2.getWidth(), bi2.getHeight());						
			
			buttons.get(indexOfPreviousSelection).setIcon(new ImageIcon(bi2));
			
			BufferedImage bi = BufferedImageTools.imageToBufferedImage(BufferedImageTools.resizeBufferedImage(frames.get(currentFrame).getImage(false), framePreviewSize, framePreviewSize, Image.SCALE_FAST));
			Graphics2D g = bi.createGraphics();
			g.setStroke(new BasicStroke(5));
			g.setColor(settings.getTheme().getSecondaryHighlightColor());
			g.drawRoundRect(0, 0, bi.getWidth(), bi.getHeight(), 20, 20);
			g.drawRect(0, 0, bi.getWidth(), bi.getHeight());						
			
			buttons.get(currentFrame).setIcon(new ImageIcon(bi));		
			
			repaint();
		}		
	}
}
