package projectFileManagement;

import java.io.Serializable;

public class FrameInfo implements Serializable {
	private static final long serialVersionUID = 8332191104334971405L;

	private long duration;
	
	public FrameInfo(long duration) {
		this.duration = duration;
	}
	
	public long getDuration() {
		return duration;
	}
	
}
