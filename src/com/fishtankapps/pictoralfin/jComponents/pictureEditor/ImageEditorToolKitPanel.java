package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JPanel;

import com.fishtankapps.pictoralfin.jComponents.pictureEditor.imageEditorTools.DrawingTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.imageEditorTools.GreenScreenTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.imageEditorTools.ImageResizer;
import com.fishtankapps.pictoralfin.listeners.LayerMouseListener;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;

public class ImageEditorToolKitPanel extends JPanel {

	private static final long serialVersionUID = -2646271125412362864L;

	private ImageEditorTool toolInFocus = null;
	
	public ImageEditorToolKitPanel(PictoralFin pictoralFin, ImageEditor imageEditor) {
		super(new GridBagLayout());
		setMinimumSize(new Dimension(250, 100));
		
		imageEditor.getImagePreview().addLayerMouseListener(new LayerMouseListener() {

				public void onMousePressed(int xOnImage, int yOnImage, BufferedImage layer) {
					if(toolInFocus != null)
						toolInFocus.onMousePressed(xOnImage, yOnImage, layer, imageEditor.getSelectedFrame());
					
					pictoralFin.repaint();
				}
	
				public void onMouseReleased(int xOnImage, int yOnImage, BufferedImage layer) {
					if(toolInFocus != null)
						toolInFocus.onMouseReleased(xOnImage, yOnImage, layer, imageEditor.getSelectedFrame());	
					
					pictoralFin.repaint();
				}
		
			});
		
		addInstalledEditors(imageEditor, pictoralFin.getConfiguration().getTheme());
		setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
	}
	
	public void requestToolFocus(ImageEditorTool toolInFocus) {
		if(this.toolInFocus != null)
			this.toolInFocus.isFocusedTool = false;
		
		this.toolInFocus = toolInFocus;
		this.toolInFocus.isFocusedTool = true;
		
		repaint();
	}
	
	public void dropToolFocus() {
		this.toolInFocus = null;		
		repaint();
	}
		
	public void addInstalledEditors(ImageEditor imageEditor, Theme theme) {
		int heightIndex = 0;
//		String name = "";
//		
//		try {
//			File imageEditorFolder = new File("src\\com\\fishtankapps\\pictoralfin\\jComponents\\pictureEditor\\imageEditorTools");
//			
//			for(File imageEditorFile : imageEditorFolder.listFiles()) {
//				name = imageEditorFile.getName().substring(0, imageEditorFile.getName().indexOf('.'));
//				
//				Class<?> clazz = Class.forName("com.fishtankapps.pictoralfin.jComponents.pictureEditor.imageEditorTools." + name);
//				Constructor<?> constructor = clazz.getConstructor(ImageEditor.class, Theme.class);
//				ImageEditorTool instance = (ImageEditorTool) constructor.newInstance(imageEditor, theme);
//				
//				add(instance,  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
//			}		
//			
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, "There was an error loading an Image Editor:\n" + name + 
//					"\nError Message: \n" + e.getMessage(), "Error Loading Editor", JOptionPane.ERROR_MESSAGE);
//		}
		
		add(new DrawingTool(imageEditor, theme),  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));		
		add(new GreenScreenTool(imageEditor, theme),  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));		
		add(new ImageResizer(imageEditor, theme),  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));		
		add(Box.createHorizontalGlue(), new ChainGBC(0, heightIndex++).setFill(true, true));
	}
}