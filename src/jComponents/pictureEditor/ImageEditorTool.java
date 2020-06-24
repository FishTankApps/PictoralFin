package jComponents.pictureEditor;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import objectBinders.Theme;

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
	}

	public void requestToolFocus() {
		editor.getEffectsPanel().requestToolFocus(this);
	}
	
	public Point getMousePointOnImage() {
		return editor.getImagePreview().getMousePointOnImage();
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor((isFocusedTool) ? theme.getSecondaryHighlightColor() : theme.getPrimaryHighlightColor());		
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	abstract void onMouseReleased(int clickX, int clickY, BufferedImage layer);
	abstract void onMousePressed(int clickX, int clickY, BufferedImage layer);

	
	public void mouseClicked(MouseEvent e) {
		requestToolFocus();
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
