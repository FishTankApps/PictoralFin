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

	private JSplitPane videoPreviewAndSettingsPane;
	private VideoPreview videoPreview;
	private VideoEditorSettingsPanel videoEditorSettingsPanel;
	
	public VideoEditor(Theme theme, PictoralFin pictoralFin) {		
		setLayout(new GridLayout(1,0));
		videoPreview = new VideoPreview(this, theme);
		videoEditorSettingsPanel = new VideoEditorSettingsPanel(theme);
		
		videoPreviewAndSettingsPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		videoPreviewAndSettingsPane.setLeftComponent(videoPreview);
		videoPreviewAndSettingsPane.setRightComponent(videoEditorSettingsPanel);
		videoPreviewAndSettingsPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> {});
		videoPreviewAndSettingsPane.setOneTouchExpandable(false);
		
		add(videoPreviewAndSettingsPane);
		
		pictoralFin.getTimeLine().addOnFrameSelectionChangeListener((oldFrame, newFrame) -> {				
				if(!videoPreview.getPreviewState()) {
					videoEditorSettingsPanel.dettachSettingsPanel();
						
					if(newFrame != null)
						videoEditorSettingsPanel.attachSettingsPanel(newFrame.generateSettingsPanel());		
				}									
			});
		
//		pictoralFin.getTimeLine().addOnFrameSelectionChangeListener((oldFrame, newFrame) -> {	
//					lastEdit = System.currentTimeMillis();
//					
//					new Thread(new Runnable() {
//						
//						public void run() {
//							try {
//								Thread.sleep(WAIT_TIME);
//								
//								if(System.currentTimeMillis() - lastEdit >= WAIT_TIME - 10) {
//									System.out.println("----[ UPDATED ]-----");
//									videoEditorSettingsPanel.dettachSettingsPanel();
//									
//									if(newFrame != null)
//										videoEditorSettingsPanel.attachSettingsPanel(newFrame.generateSettingsPanel());		
//								} else {
//									System.out.println("NOT UPDATED");
//								}
//							} catch (InterruptedException e) {}							
//						}						
//					}).start();
//					
//											
//			});
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
