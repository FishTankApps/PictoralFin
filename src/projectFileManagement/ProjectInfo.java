package projectFileManagement;

import java.io.Serializable;
import java.util.ArrayList;

import jTimeLine.AudioClip;
import mainFrame.PictoralFin;
import objectBinders.Frame;

class ProjectInfo implements Serializable {

	private static final long serialVersionUID = -4503720095593343977L;

	private ArrayList<AudioInfo> audioInfo;
	private ArrayList<FrameInfo> frameInfo;
	private PictoralFinStaticProjectSettings projectSettings;
		
	public ProjectInfo(PictoralFin pictoralFin) {
		
		//---------{ Generate Frame Info }-------------------
		frameInfo = new ArrayList<>();
		if(pictoralFin.getTimeLine().getFrames() != null)
			for(Frame frame : pictoralFin.getTimeLine().getFrames()) 
				frameInfo.add(new FrameInfo(frame.getDuration()));
		
		//--------{ Generate Audio Info }---------------------
		audioInfo = new ArrayList<>();
		if(pictoralFin.getTimeLine().getAudioClips() != null)
			for(AudioClip audioClip : pictoralFin.getTimeLine().getAudioClips()) 
				audioInfo.add(new AudioInfo(audioClip.getName(), audioClip.getStartTime(), audioClip.getEndTime(), audioClip.getVolume()));
			
		
		projectSettings = new PictoralFinStaticProjectSettings(pictoralFin);
	}

	public ArrayList<AudioInfo> getAudioInfo() {
		return audioInfo;
	}
	public ArrayList<FrameInfo> getFrameInfo() {
		return frameInfo;
	}
	public PictoralFinStaticProjectSettings getProjectSettings() {
		return projectSettings;
	}
}
