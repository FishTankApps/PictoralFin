package com.fishtankapps.pictoralfin.projectFileManagement;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

public class PictoralFinProjectSettings_v1 extends PictoralFinProjectSettings {

	private static final long serialVersionUID = -8299539571210345283L;

	private String notes;
	
	public PictoralFinProjectSettings_v1(PictoralFin pictoralFin) {
		super(pictoralFin);
		notes = pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().getNotes();
	}

	public void applySettings(PictoralFin pictoralFin) {
		pictoralFin.getVideoEditor().getVideoEditorSettingsPanel().setNotes(notes);
	}

}
