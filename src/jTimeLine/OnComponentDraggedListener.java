package jTimeLine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

class OnComponentDraggedListener extends MouseAdapter {
	
	private static Rectangle prevRect;
	private final JWindow movingComponentWindow = new JWindow();
	private JFrameButton draggedFrameButton;
	private int draggedComponentIndex = -1;
	private Component blankComponentSpacer;
	private Point startingPoint;
	private Point dragOffset;
	
	private FrameTimeLine frameTimeLine;

	
	public OnComponentDraggedListener(FrameTimeLine frameTimeLine) {
		super();
		
		this.frameTimeLine = frameTimeLine;
		
		movingComponentWindow.setBackground(new Color(0, 255, 255));
	}

	public void mousePressed(MouseEvent event) {
		if (event.getComponent().getParent().getComponentCount() <= 1) 
			startingPoint = null;
		else
			startingPoint = event.getPoint();
	}

	private void startDragging(JComponent parent, Point mouseCords) {		
		draggedFrameButton = frameTimeLine.getHighlightedJFrameButton();
		
		if(draggedFrameButton == null)
			return;
		
		draggedComponentIndex = frameTimeLine.getIndexOfJFrameButton(draggedFrameButton);	
		
		
		if (Objects.equals(draggedFrameButton, parent) || draggedComponentIndex < 0) 
			return;		

		System.out.println("<Start Drag>");

		Point componentsCordsInPanel = draggedFrameButton.getLocation();
		dragOffset = new Point(mouseCords.x - componentsCordsInPanel.x, mouseCords.y - componentsCordsInPanel.y);

		
		blankComponentSpacer = Box.createRigidArea(draggedFrameButton.getSize());
		swapComponentLocation(parent, draggedFrameButton, blankComponentSpacer, draggedComponentIndex);
		
		movingComponentWindow.removeAll();
		movingComponentWindow.add(draggedFrameButton.getCopy());
		movingComponentWindow.pack();

		updateWindowLocation(mouseCords, parent);
		movingComponentWindow.setVisible(true);
		
		frameTimeLine.requestFocus();
	}

	private void updateWindowLocation(Point pt, JComponent parent) {
		Point p = new Point(pt.x - dragOffset.x, pt.y - dragOffset.y);
		SwingUtilities.convertPointToScreen(p, SwingUtilities.getRoot(parent));
		movingComponentWindow.setLocation(p);
	}

	private static int getTargetIndex(Rectangle compnentBounds, Point pt, int componentIndex) {
		int height = (int) (.5 + compnentBounds.height * .5);
		Rectangle R1 = new Rectangle(compnentBounds.x, compnentBounds.y, compnentBounds.width, height);
		Rectangle R2 = new Rectangle(compnentBounds.x, compnentBounds.y + height, compnentBounds.width, height);

		if (R1.contains(pt)) {
			prevRect = R1;
			return componentIndex - 1 > 0 ? componentIndex : 0;
		} else if (R2.contains(pt)) {
			prevRect = R2;
			return componentIndex;
		}
		
		return -1;
	}

	private static void swapComponentLocation(Container parent, Component toRemove, Component toAdd, int idx) {
		parent.remove(toRemove);
		parent.add(toAdd, idx);
		parent.revalidate();
		parent.repaint();
	}

	public void mouseDragged(MouseEvent event) {		
		if(startingPoint == null)
			return;
		
		Point eventPoint = event.getPoint();
		JComponent parent = (JComponent) event.getComponent().getParent();

		double xDiff = Math.pow(eventPoint.x - startingPoint.x, 2);
		double yDiff = Math.pow(eventPoint.y - startingPoint.y, 2);
		
		if (draggedFrameButton == null && Math.sqrt(xDiff + yDiff) > DragSource.getDragThreshold())  // IF YOU ARE DRAGGING NEW COMPONENT
			startDragging(parent, eventPoint);			
		
		else if (movingComponentWindow.isVisible() && draggedFrameButton != null) {			
			updateWindowLocation(eventPoint, parent);
			if (prevRect != null && prevRect.contains(eventPoint)) {
				return;
			}

			for (int componentIndexCounter = 0; componentIndexCounter < parent.getComponentCount(); componentIndexCounter++) {
				Component component = parent.getComponent(componentIndexCounter);
				Rectangle componentBounds = component.getBounds();
				if (Objects.equals(component, blankComponentSpacer) && componentBounds.contains(eventPoint)) {
					return;
				}
				
				int targetIndex = getTargetIndex(componentBounds, eventPoint, componentIndexCounter);
				if (targetIndex >= 0) {
					swapComponentLocation(parent, blankComponentSpacer, blankComponentSpacer, targetIndex);
					return;
				}
			}
			parent.remove(blankComponentSpacer);
			parent.revalidate();
		}	
	}

	public void mouseReleased(MouseEvent event) {
		
		
		startingPoint = null;
		if (!movingComponentWindow.isVisible() || draggedFrameButton == null) {
			System.out.println("NO END");
			return;
		}
		
		Point eventPoint = event.getPoint();
		JComponent parent = (JComponent) event.getComponent().getParent();
		//parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		System.out.println("<End Drag>");
		
		
		// close the cursor window
		Component draggedComponent = this.draggedFrameButton;
		this.draggedFrameButton = null;
		prevRect = null;
		startingPoint = null;
		dragOffset = null;
		movingComponentWindow.setVisible(false);

		// swap the dragging panel and the dummy filler
		for (int componentIndexCount = 0; componentIndexCount < parent.getComponentCount(); componentIndexCount++) {
			Component c = parent.getComponent(componentIndexCount);
			if (Objects.equals(c, blankComponentSpacer)) {
				swapComponentLocation(parent, blankComponentSpacer, draggedComponent, componentIndexCount);
				return;
			}
			int targetIndex = getTargetIndex(c.getBounds(), eventPoint, componentIndexCount);
			if (targetIndex >= 0) {
				swapComponentLocation(parent, blankComponentSpacer, draggedComponent, targetIndex);
				return;
			}
		}
		if (parent.getParent().getBounds().contains(eventPoint)) {
			swapComponentLocation(parent, blankComponentSpacer, draggedComponent, parent.getComponentCount());
		} else {
			swapComponentLocation(parent, blankComponentSpacer, draggedComponent, draggedComponentIndex);
		}
	}
}