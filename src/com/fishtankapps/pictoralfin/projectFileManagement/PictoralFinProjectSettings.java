package com.fishtankapps.pictoralfin.projectFileManagement;

import java.io.Serializable;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

public abstract class PictoralFinProjectSettings implements Serializable {
	private static final long serialVersionUID = -3666128485607612104L;
	
	public PictoralFinProjectSettings(PictoralFin pictoralFin) {}
	
	public abstract void applySettings(PictoralFin pictoralFin);
}
