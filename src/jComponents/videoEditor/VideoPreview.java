package jComponents.videoEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;

import JTimeLine.JTimeLine;
import interfaces.Themed;
import objectBinders.Frame;
import objectBinders.Theme;
import utilities.Utilities;


public class VideoPreview extends JPanel implements Themed {
	
	private static final long serialVersionUID = -1247649334503994677L;
	private static final int PREF_BOARDER = 20;
	
	private Thread videoPreviewThread;
	
	private JButton playPause, stop, skipLeft, skipRight;
	private JPanel controlPanel, buttonPanel;
	private JSlider videoTimeLine;
	private JTextPane  frameNumber;
	private ImageIcon playIcon, pauseIcon, stopIcon, skipLeftIcon, skipRightIcon;
	
	private Frame currentFrame = null;
	
	private JTimeLine timeLine = null;
	
	private Theme theme;
	
	public VideoPreview(Theme theme) {		
		this.theme = theme;
		
		Dimension buttonDim = new Dimension(38, 38);
		
		playPause = new JButton();
		playPause.setPreferredSize(buttonDim);
		//playPause.addActionListener(e -> {previewIsPlaying = !previewIsPlaying && Utilities.getPictoralFin(this).getTimeLine().numberOfFrame() != 0; repaint();});
		
		stop = new JButton();
		stop.setPreferredSize(buttonDim);
		//stop.addActionListener(e-> {previewIsPlaying = false; videoTimeLine.setValue(0); repaint();});
		
		skipLeft = new JButton();
		skipLeft.setPreferredSize(buttonDim);
		//skipLeft.addActionListener(e-> {videoTimeLine.setValue(videoTimeLine.getValue() - 1); repaint();});
		
		skipRight = new JButton();
		skipRight.setPreferredSize(buttonDim);
		//skipRight.addActionListener(e-> {videoTimeLine.setValue(videoTimeLine.getValue() + 1); repaint();});
		
		
		frameNumber = new JTextPane();
		frameNumber.setEditable(false);
		
		frameNumber.setText("No Frames");		

		
		videoTimeLine = new JSlider(0,0,0);
		videoTimeLine.addChangeListener(e -> repaint());		
		videoTimeLine.setPreferredSize(new Dimension(30, 10));
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		
		buttonPanel = new JPanel();
		buttonPanel.setBackground(theme.getPrimaryHighlightColor());
		buttonPanel.add(skipLeft);
		buttonPanel.add(playPause);		
		buttonPanel.add(stop);
		buttonPanel.add(skipRight);
		
		controlPanel.add(videoTimeLine);
		controlPanel.add(buttonPanel);
				
		
		this.setLayout(new BorderLayout());	
		
		this.add(controlPanel,  BorderLayout.SOUTH);
		this.add(frameNumber, BorderLayout.NORTH);
		
		setUpIconImages(theme);		
	
		
		this.setMinimumSize(new Dimension(800,500));
	}	
	public void setUpIconImages(Theme theme) {
		BufferedImage playImage, pauseImage, stopImage, skipLeftImage, skipRightImage;
		
		pauseImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = pauseImage.getGraphics();
		g.setColor(theme.getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(theme.getPrimaryBaseColor().darker().darker());
		g.fillRect(9, 2, 5, 28);
		g.fillRect(18, 2, 5, 28);
		pauseIcon = new ImageIcon(pauseImage);
		
		playImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = playImage.getGraphics();
		g.setColor(theme.getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(theme.getPrimaryBaseColor().darker().darker());
		Polygon p = new Polygon();
		p.addPoint(9, 7);
		p.addPoint(23, 16);
		p.addPoint(9, 25);
		g.fillPolygon(p);			
		playIcon = new ImageIcon(playImage);
		

		stopImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = stopImage.getGraphics();
		g.setColor(theme.getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(theme.getPrimaryBaseColor().darker().darker());
		g.fillRect(5, 5, 22, 22);
		stopIcon = new ImageIcon(stopImage);
		
		skipRightImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = skipRightImage.getGraphics();
		g.setColor(theme.getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(theme.getPrimaryBaseColor().darker().darker());
		p.translate(-2, 0);
		g.fillPolygon(p);	
		g.fillRect(18, 7, 4, 18);
		skipRightIcon = new ImageIcon(skipRightImage);
		
		skipLeftImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = skipLeftImage.getGraphics();
		g.setColor(theme.getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(theme.getPrimaryBaseColor().darker().darker());
		Polygon p2 = new Polygon();
		p2.addPoint(25, 7);
		p2.addPoint(11, 16);
		p2.addPoint(25, 25);
		g.fillPolygon(p2);		
		g.fillRect(10, 7, 4, 18);
		skipLeftIcon = new ImageIcon(skipLeftImage);
		
		
		stop.setIcon(stopIcon);
		playPause.setIcon(playIcon);
		skipRight.setIcon(skipRightIcon);
		skipLeft.setIcon(skipLeftIcon);		
	}
	public void setCurrentFrame(int cf) {
		videoTimeLine.setValue(cf);
		this.repaint();
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(timeLine == null && Utilities.getPictoralFin(this) != null) {
			timeLine = Utilities.getPictoralFin(this).getTimeLine();
			timeLine.addOnFrameSelectionChangeListener((oldFrame, newFrame) -> repaint());
		}
		
		if(timeLine != null && timeLine.getCurrentFrame() != null) {
			currentFrame = timeLine.getCurrentFrame();
			
			if(currentFrame != null) {
				int displayWidth = getWidth() - PREF_BOARDER * 2;
				int displayHeight = getHeight() - frameNumber.getHeight() - controlPanel.getHeight()  - PREF_BOARDER * 2;
				
				BufferedImage image = currentFrame.getLayer(0);
				
				double ratio = (displayHeight / (double) image.getHeight()) < (displayWidth / (double) image.getWidth()) ? 
						(displayHeight / (double) image.getHeight()) : 
						(displayWidth / (double) image.getWidth());
				
				int adjustedImageWidth = (int) (image.getWidth() * ratio); 
				int adjustedImageHeight = (int) (image.getHeight() * ratio);
				
				g.drawImage(image, (displayWidth - adjustedImageWidth) / 2 + PREF_BOARDER, (displayHeight - adjustedImageHeight) / 2 + PREF_BOARDER + frameNumber.getHeight(),
						adjustedImageWidth, adjustedImageHeight, null);
			}			
		}
	}
	public void moveForward() {		
		
	}
	
	public void applyTheme(Theme theme) {
		this.theme = theme;
		
		playPause.setBackground(theme.getSecondaryBaseColor().darker().darker());
		stop.setBackground(theme.getSecondaryBaseColor().darker().darker());
		skipLeft.setBackground(theme.getSecondaryBaseColor().darker().darker());
		skipRight.setBackground(theme.getSecondaryBaseColor().darker().darker());
		frameNumber.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 14));
		frameNumber.setBackground(theme.getSecondaryHighlightColor());
		videoTimeLine.setBackground(theme.getSecondaryHighlightColor());
		controlPanel.setBackground(theme.getPrimaryBaseColor());
		setBackground(theme.getPrimaryBaseColor());
		
		
		setUpIconImages(theme);
	}
}
