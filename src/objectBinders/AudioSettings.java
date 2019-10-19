package objectBinders;

import java.util.ArrayList;

public class AudioSettings {
	private ArrayList<AudioInfo> info;
	
	public AudioSettings() {
		info = new ArrayList<>();
	}
	
	public AudioInfo getAudioInfo(int index) {
		return info.get(index);
	}	
	public void addAudioInfo(AudioInfo audioInfo) {
		info.add(audioInfo);
	}
	public ArrayList<AudioInfo> getAudioInfo() {
		return info;
	}
}
