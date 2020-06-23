package jComponents.pictureEditor;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import utilities.Utilities;

class ImagePreviewPopUpMenu {

	private JPopupMenu menu;
	private LayerButton button;

	private final ActionListener REMOVE_LAYER = e -> {
		ImageEditor imageEditor = (ImageEditor) button.getParent().getParent().getParent().getParent().getParent().getParent().getParent();
		imageEditor.getSelectedFrame().removeLayer(button.getLayerIndex());
		imageEditor.getLayerSelecter().refresh();
		
		Utilities.getPictoralFin(imageEditor).repaint();
	};

	public ImagePreviewPopUpMenu(ImagePreview imagePreview, LayerButton button, int x, int y) {
		this.button = button;
		menu = new JPopupMenu();

		JMenuItem removeFrame = new JMenuItem("Remove");
		removeFrame.addActionListener(REMOVE_LAYER);
		
		menu.add(removeFrame);
		menu.show(imagePreview, x, y);
	}
}