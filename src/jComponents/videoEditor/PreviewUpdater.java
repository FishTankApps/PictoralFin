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
					accuPause.acuSleep(10);
					preview.currentMilli += 10;
					
					if(preview.getPreviewState())
						preview.updateFrame();
				}				
			}
		} catch (InterruptedException e) {
			
		}

	}

}
