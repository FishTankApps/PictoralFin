package jTimeLine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import interfaces.SettingsPanel;
import interfaces.SettingsPanelOOI;
import interfaces.Themed;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import objectBinders.Theme;
import utilities.Utilities;

public class AudioClip extends JComponent implements MouseListener, Themed, SettingsPanelOOI {

	
	private static final long serialVersionUID = 5730191032565741283L;
	private File audioFile;
	private int startTime, endTime;
	private double volume = 1.0;
	private MediaPlayer mediaPlayer;
	private JTimeLine jTimeLine;
	boolean hovering = false, selected = false;
	
	public AudioClip(String filePath, JTimeLine jTimeLine) {
		this(new File(filePath), jTimeLine);
	}
	
	public AudioClip(File audioFile, JTimeLine jTimeLine) {
		this.audioFile = audioFile;
		this.jTimeLine = jTimeLine;
		
		startTime = 100;
		endTime = 1000;
		
		addMouseListener(this);
		
		
		try {
			mediaPlayer = new MediaPlayer(new Media(audioFile.toURI().toString()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error Opening Audio file: " + audioFile.getAbsolutePath(), "Error Playing Sound", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}		
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(getParent().getSize().width, jTimeLine.getFrameTimeLine().frameDimension/7);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Theme theme = jTimeLine.getFrameTimeLine().theme;
		
		int buttonSize = jTimeLine.getFrameTimeLine().frameDimension;
		int startIndex = (jTimeLine.getIndexOfFrameAtMilli(startTime) * buttonSize) + (5 * jTimeLine.getIndexOfFrameAtMilli(startTime)); // 5 = H-Gap in FrameTimeLine's Buttons
		int endIndex =   (jTimeLine.getIndexOfFrameAtMilli(endTime)   * buttonSize) + (5 * jTimeLine.getIndexOfFrameAtMilli(endTime));
		
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
		
		g.setColor(Color.BLACK);
		g.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, (jTimeLine.getFrameTimeLine().frameDimension/7) - 5));
		for(int count = startIndex + buttonSize; count < (endIndex - startIndex); count+=(5*buttonSize)) {
			g.drawString(audioFile.getName(), count, (jTimeLine.getFrameTimeLine().frameDimension/7) - 5);
		}
		
	}
	
	public File getAudioFile() {
		return audioFile;
	}
	public int getStartTime() {
		return startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public int getLength() {
		return endTime - startTime;
	}
	public double getVolume() {
		return volume;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public void setLength(int length) {
		endTime = startTime + length;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}

	public MediaPlayer getMediaPlayer() {
		 return mediaPlayer;
	}

	
	
	public void mouseClicked(MouseEvent arg0) {
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
		return new AudioClipSettingsPanel(this);
	}

	public void applyTheme(Theme theme) {
		repaint();		
	}
}
