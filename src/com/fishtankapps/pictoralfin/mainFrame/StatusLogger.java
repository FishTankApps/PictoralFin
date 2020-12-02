package com.fishtankapps.pictoralfin.mainFrame;

public class StatusLogger {
	
	static StatusLogger logger;

	private PictoralFin pictoralFin;
	StatusLogger(PictoralFin pictoralFin){
		this.pictoralFin = pictoralFin;
	}
	
	public static void logPrimaryStatus(String log) {
		logger.pictoralFin.setStatus(log);
	}	
	
	public static void logSecondaryStatus(String log) {
		logger.pictoralFin.setSecondaryStatus(log);
	}	
}
