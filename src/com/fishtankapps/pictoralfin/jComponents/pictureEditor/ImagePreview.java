package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.listeners.LayerMouseListener;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.ImageToolDrawableShape;
import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class ImagePreview extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

	private static final long serialVersionUID = -9021549379360151600L;
	private static final int PREF_BOARDER = 20;
	private static final byte SCROLL_AMOUNT = 20;
	
	int imageX = 0, imageY = 0, clickStartX, clickStartY, mouseX, mouseY;
	double magnification = 1;
	
	private Thread dragThread;
	
	private JLabel magnificationLabel, cordsLabel;
	private JSlider magnificationSlider;
	
	private boolean dragging = false;
	
	private ArrayList<ImageToolDrawableShape> shapesDrawnByTools;
	
	private ArrayList<LayerMouseListener> layerMouseListeners;
	private BufferedImage pictoralFinIcon, layer, clearLayer;
	private Theme theme;
	private JPanel bottomPanel, topPanel;
	
	public ImagePreview(PictoralFin pictoralFin) {
		super(new BorderLayout());
		theme = pictoralFin.getConfiguration().getTheme();
		layerMouseListeners = new ArrayList<>();
		shapesDrawnByTools = new ArrayList<>();
		
		pictoralFinIcon = GlobalImageKit.pictoralFinIcon;
				
		
		magnificationLabel = new JLabel("Magnification (100%): ");
		magnificationSlider = new JSlider(1, 1000, 100);
		magnificationSlider.addChangeListener(e -> {
				magnification = magnificationSlider.getValue() / 100.0;
				magnificationLabel.setText("Magnification (" + magnificationSlider.getValue() + "%): ");
				repaint();
			});
		
		bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {

			private static final long serialVersionUID = 1L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(theme.getPrimaryHighlightColor());
				
				int width = magnificationLabel.getWidth() + magnificationSlider.getWidth();
				
				if(theme.isSharp()) {
					Polygon p = new Polygon();
					p.addPoint(getWidth() - width - 20, 0);
					p.addPoint(getWidth(), 0);
					p.addPoint(getWidth(), getHeight());
					p.addPoint(getWidth() - width - 60, getHeight());
					
					g.fillPolygon(p);
				} else {					
					g.fillRoundRect(getWidth() - width - 40, 0, width + 100, getHeight() + 100, 60, getHeight() * 2);
				}
			}
		};
		bottomPanel.setBackground(new Color(0,0,0,0));
		bottomPanel.setCursor(Cursor.getDefaultCursor());
		bottomPanel.setVisible(false);
		
		bottomPanel.add(magnificationLabel);
		bottomPanel.add(magnificationSlider);	
		
		
		cordsLabel = new JLabel("Mouse Cordinates: (--,--)");
		
		topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)){

			private static final long serialVersionUID = 1L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(theme.getPrimaryHighlightColor());
				
				if(theme.isSharp()) {
					Polygon p = new Polygon();
					
					p.addPoint(0, getHeight());
					p.addPoint(0, 0);					
					p.addPoint(cordsLabel.getWidth() + 60, 0);
					p.addPoint(cordsLabel.getWidth() + 20, getHeight());
					
					g.fillPolygon(p);
				} else {					
					g.fillRoundRect(-40, -getHeight(), cordsLabel.getWidth() + 100, getHeight() * 2, 60, getHeight() * 2);
				}
			}
		};
		topPanel.setBackground(new Color(0,0,0,0));
		topPanel.setCursor(Cursor.getDefaultCursor());
		topPanel.setVisible(false);
		
		topPanel.add(cordsLabel);
		
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		add(bottomPanel, BorderLayout.SOUTH);
		add(topPanel, BorderLayout.NORTH);
		setMinimumSize(new Dimension(800, 100));	
		setBackground(theme.getPrimaryBaseColor());
	}
	
	public void setSelectedLayer(BufferedImage layer) {
		this.layer = layer;
		
		bottomPanel.setVisible(layer != null);
		topPanel.setVisible(layer != null);
		repaint();
		
		centerAndFitImage();
	}
	
	public Point getMousePointOnImage() {
		Point p = getMousePosition(); 
		if(p == null || layer == null)
			return null;	
		
		int mouseX = (int) ((p.getX() - (Math.ceil((getWidth()  / 2) - (layer.getWidth()  * magnification)/2 + imageX))) / magnification);
		int mouseY = (int) ((p.getY() - (Math.ceil((getHeight() / 2) - (layer.getHeight() * magnification)/2 + imageY))) / magnification);		
		
		return new Point(mouseX, mouseY);
	}
	
	public void drawShapeOnImage(ImageToolDrawableShape shape) {
		shapesDrawnByTools.add(shape);
		repaint();
	}
	
	public void clearDrawnShapesByTool(ImageEditorTool tool) {
		for(int count = 0; count < shapesDrawnByTools.size(); count++)
			if(shapesDrawnByTools.get(count).getTool() == tool) {
				shapesDrawnByTools.remove(count);
				count--;
			}
		
		repaint();
	}
	
	public void drawClearImageOnImage(BufferedImage image) {
		this.clearLayer = image;
		
		repaint();
	}
	
	public void removeClearImageFromImagePreview() {
		this.clearLayer = null;
		
		repaint();
	}
	
	public void clearAllDrawnShapes() {
		shapesDrawnByTools.clear();
		
		repaint();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) graphics;
		
		g.setPaint(new GradientPaint(100,100,theme.getPrimaryBaseColor(), getWidth()-100, getHeight()-100, theme.getPrimaryHighlightColor()));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(layer == null) {
			
			int displayWidth = getWidth() - PREF_BOARDER * 2;
			int displayHeight = getHeight() - PREF_BOARDER * 2 - 10;
			
			double ratio = (displayHeight / (double) pictoralFinIcon.getHeight()) < (displayWidth / (double) pictoralFinIcon.getWidth()) ? 
					(displayHeight / (double) pictoralFinIcon.getHeight()) : 
					(displayWidth / (double) pictoralFinIcon.getWidth());
			
			int adjustedImageWidth = (int) (pictoralFinIcon.getWidth() * ratio); 
			int adjustedImageHeight = (int) (pictoralFinIcon.getHeight() * ratio);
			
			g.drawImage(pictoralFinIcon, (displayWidth - adjustedImageWidth) / 2 + PREF_BOARDER, (displayHeight - adjustedImageHeight) / 2 + PREF_BOARDER ,
					adjustedImageWidth, adjustedImageHeight, null);
			
			clearAllDrawnShapes();
		} else {
			
			
			int adjustedImageWidth = (int) (layer.getWidth() * magnification); 
			int adjustedImageHeight = (int) (layer.getHeight() * magnification);
			
			g.drawImage(layer, ((getWidth() - adjustedImageWidth) / 2) + imageX, ((getHeight() - adjustedImageHeight) / 2) + imageY,
					adjustedImageWidth, adjustedImageHeight, null);
			
			g.setColor(Color.WHITE);
			
			g.drawRect(((getWidth() - adjustedImageWidth) / 2) + imageX - 2, ((getHeight() - adjustedImageHeight) / 2) + imageY - 2,
					adjustedImageWidth + 4, adjustedImageHeight + 4);
			
			g.setColor(Color.BLACK);
			
			g.drawRect(((getWidth() - adjustedImageWidth) / 2) + imageX - 3, ((getHeight() - adjustedImageHeight) / 2) + imageY - 3,
					adjustedImageWidth + 6, adjustedImageHeight + 6);
			
			if(clearLayer != null)
				g.drawImage(clearLayer, ((getWidth() - adjustedImageWidth) / 2) + imageX, ((getHeight() - adjustedImageHeight) / 2) + imageY,
						adjustedImageWidth, adjustedImageHeight, null);
			
			for(ImageToolDrawableShape drawableShape : shapesDrawnByTools) {
				g.setPaint(drawableShape.getPaint());
				
				Shape shape = drawableShape.getShape();
				
				if(shape instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) shape;
					
					g.drawRect(
							(int) (rectangle.getX() * magnification + ((getWidth() - adjustedImageWidth) / 2)   + imageX), 
							(int) (rectangle.getY() * magnification + ((getHeight() - adjustedImageHeight) / 2) + imageY), 
							(int) (rectangle.width  * magnification), 
							(int) (rectangle.height * magnification));
				} else 
					g.draw(shape);
				
				
			}			
		}
	}

	public void centerAndFitImage() {
		if(layer == null)
			return;
		
		imageX = imageY = 0;		
		double heightRatio, widthRatio;
		
		heightRatio = (getHeight() - PREF_BOARDER * 2) / (double) layer.getHeight();
		widthRatio  = (getWidth()  - PREF_BOARDER * 2) / (double) layer.getWidth();
		
		magnification = (heightRatio > widthRatio) ? widthRatio : heightRatio;
		
		magnificationSlider.setValue((int) (magnification * 100));
		
		repaint();
	}
	
	public void addLayerMouseListener(LayerMouseListener listener) {
		layerMouseListeners.add(listener);
	}
		
	public void mousePressed(MouseEvent e) {
		System.out.println("Mouse Button: " + e.getButton());
		
		if(e.getButton() == 3 && layer != null) {
			clickStartX = e.getXOnScreen();
			clickStartY = e.getYOnScreen();					
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			
			dragThread = new Thread(new DragRunnable(e));
			dragThread.start();
		} else if (e.getButton() == 2) {
			centerAndFitImage();
			
		} else if(layer != null) {			
			
			int x = (int) ((e.getX() - (Math.ceil((getWidth()  / 2) - (layer.getWidth()  * magnification)/2 + imageX))) / magnification);
			int y = (int) ((e.getY() - (Math.ceil((getHeight() / 2) - (layer.getHeight() * magnification)/2 + imageY))) / magnification);
			
			if(x >= 0 && x < layer.getWidth() && y >= 0 && y < layer.getHeight())			
				for(LayerMouseListener listener : layerMouseListeners)
					listener.onMousePressed(x, y, layer);
		}		
	}

	public void mouseReleased(MouseEvent e) {
	
		if(dragThread != null)
			dragThread.interrupt();
		
		for(LayerMouseListener listener : layerMouseListeners)
			listener.onMouseReleased(e.getX() + imageX, e.getY() + imageY, layer);
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {	
		if(layer == null)
			return;		
		
		double xBefore = ((e.getX() - (getWidth() /2) + (layer.getWidth()  * magnification)/2 - imageX)) / magnification;
		double yBefore = ((e.getY() - (getHeight()/2) + (layer.getHeight() * magnification)/2 - imageY)) / magnification;		
		
		double afterMagnification;
		if(magnificationSlider.getValue() - (e.getWheelRotation() * SCROLL_AMOUNT) < magnificationSlider.getMaximum())
			afterMagnification = (magnificationSlider.getValue() - (e.getWheelRotation() * SCROLL_AMOUNT)) / 100.0;	
		else 
			afterMagnification = (magnificationSlider.getMaximum()) / 100.0;	
		
		double xAfter = ((e.getX() - (getWidth()  / 2) + (layer.getWidth()  * afterMagnification)/2 - imageX)) / afterMagnification;
		double yAfter = ((e.getY() - (getHeight() / 2) + (layer.getHeight() * afterMagnification)/2 - imageY)) / afterMagnification;
		
		imageX -= (int) ((xBefore - xAfter) * afterMagnification);
		imageY -= (int) ((yBefore - yAfter) * afterMagnification);
		
		magnificationSlider.setValue(magnificationSlider.getValue() - (e.getWheelRotation() * SCROLL_AMOUNT));	
	}

	public void mouseMoved(MouseEvent e) {
		if(layer == null) {
			this.setCursor(Cursor.getDefaultCursor());
			mouseX = mouseY = -1;			
			return;
		}
		
		mouseX = (int) ((e.getX() - (Math.ceil((getWidth()  / 2) - (layer.getWidth()  * magnification)/2 + imageX))) / magnification);
		mouseY = (int) ((e.getY() - (Math.ceil((getHeight() / 2) - (layer.getHeight() * magnification)/2 + imageY))) / magnification);		
		
		if(mouseX >= 0 && mouseX < layer.getWidth() && mouseY >= 0 && mouseY < layer.getHeight()) {
			if(!dragging)
				this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		} else if(!dragging)
			this.setCursor(Cursor.getDefaultCursor());
			
		Point mousePointOnImage = getMousePointOnImage();
		
		if(mousePointOnImage != null && mousePointOnImage.x >= 0 && mousePointOnImage.x < layer.getWidth()
			&& mousePointOnImage.y >= 0 && mousePointOnImage.y < layer.getHeight())
				cordsLabel.setText("Mouse Cordinates: (" + mousePointOnImage.x + "," + mousePointOnImage.y + ")");
		else 
			cordsLabel.setText("Mouse Cordinates: (--,--)");
	}	
	
	public void onRightClick(int x, int y) {
		if(layer != null)		
			new ImagePreviewPopUpMenu(this, ((ImageEditor) getParent().getParent().getParent()).getLayerSelecter().getSelectedLayerButton(), x, y);
	}
	
	private class DragRunnable implements Runnable {
		
		private MouseEvent e;
		
		public DragRunnable(MouseEvent e) {
			this.e=e;
		}
		
		public void run() {
			int imageXStart = imageX;
			int imageYStart = imageY;
			
			dragging = true;
			
			try {				
				while(true) {
					Point mouseLocation = MouseInfo.getPointerInfo().getLocation();	
					
					imageX = imageXStart + (int) (mouseLocation.getX() - clickStartX);
					imageY = imageYStart + (int) (mouseLocation.getY() - clickStartY);
					
					Thread.sleep(10);
					repaint();
				}
			} catch (InterruptedException e) {

			}
			
			final int MIN_DRAG_AMOUNT = 5;
			
			dragging = false;
			
			if(Math.abs(imageXStart - imageX) < MIN_DRAG_AMOUNT && Math.abs(imageYStart - imageY) < MIN_DRAG_AMOUNT) {
				onRightClick(e.getX(), e.getY());
			}
		}
		
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
}
