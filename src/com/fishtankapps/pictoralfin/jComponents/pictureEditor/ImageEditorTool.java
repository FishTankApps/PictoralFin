package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.ImageToolDrawableShape;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.Constants;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public abstract class ImageEditorTool extends JPanel implements MouseListener {

	private static final long serialVersionUID = 7667372634947412881L;
	
	private ImageEditor editor;
	private Theme theme;
	
	public boolean isFocusedTool = false;
	protected boolean collapsible, collapsed = true;
	private String name;
	
	public ImageEditorTool(String name, ImageEditor editor, Theme theme, boolean collapsible) {
		this.editor = editor;
		this.theme = theme;
		this.name = name;
		
		this.collapsible = collapsible;
		
		addMouseListener(this);
		setBackground(theme.getPrimaryBaseColor());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(theme.getPrimaryBaseColor()), name));
		
		editor.getLayerSelecter().addOnSelectionLayerChangedListener((oldFrame, newFrame)->onLayerSelectionChanged(oldFrame, newFrame));
	}

	public String getToolName() {
		return name;
	}
	
	public void requestToolFocus() {
		editor.getImageEditorToolkitPanel().requestToolFocus(this);
	}
	
	protected Point getMousePointOnImage() {
		return editor.getImagePreview().getMousePointOnImage();
	}
	
	protected void callForRepaint() {
		editor.getImagePreview().repaint();
		Utilities.getPictoralFin(editor).repaint();
	}
	
	protected void callForRefresh() {
		editor.getLayerSelecter().refresh();
		Utilities.getPictoralFin(editor).repaint();
	}
	
	protected void logUndoableChange() {
		editor.logUndoableChange();
	}
	
	protected void drawShapeOnImagePreview(Shape shape, Paint paint) {
		editor.getImagePreview().drawShapeOnImage(new ImageToolDrawableShape(shape, paint, this));
	}
	
	protected void drawClearImageOnImagePreview(BufferedImage image) {
		editor.getImagePreview().drawClearImageOnImage(image);
	}
	
	protected void removeClearImageFromImagePreview() {
		editor.getImagePreview().removeClearImageFromImagePreview();
	}
	
	protected void clearDrawingsOnImagePreview() {
		editor.getImagePreview().clearDrawnShapesByTool(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(!isFocusedTool) {
			clearDrawingsOnImagePreview();
		}
		
		g.setColor((isFocusedTool) ? theme.getSecondaryHighlightColor() : theme.getPrimaryHighlightColor());		
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(collapsible) {
			g.setColor(Color.BLACK);
			
			if(collapsed) {
				g.fillOval(getWidth() - 20, 10, 15, 15);
				
				g.setColor((isFocusedTool) ? theme.getSecondaryHighlightColor() : theme.getPrimaryHighlightColor());	
				g.drawLine(getWidth() - 16, 16, getWidth() - 13, 20);
				g.drawLine(getWidth() - 13, 20, getWidth() - 10, 16);
				
			} else {
				g.fillOval(getWidth() - 20, 10, 15, 15);
				
				g.setColor((isFocusedTool) ? theme.getSecondaryHighlightColor() : theme.getPrimaryHighlightColor());	
				g.drawLine(getWidth() - 16, 20, getWidth() - 13, 16);
				g.drawLine(getWidth() - 13, 16, getWidth() - 10, 20);

			}
		}
	}
	
	protected abstract void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame);
	protected abstract void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame);
	protected abstract void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame);
	
	protected void updateCollapsedState(boolean collapsed) {
		if(collapsible) {
			for(Component c : getComponents())
				c.setVisible(!collapsed);
		}
	}
	
 	public void mouseClicked(MouseEvent e) {
 		requestToolFocus();
 		
 		if (e.getButton() == Constants.RIGHT_MOUSE_BUTTON) 
 			new ImageEditorToolPopUpMenu(this, e.getX(), e.getY());
	}

	public void mousePressed(MouseEvent e) {
		if(collapsible && e.getX() > getWidth() - 20 && 
				e.getX() < getWidth() - 5 && e.getY() < 20 && e.getY() > 5) {
			collapsed = !collapsed;
			updateCollapsedState(collapsed);
			repaint();
		}
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
