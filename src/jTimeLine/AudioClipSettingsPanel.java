package jTimeLine;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import interfaces.SettingsPanel;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import objectBinders.Theme;
import utilities.ChainGBC;

public class AudioClipSettingsPanel extends SettingsPanel {
	private static final long serialVersionUID = -2976670177379743645L;
	
	private JSpinner startMilli, lengthMilli;
	private SpinnerNumberModel startingMilliModel, lengthMilliModel;
	private JSlider volume;
	private JLabel volumeLabel, trackTitle;
	private JButton playSample, removeAudio;
	private Theme theme;
	private MediaPlayer player;
	private AudioClip audioClip;
	
	
	
	public AudioClipSettingsPanel(AudioClip audioClip, Theme theme) {
		this.audioClip = audioClip;
		player = audioClip.getMediaPlayer();
		this.theme = theme;
		
		Font labelFont = new Font(theme.getPrimaryFont(), Font.BOLD, 17);
		Font buttonFont = new Font(theme.getPrimaryFont(), Font.BOLD + Font.ITALIC, 25);
		
		setLayout(new GridBagLayout());	
		setBackground(theme.getPrimaryBaseColor());

		startingMilliModel = new SpinnerNumberModel(audioClip.getStartTime(), 0, Integer.MAX_VALUE, 10);
		startingMilliModel.addChangeListener(e -> {
			audioClip.setStartTime((int) Math.floor((double) startingMilliModel.getValue())); 
			audioClip.setLength((int) Math.floor((double) lengthMilliModel.getValue()));repaint();});
		
		lengthMilliModel = new SpinnerNumberModel(audioClip.getLength(), 0, (int) audioClip.getMaxLength(), 10);		
		lengthMilliModel.addChangeListener(e ->{
			audioClip.setLength((int) Math.floor((double) lengthMilliModel.getValue()));repaint();});
		
		startMilli = new JSpinner(startingMilliModel);
		lengthMilli = new JSpinner(lengthMilliModel);
		
		volume = new JSlider(0, 100, (int) (audioClip.getVolume() * 100));
		volume.addChangeListener(e->{volumeLabel.setText("Volume (" + volume.getValue() + "%):"); audioClip.setVolume((volume.getValue() / 100.0));});
		volumeLabel = new JLabel("Volume (" + volume.getValue() + "%):");
		volumeLabel.setFont(labelFont);
		volumeLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		playSample = new JButton("Play Sample");
		playSample.setFont(buttonFont);
		playSample.addMouseListener(new PlaySampleMouseListener());
		removeAudio = new JButton("Remove Audio");
		removeAudio.addActionListener(e-> {((AudioTimeLine) audioClip.getParent()).removeAudioClip(audioClip); dettach();});
		removeAudio.setFont(buttonFont);
		
		JLabel startLabel = new JLabel("Start Point (Milli):");
		startLabel.setHorizontalAlignment(JLabel.RIGHT);
		startLabel.setFont(labelFont);
		JLabel lengthLabel = new JLabel("Length (Milli):");
		lengthLabel.setHorizontalAlignment(JLabel.RIGHT);
		lengthLabel.setFont(labelFont);
		
		trackTitle = new JLabel("-----  " + audioClip.getName() + "  -----");
		trackTitle.setHorizontalAlignment(JLabel.CENTER);
		trackTitle.setFont(new Font(theme.getTitleFont(), Font.PLAIN, 23));
		
		add(trackTitle,                     new ChainGBC(0, 0).setWidthAndHeight(2, 1).setFill(false).setPadding(50, 5, 10, 5));
		add(startLabel,                     new ChainGBC(0, 1).setWidthAndHeight(1, 1).setFill(false).setPadding(50, 5, 10, 5));
		add(startMilli,                     new ChainGBC(1, 1).setWidthAndHeight(1, 1).setFill(true) .setPadding(5, 50, 10, 5));
		add(lengthLabel,                    new ChainGBC(0, 2).setWidthAndHeight(1, 1).setFill(false).setPadding(50, 5, 5,  5));
		add(lengthMilli,                    new ChainGBC(1, 2).setWidthAndHeight(1, 1).setFill(true) .setPadding(5, 50, 5,  5));
		add(volumeLabel,                    new ChainGBC(0, 3).setWidthAndHeight(1, 1).setFill(false).setPadding(50, 5, 5,  5));
		add(volume,                         new ChainGBC(1, 3).setWidthAndHeight(1, 1).setFill(true) .setPadding(5, 50, 5,  5));
		add(playSample,                     new ChainGBC(0, 4).setWidthAndHeight(1, 1).setFill(false).setPadding(5,  5, 5,  5));
		add(removeAudio,                    new ChainGBC(1, 4).setWidthAndHeight(1, 1).setFill(false).setPadding(5,  5, 5,  5));
	}
	
	private class PlaySampleMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}

		public void mousePressed(MouseEvent arg0) {
			player.seek(Duration.millis(-5000));
			player.play();
		}

		public void mouseReleased(MouseEvent arg0) {
			player.pause();
		}
		
	}
	
	public void paintComponent(Graphics g) {
		trackTitle.setText("-----  " + audioClip.getName() + "  -----");
		super.paintComponent(g);
		
		g.setColor(theme.getPrimaryHighlightColor());
		
		if(!theme.isSharp()) {
			g.fillRoundRect(0, 0, getWidth(), getHeight() + 1000, 60, 100);
		} else {
			Polygon p = new Polygon();
			p.addPoint(0, getHeight());
			p.addPoint(0, (int) (getHeight() * .33));
			p.addPoint(30, 0);
			p.addPoint(getWidth() - 30, 0);
			
			p.addPoint(getWidth(), (int) (getHeight() * .33));
			p.addPoint(getWidth(), getHeight());
			g.fillPolygon(p);
		}
		
	}
	
}
