package listeners;

import static globalValues.Constants.MAX_SCALE;
import static globalValues.GlobalVariables.isMousePressed;
import static globalValues.GlobalVariables.pfk;

import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import jComponents.pictureEditor.PictureEditor;

public class PictureEditorMouseListener implements MouseListener, MouseWheelListener{

	private PictureEditor pe;
	//private int draggedAmount;
	
	public PictureEditorMouseListener(PictureEditor pe) {
		this.pe = pe;
	}
	
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {
		isMousePressed = true;
		//draggedAmount = -1;
				
		if(arg0.getButton() == MouseEvent.BUTTON3)
			dragImage();
		else if(arg0.getButton() == MouseEvent.BUTTON2)
			pe.centerImage();
		//else if(arg0.getButton() == MouseEvent.BUTTON1)
			//new Drawer(pfs).draw();
	}

	public void mouseReleased(MouseEvent arg0) {
		isMousePressed = false;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {		
		int[] pixelPoint = pfk.getMousePointOnImage();
		pe.setScale(pe.getScale() - (e.getWheelRotation() * 5));
    	
    	if(pe.getScale() != MAX_SCALE){
    		double zoomDiff = (e.getWheelRotation() * 5);
    		pe.setPictureX(pe.getPictureX() + (int) (pixelPoint[0] * zoomDiff));
    		pe.setPictureY(pe.getPictureY() + (int) (pixelPoint[1] * zoomDiff));
    	}
    	
    	pe.repaint();
	}
	
	public void dragImage(){
		pfk.getFrame().setCursor(new Cursor(Cursor.MOVE_CURSOR));
        
		new Thread(new Runnable(){

			public void run() {
				java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
				int oldx = (int) mouseLocation.getX(),     oldy = (int) mouseLocation.getY();
				int oldXX = pe.getPictureX(), oldYY = pe.getPictureY();
				//int startX = oldx, starty = oldy;

	    		while(isMousePressed){
	    			pe.setPictureX(oldXX);
	    			pe.setPictureY(oldYY);
	    			mouseLocation =  MouseInfo.getPointerInfo().getLocation();
	    			pe.setPictureX(pe.getPictureX() + (int) (mouseLocation.getX() - oldx));
	    			pe.setPictureY(pe.getPictureY() + (int) (mouseLocation.getY() - oldy));
	    			pe.repaint();
	    			
					try{Thread.sleep(10);}catch(Exception ignore){}
				}	
	    		
	    		//draggedAmount = (int) (Math.abs(startX - mouseLocation.getX()) + Math.abs(starty - mouseLocation.getY()));
	    		
	    		pfk.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}).start();	
	}

}
