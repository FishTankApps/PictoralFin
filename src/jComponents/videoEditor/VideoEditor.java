package jComponents.videoEditor;

import static globalValues.GlobalVariables.*;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import objectBinders.Theme;
import objectBinders.VideoSettings;

public class VideoEditor extends JPanel {
	private static final long serialVersionUID = -3316034116785566254L;
	private JPanel videoPreviewPanel, settingsPanel;
	private JSplitPane videoPreviewAndSettingsPane;
	private VideoPreview videoPreview;
	private VideoPreviewSettings videoPreviewSettings;
	
	private Theme theme;
	
	public VideoEditor(Theme theme) {	
		this.theme = theme;
		
		setLayout(new GridLayout(1,1,1,1));
		setBackground(settings.getTheme().getPrimaryHighlightColor());
		
		setUpPanels();
		setUpPanes();
	}

	public void setUpPanels() {
		videoPreviewPanel = new JPanel();
		videoPreviewPanel.setBackground(theme.getSecondaryHighlightColor());
		settingsPanel = new JPanel(new GridLayout(1,1,1,1));
		settingsPanel.setBackground(theme.getPrimaryBaseColor());

		videoPreview = new VideoPreview(theme);
		videoPreviewPanel.add(videoPreview);
		videoPreviewSettings = new VideoPreviewSettings();
		settingsPanel.add(videoPreviewSettings);
	}
	public void setUpPanes() {
		videoPreviewAndSettingsPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, videoPreviewPanel, settingsPanel);
		videoPreviewAndSettingsPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> refresh());
		videoPreviewAndSettingsPane.setOneTouchExpandable(false);
		videoPreviewAndSettingsPane.setBackground(theme.getPrimaryHighlightColor());
	}	

	public VideoPreview getVideoPreview() {
		return this.videoPreview;
	}
	

	public VideoSettings generateSettings(){
		VideoSettings toReturn = new VideoSettings();
		toReturn.setFrameRate(framesPerSecond);
		toReturn.setVideoFormat(VideoSettings.MP4);
		toReturn.setVideoQuality(0);
		toReturn.setVideoBitRate(9000);
		toReturn.setAudioSettings(videoPreviewSettings.getAudioBoard().generateAudioSettings());
		
		return toReturn;
	}
	
	public void setDividersToDefaultLocations() {
		videoPreviewAndSettingsPane.setDividerLocation(1100);
		
	}
	public void refresh() {
		this.validate();
		repaint();
		
		videoPreview.refresh();	
		videoPreviewSettings.refresh();					
	}
}
