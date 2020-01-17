package JTimeLine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import interfaces.SettingsPanel;
import interfaces.SettingsPanelOOI;
import interfaces.Themed;
import objectBinders.Frame;
import objectBinders.Theme;

public class JFrameButton extends JComponent implements MouseListener, Themed, SettingsPanelOOI {

	private static final long serialVersionUID = -6457692933405341223L;	
	
	private final Dimension minSize = new Dimension(50, 50);
	private final Dimension maxSize = new Dimension(2000, 2000);
	private Frame frame;
	private Theme theme;
	
	private int thumbnailSize;
	private int xOverhang;
	private int yOverhang;
	
	private boolean selected = false;
	private boolean highlighted = false;
	
	public JFrameButton(Frame frame, Theme theme) {
		this.frame = frame;
		this.theme = theme;
		
		enableInputMethods(true);   
		addMouseListener(this);
		setFocusable(true);
		setPreferredHeight(10);
	}
	
	public void setThumbnailSize(int newSize) {
		thumbnailSize = newSize;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Frame getFrame() {
		return frame;
	}
	public JLabel getCopy() {
		JLabel label = new JLabel("TEST");
		
		label.setPreferredSize(getSize());
		
		return label;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(thumbnailSize + (xOverhang * frame.getNumberOfLayers()), thumbnailSize + (yOverhang * frame.getNumberOfLayers()));
	}
	public Dimension getMinimumSize()  {
		return minSize;
	}
	public Dimension getMaximumSize() {
		return maxSize;
	}
	public void setPreferredHeight(int height) {
		yOverhang = (height / 10);
		xOverhang = yOverhang;
		thumbnailSize = height - (frame.getNumberOfLayers() * yOverhang);
	}
	
	@Override
	public void paint(Graphics g) {		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(thumbnailSize / 40));
		
		g2d.setColor((selected) ? theme.getSecondaryHighlightColor() : theme.getPrimaryHighlightColor());
		
		if(highlighted) 
			g2d.setColor(g2d.getColor().brighter().brighter());		
		
		if(theme.isSharp()) {
			int index;		
						for(index = frame.getNumberOfLayers() - 1; index >= 0; index--) {
				
				g2d.drawImage(frame.getLayer(index), (index * xOverhang), (index * yOverhang), thumbnailSize, thumbnailSize, null);
				g2d.drawRect((index * xOverhang), (index * yOverhang), thumbnailSize, thumbnailSize);
			}
		} else {
			int index;	
			Color boarderColor = g2d.getColor();
			for(index = frame.getNumberOfLayers() - 1; index >= 0; index--) {				
				g2d.drawImage(frame.getLayer(index), (index * xOverhang), (index * yOverhang), thumbnailSize, thumbnailSize, null);	
				
				g2d.setColor(theme.getPrimaryBaseColor());
				g2d.drawRect((index * xOverhang), (index * yOverhang), thumbnailSize, thumbnailSize);	
				
				g2d.setColor(boarderColor);
				g2d.drawRoundRect((index * xOverhang), (index * yOverhang), thumbnailSize, thumbnailSize, thumbnailSize / 7, thumbnailSize / 7);				
			}
		}	
		
		if(frame.getDuration() >= 1) {
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("LCD", Font.ITALIC, (int) (getHeight() * 0.16)));
			g2d.drawString(formatFrameLength(), (int) (getHeight() * 0.08) + 2, (int) (getHeight() * 0.16) + 2);
			
			g2d.setColor(Color.WHITE);
			g2d.drawString(formatFrameLength(), (int) (getHeight() * 0.08), (int) (getHeight() * 0.16));
		}		
	}
	
	private String formatFrameLength() {
		int seconds = (int) ((frame.getDuration()) / 1_000);
		int millis = (int) ((frame.getDuration() - (1_000 * seconds)));
		
		return ((seconds < 10) ? "0" : "") + seconds + "."
		+  ((millis < 100) ? ((millis < 10) ? "00" : "0") : "") + millis;
	}
	
	public void mouseClicked(MouseEvent mouseEvent) {
		FrameTimeLine ftl = (FrameTimeLine) getParent();
		
		if(mouseEvent.getButton() == MouseEvent.BUTTON3) 
			new FramePopUpMenu(this, mouseEvent.getX(), mouseEvent.getY());
		else
			ftl.onJFrameButtonClicked(this);
		
		requestFocus();
		
		repaint();
	}
	public void mouseEntered(MouseEvent arg0) {
		if(getParent() instanceof FrameTimeLine)
			((FrameTimeLine) getParent()).onJFrameButtonHighlighted(this);
		
		highlighted = true;
		repaint();		
	}
	public void mouseExited(MouseEvent arg0) {
		if(getParent() instanceof FrameTimeLine)
			((FrameTimeLine) getParent()).onJFrameButtonUnHighlighted(this);
		
		highlighted = false;
		repaint();		
	}	
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

	
	public void applyTheme(Theme theme) {
		this.theme = theme;		
	}

	
	public SettingsPanel generateSettingsPanel() {		
		return new FrameSettingsPanel(this, theme);
	}
}
