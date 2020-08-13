package com.fishtankapps.pictoralfin.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

import javafx.application.Platform;

public class OnAddAudioRequest implements ActionListener {

	private PictoralFin pictoralFin;

	public OnAddAudioRequest(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Platform.runLater(()->pictoralFin.getAudioImporter().importAudio());
	}

}
