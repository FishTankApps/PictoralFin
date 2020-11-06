package com.fishtankapps.pictoralfin.jComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class JColorPreview extends JComponent {

	private static final long serialVersionUID = -9218940660828798811L;

	private Color color = null;
	private Theme theme;
	
	public JColorPreview(Theme theme) {	
		this.theme = theme;
		TitledBorder border = BorderFactory.createTitledBorder("Color:");
		
		border.setTitleColor(Color.WHITE);
		
		this.setBorder(border);
		
		this.setPreferredSize(new Dimension(50, 50));
		this.setMinimumSize(new Dimension(50, 50));
		this.setBackground(new Color(0,0,0,0));
	}
	
	public void setColor(Color color) {
		this.color = color;
		repaint();
	}
	
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		
		Graphics2D g = (Graphics2D) graphics;
		
		
		if(color != null) {
			int sideGap = (this.getWidth() - 20)/2;
			int rectangleWidth = this.getWidth() - 20;
			int rectangleHeight = (this.getHeight() - 23)/3;
			
			if(theme.isSharp()) {				
				// Red - Top
				g.setPaint(new GradientPaint(sideGap, getHeight()/3, new Color(color.getRed(), 0, 0), getWidth() - sideGap, getHeight()/3, color));
				g.fillRect(10, 10, rectangleWidth, rectangleHeight + 10);
				
				// Blue - Bottom
				g.setPaint(new GradientPaint(sideGap, getHeight()/3, new Color(0, 0, color.getBlue()), getWidth() - sideGap, getHeight()/3, color));
				g.fillRect(10, rectangleHeight * 2, rectangleWidth, rectangleHeight + 10);		
				
			} else {
				// Red - Top
				g.setPaint(new GradientPaint(sideGap, getHeight()/3, new Color(color.getRed(), 0, 0), getWidth() - sideGap, getHeight()/3, color));
				g.fillRoundRect(10, 15, rectangleWidth, rectangleHeight + 10, 10, 10);
				
				// Blue - Bottom
				g.setPaint(new GradientPaint(sideGap, getHeight()/3, new Color(0, 0, color.getBlue()), getWidth() - sideGap, getHeight()/3, color));
				g.fillRoundRect(10, rectangleHeight * 2 + 5, rectangleWidth, rectangleHeight + 10, 10, 10);
			}
			
			// Green - Middle
			g.setPaint(new GradientPaint(sideGap, getHeight()/3, new Color(0, color.getGreen(), 0), getWidth() - sideGap, getHeight()/3, color));
			g.fillRect(10, rectangleHeight + 14, rectangleWidth, rectangleHeight);
			
		} else {
			g.setColor(Color.WHITE);
			g.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 20));
			int width = g.getFontMetrics().stringWidth("No Color");
			
			g.drawString("No Color", getWidth()/2 - width/2, getHeight()/2 + 20/2);
		}
		
		
	}
}