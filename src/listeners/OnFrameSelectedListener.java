package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import static globalValues.GlobalVariables.*;

public class OnFrameSelectedListener implements ActionListener {
	
	public OnFrameSelectedListener() {
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		int indexOfButton = pfk.getFrameTimeLine().getButtonIndex((JButton) arg0.getSource());
		pfk.getFrameTimeLine().setCurrentFrame(indexOfButton);
		if(openView == 0) {
			if(lastInFocus instanceof JFormattedTextField)
				((JFormattedTextField) lastInFocus).setText((indexOfButton + 1) + "");
			lastInFocus = arg0.getSource();
			
			pfk.getVideoEditor().getVideoPreview().setCurrentFrame(indexOfButton);
			pfk.getFrameTimeLine().refresh(false);
		}else {
			pfk.getPictureEditor().refresh();
			pfk.getVideoEditor().getVideoPreview().setCurrentFrame(indexOfButton);
		}	
	}
}
