package JTimeLine;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import interfaces.Themed;
import mainFrame.PictoralFin;
import objectBinders.Theme;

public class AudioTimeLine extends JPanel implements Themed {

	private static final long serialVersionUID = 8196866343586565813L;
	private ArrayList<AudioClip> audioClips;
	private Theme theme;
	private JButton addAudio;

	public AudioTimeLine(PictoralFin pictoralFin) {
		this.theme = pictoralFin.getSettings().getTheme();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		audioClips = new ArrayList<>();
		setBackground(theme.getPrimaryBaseColor());
		
		addAudio = new JButton("Add Audio");
		addAudio.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddAudioRequest);
		addAudio.setIcon(new ImageIcon(pictoralFin.getGlobalImageKit().audioIcon));
		addAudio.setFont(new Font(theme.getPrimaryFont(), Font.ITALIC, 50));
		addAudio.setBackground(theme.getPrimaryHighlightColor());
		add(addAudio);
	}
	
	
	public void addAudioClip(AudioClip audioClip) {
		audioClips.add(audioClip);
		System.out.println(audioClips.size());
	}	
	
	public void applyTheme(Theme theme) {
		// TODO Auto-generated method stub
		
	}
	
}
