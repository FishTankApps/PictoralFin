package com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTools;

import java.awt.image.BufferedImage;

//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JTextField;

import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class ImageResizer extends ImageEditorTool {

	private static final long serialVersionUID = -2297726745512899997L;

//	private JLabel size;
//	private JTextField width, height;
//	private JButton resize;

	public ImageResizer(ImageEditor editor, Theme theme) {
		super("Image Resizer", editor, theme);
	}


	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {}
	protected void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame) {}
}
