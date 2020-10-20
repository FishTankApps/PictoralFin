package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;
import com.fishtankapps.pictoralfin.utilities.Constants;
import com.fishtankapps.pictoralfin.utilities.Utilities;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

class ImagePreviewPopUpMenu {

	private JPopupMenu menu;
	private LayerButton button;

	private final ActionListener REMOVE_LAYER = e -> {
		ImageEditor imageEditor = (ImageEditor) button.getParent().getParent().getParent().getParent().getParent().getParent().getParent();
		imageEditor.getSelectedFrame().removeLayer(button.getLayerIndex());
		
		PictoralFin pictoralFin = Utilities.getPictoralFin(imageEditor);
		
		if(imageEditor.getSelectedFrame().getNumberOfLayers() == 0) {
			pictoralFin.getTimeLine().removeFrame(pictoralFin.getTimeLine().getCurrentFrameIndex());		
		}
				
		imageEditor.getLayerSelecter().triggerOnSelectedLayerChangedListeners(null, null);
		
		imageEditor.getLayerSelecter().refresh();				
		pictoralFin.getTimeLine().revalidate();			
		pictoralFin.getTimeLine().repaint();
	};
	private final ActionListener UNDO = e -> {
		((ImageEditor) button.getParent().getParent().getParent().getParent().getParent().getParent().getParent()).undo();
	};
	private final ActionListener REDO = e -> {
		((ImageEditor) button.getParent().getParent().getParent().getParent().getParent().getParent().getParent()).redo();
	};
	private final ActionListener EXPORT_CURRENT_LAYER = e -> {
		Platform.runLater(() -> {
			
			PictoralFin pictoralFin = Utilities.getPictoralFin(button);
			FileChooser fileChooser = new FileChooser();
			
			String name;
			if(pictoralFin.openProject != null) {
				name = pictoralFin.openProject.getName();
				
				if(name.endsWith(".pfp"))
					name = name.split("\\.")[0] + ".png";
			} else
				name = "";
			
			
			fileChooser.setInitialFileName(name);
			fileChooser.setInitialDirectory(new File(pictoralFin.getDataFile().getLastOpenedPictureLocation()).getParentFile());
			fileChooser.setTitle("Export Frame To...");			
		
			String[] importableImageFiles = Utilities.getCompatibleImageFiles();
			
			
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("Image Files", importableImageFiles),
					new ExtensionFilter("All Files", "*"));
			
			fileChooser.setInitialFileName(name);
			
			File selectedFile = fileChooser.showSaveDialog(null);
			
			if(selectedFile == null)
				return;
			
			try {
				ImageIO.write(button.getLayer(), ".png", selectedFile);
			} catch (Exception error) {
				System.out.println("Empty catch block: ImagePreviewPopUpMenu.EXPORT_CURRENT_LAYER (lamba):");
				error.printStackTrace();
			}
		});		
	};
	private final ActionListener COPY_LAYER = e -> {
		
		PictoralFin pictoralFin = Utilities.getPictoralFin(button);
		
		StatusLogger.logStatus("Copying Image...");
		
		pictoralFin.getTimeLine().addFrame(BufferedImageUtil.copyBufferedImage(button.getLayer()), pictoralFin.getTimeLine().getCurrentFrameIndex());
					
		pictoralFin.getTimeLine().revalidate();			
		pictoralFin.getTimeLine().repaint();
		
		StatusLogger.logStatus("Image Added!");
	};
	
	public ImagePreviewPopUpMenu(ImagePreview imagePreview, LayerButton button, int x, int y) {
		if(button == null)
			return;
		
		this.button = button;
		
		menu = new JPopupMenu();

		JMenuItem removeFrame = new JMenuItem("Remove");
		JMenuItem undoButton = new JMenuItem("Undo");
		JMenuItem redoButton = new JMenuItem("Redo");
		JMenuItem exportLayer = new JMenuItem("Export Current Layer");
		JMenuItem copyLayer = new JMenuItem("Copy Layer to New Frame");
		
		removeFrame.addActionListener(REMOVE_LAYER);
		undoButton.addActionListener(UNDO);
		redoButton.addActionListener(REDO);
		exportLayer.addActionListener(EXPORT_CURRENT_LAYER);
		copyLayer.addActionListener(COPY_LAYER);
		
		undoButton.setAccelerator(KeyStroke.getKeyStroke('Z', Constants.CTRL));
		redoButton.setAccelerator(KeyStroke.getKeyStroke('Y', Constants.CTRL));
		
		menu.add(removeFrame);
		
		menu.addSeparator();
		
		menu.add(undoButton);
		menu.add(redoButton);
		
		menu.addSeparator();
		
		menu.add(exportLayer);
		menu.add(copyLayer);
		
		menu.show(imagePreview, x, y);
	}
}