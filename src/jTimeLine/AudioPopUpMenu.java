package jTimeLine;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import utilities.Utilities;

class AudioPopUpMenu{
	
	private JPopupMenu menu;
	private AudioClip clip;
	
	private final ActionListener REMOVE_AUDIO = e -> {
		AudioTimeLine audioTimeLine = (AudioTimeLine) clip.getParent();
		audioTimeLine.removeAudioClip(clip);
		menu.setVisible(false);
		
		Utilities.getPictoralFin(audioTimeLine).getVideoEditor().getVideoEditorSettingsPanel().dettachSettingsPanel();
	};
	
	private final ActionListener RENAME_AUDIO = e -> {
		String s = JOptionPane.showInputDialog("Enter the new name:", clip.getName());
		
		if(s != null)
			clip.setName(s);
		
		clip.getParent().repaint();
		Utilities.getPictoralFin(clip).getVideoEditor().getVideoEditorSettingsPanel().repaint();
	};
	
	public AudioPopUpMenu(AudioClip clip, int x, int y){		
		this.clip = clip;		
		menu = new JPopupMenu();
		
		JMenuItem renameFrame = new JMenuItem("Rename");
		renameFrame.addActionListener(RENAME_AUDIO);
		
		JMenuItem removeFrame = new JMenuItem("Remove");
		removeFrame.addActionListener(REMOVE_AUDIO);

		menu.add(removeFrame);
		menu.add(renameFrame);
		menu.show(clip, x, y);
	}
}
