package listeners;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import static globalValues.GlobalVariables.pfk;

public class OnWindowResizedListener implements ComponentListener{

	public void componentShown(ComponentEvent arg0) {}
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	
	public void componentResized(ComponentEvent arg0) {
		if(pfk != null)
			pfk.refresh();
	}
}
