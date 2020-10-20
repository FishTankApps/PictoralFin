package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Frame;

public class ImageEditor extends JPanel {
	
	private static final long serialVersionUID = 5126222855352762720L;
	private ImagePreview imagePreview;
	private LayerSelecter layerSelecter;
	private EffectsPanel effectsPanel;
	private Frame selectedFrame = null;
	
	private PictoralFin pictoralFin;

	
	public ImageEditor(PictoralFin pictoralFin) {
		super(new BorderLayout());
		this.pictoralFin = pictoralFin;
		
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		imagePreview = new ImagePreview(pictoralFin);
		layerSelecter = new LayerSelecter(pictoralFin);
		effectsPanel = new EffectsPanel(pictoralFin, this);
		
		JSplitPane leftAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane rightAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		leftAndCenterSplit.setLeftComponent(layerSelecter);
		leftAndCenterSplit.setRightComponent(imagePreview);
		leftAndCenterSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e->{layerSelecter.setButtonWidth(((int) e.getNewValue()) - 40);});
		leftAndCenterSplit.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		leftAndCenterSplit.setResizeWeight(0.10);		
		
		layerSelecter.setButtonWidth(((int) leftAndCenterSplit.getDividerLocation()) - 40);
		
		rightAndCenterSplit.setLeftComponent(leftAndCenterSplit);
		rightAndCenterSplit.setRightComponent(effectsPanel);
		rightAndCenterSplit.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		rightAndCenterSplit.setResizeWeight(0.80);		
		
		add(rightAndCenterSplit, BorderLayout.CENTER);				
		
		pictoralFin.getTimeLine().addOnFrameSelectionChangeListener((old, newFrame) -> {
				if(newFrame != null && newFrame.getFrame() != null) {
					imagePreview.setSelectedLayer(newFrame.getFrame().getLayer(0));
					layerSelecter.setSelectedFrame(newFrame.getFrame());
					selectedFrame = newFrame.getFrame();
				} else {
					imagePreview.setSelectedLayer(null);
					layerSelecter.setSelectedFrame(null);
					selectedFrame = null;
				}
				
				repaint();
			});
		
		layerSelecter.addOnSelectionLayerChangedListener((old, newFrame) -> {
				if(newFrame == null)
					return;
				
				imagePreview.setSelectedLayer(newFrame.getLayer());
				imagePreview.repaint();
			});
	}
	
	public Frame getSelectedFrame() {
		return selectedFrame;
	}
	
	EffectsPanel getEffectsPanel() {
		return effectsPanel;
	}
	
	public LayerSelecter getLayerSelecter() {
		return layerSelecter;
	}
	
	ImagePreview getImagePreview() {
		return imagePreview;
	}

	public boolean undo() {		
		if(selectedFrame == null)
			return false;
			
		int beforeImageX = imagePreview.imageX, beforeImageY = imagePreview.imageY;
		double beforeMagnification = imagePreview.magnification;
		
		if(selectedFrame.undo()) {
			layerSelecter.refresh();
			
			imagePreview.imageX = beforeImageX;
			imagePreview.imageY = beforeImageY;
			imagePreview.magnification = beforeMagnification;
			
			pictoralFin.repaint();
			return true;
		}
		
		return false;
	}
	
	public boolean redo() {
		if(selectedFrame == null)
			return false;
			
		if(selectedFrame.redo()) {
			layerSelecter.refresh();
			pictoralFin.repaint();
			return true;
		}
		
		return false;
	}
	
	public void logUndoableChange() {
		
		if(selectedFrame != null)
			selectedFrame.logUndoableChange();	
	}
}
