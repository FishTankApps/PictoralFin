package projectFileManagement;

import java.io.Serializable;

import mainFrame.PictoralFin;

public class PictoralFinStaticProjectSettings implements Serializable {
	private static final long serialVersionUID = -3666128485607612104L;
	
	private String notes;
	
	public PictoralFinStaticProjectSettings(PictoralFin pictoralFin) {
		notes = pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().getNotes();
	}
	
	public void applySettings(PictoralFin pictoralFin) {
		pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().setNotes(notes);
	}
	
	// Project-specific settings will go here
}
