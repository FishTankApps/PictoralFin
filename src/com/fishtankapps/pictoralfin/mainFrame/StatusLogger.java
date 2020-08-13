package com.fishtankapps.pictoralfin.mainFrame;

public class StatusLogger {
	
	static StatusLogger logger;

	private PictoralFin pictoralFin;
	StatusLogger(PictoralFin pictoralFin){
		this.pictoralFin = pictoralFin;
	}
	
	public static void logStatus(String log) {
		logger.pictoralFin.setStatus(log);
	}	
}
