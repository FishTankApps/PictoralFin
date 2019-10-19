package jComponents.videoEditor;

import static globalValues.GlobalVariables.*;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import objectBinders.VideoSettings;

public class VideoEditor extends JPanel {
	private static final long serialVersionUID = -3316034116785566254L;
	private JPanel videoPreviewPanel, frameTimeLinePanel, settingsPanel;
	private JSplitPane videoPreviewAndSettingsPane, frameTimeLineAndVideoPreviewAndSettingsPanePane;
	private VideoPreview videoPreview;
	private VideoPreviewSettings videoPreviewSettings;
	
	public VideoEditor() {
		
		setLayout(new GridLayout(1,1,1,1));
		setBackground(settings.getTheme().getPrimaryHighlightColor());
		
		setUpPanels();
		setUpPanes();
	}

	public void setUpPanels() {
		videoPreviewPanel = new JPanel();
		videoPreviewPanel.setBackground(settings.getTheme().getSecondaryHighlightColor());
		frameTimeLinePanel = new JPanel(new GridLayout(1,1,1,1));
		frameTimeLinePanel.setBackground(settings.getTheme().getPrimaryBaseColor());
		settingsPanel = new JPanel(new GridLayout(1,1,1,1));
		settingsPanel.setBackground(settings.getTheme().getPrimaryBaseColor());

		frameTimeLinePanel.add(pfk.getFrameTimeLine());
		videoPreview = new VideoPreview(videoPreviewPanel);
		videoPreviewPanel.add(videoPreview);
		videoPreviewSettings = new VideoPreviewSettings();
		settingsPanel.add(videoPreviewSettings);
	}
	public void setUpPanes() {
		videoPreviewAndSettingsPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, videoPreviewPanel, settingsPanel);
		videoPreviewAndSettingsPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> refresh());
		videoPreviewAndSettingsPane.setOneTouchExpandable(false);
		videoPreviewAndSettingsPane.setBackground(settings.getTheme().getPrimaryHighlightColor());
		
		
		frameTimeLineAndVideoPreviewAndSettingsPanePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, videoPreviewAndSettingsPane, frameTimeLinePanel);
		frameTimeLineAndVideoPreviewAndSettingsPanePane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> refresh());
		frameTimeLineAndVideoPreviewAndSettingsPanePane.setOneTouchExpandable(false);		
		frameTimeLineAndVideoPreviewAndSettingsPanePane.setBackground(settings.getTheme().getPrimaryHighlightColor());

		this.add(frameTimeLineAndVideoPreviewAndSettingsPanePane);		
	}	

	public VideoPreview getVideoPreview() {
		return this.videoPreview;
	}
	
	public void detachFrameTimeLine() {
		frameTimeLinePanel.remove(pfk.getFrameTimeLine());
	}
	
	public void attachFrameTimeLine() {
		frameTimeLinePanel.add(pfk.getFrameTimeLine());
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
		frameTimeLineAndVideoPreviewAndSettingsPanePane.setDividerLocation(645);
	}
	public void refresh() {
		this.validate();
		repaint();
		
		pfk.getFrameTimeLine().refresh(true);
		videoPreview.refresh();	
		videoPreviewSettings.refresh();					
	}
}
