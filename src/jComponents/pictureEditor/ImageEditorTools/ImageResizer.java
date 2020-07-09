package jComponents.pictureEditor.ImageEditorTools;

import java.awt.image.BufferedImage;

import jComponents.pictureEditor.ImageEditor;
import jComponents.pictureEditor.ImageEditorTool;
import objectBinders.Theme;

public class ImageResizer extends ImageEditorTool {

	private static final long serialVersionUID = -2297726745512899997L;


	public ImageResizer(ImageEditor editor, Theme theme) {
		super("Image Resizer", editor, theme);
	}


	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer) {}
	protected void onMousePressed(int clickX, int clickY, BufferedImage layer) {}
}
