package jTimeLine;

import java.io.Serializable;

public interface OnVideoDurrationChangedListener extends Serializable {

	public abstract void onVideoDurrationChanged(long durration);
	
}
