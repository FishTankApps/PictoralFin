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
import objectBinders.Frame;
import objectBinders.Theme;
import utilities.AudioUtil;
import utilities.Utilities;

public class AudioClip extends JComponent implements MouseListener, Themed, SettingsPanelOOI, Closeable {

	private static final long serialVersionUID = 5730191032565741283L;
	private File audioFile;
	private MediaPlayer mediaPlayer;
	private JTimeLine jTimeLine;
	boolean hovering = false, selected = false;
	
	Thread checkerThread;
    Checker checker;
	
    private long currentMillis = 0;
    private boolean playing = false;
    
    private AudioClipData audioClipData;
	
    public AudioClip(AudioClipData audioClipData, JTimeLine jTimeLine) {
    	this.audioClipData = audioClipData;
    	this.audioFile = audioClipData.generateAudioTempFile();
		this.jTimeLine = jTimeLine;
		this.checker = new Checker();
		
		mediaPlayer = new MediaPlayer(new Media(audioFile.toURI().toString()));		
		
		addMouseListener(this);
	
		checkerThread = new Thread(checker);
		checkerThread.start();
    }
    
	public AudioClip(String filePath, JTimeLine jTimeLine) {
		this(new File(filePath), jTimeLine);
	}
	
	public AudioClip(File audioFile, JTimeLine jTimeLine) {
		this.audioFile = audioFile;
		this.jTimeLine = jTimeLine;
		this.checker = new Checker();
		
		audioFile = AudioUtil.convertAudioFileToMP3(audioFile);		

		audioClipData = new AudioClipData(audioFile);
		audioClipData.setName(audioFile.getName());
		audioClipData.setStartTime(0);
		audioClipData.setLength(-1);
		
		mediaPlayer = new MediaPlayer(new Media(audioFile.toURI().toString()));

		
		mediaPlayer.setOnReady(new Runnable() {
		        public void run() {
		        	audioClipData.setLength((jTimeLine.getVideoDurration() < mediaPlayer.getTotalDuration().toMillis()) ? 
		            		jTimeLine.getVideoDurration() : (int) mediaPlayer.getTotalDuration().toMillis());
		        }
		    });
		
		while(audioClipData.getLength() == -1) {try {Thread.sleep(10);}catch(Exception e) {}}
		
		
		addMouseListener(this);
	
		checkerThread = new Thread(checker);
		checkerThread.start();
		
		setVolume(.75);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getSize().width, jTimeLine.getFrameTimeLine().frameDimension/7);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Theme theme = jTimeLine.getFrameTimeLine().theme;
		
		int buttonSize = jTimeLine.getFrameTimeLine().frameDimension + 5; // 5 = H-Gap in FrameTimeLine's Buttons
		Frame endFrame =  jTimeLine.getFrameAtMilli(audioClipData.getStartTime()+audioClipData.getLength());
		int startButton = jTimeLine.getIndexOfFrameAtMilli(audioClipData.getStartTime());
		int endButton   = jTimeLine.getIndexOfFrameAtMilli(audioClipData.getStartTime()+audioClipData.getLength());
		int leftOver =    (int) (jTimeLine.getMilliAtFrame(endFrame) - audioClipData.getStartTime() - audioClipData.getLength());
		
		int startIndex = (startButton      * buttonSize);
		int endIndex =   ((endButton - ((leftOver == 0) ? 2 : 1))  * buttonSize);
		
		if(endButton >= 0) {
			endIndex += (int) ((buttonSize - 5) * ((endFrame.getDuration()-leftOver)/(double) endFrame.getDuration()));
		}
		
		if(startButton == -1) {
			startIndex = 0;
		} else if(startButton == -2) {
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
		return audioClipData.getStartTime();
	}
	public long getEndTime() {
		return audioClipData.getStartTime()+audioClipData.getLength();
	}
	public long getLength() {
		return audioClipData.getLength();
	}
	public long getMaxLength() {
		return (long) mediaPlayer.getTotalDuration().toMillis();
	}
	public double getVolume() {
		return audioClipData.getVolume();
	}
	public String getName() {
		return audioClipData.getName();
	}
	public void setStartTime(long startTime) {
		audioClipData.setStartTime(startTime);
		repaint();
	}
	public void setEndTime(long endTime) {
		audioClipData.setLength(endTime - audioClipData.getStartTime());
		repaint();
	}
	public void setLength(long length) {
		audioClipData.setLength(length);
		repaint();
	}
	public void setVolume(double volume) {
		mediaPlayer.setVolume(volume);
	}
	public void setName(String name) {
		audioClipData.setName(name);
	}
	
	public MediaPlayer getMediaPlayer() {
		 return mediaPlayer;
	}

	public AudioClipData getAudioClipData() {
		return audioClipData;
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
						
						if(!playerIsPlaying && currentMillis > audioClipData.getStartTime() && currentMillis < audioClipData.getStartTime()+audioClipData.getLength()) {
							mediaPlayer.seek(Duration.millis(currentMillis - audioClipData.getStartTime()));
							mediaPlayer.play();		
							playerIsPlaying = true;
						} else if (playerIsPlaying && (currentMillis >= audioClipData.getStartTime()+audioClipData.getLength())) 
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
		if(arg0.getButton() == 1) {
			((AudioTimeLine) getParent()).unselectAudioClip();
			selected = true;
			Utilities.getPictoralFin(this).getVideoEditor().getVideoEditorSettingsPanel().attachSettingsPanel(generateSettingsPanel());
		} else if (arg0.getButton() == 3) {
			new AudioPopUpMenu(this, arg0.getX(), arg0.getY());
		}
		
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
