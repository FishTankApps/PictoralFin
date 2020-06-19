package jComponents.pictureEditor;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import mainFrame.PictoralFin;

public class ImageEditor extends JPanel {
	
	private static final long serialVersionUID = 5126222855352762720L;
	private ImagePreview imagePreview;
	private LayerSelecter layerSelector;
	private EffectsPanel effectsPanel;
	
	public ImageEditor(PictoralFin pictoralFin) {
		super(new BorderLayout());
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		imagePreview = new ImagePreview(pictoralFin);
		layerSelector = new LayerSelecter(pictoralFin);
		effectsPanel = new EffectsPanel();
		
		JSplitPane leftAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane rightAndCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		leftAndCenterSplit.setLeftComponent(layerSelector);
		leftAndCenterSplit.setRightComponent(imagePreview);
		leftAndCenterSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e->{layerSelector.setButtonWidth(((int) e.getNewValue()) - 40);});
		leftAndCenterSplit.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		
		
		rightAndCenterSplit.setLeftComponent(leftAndCenterSplit);
		rightAndCenterSplit.setRightComponent(effectsPanel);
		rightAndCenterSplit.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		SwingUtilities.invokeLater(()->{
			leftAndCenterSplit.setDividerLocation(200);
			rightAndCenterSplit.setDividerLocation(.75);
		});
		
		
		add(rightAndCenterSplit, BorderLayout.CENTER);
		
				
		
		pictoralFin.getTimeLine().addOnFrameSelectionChangeListener((old, newFrame) -> {
				imagePreview.setSelectedFrame(newFrame.getFrame());
				layerSelector.setSelectedFrame(newFrame.getFrame());
				repaint();
			});
	}
	
}
