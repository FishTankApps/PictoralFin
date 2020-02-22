package JTimeLine;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import interfaces.SettingsPanel;
import mainFrame.PictoralFin;
import objectBinders.Theme;
import utilities.ChainGBC;
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
	private JFrameDurationEditor durationEditor;
	private JButton removeFrame, editImage;	
	
	private Theme theme;
	
	public FrameSettingsPanel(JFrameButton toEdit, Theme theme) {
		this.theme = theme;
		frameButton = toEdit;
		
		setLayout(new GridBagLayout());
		
		removeFrame = new JButton("Remove This Frame");
		removeFrame.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 17));
		editImage = new JButton("Edit Image");
		editImage.setFont(removeFrame.getFont());
		
		durationEditor = new JFrameDurationEditor(toEdit, theme);
		
		removeFrame.addActionListener(REMOVE_FRAME);
		editImage.addActionListener(EDIT_IMAGE);

		add(removeFrame,    new ChainGBC(0, 0).setPadding(35, 5, 5, 5).setWidthAndHeight(1, 1).setFill(true));
		add(editImage,      new ChainGBC(1, 0).setPadding(5, 35, 5, 5).setWidthAndHeight(1, 1).setFill(true));
		add(durationEditor, new ChainGBC(0, 1).setPadding(5).setWidthAndHeight(2, 1).setFill(true));
		
		setBackground(theme.getPrimaryBaseColor());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(theme.getPrimaryHighlightColor());
		
		if(!theme.isSharp()) {
			g.fillRoundRect(0, 0, getWidth(), getHeight() + 1000, 60, 100);
		} else {
			Polygon p = new Polygon();
			p.addPoint(0, getHeight());
			p.addPoint(0, (int) (getHeight() * .33));
			p.addPoint(30, 0);
			p.addPoint(getWidth() - 30, 0);
			
			p.addPoint(getWidth(), (int) (getHeight() * .33));
			p.addPoint(getWidth(), getHeight());
			g.fillPolygon(p);
		}
		
	}
	
}
