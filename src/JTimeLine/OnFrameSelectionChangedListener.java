package JTimeLine;

import objectBinders.Frame;

public interface OnFrameSelectionChangedListener {

	abstract void frameSelectionChanged(Frame oldFrame, Frame newFrame);
}
