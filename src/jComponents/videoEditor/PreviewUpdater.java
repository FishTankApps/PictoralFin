package jComponents.videoEditor;

import utilities.AccuPause;

public class PreviewUpdater implements Runnable {

	private VideoPreview preview;
	
	public PreviewUpdater(VideoPreview preview) {
		this.preview = preview;
	}
	
	@Override
	public void run() {
		try {
			AccuPause accuPause = new AccuPause();
			while(true) {
				accuPause.resetStartTime();

				Thread.sleep(10);
				
				while(preview.getPreviewState()) {		
					accuPause.acuSleep(preview.getTimeLine().getCurrentFrame().getDuration());
					
					if(preview.getPreviewState())
						preview.goToNextFrame();
				}				
			}
		} catch (InterruptedException e) {
			
		}

	}

}
