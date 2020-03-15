package globalToolKits;

import listeners.OnAddAudioRequest;
import listeners.OnAddPictureRequest;
import mainFrame.PictoralFin;

public class GlobalListenerToolKit {

	public OnAddPictureRequest onAddPictureRequest;
	public OnAddAudioRequest onAddAudioRequest;
	
	public GlobalListenerToolKit(PictoralFin pictoralFin) {
		onAddPictureRequest = new OnAddPictureRequest(pictoralFin);
		onAddAudioRequest = new OnAddAudioRequest(pictoralFin);
	}	
}
