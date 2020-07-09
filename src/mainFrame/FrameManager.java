package mainFrame;

import objectBinders.Frame;

@Deprecated
public class FrameManager {

	static FrameManager frameManager;
	
	private PictoralFin pictoralFin;
	
	FrameManager(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
	}
	
	private void deepClean() {
		Frame[] frames = pictoralFin.getTimeLine().getFrames();
		
		if(frames == null || frames.length == 0)
			return;
		
		int indexOfSelectedFrame = pictoralFin.getTimeLine().getCurrentFrameIndex();
		int averageStashingTime = Frame.averageStashAndLoadTime;
		
		long durrationFromSelectedFrame = 0;
		
		for(int index = 0; index < frames.length; index++) {
			if(index < indexOfSelectedFrame)
				frames[index].stashImages();
			else if(index > indexOfSelectedFrame) {
				durrationFromSelectedFrame += frames[index].getDuration();
				
				if(durrationFromSelectedFrame <=  averageStashingTime)
					frames[index].loadImages();
				else
					frames[index].stashImages();
			}			
		}
	}
	
	public static void performDeepClean() {
		new Thread(()->frameManager.deepClean()).start();
	}
	
	public static void performQuickClean() {
		new Thread(()->frameManager.deepClean()).start();
	}
	
}
