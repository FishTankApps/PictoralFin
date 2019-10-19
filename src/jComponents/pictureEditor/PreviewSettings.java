package jComponents.pictureEditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;

import jComponents.FrameTimeLine;

import static globalValues.GlobalVariables.pfk;
import static globalValues.GlobalVariables.settings;
import static globalValues.Constants.MAX_SCALE;

public class PreviewSettings extends JPanel{

	private static final long serialVersionUID = -8126759417946584045L;

	private JLabel sizeLabel;
	private JSlider sizeSlider;
	private SpringLayout sl;
	private FrameTimeLine frameTimeLine;
	
	public PreviewSettings() {
		this.setBackground(settings.getTheme().getPrimaryBaseColor());		
		this.frameTimeLine = pfk.getFrameTimeLine();
		
		sizeLabel = new JLabel("Picture Scale (100.0%):");
		sizeLabel.setHorizontalTextPosition(JLabel.RIGHT);
		sizeLabel.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 14));
		
		sizeSlider = new JSlider(10, MAX_SCALE * 10, 1000);
		sizeSlider.addChangeListener(e -> {sizeLabel.setText("Picture Scale (" + String.format("%.1f", (sizeSlider.getValue() / 10.0)) + "%) :"); getParent().repaint();});
				
		sizeSlider.setBackground(settings.getTheme().getSecondaryHighlightColor());
		
		sl = new SpringLayout();
		sl.putConstraint(SpringLayout.NORTH, sizeLabel, 7, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.NORTH, sizeSlider, 5, SpringLayout.NORTH, this);
		
		sl.putConstraint(SpringLayout.WEST, sizeLabel, 20, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.WEST, sizeSlider, 3, SpringLayout.EAST, sizeLabel);
		sl.putConstraint(SpringLayout.EAST, sizeSlider, -20, SpringLayout.EAST, this);
		
		this.setLayout(sl);
		this.add(sizeLabel);
		this.add(sizeSlider);
		
		setPictureScale(100.0);
	}
	
	
	public void attachFrameTimeLine() {
		sl.putConstraint(SpringLayout.NORTH, frameTimeLine, 10, SpringLayout.SOUTH, sizeSlider);
		
		sl.putConstraint(SpringLayout.SOUTH, frameTimeLine, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.WEST, frameTimeLine, 20, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.EAST, frameTimeLine, -20, SpringLayout.EAST, this);
		
		this.add(frameTimeLine);
	}
	public void detachFrameTimeLine() {
		this.remove(frameTimeLine);
	}
	
	//---------{ GETTERS AND SETTERS }-------------------------------------------------------------
	public double getPictureScale() {
		return (sizeSlider.getValue() / 10.0);
	}
	public void setPictureScale(double scale) {
		sizeSlider.setValue((int) (scale * 10));
	}
		
	public void refresh() {	
		frameTimeLine.refresh(false);
		frameTimeLine.setPrefSize(this.getWidth() - 40, (sizeSlider.getHeight() * 7) - 10);		
		setPreferredSize(new Dimension(this.getParent().getSize().width, (sizeSlider.getHeight() * 8) + 15));
		
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(settings.getTheme().getPrimaryHighlightColor());
		g.fillRoundRect(0, 0, getWidth()+2, getHeight() + 70, 40, 40);
		
		g.setColor(settings.getTheme().getPrimaryBaseColor());
		g.fillRoundRect(13, 35, getWidth()-30, getHeight() + 70, 20, 20);
	}
}
