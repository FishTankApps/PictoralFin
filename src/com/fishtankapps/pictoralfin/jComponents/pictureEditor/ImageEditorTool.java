package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;

public abstract class ImageEditorTool extends JPanel implements MouseListener {

	private static final long serialVersionUID = 7667372634947412881L;
	
	private ImageEditor editor;
	private Theme theme;
	
	public boolean isFocusedTool = false;
	
	public ImageEditorTool(String name, ImageEditor editor, Theme theme) {
		this.editor = editor;
		this.theme = theme;
		
		addMouseListener(this);
		setBackground(theme.getPrimaryBaseColor());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(theme.getPrimaryBaseColor()), name));
		
		editor.getLayerSelecter().addOnSelectionLayerChangedListener((oldFrame, newFrame)->onLayerSelectionChanged(oldFrame, newFrame));
	}

	public void requestToolFocus() {
		editor.getEffectsPanel().requestToolFocus(this);
	}
	
	protected Point getMousePointOnImage() {
		return editor.getImagePreview().getMousePointOnImage();
	}
	
	protected void callForRepaint() {
		editor.getImagePreview().repaint();
	}
	
	protected void logUndoableChange() {
		editor.logUndoableChange();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor((isFocusedTool) ? theme.getSecondaryHighlightColor() : theme.getPrimaryHighlightColor());		
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	protected abstract void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame);
	protected abstract void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame);
	protected abstract void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame);
	
	
	public void mouseClicked(MouseEvent e) {
		requestToolFocus();
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
