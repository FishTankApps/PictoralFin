package projectFileManagement;

import java.io.Serializable;

class AudioInfo implements Serializable {
	private static final long serialVersionUID = 1049524287688712939L;

	private String name;
	private long startTime;
	private long endTime;
	private double volume;
	
	public AudioInfo(String name, long startTime, long endTime, double volume) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.volume = volume;
	}

	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public double getVolume() {
		return volume;
	}
    public String getName() {
    	return name;
    }
}
