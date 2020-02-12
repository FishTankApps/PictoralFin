package jComponents.videoEditor;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import interfaces.Themed;
import mainFrame.PictoralFin;
import objectBinders.Theme;
import objectBinders.VideoSettings;

public class VideoEditor extends JPanel implements Themed {
	private static final long serialVersionUID = -3316034116785566254L;

	public static final int FRAME = 0;
	
	private JSplitPane horizontalSplitPane;
	private VideoPreview videoPreview;
	private VideoEditorSettingsPanel videoEditorSettingsPanel;
	
	private PictoralFin pictoralFin;
	
	public VideoEditor(Theme theme, PictoralFin pictoralFin) {	
		this.pictoralFin = pictoralFin;
		
		setLayout(new GridLayout(1,0));
		videoPreview = new VideoPreview(this, theme);
		videoEditorSettingsPanel = new VideoEditorSettingsPanel(theme);
		
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		horizontalSplitPane.setLeftComponent(videoPreview);
		horizontalSplitPane.setRightComponent(videoEditorSettingsPanel);
		horizontalSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> {});
		horizontalSplitPane.setOneTouchExpandable(false);
		
		add(horizontalSplitPane);
		
		pictoralFin.getTimeLine().addOnFrameSelectionChangeListener((oldFrame, newFrame) -> {				
				if(!videoPreview.getPreviewState()) {
					videoEditorSettingsPanel.dettachSettingsPanel();
						
					if(newFrame != null)
						videoEditorSettingsPanel.attachSettingsPanel(newFrame.generateSettingsPanel());		
				}									
			});
	}

	public VideoPreview getVideoPreview() {
		return this.videoPreview;
	}
	public VideoEditorSettingsPanel getVideoEditorSettingsPanel() {
		return videoEditorSettingsPanel;
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

	public void updateSettingsPanel(int aspectToFocusOn) {
		if(aspectToFocusOn == FRAME) {
			videoEditorSettingsPanel.dettachSettingsPanel();			
			
			videoEditorSettingsPanel.attachSettingsPanel(pictoralFin.getTimeLine().getCurrentFrameButton().generateSettingsPanel());				
		}
	}
	
	public void pausePreview() {
		videoPreview.setPreviewState(false);
	}
	
	public void applyTheme(Theme theme) {
		setBackground(theme.getPrimaryBaseColor());
		horizontalSplitPane.setBackground(theme.getPrimaryHighlightColor());		
	}
}
