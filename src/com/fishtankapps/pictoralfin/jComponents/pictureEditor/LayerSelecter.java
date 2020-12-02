package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.listeners.OnSelectedLayerChangedListener;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;

public class LayerSelecter extends JPanel {

	private static final long serialVersionUID = 2974910716605097500L;

	private JPanel buttonPanel;
	private Frame selectedFrame = null;
	private PictoralFin pictoralFin;
	private LayerButton addNewLayer;
	private int buttonWidth = 100;

	private ArrayList<OnSelectedLayerChangedListener> listeners;
	
	private LayerButton selectedLayerButton = null;

	public LayerSelecter(PictoralFin pictoralFin) {
		super(new GridLayout(1,1,0,0));
		
		this.pictoralFin = pictoralFin;
		listeners = new ArrayList<>();
		
		BufferedImage addLayer = GlobalImageKit.readImage("AddLayerIcon.png");
		BufferedImageUtil.applyColorThemeToImage(addLayer, pictoralFin.getConfiguration().getTheme());
		addNewLayer = new LayerButton(addLayer, pictoralFin.getConfiguration().getTheme(), LayerButton.ADD_LAYER_INDEX, 100);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		
		JScrollPane jScrollPane = new JScrollPane(buttonPanel);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(jScrollPane);
		setMinimumSize(new Dimension(150, 100));
		
		setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		
		refresh();
	}
	
	public void addOnSelectionLayerChangedListener(OnSelectedLayerChangedListener oscl) {
		listeners.add(oscl);
	}
	
	public void setButtonWidth(int width) {
		buttonWidth = width;
		for(Component c : buttonPanel.getComponents()) {
			if(c instanceof LayerButton)
				((LayerButton) c).setThumbnailSize(width);
		}
	}
	
	public void setSelectedFrame(Frame frame) {
		selectedFrame = frame;
		refresh();
	}
	
	public LayerButton getSelectedLayerButton() {
		return selectedLayerButton;
	}
	
	public int getSelectedLayerIndex() {
		if(selectedLayerButton == null)
			return -1;
		
		return selectedLayerButton.getLayerIndex();
	}
	
	void onLayerButtonClicked(LayerButton layerButton) {
		if(layerButton.getLayerIndex() == LayerButton.ADD_LAYER_INDEX) {
			addLayerToFrame();
			
			refresh();
		} else {
			if(selectedLayerButton != null) {
				selectedLayerButton.setSelected(false);
				selectedLayerButton.repaint();
			}
			
			for(OnSelectedLayerChangedListener listener : listeners)
				listener.selectedlayerChanged(selectedLayerButton, layerButton);
			
			selectedLayerButton = layerButton;
			selectedLayerButton.setSelected(true);
			selectedLayerButton.repaint();
		}
	}
	
	public void triggerOnSelectedLayerChangedListeners(LayerButton oldLayerButton, LayerButton newLayerButton) {
		for(OnSelectedLayerChangedListener listener : listeners)
			listener.selectedlayerChanged(oldLayerButton, newLayerButton);
	}
	
	public void addLayerToFrame() {
		Frame.addLayerToFrame(pictoralFin, selectedFrame);
		
		refresh();
		
		pictoralFin.getTimeLine().repaint();
	}
	
	public void refresh() {
		buttonPanel.removeAll();
		
		byte index = 0;
		
		if(selectedFrame != null) {
			for(BufferedImage i : selectedFrame.getLayers()) {
				buttonPanel.add(new LayerButton(i, pictoralFin.getConfiguration().getTheme(), index++, buttonWidth));
				buttonPanel.add(Box.createVerticalStrut(10));
			}
		
			buttonPanel.add(addNewLayer);
			
			if(buttonPanel.getComponent(0) instanceof LayerButton)
				(selectedLayerButton = ((LayerButton) buttonPanel.getComponent(0))).setSelected(true);
			
			for(OnSelectedLayerChangedListener listener : listeners)
				listener.selectedlayerChanged(null, selectedLayerButton);
		}
		
		revalidate();
		repaint();
	}
	
	public void moveSelectedLayer(boolean moveUp) {
		if(selectedFrame == null || selectedFrame.getNumberOfLayers() < 2)
			return;
		
		if(moveUp) {
			int currentIndex = getSelectedLayerIndex();
			
			if(currentIndex == 0)
				return;
			
			BufferedImage currentLayer = selectedFrame.getLayer(currentIndex);
			
			selectedFrame.removeLayer(currentIndex, false);
			selectedFrame.addLayerAtIndex(currentLayer, currentIndex - 1);
			
		} else {
			int currentIndex = getSelectedLayerIndex();
			
			if(currentIndex == selectedFrame.getNumberOfLayers() - 1)
				return;
			
			BufferedImage currentLayer = selectedFrame.getLayer(currentIndex);
			
			selectedFrame.removeLayer(currentIndex, false);
			selectedFrame.addLayerAtIndex(currentLayer, currentIndex + 1);
		}
		
		refresh();
		pictoralFin.repaint();
	}
}
