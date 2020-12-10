

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JSlider;

import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.LayerButton;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.Constants;

public class ErasingTool extends ImageEditorTool {

	private static final long serialVersionUID = 1098890128878335706L;
	
	private JSlider drawSize;
	private Thread drawThread = null;
	
	public ErasingTool(ImageEditor editor, Theme theme) {
		super("Erasing Tool", editor, theme, false);	
		
		setLayout(new GridBagLayout());
		
		drawSize = new JSlider(1, 50, 10);
		JLabel drawSizeLabel = new JLabel("Erase Size (10): ", JLabel.RIGHT);
		drawSizeLabel.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 12));
		
		drawSize.addChangeListener(e-> drawSizeLabel.setText("Erase Size (" + drawSize.getValue() + "): "));
		
		add(drawSizeLabel, new ChainGBC(0,0).setFill(false, false).setPadding(5));
		add(drawSize,      new ChainGBC(1,0).setFill(true,  false).setPadding(5).setWidthAndHeight(2, 1));
	}
	
	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {
		if(drawThread != null) 
			drawThread.interrupt();		
	}

	protected void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame) {	
		drawThread = new Thread(new DrawRunnable(layer));
		drawThread.start();
	}
	
	private class DrawRunnable implements Runnable {

		private BufferedImage layer;
		
		public DrawRunnable(BufferedImage layer) {
			this.layer = layer;
		}
		
		public void run() {
			try {
				Graphics2D layerGraphics, clearImageGraphics;
				BufferedImage clearLayer;
				Point mousePoint;
				
				layerGraphics = layer.createGraphics();
				
				layerGraphics.setColor(new Color(0,0,0,0));
				layerGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1.0f));
								
				while(true) {
					Thread.sleep(5);
					
					clearLayer = new BufferedImage(layer.getWidth(), layer.getHeight(), Constants.ALPHA_IMAGE_TYPE);
					clearImageGraphics = clearLayer.createGraphics();
					clearImageGraphics.setColor(Color.WHITE);						
					
					mousePoint = getMousePointOnImage();
					
					if(mousePoint == null)
						continue;
					
					clearImageGraphics.drawRect(mousePoint.x - (drawSize.getValue() / 2), mousePoint.y - (drawSize.getValue() / 2), drawSize.getValue(), drawSize.getValue());
					layerGraphics.fillRect(mousePoint.x - (drawSize.getValue() / 2), mousePoint.y - (drawSize.getValue() / 2), drawSize.getValue(), drawSize.getValue());	
					
					drawClearImageOnImagePreview(clearLayer);
					
					callForRepaint();
				}
				
				
			} catch (InterruptedException e) {}
			
			removeClearImageFromImagePreview();
			logUndoableChange();
		}		
	}
	
	protected void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame) {}

}
