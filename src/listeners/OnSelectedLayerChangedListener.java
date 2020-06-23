package listeners;

import jComponents.pictureEditor.LayerButton;

public interface OnSelectedLayerChangedListener {

	public abstract void selectedlayerChanged(LayerButton oldLayerButton, LayerButton newLayerButton);
	
}
