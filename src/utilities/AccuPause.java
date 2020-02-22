package utilities;

public class AccuPause {

	private long startTime;
	
	public AccuPause() {
		startTime = System.currentTimeMillis();
	}
	
	public void acuSleep(long pauseTimeInMillis) throws InterruptedException {
		if(Thread.currentThread().isInterrupted())
			throw new InterruptedException();
		
		long currentTime = System.currentTimeMillis();
		if(currentTime-startTime < pauseTimeInMillis)
			System.out.println("SKIPPED");
		
		
		
		while(currentTime-startTime < pauseTimeInMillis){
			currentTime = System.currentTimeMillis();
		}
		
		startTime = System.currentTimeMillis();
	}
	public void resetStartTime() {
		startTime = System.currentTimeMillis();
	}
}
