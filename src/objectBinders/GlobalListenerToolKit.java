package objectBinders;

import listeners.OnAddPictureRequest;
import mainFrame.PictoralFin;

public class GlobalListenerToolKit {

	public OnAddPictureRequest onAddPictureRequest;
	
	public GlobalListenerToolKit(PictoralFin pictoralFin) {
		onAddPictureRequest = new OnAddPictureRequest(pictoralFin);
	}	
}
