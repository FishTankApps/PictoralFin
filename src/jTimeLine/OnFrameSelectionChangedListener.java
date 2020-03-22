package jTimeLine;

import java.io.Serializable;

public interface OnFrameSelectionChangedListener extends Serializable {

	abstract void frameSelectionChanged(JFrameButton oldFrame, JFrameButton newFrame);
}
