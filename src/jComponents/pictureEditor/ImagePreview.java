package jComponents.pictureEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
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

import listeners.LayerMouseListener;
import mainFrame.PictoralFin;
import objectBinders.Theme;
import utilities.Utilities;

public class ImagePreview extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

	private static final long serialVersionUID = -9021549379360151600L;
	private static final int PREF_BOARDER = 20;
	
	private int imageX = 0, imageY = 0, clickStartX, clickStartY;
	private double magnification = 1;
	
	private Thread dragThread;
	
	private JLabel magnificationLabel;
	private JSlider magnificationSlider;
	
	private boolean dragging = false;
	
	private ArrayList<LayerMouseListener> layerMouseListeners;
	private BufferedImage pictoralFinIcon, layer;
	private Theme theme;
	
	public ImagePreview(PictoralFin pictoralFin) {
		super(new BorderLayout());
		theme = pictoralFin.getSettings().getTheme();
		layerMouseListeners = new ArrayList<>();
		
		pictoralFinIcon = pictoralFin.getGlobalImageKit().pictoralFinIcon;
		
		magnificationLabel = new JLabel("Magnification (100%): ");
		magnificationSlider = new JSlider(1, 1000, 100);
		magnificationSlider.addChangeListener(e -> {
				magnification = magnificationSlider.getValue() / 100.0;
				magnificationLabel.setText("Magnification (" + magnificationSlider.getValue() + "%): ");
				repaint();
			});
		
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {

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
		
		bottomPanel.add(magnificationLabel);
		bottomPanel.add(magnificationSlider);		
		
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		add(bottomPanel, BorderLayout.SOUTH);
		setMinimumSize(new Dimension(800, 100));	
		setBackground(theme.getPrimaryBaseColor());
	}
	
	public void setSelectedLayer(BufferedImage layer) {
		this.layer = layer;
		centerAndFitImage();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
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
		} else {
			
			int adjustedImageWidth = (int) (layer.getWidth() * magnification); 
			int adjustedImageHeight = (int) (layer.getHeight() * magnification);
			
			g.drawImage(layer, ((getWidth() - adjustedImageWidth) / 2) + imageX, ((getHeight() - adjustedImageHeight) / 2) + imageY,
					adjustedImageWidth, adjustedImageHeight, null);
		}
	}

	public void centerAndFitImage() {
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
		if(e.getButton() == 3) {
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
		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		if(dragThread != null)
			dragThread.interrupt();
		
		for(LayerMouseListener listener : layerMouseListeners)
			listener.onMouseReleased(e.getX() + imageX, e.getY() + imageY, layer);
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		magnificationSlider.setValue(magnificationSlider.getValue() - (e.getWheelRotation() * 5));
	}

	public void mouseMoved(MouseEvent e) {
		if(layer == null)
			return;
		
		int x = (int) ((e.getX() - (Math.ceil((getWidth()  / 2) - (layer.getWidth()  * magnification)/2 + imageX))) / magnification);
		int y = (int) ((e.getY() - (Math.ceil((getHeight() / 2) - (layer.getHeight() * magnification)/2 + imageY))) / magnification);
		
		if(x >= 0 && x < layer.getWidth() && y >= 0 && y < layer.getHeight()) {
			if(!dragging)
				this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		} else if(!dragging)
			this.setCursor(Cursor.getDefaultCursor());
			
	}	
	
	public void onRightClick(int x, int y) {
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
				Utilities.debug("ImagePreview.DragRunnable.run() - Picture Dragging Thread Closed");
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
