package com.fishtankapps.pictoralfin.jComponents.pictureEditor.imageEditorTools;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.LayerButton;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.Constants;

public class LayerManipulatorTool extends ImageEditorTool {

	private static final long serialVersionUID = 910560043106942250L;

	private JButton moveLayerUp, moveLayerDown, combineLayers;
	
	private ImageEditor editor;
	
	public LayerManipulatorTool(ImageEditor editor, Theme theme) {
		super("Layer Manipulator", editor, theme, false);		
		this.editor = editor;

		this.setLayout(new GridBagLayout());
		
		moveLayerUp = new JButton("Move Layer Up");
		moveLayerDown = new JButton("Move Layer Down");
		
		combineLayers = new JButton("Combine Layers");
		
		
		moveLayerUp.addActionListener(e->editor.getLayerSelecter().moveSelectedLayer(true));
		moveLayerDown.addActionListener(e->editor.getLayerSelecter().moveSelectedLayer(false));
		
		combineLayers.addActionListener(e->combineLayers());
		
		
		add(moveLayerUp,   new ChainGBC(0,0).setPadding(5).setFill(true, false));
		add(moveLayerDown, new ChainGBC(0,1).setPadding(5).setFill(true, false));
		add(combineLayers, new ChainGBC(1,0).setPadding(5).setFill(true, false).setWidthAndHeight(1, 2));
	}

	
	private void combineLayers() {
		if(editor.getSelectedFrame() == null || editor.getSelectedFrame().getNumberOfLayers() < 2)
			return;
		
		int largestWidth = 0;
		int largestHeight = 0;
		
		for(BufferedImage layer : editor.getSelectedFrame().getLayers()) {
			if(layer.getWidth() > largestWidth)
				largestWidth = layer.getWidth();
			
			if(layer.getHeight() > largestHeight)
				largestHeight = layer.getHeight();
		}
		
		BufferedImage combinedLayer = new BufferedImage(largestWidth, largestHeight, Constants.IMAGE_TYPE);
		Graphics imageGraphics = combinedLayer.getGraphics();
		BufferedImage layer;
		
		for(int index = editor.getSelectedFrame().getNumberOfLayers() - 1; index >= 0; index--) {
			layer = editor.getSelectedFrame().getLayer(index);
			imageGraphics.drawImage(layer, (largestWidth - layer.getWidth()) / 2, 
					(largestHeight - layer.getHeight()) / 2, layer.getWidth(), layer.getHeight(), null);
		}
		
		editor.getSelectedFrame().getLayers().clear();		
		editor.getSelectedFrame().addLayer(combinedLayer);
		
		callForRefresh();
	}
	
	
	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {}
	protected void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame) {}
	protected void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame) {}

}
