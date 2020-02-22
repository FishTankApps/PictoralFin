package jComponents.videoEditor;

public class PreviewUpdater implements Runnable {

	private VideoPreview preview;
	
	public PreviewUpdater(VideoPreview preview) {
		this.preview = preview;
	}
	
	@Override
	public void run() {
		try {
			long startTime;
			while(true) {
				

				Thread.sleep(10);
				
				while(preview.getPreviewState()) {		
					startTime = System.currentTimeMillis();
					Thread.sleep(10);
					preview.currentMilli += System.currentTimeMillis() - startTime;
					
					if(preview.getPreviewState())
						preview.updateFrame();
				}				
			}
		} catch (InterruptedException e) {
			
		}

	}

}
