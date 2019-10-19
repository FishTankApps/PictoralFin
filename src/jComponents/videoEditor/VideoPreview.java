package jComponents.videoEditor;

import static globalValues.GlobalVariables.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;

import objectBinders.Picture;


public class VideoPreview extends JPanel{
	
	private static final long serialVersionUID = -1247649334503994677L;

	private Thread videoPreviewThread;
	
	private JButton playPause, stop, skipLeft, skipRight;
	private JPanel buttonPanel, panelContainedIn;
	private JSlider videoTimeLine;
	private JTextPane  frameNumber;
	private ImageIcon playIcon, pauseIcon, stopIcon, skipLeftIcon, skipRightIcon;
	
	public VideoPreview(JPanel panelContainedIn) {
		this.panelContainedIn = panelContainedIn;
		
		playPause = new JButton();
		playPause.addActionListener(e -> {previewIsPlaying = !previewIsPlaying && pfk.getFrameTimeLine().length() != 0; repaint();});
		playPause.setBackground(settings.getTheme().getSecondaryBaseColor().darker().darker());
		stop = new JButton();
		stop.addActionListener(e-> {previewIsPlaying = false; videoTimeLine.setValue(0); repaint();});
		stop.setBackground(settings.getTheme().getSecondaryBaseColor().darker().darker());
		skipLeft = new JButton();
		skipLeft.addActionListener(e-> {videoTimeLine.setValue(videoTimeLine.getValue() - 1); repaint();});
		skipLeft.setBackground(settings.getTheme().getSecondaryBaseColor().darker().darker());
		skipRight = new JButton();
		skipRight.addActionListener(e-> {videoTimeLine.setValue(videoTimeLine.getValue() + 1); repaint();});
		skipRight.setBackground(settings.getTheme().getSecondaryBaseColor().darker().darker());
		
		frameNumber = new JTextPane();
		frameNumber.setEditable(false);
		frameNumber.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 14));
		frameNumber.setText("");
		frameNumber.setBackground(settings.getTheme().getSecondaryHighlightColor());

		
		videoTimeLine = new JSlider(0,0,0);
		videoTimeLine.addChangeListener(e -> repaint());
		videoTimeLine.setBackground(settings.getTheme().getSecondaryHighlightColor());
		
		buttonPanel = new JPanel() {
			private static final long serialVersionUID = -2707726343692579133L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(settings.getTheme().getPrimaryHighlightColor());
				g.fillRoundRect(0, 0, getWidth(), getHeight()+100, 50, 50);
				
			}
		};
		buttonPanel.add(videoTimeLine);
		buttonPanel.add(skipLeft);
		buttonPanel.add(playPause);		
		buttonPanel.add(stop);
		buttonPanel.add(skipRight);
		buttonPanel.setBackground(settings.getTheme().getPrimaryBaseColor());
		
		
		this.setLayout(new BorderLayout());	
		
		this.add(buttonPanel,  BorderLayout.SOUTH);
		this.add(frameNumber, BorderLayout.NORTH);
		
		setUpIconImages();
		
		this.setBackground(settings.getTheme().getPrimaryBaseColor());
		videoPreviewThread = new Thread(new Runnable() {

			@Override
			public void run() {		
				
				try {
					long timeSinceLastWait = System.currentTimeMillis();
	           	    
					while(currentlyRunning) {
						if(previewIsPlaying && System.currentTimeMillis() - timeSinceLastWait > (int) ((1.0/framesPerSecond) * 1000)) {								
							timeSinceLastWait = System.currentTimeMillis();
							moveForward();	
						}else {
							Thread.sleep(0,1);
						}
					}
				}catch(Exception i) {i.printStackTrace();}				
			}
			
		});
		videoPreviewThread.start();		
		
		this.setMinimumSize(new Dimension(1000,500));
	}	
	public void setUpIconImages() {
		BufferedImage playImage, pauseImage, stopImage, skipLeftImage, skipRightImage;
		
		pauseImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = pauseImage.getGraphics();
		g.setColor(settings.getTheme().getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(settings.getTheme().getPrimaryBaseColor().darker().darker());
		g.fillRect(9, 2, 5, 28);
		g.fillRect(18, 2, 5, 28);		
		pauseIcon = new ImageIcon(pauseImage);
		
		playImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = playImage.getGraphics();
		g.setColor(settings.getTheme().getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(settings.getTheme().getPrimaryBaseColor().darker().darker());
		Polygon p = new Polygon();
		p.addPoint(9, 7);
		p.addPoint(23, 16);
		p.addPoint(9, 25);
		g.fillPolygon(p);			
		playIcon = new ImageIcon(playImage);
		

		stopImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = stopImage.getGraphics();
		g.setColor(settings.getTheme().getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(settings.getTheme().getPrimaryBaseColor().darker().darker());
		g.fillRect(5, 5, 22, 22);
		stopIcon = new ImageIcon(stopImage);
		
		skipRightImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = skipRightImage.getGraphics();
		g.setColor(settings.getTheme().getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(settings.getTheme().getPrimaryBaseColor().darker().darker());
		p.translate(-2, 0);
		g.fillPolygon(p);	
		g.fillRect(18, 7, 4, 18);
		skipRightIcon = new ImageIcon(skipRightImage);
		
		skipLeftImage = new BufferedImage(32,32, BufferedImage.TYPE_3BYTE_BGR);
		g = skipLeftImage.getGraphics();
		g.setColor(settings.getTheme().getSecondaryBaseColor());
		g.fillRect(0, 0, 35, 35);
		g.setColor(settings.getTheme().getPrimaryBaseColor().darker().darker());
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
	public void refresh() {
		final Dimension BUTTON_SIZE = new Dimension(40,40);		

		this.setPreferredSize(panelContainedIn.getSize());

		buttonPanel.setPreferredSize(new Dimension(panelContainedIn.getWidth() - 10, 70));
		videoTimeLine.setPreferredSize(new Dimension(panelContainedIn.getWidth() - 20, 10));
		videoTimeLine.setMaximum((pfk.getFrameTimeLine().length() == 0) ? 0 : pfk.getFrameTimeLine().length() - 1);
		
		playPause.setPreferredSize(BUTTON_SIZE);
		stop.setPreferredSize(BUTTON_SIZE);
		skipLeft.setPreferredSize(BUTTON_SIZE);
		skipRight.setPreferredSize(BUTTON_SIZE);
		
		this.repaint();		
	}	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		playPause.setIcon((previewIsPlaying) ? pauseIcon : playIcon);
		
		if(pfk.getFrameTimeLine().length() > 0) {
			int width, height;
			double widthRatio, heightRatio, ratio;
			
			frameNumber.setText("   Frame: #" + (videoTimeLine.getValue() + 1));
			pfk.getFrameTimeLine().setCurrentFrame(videoTimeLine.getValue());
			
			BufferedImage currentFrame = pfk.getFrameTimeLine().getCurrentFrame().getImage(false);
			
			widthRatio = ((double) panelContainedIn.getWidth() - 50) / currentFrame.getWidth();
			heightRatio = ((double) panelContainedIn.getHeight() - 50 - buttonPanel.getHeight()) / currentFrame.getHeight();		
			ratio = (widthRatio < heightRatio) ? widthRatio : heightRatio;
			
			width = (int) (currentFrame.getWidth() * ratio);
			height = (int) (currentFrame.getHeight() * ratio);
			
			g.drawImage(currentFrame, (panelContainedIn.getWidth() - width)/2, (panelContainedIn.getHeight() - height)/2 - (buttonPanel.getHeight()/2), width, height, null);	
			
			g.setColor(settings.getTheme().getSecondaryHighlightColor());
			g.fillRoundRect(0, -20, getWidth(), frameNumber.getHeight() + 27, 20, 20);
		}else {
			frameNumber.setText("");
		}
	}
	public void moveForward() {		
		if(videoTimeLine.getValue() == pfk.getFrameTimeLine().length() - 1) { 
			previewIsPlaying = false;
			videoTimeLine.setValue(0);
			this.repaint();
			return;
		}
		
		videoTimeLine.setValue(videoTimeLine.getValue() + 1);
		this.repaint();
	}

	public Picture getCurrentFrame() {
		return pfk.getFrameTimeLine().getCurrentFrame();
	}
}
