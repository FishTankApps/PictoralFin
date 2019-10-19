package listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static globalValues.GlobalVariables.lastInFocus;

public class GlobalFocusListener implements FocusListener {

	public void focusGained(FocusEvent arg0) {}
	public void focusLost(FocusEvent arg0) {
		lastInFocus = arg0.getSource();
	}
}
