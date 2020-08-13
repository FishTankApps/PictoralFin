package com.fishtankapps.pictoralfin.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

import javafx.application.Platform;

public class OnAddVideoRequest implements ActionListener {
	private PictoralFin pictoralFin;
	
	public OnAddVideoRequest(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Platform.runLater(()->pictoralFin.getVideoImporter().importVideo());
	}
}
