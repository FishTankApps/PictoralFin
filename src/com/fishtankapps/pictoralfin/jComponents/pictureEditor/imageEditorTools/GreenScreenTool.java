package com.fishtankapps.pictoralfin.jComponents.pictureEditor.imageEditorTools;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import com.fishtankapps.pictoralfin.jComponents.JColorPreview;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.LayerButton;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.GreenScreenUtils;

public class GreenScreenTool extends ImageEditorTool {
	
	private static final long serialVersionUID = -5473460954928781149L;
	
	private static final String RGB_VALUE_FILTER = "RGB Value Filter Method";
	//private static final String RGB_RATIO_FILTER = "RGB Ratio Filter Method";
	private static final String HSB_VALUE_FILTER = "HSB Value Filter Method";
	
	private Color dragHighlightColor;
	
	private Rectangle selectedRect = null;
	private Color greenScreenColor = null;
	private Thread drawThread = null;	
	
	private JColorPreview colorPreview;
	private JLabel rgbValueLabel, hsbValueLabel;
	private JPanel filterMethodsPanel;
	
	private BufferedImage currentLayer = null;
	
	public GreenScreenTool(ImageEditor editor, Theme theme) {
		super("Green Screen Tool", editor, theme, true);
		setLayout(new GridBagLayout());
				
		dragHighlightColor = theme.getSecondaryHighlightColor();		
		
		colorPreview = new JColorPreview(theme);
		JLabel previewLabel = new JLabel("Green Screen Color: ", JLabel.RIGHT);	
		previewLabel.setFont(new Font(theme.getTitleFont(), Font.PLAIN, 20));
		
		rgbValueLabel = new JLabel("No Color Selected", JLabel.CENTER);
		hsbValueLabel = new JLabel("No Color Selected", JLabel.CENTER);
		
		filterMethodsPanel = new JPanel(new GridBagLayout()) {
			private static final long serialVersionUID = 1L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				if(theme.isSharp()) {
					g.setColor(theme.getPrimaryBaseColor());
					
					Polygon p = new Polygon();
					
					p.addPoint(0, 30);
					p.addPoint(30, 0);
					p.addPoint(getWidth(), 0);
					p.addPoint(getWidth(), getHeight() - 30);
					p.addPoint(getWidth() - 30, getHeight());
					p.addPoint(0, getHeight());
					
					g.fillPolygon(p);
				} else {
					g.setColor(theme.getPrimaryBaseColor());
					g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 15, 30);
				}	
			}
		};
		filterMethodsPanel.setBackground(new Color(0,0,0,0));
		
		JPanel filterMethodCards = new JPanel(new CardLayout());
		
		initCardPanels(filterMethodCards, theme);
		
		String[] comboBoxItems = { RGB_VALUE_FILTER, HSB_VALUE_FILTER };
		JComboBox<String> cb = new JComboBox<>(comboBoxItems);
		cb.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 12));
		cb.setEditable(false);
		cb.addItemListener(e -> ((CardLayout) filterMethodCards.getLayout()).show(filterMethodCards, (String) e.getItem()));
		
		filterMethodsPanel.add(cb,                new ChainGBC(0,0).setFill(true, false).setPadding(20,20,20,5));
		filterMethodsPanel.add(filterMethodCards, new ChainGBC(0,1).setFill(true, false).setPadding(20,20,5, 20));
		
		add(previewLabel, new ChainGBC(0, 0).setFill(false, false).setPadding(5));
		add(rgbValueLabel, new ChainGBC(0, 1).setFill(false, false).setPadding(0));
		add(hsbValueLabel, new ChainGBC(0, 2).setFill(false, false).setPadding(0));
		add(colorPreview, new ChainGBC(1, 0).setFill(true, false).setPadding(10).setWidthAndHeight(1, 3));
		add(filterMethodsPanel, new ChainGBC(0, 3).setFill(true, false).setPadding(10).setWidthAndHeight(2, 1));
	
		updateCollapsedState(true);
	}
	
	private void initCardPanels(JPanel filterMethodCards, Theme theme) {
		JPanel rgbValueMethodCard = new JPanel(new GridBagLayout()) {
			private static final long serialVersionUID = 5489808135277106564L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);				
				g.setColor(theme.getSecondaryBaseColor());
				
				if(theme.isSharp())
					g.fillRect(0, 0, getWidth(), getHeight());
				else
					g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 10);
			}
			
		};
		JPanel rgbRatioMethodCard = new JPanel(new GridBagLayout()) {
			private static final long serialVersionUID = 5489808135277106564L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);				
				g.setColor(theme.getSecondaryBaseColor());
				
				if(theme.isSharp())
					g.fillRect(0, 0, getWidth(), getHeight());
				else
					g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 10);
			}
			
		};
		JPanel hsbValueMethodCard = new JPanel(new GridBagLayout()) {
			private static final long serialVersionUID = 5489808135277106564L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);				
				g.setColor(theme.getSecondaryBaseColor());

				if(theme.isSharp())
					g.fillRect(0, 0, getWidth(), getHeight());
				else
					g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 10);
			}
			
		};
		
		rgbValueMethodCard.setBackground(new Color(0,0,0,0));
		rgbRatioMethodCard.setBackground(new Color(0,0,0,0));
		hsbValueMethodCard.setBackground(new Color(0,0,0,0));
		
		setUpRGBValueMethodCard(rgbValueMethodCard);
		setUpHSBValueMethodCard(hsbValueMethodCard);

		rgbRatioMethodCard.add(new JButton("RGB RATIO"));
		
		filterMethodCards.add(rgbValueMethodCard, RGB_VALUE_FILTER);
		//filterMethodCards.add(rgbRatioMethodCard, RGB_RATIO_FILTER);
		filterMethodCards.add(hsbValueMethodCard, HSB_VALUE_FILTER);
	}
	
	private void setUpRGBValueMethodCard(JPanel rgbValueMethodCard) {
		JLabel redUpperLabel = new JLabel("Red Upper (10):", JLabel.RIGHT);
		JLabel redLowerLabel = new JLabel("Red Lower (10):", JLabel.RIGHT);
		JLabel greenUpperLabel = new JLabel("Green Upper (10):", JLabel.RIGHT);
		JLabel greenLowerLabel = new JLabel("Green Lower (10):", JLabel.RIGHT);
		JLabel blueUpperLabel = new JLabel("Blue Upper (10):", JLabel.RIGHT);
		JLabel blueLowerLabel = new JLabel("Blue Lower (10):", JLabel.RIGHT);		
		
		JSlider redUpperTolerance =  new JSlider(0,256,10);
		
		JSlider redLowerTolerance =  new JSlider(0,256,10);
		JSlider greenUpperTolerance = new JSlider(0,256,10);
		JSlider greenLowerTolerance = new JSlider(0,256,10);
		JSlider blueUpperTolerance =  new JSlider(0,256,10);
		JSlider blueLowerTolerance =  new JSlider(0,256,10);
		
		
		ChangeListener sliderChangeListener = e -> {
			if(currentLayer == null)
				return;
			
			removeClearImageFromImagePreview();
			
			BufferedImage image = GreenScreenUtils.applyRGBValueFilterToClearImage(currentLayer, greenScreenColor, 
					redUpperTolerance.getValue(), redLowerTolerance.getValue(), 
					greenUpperTolerance.getValue(), greenLowerTolerance.getValue(), 
					blueUpperTolerance.getValue(), blueLowerTolerance.getValue());
			
			drawClearImageOnImagePreview(image);
		};
		
		
		redUpperTolerance.addChangeListener(e->redUpperLabel.setText("Red Upper (" + redUpperTolerance.getValue() + "):"));
		redLowerTolerance.addChangeListener(e->redLowerLabel.setText("Red Lower (" + redLowerTolerance.getValue() + "):"));
		greenUpperTolerance.addChangeListener(e->greenUpperLabel.setText("Green Upper (" + greenUpperTolerance.getValue() + "):"));
		greenLowerTolerance.addChangeListener(e->greenLowerLabel.setText("Green Lower (" + greenLowerTolerance.getValue() + "):"));
		blueUpperTolerance.addChangeListener(e->blueUpperLabel.setText("Blue Upper (" + blueUpperTolerance.getValue() + "):"));
		blueLowerTolerance.addChangeListener(e->blueLowerLabel.setText("Blue Lower (" + blueLowerTolerance.getValue() + "):"));
		
		redUpperTolerance.addChangeListener(sliderChangeListener);
		redLowerTolerance.addChangeListener(sliderChangeListener);
		greenUpperTolerance.addChangeListener(sliderChangeListener);
		greenLowerTolerance.addChangeListener(sliderChangeListener);
		blueUpperTolerance.addChangeListener(sliderChangeListener);
		blueLowerTolerance.addChangeListener(sliderChangeListener);
		
		JButton runFilter = new JButton("Run Filter");
		runFilter.addActionListener(e-> {
			if(currentLayer != null && greenScreenColor != null) {
				removeClearImageFromImagePreview();
				GreenScreenUtils.applyRGBValueFilterToImage(currentLayer, greenScreenColor, 
						redUpperTolerance.getValue(), redLowerTolerance.getValue(), 
						greenUpperTolerance.getValue(), greenLowerTolerance.getValue(), 
						blueUpperTolerance.getValue(), blueLowerTolerance.getValue());
			
				logUndoableChange();
				callForRepaint();
			}});
		
		rgbValueMethodCard.add(redUpperLabel,   new ChainGBC(0,0).setFill(false).setPadding(5));
		rgbValueMethodCard.add(redLowerLabel,   new ChainGBC(0,1).setFill(false).setPadding(5));
		rgbValueMethodCard.add(greenUpperLabel, new ChainGBC(0,2).setFill(false).setPadding(5));
		rgbValueMethodCard.add(greenLowerLabel, new ChainGBC(0,3).setFill(false).setPadding(5));
		rgbValueMethodCard.add(blueUpperLabel,  new ChainGBC(0,4).setFill(false).setPadding(5));
		rgbValueMethodCard.add(blueLowerLabel,  new ChainGBC(0,5).setFill(false).setPadding(5));
		
		rgbValueMethodCard.add(redUpperTolerance,   new ChainGBC(1,0).setFill(true, false).setPadding(5));
		rgbValueMethodCard.add(redLowerTolerance,   new ChainGBC(1,1).setFill(true, false).setPadding(5));
		rgbValueMethodCard.add(greenUpperTolerance, new ChainGBC(1,2).setFill(true, false).setPadding(5));
		rgbValueMethodCard.add(greenLowerTolerance, new ChainGBC(1,3).setFill(true, false).setPadding(5));
		rgbValueMethodCard.add(blueUpperTolerance,  new ChainGBC(1,4).setFill(true, false).setPadding(5));
		rgbValueMethodCard.add(blueLowerTolerance,  new ChainGBC(1,5).setFill(true, false).setPadding(5));
		
		rgbValueMethodCard.add(runFilter,  new ChainGBC(0,6).setFill(true, false).setPadding(10).setWidthAndHeight(2, 1));
	}
	private void setUpHSBValueMethodCard(JPanel hsbValueMethodCard) {
		JLabel hueUpperLabel = new JLabel("Hue Upper (1.00):", JLabel.RIGHT);
		JLabel hueLowerLabel = new JLabel("Hue Lower (1.00):", JLabel.RIGHT);
		JLabel saturationUpperLabel = new JLabel("Sat. Upper (10.0):", JLabel.RIGHT);
		JLabel saturationLowerLabel = new JLabel("Sat. Lower (10.0):", JLabel.RIGHT);
		JLabel brightnessUpperLabel = new JLabel("Bright. Upper (10.0):", JLabel.RIGHT);
		JLabel brightnessLowerLabel = new JLabel("Bright. Lower (10.0):", JLabel.RIGHT);		
		
		JSlider hueUpperTolerance =  new JSlider(0,1000,100);
		JSlider hueLowerTolerance =  new JSlider(0,1000,100);
		JSlider saturationUpperTolerance = new JSlider(0,1000,100);
		JSlider saturationLowerTolerance = new JSlider(0,1000,100);
		JSlider brightnessUpperTolerance =  new JSlider(0,1000,100);
		JSlider brightnessLowerTolerance =  new JSlider(0,1000,100);
		
		
		ChangeListener sliderChangeListener = e -> {
			if(currentLayer == null)
				return;
			
			removeClearImageFromImagePreview();
			
			BufferedImage image = GreenScreenUtils.applyHSBValueFilterToClearImage(currentLayer, greenScreenColor, 
					hueUpperTolerance.getValue() / 100f, hueLowerTolerance.getValue() / 100f, 
					saturationUpperTolerance.getValue() / 10f, saturationLowerTolerance.getValue() / 10f, 
					brightnessUpperTolerance.getValue() / 10f, brightnessLowerTolerance.getValue() / 10f);
			
			drawClearImageOnImagePreview(image);
		};
		
		
		hueUpperTolerance.addChangeListener(e->hueUpperLabel.setText("Hue Upper (" + String.format("%.2f", hueUpperTolerance.getValue() / 100.0) + "):"));
		hueLowerTolerance.addChangeListener(e->hueLowerLabel.setText("Hue Lower (" + String.format("%.2f", hueLowerTolerance.getValue() / 100.0) + "):"));
		saturationUpperTolerance.addChangeListener(e->saturationUpperLabel.setText("Sat. Upper (" + String.format("%.1f", saturationUpperTolerance.getValue() / 10.0) + "):"));
		saturationLowerTolerance.addChangeListener(e->saturationLowerLabel.setText("Sat. Lower (" + String.format("%.1f", saturationLowerTolerance.getValue() / 10.0) + "):"));
		brightnessUpperTolerance.addChangeListener(e->brightnessUpperLabel.setText("Bright. Upper (" + String.format("%.1f", brightnessUpperTolerance.getValue() / 10.0) + "):"));
		brightnessLowerTolerance.addChangeListener(e->brightnessLowerLabel.setText("Bright. Lower (" + String.format("%.1f", brightnessLowerTolerance.getValue() / 10.0) + "):"));
		
		hueUpperTolerance.addChangeListener(sliderChangeListener);
		hueLowerTolerance.addChangeListener(sliderChangeListener);
		saturationUpperTolerance.addChangeListener(sliderChangeListener);
		saturationLowerTolerance.addChangeListener(sliderChangeListener);
		brightnessUpperTolerance.addChangeListener(sliderChangeListener);
		brightnessLowerTolerance.addChangeListener(sliderChangeListener);
		
		JButton runFilter = new JButton("Run Filter");
		runFilter.addActionListener(e-> {
			if(currentLayer != null && greenScreenColor != null) {
				removeClearImageFromImagePreview();
				GreenScreenUtils.applyHSBValueFilterToImage(currentLayer, greenScreenColor, 
						hueUpperTolerance.getValue() / 100f, hueLowerTolerance.getValue() / 100f, 
						saturationUpperTolerance.getValue() / 10f, saturationLowerTolerance.getValue() / 10f, 
						brightnessUpperTolerance.getValue() / 10f, brightnessLowerTolerance.getValue() / 10f);
			
				logUndoableChange();
				callForRepaint();
			}});
		
		hsbValueMethodCard.add(hueUpperLabel,         new ChainGBC(0,0).setFill(false).setPadding(5));
		hsbValueMethodCard.add(hueLowerLabel,         new ChainGBC(0,1).setFill(false).setPadding(5));
		hsbValueMethodCard.add(saturationUpperLabel,  new ChainGBC(0,2).setFill(false).setPadding(5));
		hsbValueMethodCard.add(saturationLowerLabel,  new ChainGBC(0,3).setFill(false).setPadding(5));
		hsbValueMethodCard.add(brightnessUpperLabel,  new ChainGBC(0,4).setFill(false).setPadding(5));
		hsbValueMethodCard.add(brightnessLowerLabel,  new ChainGBC(0,5).setFill(false).setPadding(5));
		
		hsbValueMethodCard.add(hueUpperTolerance,         new ChainGBC(1,0).setFill(true, false).setPadding(5));
		hsbValueMethodCard.add(hueLowerTolerance,         new ChainGBC(1,1).setFill(true, false).setPadding(5));
		hsbValueMethodCard.add(saturationUpperTolerance,  new ChainGBC(1,2).setFill(true, false).setPadding(5));
		hsbValueMethodCard.add(saturationLowerTolerance,  new ChainGBC(1,3).setFill(true, false).setPadding(5));
		hsbValueMethodCard.add(brightnessUpperTolerance,  new ChainGBC(1,4).setFill(true, false).setPadding(5));
		hsbValueMethodCard.add(brightnessLowerTolerance,  new ChainGBC(1,5).setFill(true, false).setPadding(5));
		
		hsbValueMethodCard.add(runFilter,  new ChainGBC(0,6).setFill(true, false).setPadding(10).setWidthAndHeight(2, 1));
	}
	
	
	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {
		if(drawThread != null) {
			drawThread.interrupt();
			drawThread = null;

			clearDrawingsOnImagePreview();			
			
			if(selectedRect != null) {
				if(selectedRect.height * selectedRect.width == 0) 
					greenScreenColor = new Color(layer.getRGB(selectedRect.x, selectedRect.y));
				else {		
					int averageRed = 0;
					int averageBlue = 0;
					int averageGreen = 0;
					int rgbInt;
					
					for(int x = (selectedRect.x < 0) ? 0 : selectedRect.x; x < selectedRect.x + selectedRect.width && x < layer.getWidth(); x++)
						for(int y = (selectedRect.y < 0) ? 0 : selectedRect.y; y < selectedRect.y + selectedRect.height && y < layer.getHeight(); y++) {
							rgbInt = layer.getRGB(x, y);
							averageRed +=   ((rgbInt >> 16) & 0xFF);
							averageGreen += ((rgbInt >> 8)  & 0xFF);
							averageBlue +=  (rgbInt         & 0xFF);
						}
					
					averageRed   /= selectedRect.height * selectedRect.width;
					averageGreen /= selectedRect.height * selectedRect.width;
					averageBlue  /= selectedRect.height * selectedRect.width;
					greenScreenColor = new Color(averageRed, averageGreen, averageBlue);
				}
				
				
				
				colorPreview.setColor(greenScreenColor);
				rgbValueLabel.setText("R=" + greenScreenColor.getRed() + ", G=" + greenScreenColor.getGreen() 
						+ ", B=" + greenScreenColor.getBlue());
				
				float[] hsbValues = Color.RGBtoHSB(greenScreenColor.getRed(), greenScreenColor.getGreen(), 
						greenScreenColor.getBlue(), null);
						
				hsbValueLabel.setText("H=" + String.format("%.1f", hsbValues[0]*100) + ", S=" + String.format("%.1f", hsbValues[1]*100) 
						+ ", B=" + String.format("%.1f", hsbValues[2]*100));
			
			}			
		}
	}
	protected void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame) {		
		drawThread = new Thread(new DragRunnable());
		drawThread.start();		
	}

	protected void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame) {
		if(newFrame == null)
			currentLayer = null;
		else		
			currentLayer = newFrame.getLayer();
	}

	protected void updateCollapsedState(boolean collapsed) {
		filterMethodsPanel.setVisible(!collapsed);
	}
	
	private class DragRunnable implements Runnable {
		
		public void run() {		
			
			try {
				boolean dragged = false;
				Point startingPoint = getMousePointOnImage();
				Point newPoint;
				
				selectedRect = new Rectangle(startingPoint.x, startingPoint.y, 1, 1);
				
				while(true) {
					Thread.sleep(5);	
					
					newPoint = getMousePointOnImage();
					 
					dragged = dragged || Math.abs(newPoint.x - startingPoint.x) + Math.abs(newPoint.y - startingPoint.y) >=4;
					
					if(newPoint == null || !dragged)
						continue;
					
					clearDrawingsOnImagePreview();
					
					if(newPoint.x > startingPoint.x) {
						if(newPoint.y > startingPoint.y) { // Quad. IV (SE)
							selectedRect = new Rectangle(startingPoint.x, startingPoint.y, 
									newPoint.x - startingPoint.x,  newPoint.y - startingPoint.y);
							drawShapeOnImagePreview(selectedRect, dragHighlightColor);
						} else { // Quad. I (NE)
							selectedRect = new Rectangle(startingPoint.x, newPoint.y, 
									newPoint.x - startingPoint.x,  startingPoint.y - newPoint.y);
							drawShapeOnImagePreview(selectedRect, dragHighlightColor);
						}
					} else {
						if(newPoint.y > startingPoint.y) { // Quad. III (SW)
							selectedRect = new Rectangle(newPoint.x, startingPoint.y, 
									startingPoint.x - newPoint.x, newPoint.y - startingPoint.y);
							drawShapeOnImagePreview(selectedRect, dragHighlightColor);							
						} else { // Quad. II (NW)
							selectedRect = new Rectangle(newPoint.x, newPoint.y, 
									startingPoint.x - newPoint.x, startingPoint.y - newPoint.y);
							drawShapeOnImagePreview(selectedRect, dragHighlightColor);
						}
					}
					
					callForRepaint();									
				}
			} catch (InterruptedException e) {

			}
			
			logUndoableChange();
		}		
	}
}
