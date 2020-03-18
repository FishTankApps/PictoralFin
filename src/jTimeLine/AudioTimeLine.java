package jTimeLine;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import interfaces.Themed;
import javafx.embed.swing.JFXPanel;
import javafx.util.Duration;
import mainFrame.PictoralFin;
import objectBinders.Theme;

public class AudioTimeLine extends JFXPanel implements Themed {

	private static final long serialVersionUID = 8196866343586565813L;
	private Theme theme;
	private JButton addAudio;
	private JTimeLine jTimeLine;
	private PictoralFin pictoralFin;

	public AudioTimeLine(PictoralFin pictoralFin, JTimeLine jTimeLine) {
		this.jTimeLine = jTimeLine;
		this.pictoralFin = pictoralFin;
		this.theme = pictoralFin.getSettings().getTheme();
		setLayout(new GridLayout(0, 1, 10, 10));
		
		setBackground(theme.getPrimaryBaseColor());
		
		addAudio = new JButton("Add Audio");
		addAudio.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddAudioRequest);
		addAudio.setIcon(new ImageIcon(pictoralFin.getGlobalImageKit().audioIcon));
		addAudio.setFont(new Font(theme.getPrimaryFont(), Font.ITALIC, 50));
		addAudio.setBackground(theme.getPrimaryHighlightColor());
		//add(addAudio);
		
		jTimeLine.addOnFrameSelectionChangeListener((o,n)->unselectAudioClip()); 
	}
	
	
	public void addAudioClip(AudioClip audioClip) {
		add(audioClip);		
		audioClip.checker.passPictoralFin(pictoralFin);
		revalidate();
		repaint();
	}	
	
	public void removeAudioClip(AudioClip audioClip) {
		remove(audioClip);
		revalidate();
		repaint();
	}
	
	public void unselectAudioClip() {
		for(Component c : getComponents()) {
			if(c instanceof AudioClip)
				((AudioClip) c).selected = false;
		}
		revalidate();
		repaint();
	}
	
	public void applyTheme(Theme theme) {
		jTimeLine.repaint();		
	}
	
	
	public void seekTo(int milli) {
		for(Component c : getComponents()) {
			if(c instanceof AudioClip) {
				((AudioClip) c).getMediaPlayer().seek(Duration.millis(milli));
			}
		}
	}	
	public void play() {
		for(Component c : getComponents()) {
			if(c instanceof AudioClip) {
				((AudioClip) c).play();
			}
		}
	}
	public void pause() {
		for(Component c : getComponents()) {
			if(c instanceof AudioClip) {
				((AudioClip) c).pause();
			}
		}
	}
	public void stop() {
		for(Component c : getComponents()) {
			if(c instanceof AudioClip) {
				((AudioClip) c).stop();
			}
		}
	}
}
