package com.fishtankapps.pictoralfin.listeners;

import com.fishtankapps.pictoralfin.jComponents.pictureEditor.LayerButton;

public interface OnSelectedLayerChangedListener {

	public abstract void selectedlayerChanged(LayerButton oldLayerButton, LayerButton newLayerButton);
	
}
