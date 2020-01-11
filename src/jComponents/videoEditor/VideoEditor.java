package jComponents.videoEditor;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import interfaces.Themed;
import objectBinders.Theme;
import objectBinders.VideoSettings;

public class VideoEditor extends JPanel implements Themed {
	private static final long serialVersionUID = -3316034116785566254L;
	//private JPanel settingsPanel;
	private JSplitPane videoPreviewAndSettingsPane;
	private VideoPreview videoPreview;
	//private boolean firstTime = true;
	
	public VideoEditor(Theme theme) {		
		setLayout(new GridLayout(1,0));
		
		//videoPreviewSettings = new VideoPreviewSettings();
		videoPreview = new VideoPreview(theme);
		
		//settingsPanel = new JPanel(new GridLayout(1,0));
		
		//settingsPanel.add(videoPreviewSettings);
		
		
		videoPreviewAndSettingsPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		videoPreviewAndSettingsPane.setLeftComponent(videoPreview);
		videoPreviewAndSettingsPane.setRightComponent(new JButton("WOAH! I am on the RIGHT!"));
		videoPreviewAndSettingsPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> {});
		videoPreviewAndSettingsPane.setOneTouchExpandable(false);
		
		add(videoPreviewAndSettingsPane);
	}

	public VideoPreview getVideoPreview() {
		return this.videoPreview;
	}

	public VideoSettings generateSettings(){
		VideoSettings toReturn = new VideoSettings();
		toReturn.setFrameRate(10);
		toReturn.setVideoFormat(VideoSettings.MP4);
		toReturn.setVideoQuality(0);
		toReturn.setVideoBitRate(9000);
		//toReturn.setAudioSettings(videoPreviewSettings.getAudioBoard().generateAudioSettings());
		
		return toReturn;
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
		videoPreviewAndSettingsPane.setBackground(theme.getPrimaryHighlightColor());		
	}
}
