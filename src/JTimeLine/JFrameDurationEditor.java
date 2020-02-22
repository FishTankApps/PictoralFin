package JTimeLine;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Polygon;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import objectBinders.Theme;
import utilities.ChainGBC;

public class JFrameDurationEditor extends JPanel {

	private static final long serialVersionUID = -6268190339352421685L;
	
	private static final int DURRATION_PRECISION = 1;
	
	private JLabel labelFPS;
	private JLabel labelMili;
	private JSlider durationInFPS;
	private JSlider durationInMili;
	private JButton applyToAll;
	
	private boolean adjustingValues = false;	
	private Theme theme;
	
	public JFrameDurationEditor(JFrameButton frameButton, Theme theme) {
		this.theme = theme;
		
		setBackground(theme.getPrimaryHighlightColor());
		setLayout(new GridBagLayout());		
		
		durationInFPS  = new JSlider();
		durationInFPS.setMaximum(50);
		durationInFPS.setMinimum(2);
		durationInFPS.setValue((int) (1000.0/frameButton.getFrame().getDuration()));		
		durationInFPS.setMajorTickSpacing(5);
		durationInFPS.setMinorTickSpacing(1);
		durationInFPS.setPaintTicks(true);
		durationInFPS.setBackground(theme.getSecondaryBaseColor());
		
		durationInMili = new JSlider();
		durationInMili.setMaximum(500);
		durationInMili.setMinimum(20);			
		durationInMili.setValue((int) frameButton.getFrame().getDuration() / DURRATION_PRECISION);
		durationInMili.setMajorTickSpacing(10);
		durationInMili.setMinorTickSpacing(2);
		durationInMili.setPaintTicks(true);	
		durationInMili.setBackground(theme.getSecondaryBaseColor());
		
		labelFPS  = new JLabel("Frames Per Second (" + durationInFPS.getValue() + "):");
		labelFPS.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 15));
		labelMili = new JLabel("Durration In Mili: (" + durationInMili.getValue() * DURRATION_PRECISION + "):");		
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
					durationInMili.setValue((int) ((1000.0 / DURRATION_PRECISION) / durationInFPS.getValue()));
					
					frameButton.getFrame().setDuration(durationInMili.getValue() * DURRATION_PRECISION);
					frameButton.repaint();
					adjustingValues = false;
				}	
				
				frameTimeLine.flagDurrationChanged();
			});
		durationInMili.addChangeListener(e -> {
				labelMili.setText("Durration In Mili: (" + durationInMili.getValue() * DURRATION_PRECISION + "):");
				FrameTimeLine frameTimeLine = (FrameTimeLine) frameButton.getParent();
				if(!adjustingValues) {
					adjustingValues = true;				
					durationInFPS.setValue((int) (1000.0 / (durationInMili.getValue() * DURRATION_PRECISION)));
					
					frameButton.getFrame().setDuration(durationInMili.getValue() * DURRATION_PRECISION);
					frameButton.repaint();
					adjustingValues = false;
					frameTimeLine.flagDurrationChanged();
				}				
			});
		
		applyToAll.addActionListener(e -> {
			FrameTimeLine frameTimeLine = (FrameTimeLine) frameButton.getParent();
			for(Component c : frameTimeLine.getComponents())
				if(c instanceof JFrameButton)
					((JFrameButton) c).getFrame().setDuration(durationInMili.getValue() * DURRATION_PRECISION);
			
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
