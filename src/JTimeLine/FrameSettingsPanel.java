package JTimeLine;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import interfaces.SettingsPanel;
import mainFrame.PictoralFin;
import utilities.Utilities;

public class FrameSettingsPanel extends SettingsPanel {

	private static final long serialVersionUID = 5935805967298622431L;
	
	private ActionListener REMOVE_FRAME = e -> {
		FrameTimeLine frameTimeLine = (FrameTimeLine) FrameSettingsPanel.this.frameButton.getParent();
		frameTimeLine.removeFrame(FrameSettingsPanel.this.frameButton);
	};
	
	private ActionListener EDIT_IMAGE = e -> {
		PictoralFin pictoralFin = Utilities.getPictoralFin(FrameSettingsPanel.this.frameButton);
		pictoralFin.setCurrentMode(PictoralFin.EditorMode.STATIC);
	};
	
	private JFrameButton frameButton;
	
	private JButton removeFrame, editImage;
	
	
	
	public FrameSettingsPanel(JFrameButton toEdit) {
		frameButton = toEdit;
		
		setLayout(new GridBagLayout());
		
		removeFrame = new JButton("Remove This Frame");
		editImage = new JButton("Edit Image");
		
		removeFrame.addActionListener(REMOVE_FRAME);
		editImage.addActionListener(EDIT_IMAGE);

		add(removeFrame, Utilities.generateGBC(0, 0, 1, 1));
		add(editImage, Utilities.generateGBC(1, 0, 1, 1));
	}
	
}
