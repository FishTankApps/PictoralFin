package jComponents.pictureEditor;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JPanel;

import jComponents.pictureEditor.ImageEditorTools.DrawingTool;
import jComponents.pictureEditor.ImageEditorTools.ImageResizer;
import listeners.LayerMouseListener;
import mainFrame.PictoralFin;
import objectBinders.Theme;
import utilities.ChainGBC;

public class EffectsPanel extends JPanel{

	private static final long serialVersionUID = -2646271125412362864L;

	private ImageEditorTool toolInFocus = null;
	
	public EffectsPanel(PictoralFin pictoralFin, ImageEditor imageEditor) {
		super(new GridBagLayout());
		setMinimumSize(new Dimension(250, 100));
		
		imageEditor.getImagePreview().addLayerMouseListener(new LayerMouseListener() {

				public void onMousePressed(int xOnImage, int yOnImage, BufferedImage layer) {
					if(toolInFocus != null)
						toolInFocus.onMousePressed(xOnImage, yOnImage, layer);
					
					pictoralFin.repaint();
				}
	
				public void onMouseReleased(int xOnImage, int yOnImage, BufferedImage layer) {
					if(toolInFocus != null)
						toolInFocus.onMouseReleased(xOnImage, yOnImage, layer);	
					
					pictoralFin.repaint();
				}
		
			});
		
		addInstalledEditors(imageEditor, pictoralFin.getSettings().getTheme());
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
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
		add(new DrawingTool(imageEditor, theme),  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
		add(new ImageResizer(imageEditor, theme), new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
		
		add(Box.createHorizontalGlue(), new ChainGBC(0, heightIndex++).setFill(true, true));
	}
}