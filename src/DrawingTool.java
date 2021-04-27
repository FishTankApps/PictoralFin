import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;

import com.fishtankapps.pictoralfin.jComponents.JColorChooser;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.LayerButton;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.Constants;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public class DrawingTool extends ImageEditorTool {

	private static final long serialVersionUID = 1098890128878335706L;

	private static final byte CIRCLE = 1;
	private static final byte SQUARE = 2;
	
	private JSlider drawSize;
	private Thread drawThread = null;
	private JButton changeShape, changeColor;
	private DrawPreview drawPreview;
	private JCheckBox drawOverOrReplace;
	
	private Color drawColor = Color.BLACK;
	
	private Theme theme;
	
	private byte drawShape = CIRCLE;
	
	public DrawingTool(ImageEditor editor, Theme theme) {
		super("Drawing Tool", editor, theme, true);	
		this.theme = theme;
		
		setLayout(new GridBagLayout());
		
		drawSize = new JSlider(1, 200, 10);
		JLabel drawSizeLabel = new JLabel("Draw Size (10): ", JLabel.RIGHT);
		drawSizeLabel.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 12));
		
		drawSize.addChangeListener(e-> drawSizeLabel.setText("Draw Size (" + drawSize.getValue() + "): "));
		
		changeShape = new JButton("Draw Shape");
		changeShape.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 10));
		changeShape.setToolTipText("Change between square and circle");
		changeShape.addActionListener(e->{drawShape = (drawShape == CIRCLE) ? SQUARE : CIRCLE; drawPreview.repaint();});
		
		changeColor = new JButton("Draw Color");
		changeColor.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 10));
		changeColor.addActionListener(e-> {
			new Thread(()-> {
				Color color =  JColorChooser.showChooserDialog(theme, drawColor);
				
				if(color != null)
					drawColor = color;
			
				repaint();
			}).start();
		});
		
		drawOverOrReplace = new JCheckBox("Replace Pixels?", false);
		drawOverOrReplace.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 8));
		
		drawPreview = new DrawPreview();
		drawPreview.setBorder(BorderFactory.createTitledBorder("Preview"));
		
		add(drawSizeLabel, new ChainGBC(0,0).setFill(false, false).setPadding(5));
		add(drawSize,      new ChainGBC(1,0).setFill(true,  false).setPadding(5, 20, 5, 5).setWidthAndHeight(2, 1));
		
		add(drawPreview, new ChainGBC(0,1).setFill(true, true).setPadding(5).setWidthAndHeight(1, 2));
		add(changeColor, new ChainGBC(1,1).setFill(false, false).setPadding(5).setWidthAndHeight(2, 1));
		add(changeShape, new ChainGBC(1,2).setFill(false, false).setPadding(5));
		add(drawOverOrReplace, new ChainGBC(2,2).setFill(false, false).setPadding(5));
		
		updateCollapsedState(true);
	}
	
	
	protected void updateCollapsedState(boolean collapsed) {
		changeShape.setVisible(!collapsed);
		changeColor.setVisible(!collapsed);
		drawPreview.setVisible(!collapsed);
		drawOverOrReplace.setVisible(!collapsed);
	}
	
	
	private int[] xCords; // Allow you to draw for approx. 2 minutes.
	private int[] yCords;
	private int cordArrayIndex;
	
	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {
		if(drawThread != null && cordArrayIndex != -1) {
			drawThread.interrupt();
						
			Graphics2D g = layer.createGraphics();
			g.setStroke(new BasicStroke(drawSize.getValue(), (drawShape == CIRCLE) ? BasicStroke.CAP_ROUND : BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
			g.setColor(drawColor);
			
			if(drawOverOrReplace.isSelected())
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, (drawColor.getAlpha() / 255.0f)));
			
			g.drawPolyline(xCords, yCords, cordArrayIndex);	
			
			
			
			logUndoableChange();
			
			cordArrayIndex = -1;
		}
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
				Graphics2D g;
				
				Point oldPoint = getMousePointOnImage();
				Point newPoint;
				
				xCords = new int[5000]; // Allow you to draw for approx. 1 minutes. Only 40 Kilobytes (0.03 floppy disks)
				yCords = new int[5000];				
				
				xCords[0] = oldPoint.x;
				yCords[0] = oldPoint.y;
				cordArrayIndex = 1;
				
				BufferedImage clearLayer;
				Color invertedColor = Utilities.invertColor(drawColor);
				
				while(true) {
					Thread.sleep(5);
					
					clearLayer = new BufferedImage(layer.getWidth(), layer.getHeight(), Constants.ALPHA_IMAGE_TYPE);
					g = clearLayer.createGraphics();
					g.setStroke(new BasicStroke(drawSize.getValue(), (drawShape == CIRCLE) ? BasicStroke.CAP_ROUND : BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
					g.setColor(drawColor);						
					
					newPoint = getMousePointOnImage();
					
					if(newPoint == null || cordArrayIndex == -1)
						continue;
					
					xCords[cordArrayIndex] = newPoint.x;
					yCords[cordArrayIndex] = newPoint.y;
					
					cordArrayIndex++;
					
					g.drawPolyline(xCords, yCords, cordArrayIndex);
					
					g.setColor(invertedColor);
					g.setStroke(new BasicStroke(1));
					g.drawOval(newPoint.x - (drawSize.getValue() / 2), newPoint.y - (drawSize.getValue() / 2), drawSize.getValue(), drawSize.getValue());	
					
					
					drawClearImageOnImagePreview(clearLayer);
					
					callForRepaint();
				}
				
				
			} catch (InterruptedException e) {}
			
			removeClearImageFromImagePreview();
		}		
	}

	private class DrawPreview extends JComponent {

		private static final long serialVersionUID = -2071800696538648567L;

		public void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			
			Graphics2D g = (Graphics2D) graphics;
			g.setPaint(new GradientPaint(0, 0, theme.getPrimaryBaseColor(), DrawPreview.this.getWidth(), DrawPreview.this.getHeight(), theme.getPrimaryHighlightColor(), true));
			
			g.fillRect(0, 0, DrawPreview.this.getWidth(), DrawPreview.this.getHeight());
			
			g.setColor(drawColor);
			
			final int borderWidth = 10;
			
			if(drawShape == CIRCLE) {
				g.fillOval((DrawPreview.this.getWidth() - DrawPreview.this.getHeight() + borderWidth*2) / 2, borderWidth, DrawPreview.this.getHeight() - borderWidth*2, DrawPreview.this.getHeight() - borderWidth*2);
			} else {
				g.fillRect((DrawPreview.this.getWidth() - DrawPreview.this.getHeight() + borderWidth*2) / 2, borderWidth, DrawPreview.this.getHeight() - borderWidth*2, DrawPreview.this.getHeight() - borderWidth*2);
			}
		}
		
	}


	protected void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame) {}

}
