package com.fishtankapps.pictoralfin.jTimeLine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.sound.sampled.Clip;
import javax.swing.JComponent;

import com.fishtankapps.pictoralfin.interfaces.Closeable;
import com.fishtankapps.pictoralfin.interfaces.SettingsPanel;
import com.fishtankapps.pictoralfin.interfaces.SettingsPanelOOI;
import com.fishtankapps.pictoralfin.interfaces.Themed;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public class AudioClip extends JComponent implements MouseListener, Themed, SettingsPanelOOI, Closeable {

	private static final long serialVersionUID = 5730191032565741283L;
	private File audioFile;
	private Clip playableAudioClip;
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
		
		playableAudioClip = audioClipData.getAudioClip();
		
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

		audioClipData = new AudioClipData(audioFile);
		playableAudioClip = audioClipData.getAudioClip();
		
		audioClipData.setName(audioFile.getName());
		audioClipData.setStartTime(0);
		
		
		audioClipData.setLength((jTimeLine.getVideoDurration() < getMaxLength()) ? 
        		jTimeLine.getVideoDurration() : (int) getMaxLength());		
		
		
		addMouseListener(this);
	
		checkerThread = new Thread(checker);
		checkerThread.start();
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getSize().width, 20);
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
		g.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 15));
		int nameWidth = g.getFontMetrics().stringWidth(getName());
		
		for(int count = startIndex + nameWidth * 2; count < (endIndex - startIndex); count+=(nameWidth*2)) {
			g.drawString(getName(), count, 17);
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
		return (long) (playableAudioClip.getMicrosecondLength() / 1000);
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
	public void setName(String name) {
		audioClipData.setName(name);
	}
	
	public Clip getPlayableClip() {
		return playableAudioClip;
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
							playableAudioClip.setMicrosecondPosition((currentMillis - audioClipData.getStartTime()) * 1000);
							playableAudioClip.start();		
							playerIsPlaying = true;
						} else if (playerIsPlaying && (currentMillis >= audioClipData.getStartTime()+audioClipData.getLength())) 
							break;				
					}	
					
					playableAudioClip.stop();
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
		playableAudioClip.close();		
	}
}
