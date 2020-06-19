package jComponents.pictureEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mainFrame.PictoralFin;
import objectBinders.Frame;
import utilities.BufferedImageUtil;

public class LayerSelecter extends JPanel {

	private static final long serialVersionUID = 2974910716605097500L;

	private JPanel buttonPanel;
	private Frame selectedFrame = null;
	private PictoralFin pictoralFin;
	private LayerButton addNewLayer;

	public LayerSelecter(PictoralFin pictoralFin) {
		super(new GridLayout(1,1,0,0));
		
		this.pictoralFin = pictoralFin;
		
		BufferedImage addLayer = pictoralFin.getGlobalImageKit().readImage("AddLayerIcon.png");
		BufferedImageUtil.applyColorThemeToImage(addLayer, pictoralFin.getSettings().getTheme());
		addNewLayer = new LayerButton(addLayer, pictoralFin.getSettings().getTheme());
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		JScrollPane jScrollPane = new JScrollPane(buttonPanel);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(jScrollPane);
		setMinimumSize(new Dimension(150, 100));
		
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		refresh();
	}
	
	public void setButtonWidth(int width) {
		for(Component c : buttonPanel.getComponents()) {
			if(c instanceof LayerButton)
				((LayerButton) c).setThumbnailSize(width);
		}
	}
	
	public void setSelectedFrame(Frame frame) {
		selectedFrame = frame;
		refresh();
	}
	
	public void refresh() {
		buttonPanel.removeAll();
		
		if(selectedFrame != null)
			for(BufferedImage i : selectedFrame.getLayers())
				buttonPanel.add(new LayerButton(i, pictoralFin.getSettings().getTheme()));
		
		buttonPanel.add(addNewLayer);
		
		revalidate();
		repaint();
	}
	
}
