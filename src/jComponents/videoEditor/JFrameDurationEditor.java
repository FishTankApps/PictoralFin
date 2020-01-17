package jComponents.videoEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import JTimeLine.JFrameButton;
import interfaces.Themed;
import objectBinders.Theme;
import utilities.Utilities;

public class JFrameDurationEditor extends JPanel implements Themed {

	private static final long serialVersionUID = -6268190339352421685L;
	
	private static final int DURRATION_PRECISION = 5;
	
	private Theme theme;
	
	private JLabel labelFPS;
	private JLabel labelMili;
	private JSlider durationInFPS;
	private JSlider durationInMili;
	private JButton applyToAll;
	
	private boolean adjustingValues = false;	
	
	public JFrameDurationEditor(JFrameButton frameButton, Theme theme) {
		this.theme = theme;		
		
		setLayout(new GridBagLayout());		
		
		durationInFPS  = new JSlider();
		durationInFPS.setMaximum(50);
		durationInFPS.setMinimum(1);
		durationInFPS.setValue((int) (1000.0/frameButton.getFrame().getDuration()));		
		durationInFPS.setMajorTickSpacing(5);
		durationInFPS.setMinorTickSpacing(1);
		durationInFPS.setPaintTicks(true);
		
		durationInMili = new JSlider();
		durationInMili.setMaximum(100);
		durationInMili.setMinimum(2);			
		durationInMili.setValue((int) frameButton.getFrame().getDuration() / DURRATION_PRECISION);
		durationInMili.setMajorTickSpacing(10);
		durationInMili.setMinorTickSpacing(2);
		durationInMili.setPaintTicks(true);	
		
		labelFPS  = new JLabel("Frames Per Second (" + durationInFPS.getValue() + "):");
		labelMili = new JLabel("Durration In Mili: (" + durationInMili.getValue() * DURRATION_PRECISION + "):");		
		
		applyToAll   = new JButton("Apply Time to All Frames");
		
		add(labelFPS,       Utilities.generateGBC(0, 0, 1, 1));
		add(durationInFPS,  Utilities.generateGBC(1, 0, 1, 1));
		add(labelMili,      Utilities.generateGBC(0, 1, 1, 1));
		add(durationInMili, Utilities.generateGBC(1, 1, 1, 1));
		add(applyToAll,     Utilities.generateGBC(0, 3, 2, 1));
		
		durationInFPS.addChangeListener(e -> {
				labelFPS.setText("Frames Per Second (" + durationInFPS.getValue() + "):");
				
				if(!adjustingValues) {
					adjustingValues = true;				
					durationInMili.setValue((int) ((1000.0 / DURRATION_PRECISION) / durationInFPS.getValue()));
					
					frameButton.getFrame().setDuration(durationInMili.getValue() * DURRATION_PRECISION);
					frameButton.repaint();
					adjustingValues = false;
				}	
			});
		durationInMili.addChangeListener(e -> {
				labelMili.setText("Durration In Mili: (" + durationInMili.getValue() * DURRATION_PRECISION + "):");
				
				if(!adjustingValues) {
					adjustingValues = true;				
					durationInFPS.setValue((int) (1000.0 / (durationInMili.getValue() * DURRATION_PRECISION)));
					
					frameButton.getFrame().setDuration(durationInMili.getValue() * DURRATION_PRECISION);
					frameButton.repaint();
					adjustingValues = false;
				}				
			});
		
		applyToAll.addActionListener(e -> {
			JTimeLine.FrameTimeLine frameTimeLine = (JTimeLine.FrameTimeLine) frameButton.getParent();
			
			for(Component c : frameTimeLine.getComponents())
				if(c instanceof JFrameButton)
					((JFrameButton) c).getFrame().setDuration(durationInMili.getValue() * DURRATION_PRECISION);
			
			frameTimeLine.repaint();
		});
	}
	
	public Dimension getPrefferedSize() {
		super.getPreferredSize();
		
		return new Dimension(getParent().getSize().width - 100, getParent().getSize().height - 100);
	}

	
	public void applyTheme(Theme theme) {
		this.theme = theme;		
	}

}
