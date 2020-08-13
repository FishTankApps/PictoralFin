package com.fishtankapps.pictoralfin.jTimeLine;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.fishtankapps.pictoralfin.interfaces.SettingsPanel;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public class FrameSettingsPanel extends SettingsPanel {

	private static final long serialVersionUID = 5935805967298622431L;
	
	private ActionListener REMOVE_FRAME = e -> {
		dettach();
		FrameTimeLine frameTimeLine = (FrameTimeLine) FrameSettingsPanel.this.frameButton.getParent();
		frameTimeLine.removeFrame(FrameSettingsPanel.this.frameButton);		
	};
	private ActionListener EDIT_IMAGE = e -> {
		Utilities.getPictoralFin(FrameSettingsPanel.this.frameButton).setCurrentMode(PictoralFin.EditorMode.STATIC);
	};

	
	private JFrameButton frameButton;
	private JFrameDurationEditor durationEditor;
	private JButton removeFrame, editImage, moveLeft, moveRight, moveToEnd, moveToBeginning;	
	private JLabel imageSize;
	
	private Theme theme;
	
	public FrameSettingsPanel(JFrameButton toEdit, Theme theme) {
		this.theme = theme;
		frameButton = toEdit;
		
		setLayout(new GridBagLayout());
		
		removeFrame = new JButton("Remove This Frame");
		removeFrame.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 17));
		editImage = new JButton("Edit Image");
		editImage.setFont(removeFrame.getFont());
		
		moveToBeginning = new JButton("<<");
		moveToBeginning.addActionListener(e->toEdit.move(JFrameButton.BEGINNING));
		moveLeft =        new JButton("<");
		moveLeft.addActionListener(e->toEdit.move(JFrameButton.LEFT));
		moveRight =       new JButton(">");
		moveRight.addActionListener(e->toEdit.move(JFrameButton.RIGHT));
		moveToEnd =       new JButton(">>");
		moveToEnd.addActionListener(e->toEdit.move(JFrameButton.END));
		
		imageSize = new JLabel("Size: " + toEdit.getFrame().getLayer(0).getWidth() + "x" + toEdit.getFrame().getLayer(0).getHeight(), JLabel.CENTER);
		imageSize.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 12));
		
		durationEditor = new JFrameDurationEditor(toEdit, theme);
		
		removeFrame.addActionListener(REMOVE_FRAME);
		editImage.addActionListener(EDIT_IMAGE);
		
		JLabel image = new JLabel();
		image.setIcon(new ImageIcon(BufferedImageUtil.resizeBufferedImage(toEdit.getFrame().getLayer(0), 50, 50, BufferedImage.SCALE_FAST)));

		add(removeFrame,    new ChainGBC(0, 0).setPadding(35, 0, 5, 5).setWidthAndHeight(3, 1).setFill(true));
		add(image,          new ChainGBC(3, 0).setPadding(10, 10,  5, 5).setWidthAndHeight(1, 1).setFill(false));
		add(editImage,      new ChainGBC(4, 0).setPadding(0, 35, 5, 5).setWidthAndHeight(3, 1).setFill(true));
		add(durationEditor, new ChainGBC(0, 1).setPadding(5).setWidthAndHeight(7, 1).setFill(true));
		
		add(moveToBeginning, new ChainGBC(1, 2).setPadding(5).setWidthAndHeight(1, 1).setFill(true));
		add(moveLeft,        new ChainGBC(2, 2).setPadding(5).setWidthAndHeight(1, 1).setFill(true));
		add(imageSize,       new ChainGBC(3, 2).setPadding(5).setWidthAndHeight(1, 1).setFill(false));
		add(moveRight,       new ChainGBC(4, 2).setPadding(5).setWidthAndHeight(1, 1).setFill(true));
		add(moveToEnd,       new ChainGBC(5, 2).setPadding(5).setWidthAndHeight(1, 1).setFill(true));
		
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
