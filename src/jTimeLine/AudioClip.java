package jTimeLine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JComponent;

import interfaces.Closeable;
import interfaces.SettingsPanel;
import interfaces.SettingsPanelOOI;
import interfaces.Themed;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import mainFrame.PictoralFin;
import objectBinders.Theme;
import utilities.Utilities;

public class AudioClip extends JComponent implements MouseListener, Themed, SettingsPanelOOI, Closeable {

	
	private static final long serialVersionUID = 5730191032565741283L;
	private File audioFile;
	private String name;
	private long startTime, length;
	private double volume = 1.0;
	private MediaPlayer mediaPlayer;
	private JTimeLine jTimeLine;
	boolean hovering = false, selected = false;
	Checker checker;
	
	private long currentMillis = 0;
	private boolean playing = false;
	
	public AudioClip(String filePath, JTimeLine jTimeLine) {
		this(new File(filePath), jTimeLine);
	}
	
	public AudioClip(File audioFile, JTimeLine jTimeLine) {
		this.audioFile = audioFile;
		this.jTimeLine = jTimeLine;
		this.checker = new Checker();
		name = audioFile.getName();
		
		mediaPlayer = new MediaPlayer(new Media(audioFile.toURI().toString()));
		
		startTime = 0;
		length = -1;

		
		mediaPlayer.setOnReady(new Runnable() {
		        public void run() {
		        	length = (jTimeLine.getVideoDurration() < mediaPlayer.getTotalDuration().toMillis()) ? 
		            		jTimeLine.getVideoDurration() : (int) mediaPlayer.getTotalDuration().toMillis();
		        }
		    });
		
		while(length == -1) {try {Thread.sleep(10);}catch(Exception e) {}}
		
		
		addMouseListener(this);
	
		new Thread(checker).start();
		
		
		setVolume(.75);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getSize().width, jTimeLine.getFrameTimeLine().frameDimension/7);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Theme theme = jTimeLine.getFrameTimeLine().theme;
		
		int buttonSize = jTimeLine.getFrameTimeLine().frameDimension;
		int startIndex = (jTimeLine.getIndexOfFrameAtMilli(startTime) * buttonSize) + (5 * jTimeLine.getIndexOfFrameAtMilli(startTime)); // 5 = H-Gap in FrameTimeLine's Buttons
		int endIndex =   (jTimeLine.getIndexOfFrameAtMilli(startTime+length)  * buttonSize) + (5 * jTimeLine.getIndexOfFrameAtMilli(startTime+length));
		
		if(jTimeLine.getIndexOfFrameAtMilli(startTime) == -1) {
			startIndex = 0;
		} else if(jTimeLine.getIndexOfFrameAtMilli(startTime) == -2) {
			startIndex = (jTimeLine.numberOfFrame() * buttonSize) + (5 * jTimeLine.numberOfFrame());
		}
		
		endIndex = (endIndex <= 0) ? getWidth() : endIndex;
		
		Color buttonColor = (!selected) ? theme.getPrimaryHighlightColor() : theme.getSecondaryHighlightColor();
		
		g.setColor((hovering) ? buttonColor.brighter().brighter() : buttonColor);
		
		if(theme.isSharp())		
			g.fillRect(startIndex, 0, (endIndex - startIndex), getHeight());
		else
			g.fillRoundRect(startIndex, 0, (endIndex - startIndex), getHeight(), 10, 10);
		
		g.setColor(theme.getPrimaryBaseColor());
		g.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, (jTimeLine.getFrameTimeLine().frameDimension/7) - 5));
		for(int count = startIndex + buttonSize; count < (endIndex - startIndex); count+=(5*buttonSize)) {
			g.drawString(getName(), count, (jTimeLine.getFrameTimeLine().frameDimension/7) - 5);
		}
		
	}
	
	public File getAudioFile() {
		return audioFile;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return startTime+length;
	}
	public long getLength() {
		return length;
	}
	public long getMaxLength() {
		return (long) mediaPlayer.getTotalDuration().toMillis();
	}
	public double getVolume() {
		return volume;
	}
	public String getName() {
		return name;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
		repaint();
	}
	public void setEndTime(long endTime) {
		this.length = endTime - startTime;
		repaint();
	}
	public void setLength(long length) {
		this.length = length;
		repaint();
	}
	public void setVolume(double volume) {
		this.volume = volume;
		mediaPlayer.setVolume(volume);
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public MediaPlayer getMediaPlayer() {
		 return mediaPlayer;
	}

	
	public void seek(int milli) {
		currentMillis = milli;
	}
	public void pause() {
		playing = false;
	}
	public void play() {
		playing = true;
	}
	public void stop() {
		playing = false;
	}
			
	
	class Checker implements Runnable {

		long lastCheck = 0;
		PictoralFin pictoralFin = null;
		boolean playerIsPlaying = false;
	
		public Checker() {
			lastCheck = System.currentTimeMillis();
		}
		
		void passPictoralFin(PictoralFin pictoralFin) {
			this.pictoralFin = pictoralFin;
		}
		
		public void run() {
			try {
				while(pictoralFin == null) {Thread.sleep(10);}
				
				while(true) {			
					
					while(playing) {						
						Thread.sleep(10);
						
						if(Math.abs(currentMillis - pictoralFin.getVideoEditor().getVideoPreview().currentMilli) > 50)
							playerIsPlaying = false;
						
						currentMillis = pictoralFin.getVideoEditor().getVideoPreview().currentMilli;
						
						if(!playerIsPlaying && currentMillis > startTime && currentMillis < startTime+length) {
							mediaPlayer.seek(Duration.millis(currentMillis - startTime));
							mediaPlayer.play();		
							playerIsPlaying = true;
						} else if (playerIsPlaying && (currentMillis >= startTime+length)) 
							break;				
					}	
					
					mediaPlayer.pause();
					playerIsPlaying = false;
					
					while(!playing) {Thread.sleep(10);}
				}
			} catch (Exception e) {}
		}		
	}
	
	
	public void mouseClicked(MouseEvent arg0) {		
		((AudioTimeLine) getParent()).unselectAudioClip();
		selected = true;
		Utilities.getPictoralFin(this).getVideoEditor().getVideoEditorSettingsPanel().attachSettingsPanel(generateSettingsPanel());
	}

	public void mouseEntered(MouseEvent arg0) {
		hovering = true;
		repaint();
	}
	public void mouseExited(MouseEvent arg0) {
		hovering = false;
		repaint();
	}
	public void mousePressed(MouseEvent arg0) {}	
	public void mouseReleased(MouseEvent arg0) {}
	
	public SettingsPanel generateSettingsPanel() {
		return new AudioClipSettingsPanel(this, jTimeLine.getFrameTimeLine().theme);
	}

	public void applyTheme(Theme theme) {
		repaint();		
	}

	
	public void close() {
		mediaPlayer.dispose();		
	}
}
