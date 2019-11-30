package JTimeLine;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import objectBinders.Frame;

public class JFrameButton extends JComponent implements MouseListener{

	private static final long serialVersionUID = -6457692933405341223L;	
	
	private final Dimension minSize = new Dimension(50, 50);
	private final Dimension maxSize = new Dimension(500, 500);
	private Frame frame;
	
	private int thumbnailSize = 100;
	private int xOverhang = (thumbnailSize / 8);
	private int yOverhang = (thumbnailSize / 4);
	
	private boolean selected = false;
	
	private Color selectedFrameBoarderColor = Color.RED;
	private Color hoverFrameBoarderColor = Color.BLUE;
	private Color frameBoarderColor = Color.BLUE.darker().darker();
	
	private Color currentColor = frameBoarderColor;
	
	public JFrameButton(Frame frame) {
		super();		
		this.frame = frame;
		
		enableInputMethods(true);   
		addMouseListener(this);
		setFocusable(true);
	}
	
	public void setThumbnailSize(int newSize) {
		thumbnailSize = newSize;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
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
		thumbnailSize = (4 * height) / (frame.getNumberOfLayers() + 4);
		xOverhang = (height) / ((frame.getNumberOfLayers() + 4)*2);
		yOverhang = (height) / (frame.getNumberOfLayers() + 4);
	}
	
	@Override
	public void paint(Graphics g) {		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(thumbnailSize / 40));
		g2d.setColor((selected) ? selectedFrameBoarderColor : currentColor);
		
		int index;		
		for(index = frame.getNumberOfLayers() - 1; index >= 0; index--) {
			
			g2d.drawImage(frame.getLayer(index), (index * xOverhang), (index * yOverhang), thumbnailSize, thumbnailSize, null);
			g2d.drawRect((index * xOverhang), (index * yOverhang), thumbnailSize, thumbnailSize);
		}
	}
	
	public void mouseClicked(MouseEvent arg0) {
		FrameTimeLine ftl = (FrameTimeLine) getParent().getParent().getParent().getParent();
		ftl.onJFrameButtonClicked();
	}
	public void mouseEntered(MouseEvent arg0) {
		currentColor = hoverFrameBoarderColor; 
		repaint();		
	}
	public void mouseExited(MouseEvent arg0) {
		currentColor = frameBoarderColor; 
		repaint();		
	}	
	public void mousePressed(MouseEvent arg0) {
		
	}
	public void mouseReleased(MouseEvent arg0) {}
}
