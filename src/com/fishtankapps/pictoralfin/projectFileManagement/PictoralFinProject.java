package com.fishtankapps.pictoralfin.projectFileManagement;

import java.io.Serializable;

import com.fishtankapps.pictoralfin.jTimeLine.AudioClipData;
import com.fishtankapps.pictoralfin.objectBinders.Frame;

public class PictoralFinProject implements Serializable {

	private static final long serialVersionUID = 8288529796501821226L;
	private PictoralFinProjectSettings settings;
	private AudioClipData[] audioData;
	private Frame[] frames;	
	
	public PictoralFinProject(Frame[] frames, AudioClipData[] audioData, PictoralFinProjectSettings settings) {
		this.frames = frames;
		this.settings = settings;
		this.audioData = audioData;
	}

	public Frame[] getFrames() {
		return frames;
	}

	public AudioClipData[] getAudioData() {
		return audioData;
	}	
	
	public PictoralFinProjectSettings getSettings() {
		return settings;
	}	
}
