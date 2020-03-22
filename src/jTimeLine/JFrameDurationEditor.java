package jTimeLine;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Polygon;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import objectBinders.Theme;
import utilities.ChainGBC;

public class JFrameDurationEditor extends JPanel {

	private static final long serialVersionUID = -6268190339352421685L;
	
	
	private JLabel labelFPS;
	private JLabel labelMili;
	private JSlider durationInFPS;
	private SpinnerNumberModel durationInMilliModel;
	private JSpinner durationInMili;
	private JButton applyToAll;
	
	private boolean adjustingValues = false;	
	private Theme theme;
	
	public JFrameDurationEditor(JFrameButton frameButton, Theme theme) {
		this.theme = theme;
		
		setBackground(theme.getPrimaryHighlightColor());
		setLayout(new GridBagLayout());		
		
		
		durationInMilliModel = new SpinnerNumberModel(frameButton.getFrame().getDuration(), 2, Integer.MAX_VALUE, 50);
		
		durationInMili = new JSpinner(durationInMilliModel);
		durationInMili.setBackground(theme.getSecondaryBaseColor());
		
		durationInFPS  = new JSlider();
		durationInFPS.setMaximum(50);
		durationInFPS.setMinimum(2);
		durationInFPS.setValue((int) (1000.0/frameButton.getFrame().getDuration()));		
		durationInFPS.setMajorTickSpacing(5);
		durationInFPS.setMinorTickSpacing(1);
		durationInFPS.setPaintTicks(true);
		durationInFPS.setBackground(theme.getSecondaryBaseColor());
		
		
		labelFPS  = new JLabel("Frames Per Second (" + durationInFPS.getValue() + "):");
		labelFPS.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 15));
		labelMili = new JLabel("Durration In Mili: (" + durationInMilliModel.getNumber() + "):");		
		labelMili.setFont(labelFPS.getFont());
		
		applyToAll   = new JButton("Apply Time to All Frames");
		applyToAll.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 17));
		
		add(labelFPS,       new ChainGBC(0, 0).setPadding(20, 2, 20, 2).setWidthAndHeight(1, 1).setFill(false));
		add(durationInFPS, 	new ChainGBC(1, 0).setPadding(2, 20, 20, 2).setWidthAndHeight(1, 1).setFill(true));
		add(labelMili,      new ChainGBC(0, 1).setPadding(20, 2, 2, 5).setWidthAndHeight(1, 1).setFill(false));
		add(durationInMili, new ChainGBC(1, 1).setPadding(2, 20, 2, 5).setWidthAndHeight(1, 1).setFill(true));
		add(applyToAll,     new ChainGBC(0, 2).setPadding(20).setWidthAndHeight(2, 1).setFill(true));
		
		durationInFPS.addChangeListener(e -> {
				labelFPS.setText("Frames Per Second (" + durationInFPS.getValue() + "):");
				FrameTimeLine frameTimeLine = (FrameTimeLine) frameButton.getParent();
				if(!adjustingValues) {
					adjustingValues = true;				
					durationInMilliModel.setValue(1000 / (int) Math.floor(durationInFPS.getValue()));
					
					frameButton.getFrame().setDuration((int) durationInMilliModel.getValue());
					frameButton.repaint();
					adjustingValues = false;
				}	
				
				frameTimeLine.flagDurrationChanged();
			});
		
		durationInMili.addChangeListener(e -> {
				FrameTimeLine frameTimeLine = (FrameTimeLine) frameButton.getParent();
				if(!adjustingValues) {
					adjustingValues = true;				
					durationInFPS.setValue((int) (1000.0 / ((int) Math.floor((double) durationInMili.getValue()))));
					
					frameButton.getFrame().setDuration((int) Math.floor((double) durationInMili.getValue()));
					frameButton.repaint();
					adjustingValues = false;
					frameTimeLine.flagDurrationChanged();
				}				
			});
		
		applyToAll.addActionListener(e -> {
			FrameTimeLine frameTimeLine = (FrameTimeLine) frameButton.getParent();
			for(Component c : frameTimeLine.getComponents())
				if(c instanceof JFrameButton)
					((JFrameButton) c).getFrame().setDuration((long) Math.floor((double) durationInMilliModel.getValue()));
			
			frameTimeLine.flagDurrationChanged();
			
			frameTimeLine.repaint();
			
		});
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(theme.getSecondaryHighlightColor());
		if(theme.isSharp()) {
			Polygon p = new Polygon();
			p.addPoint(5, 15);
			p.addPoint(15, 5);

			p.addPoint(getWidth() - 5, 5);			
			
			p.addPoint(getWidth() - 5, getHeight() - 25);
			p.addPoint(getWidth() - 25, getHeight() - 5);
			
			p.addPoint(5, getHeight() - 5);
			g.fillPolygon(p);
		} else 
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
	}
}
