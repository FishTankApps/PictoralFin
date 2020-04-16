package projectFileManagement;

import java.io.Serializable;

import jTimeLine.AudioClipData;
import objectBinders.Frame;

public class PictoralFinStaticProject implements Serializable {

	private static final long serialVersionUID = 8288529796501821226L;
	private PictoralFinStaticProjectSettings settings;
	private AudioClipData[] audioData;
	private Frame[] frames;	
	
	public PictoralFinStaticProject(Frame[] frames, AudioClipData[] audioData, PictoralFinStaticProjectSettings settings) {
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
	
	public PictoralFinStaticProjectSettings getSettings() {
		return settings;
	}	
}
