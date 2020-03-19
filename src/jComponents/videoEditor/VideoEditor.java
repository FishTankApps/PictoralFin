package jComponents.videoEditor;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import interfaces.Themed;
import mainFrame.PictoralFin;
import objectBinders.Theme;

public class VideoEditor extends JPanel implements Themed {
	private static final long serialVersionUID = -3316034116785566254L;

	public static final int FRAME = 0;
	
	private JSplitPane horizontalSplitPane;
	private VideoPreview videoPreview;
	private VideoEditorSettingsPanel videoEditorSettingsPanel;
	
	public VideoEditor(Theme theme, PictoralFin pictoralFin) {	
		
		setLayout(new GridLayout(1,0));
		videoPreview = new VideoPreview(this, theme);
		videoEditorSettingsPanel = new VideoEditorSettingsPanel(pictoralFin);
		
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		horizontalSplitPane.setLeftComponent(videoPreview);
		horizontalSplitPane.setRightComponent(videoEditorSettingsPanel);
		horizontalSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> {});
		horizontalSplitPane.setOneTouchExpandable(false);
		
		add(horizontalSplitPane);
	}

	public VideoPreview getVideoPreview() {
		return this.videoPreview;
	}
	public VideoEditorSettingsPanel getVideoEditorSettingsPanel() {
		return videoEditorSettingsPanel;
	}
	
	public void setDividersToDefaultLocations() {
		//videoPreviewAndSettingsPane.setDividerLocation(1100);		
	}
	public void refresh() {
		//this.validate();
		//repaint();
		
		//videoPreview.refresh();	
		//videoPreviewSettings.refresh();					
	}
	
	public void pausePreview() {
		videoPreview.setPreviewState(false);
	}
	
	public void applyTheme(Theme theme) {
		setBackground(theme.getPrimaryBaseColor());
		horizontalSplitPane.setBackground(theme.getPrimaryHighlightColor());		
	}
}
