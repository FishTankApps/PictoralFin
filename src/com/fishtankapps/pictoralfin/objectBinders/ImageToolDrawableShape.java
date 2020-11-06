package com.fishtankapps.pictoralfin.objectBinders;

import java.awt.Paint;
import java.awt.Shape;

import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;

public class ImageToolDrawableShape {

	private Shape shape;
	private Paint paint;
	private ImageEditorTool tool;	
	
	public ImageToolDrawableShape(Shape shape, Paint paint, ImageEditorTool tool) {
		this.shape = shape;
		this.paint = paint;
		this.tool = tool;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public Paint getPaint() {
		return paint;
	}
	
	public ImageEditorTool getTool() {
		return tool;
	}
}
