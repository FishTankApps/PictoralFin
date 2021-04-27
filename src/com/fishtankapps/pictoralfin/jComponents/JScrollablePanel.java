package com.fishtankapps.pictoralfin.jComponents;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class JScrollablePanel extends JPanel implements Scrollable {

	private static final long serialVersionUID = -3253915251775482429L;


	public JScrollablePanel() {
		super();
	}

	public JScrollablePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public JScrollablePanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public JScrollablePanel(LayoutManager layout) {
		super(layout);
	}

	public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
       return 10;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width) - 10;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
