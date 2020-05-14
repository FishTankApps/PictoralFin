package globalToolKits;

import listeners.OnAddAudioRequest;
import listeners.OnAddPictureRequest;
import listeners.OnAddVideoRequest;
import mainFrame.PictoralFin;

public class GlobalListenerToolKit {

	public OnAddPictureRequest onAddPictureRequest;
	public OnAddAudioRequest onAddAudioRequest;
	public OnAddVideoRequest onAddVideoRequest;
	
	public GlobalListenerToolKit(PictoralFin pictoralFin) {
		onAddPictureRequest = new OnAddPictureRequest(pictoralFin);
		onAddAudioRequest = new OnAddAudioRequest(pictoralFin);
		onAddVideoRequest = new OnAddVideoRequest(pictoralFin);
	}	
}
