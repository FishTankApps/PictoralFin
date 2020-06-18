package interfaces;

import javax.swing.JPanel;

import jComponents.videoEditor.VideoEditorSettingsPanel;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = -868608342766727479L;
	
	public void dettach() {
		((VideoEditorSettingsPanel) getParent().getParent().getParent()).dettachSettingsPanel();
	}
}
