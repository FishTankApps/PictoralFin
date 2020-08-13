package com.fishtankapps.pictoralfin.globalToolKits;

import com.fishtankapps.pictoralfin.listeners.OnAddAudioRequest;
import com.fishtankapps.pictoralfin.listeners.OnAddPictureRequest;
import com.fishtankapps.pictoralfin.listeners.OnAddVideoRequest;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

public class GlobalListenerToolKit {

	public static OnAddPictureRequest onAddPictureRequest;
	public static OnAddAudioRequest onAddAudioRequest;
	public static OnAddVideoRequest onAddVideoRequest;

	public static void initialize(PictoralFin pictoralFin) {
		onAddPictureRequest = new OnAddPictureRequest(pictoralFin);
		onAddAudioRequest = new OnAddAudioRequest(pictoralFin);
		onAddVideoRequest = new OnAddVideoRequest(pictoralFin);
	}
}
