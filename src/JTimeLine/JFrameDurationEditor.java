package JTimeLine;

import java.awt.Component;
import java.awt.GridBagLayout;

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
	
	public JFrameDurationEditor(JFrameButton frameButton, Theme theme) {
		
		setLayout(new GridBagLayout());		
		
		durationInFPS  = new JSlider();
		durationInFPS.setMaximum(50);
		durationInFPS.setMinimum(2);
		durationInFPS.setValue((int) (1000.0/frameButton.getFrame().getDuration()));		
		durationInFPS.setMajorTickSpacing(5);
		durationInFPS.setMinorTickSpacing(1);
		durationInFPS.setPaintTicks(true);
		
		durationInMili = new JSlider();
		durationInMili.setMaximum(500);
		durationInMili.setMinimum(20);			
		durationInMili.setValue((int) frameButton.getFrame().getDuration() / DURRATION_PRECISION);
		durationInMili.setMajorTickSpacing(10);
		durationInMili.setMinorTickSpacing(2);
		durationInMili.setPaintTicks(true);	
		
		labelFPS  = new JLabel("Frames Per Second (" + durationInFPS.getValue() + "):");
		labelMili = new JLabel("Durration In Mili: (" + durationInMili.getValue() * DURRATION_PRECISION + "):");		
		
		applyToAll   = new JButton("Apply Time to All Frames");
		
		add(labelFPS,       new ChainGBC(0, 0).setPadding(2).setWidthAndHeight(1, 1).setFill(false));
		add(durationInFPS, 	new ChainGBC(1, 0).setPadding(2).setWidthAndHeight(1, 1).setFill(true));
		add(labelMili,      new ChainGBC(0, 1).setPadding(2).setWidthAndHeight(1, 1).setFill(false));
		add(durationInMili, new ChainGBC(1, 1).setPadding(2).setWidthAndHeight(1, 1).setFill(true));
		add(applyToAll,     new ChainGBC(0, 2).setPadding(2).setWidthAndHeight(2, 1).setFill(true));
		
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
}
