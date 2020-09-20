package com.fishtankapps.pictoralfin.jComponents;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;

import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public class JColorChooser extends JFrame{

	private static final long serialVersionUID = 5844485031381749104L;
	private static final float HSB_DIVISOR = 250;
	
	private JTabbedPane selectionTypePane;
	
	private JPanel rgbPanel, hsbPanel, mainPanel;
	private ColorPreview colorPreview;
	private JButton done;
	
	private JLabel redLabel, greenLabel, blueLabel;
	private JSlider red, green, blue;
	private JLabel hueLabel, saturationLabel, brightnessLabel;
	private JSlider hue, saturation, brightness;
	
	private Color color;
	
	private boolean IChangedIt = false;
	
	private boolean colorHasBeenPicked = false;
	
	private Theme theme;
	
	private ChangeListener rgbListener = e->{
		if(!IChangedIt)
			color = new Color(red.getValue(), green.getValue(), blue.getValue());
		
		redLabel  .setText("Red (" + color.getRed() + "):");
		greenLabel.setText("Green (" + color.getGreen() + "):");
		blueLabel .setText("Blue (" + color.getBlue() + "):");
		
		repaint();		
	};
	
	private ChangeListener hsbListener = e->{
		if(!IChangedIt)
			color = new Color(Color.HSBtoRGB(hue.getValue() / HSB_DIVISOR, saturation.getValue() / HSB_DIVISOR, brightness.getValue() / HSB_DIVISOR));
		
		hueLabel       .setText("Hue ("        + String.format("%.1f", (hue.getValue() / HSB_DIVISOR) * 360)        + "/360):");
		saturationLabel.setText("Saturation (" + String.format("%.1f", (saturation.getValue() / HSB_DIVISOR) * 100) + "/100):");
		brightnessLabel.setText("Brightness (" + String.format("%.1f", (brightness.getValue() / HSB_DIVISOR) * 100) + "/100):");
		
		repaint();		
	};
	
	private JColorChooser(Theme theme, Color startingColor){
		this.theme = theme;
		
		if((color = startingColor) == null)
			color = Color.WHITE;
		
		setSize(320, 400);
		setLayout(new GridLayout(1,1,0,0));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Color Chooser");
		setLocationRelativeTo(null);
		addWindowListener(new WindowListener() {

			public void windowClosing(WindowEvent e) {
				color = null;
				colorHasBeenPicked = true;
			}
			
			public void windowOpened(WindowEvent e) {}			
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {				
				color = null;
				colorHasBeenPicked = true;
				
				dispose();
			}		
		});
		
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(theme.getPrimaryBaseColor());
		
		colorPreview = new ColorPreview();		
		selectionTypePane = new JTabbedPane();
		selectionTypePane.addChangeListener(e->{
				if(selectionTypePane.getSelectedIndex() == 0) {
					IChangedIt = true;
					red.setValue(color.getRed());
					blue.setValue(color.getBlue());
					green.setValue(color.getGreen());
					IChangedIt = false;
				} else {
					float[] hsbValues = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
					IChangedIt = true;
					hue.setValue((int) (hsbValues[0] * HSB_DIVISOR));
					saturation.setValue((int) (hsbValues[1] * HSB_DIVISOR));
					brightness.setValue((int) (hsbValues[2] * HSB_DIVISOR));
					IChangedIt = false;
				}
			});
		
		rgbPanel = new JPanel(new GridBagLayout());
		redLabel = new JLabel("Red (" + color.getRed() + "):", JLabel.RIGHT);
		greenLabel = new JLabel("Green (" + color.getGreen() + "):", JLabel.RIGHT);
		blueLabel = new JLabel("Blue (" + color.getBlue() + "):", JLabel.RIGHT);
		red =   new JSlider(0,255,color.getRed());
		green = new JSlider(0,255,color.getGreen());
		blue =  new JSlider(0,255,color.getBlue());
		red.addChangeListener(rgbListener);
		green.addChangeListener(rgbListener);
		blue.addChangeListener(rgbListener);
		
		rgbPanel.setBackground(theme.getSecondaryBaseColor());
		rgbPanel.add(redLabel,   new ChainGBC(0,0).setFill(false, false).setPadding(5));
		rgbPanel.add(red,        new ChainGBC(1,0).setFill(true,  false).setPadding(5));
		rgbPanel.add(greenLabel, new ChainGBC(0,1).setFill(false, false).setPadding(5));
		rgbPanel.add(green,      new ChainGBC(1,1).setFill(true,  false).setPadding(5));
		rgbPanel.add(blueLabel,  new ChainGBC(0,2).setFill(false, false).setPadding(5));
		rgbPanel.add(blue,       new ChainGBC(1,2).setFill(true,  false).setPadding(5));
		
		
		float[] hsbValues = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		
		hsbPanel = new JPanel(new GridBagLayout());
		hueLabel =        new JLabel("Hue ("        + String.format("%.1f", hsbValues[0] * 360) + "/360):", JLabel.RIGHT);
		saturationLabel = new JLabel("Saturation (" + String.format("%.1f", hsbValues[1] * 100) + "/100):", JLabel.RIGHT);
		brightnessLabel = new JLabel("Brightness (" + String.format("%.1f", hsbValues[2] * 100) + "/100):", JLabel.RIGHT);
		hue =         new JSlider(0, (int) HSB_DIVISOR, (int) (hsbValues[0] * HSB_DIVISOR));
		saturation =  new JSlider(0, (int) HSB_DIVISOR, (int) (hsbValues[1] * HSB_DIVISOR));
		brightness =  new JSlider(0, (int) HSB_DIVISOR, (int) (hsbValues[2] * HSB_DIVISOR));
		hue.addChangeListener(hsbListener);
		saturation.addChangeListener(hsbListener);
		brightness.addChangeListener(hsbListener);
		
		hsbPanel.setBackground(theme.getSecondaryBaseColor());
		hsbPanel.add(hueLabel,   new ChainGBC(0,0).setFill(false, false).setPadding(5));
		hsbPanel.add(hue,        new ChainGBC(1,0).setFill(true,  false).setPadding(5));
		hsbPanel.add(saturationLabel, new ChainGBC(0,1).setFill(false, false).setPadding(5));
		hsbPanel.add(saturation,      new ChainGBC(1,1).setFill(true,  false).setPadding(5));
		hsbPanel.add(brightnessLabel,  new ChainGBC(0,2).setFill(false, false).setPadding(5));
		hsbPanel.add(brightness,       new ChainGBC(1,2).setFill(true,  false).setPadding(5));
		
		selectionTypePane.addTab("Red-Green-Blue", rgbPanel);
		selectionTypePane.addTab("Hue-Saturation-Brightness", hsbPanel);
		
		done = new JButton("Done");
		done.addActionListener(e->{colorHasBeenPicked = true; dispose();});
		
		mainPanel.add(colorPreview,      new ChainGBC(0,0).setFill(true, true).setPadding(5));	
		mainPanel.add(selectionTypePane, new ChainGBC(0,1).setFill(true, true).setPadding(5));
		mainPanel.add(done,              new ChainGBC(0,2).setFill(true, false).setPadding(5));
		
		add(mainPanel);
	}
	
	private Color chooseColor() {
		setVisible(true);
		colorHasBeenPicked = false;
		
		while(!colorHasBeenPicked) {Utilities.doNothing();}
		return color;
	}
	
	public static Color showChooserDialog(Theme theme, Color startingColor) {
		return new JColorChooser(theme, startingColor).chooseColor();
	}
	
	private class ColorPreview extends JComponent {
		
		private static final long serialVersionUID = 5887549617879414663L;

		public ColorPreview() {
			TitledBorder border = BorderFactory.createTitledBorder("Chosen Color:");
			border.setTitleColor(Utilities.invertColor(theme.getPrimaryBaseColor()));
			
			this.setBorder(border);
		}
		
		public void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			
			Graphics2D g = (Graphics2D) graphics;
			g.setPaint(new GradientPaint(0, 0, theme.getPrimaryBaseColor(), ColorPreview.this.getWidth(), ColorPreview.this.getHeight(), theme.getPrimaryHighlightColor(), true));
			
			g.fillRect(0, 0, ColorPreview.this.getWidth(), ColorPreview.this.getHeight());
			
			g.setColor(color);
			
			final int border = 20;
			
			g.fillRect(border, border, ColorPreview.this.getWidth() - border*2, ColorPreview.this.getHeight() - border*2);
		}
		
	}
}