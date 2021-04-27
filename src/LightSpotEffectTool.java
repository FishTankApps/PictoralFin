import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
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

public class LightSpotEffectTool extends ImageEditorTool {
	private static final long serialVersionUID = 4604201034354560301L;

	private JSlider startingBrightness, endingBrightness, rate;
	private int lastClickX = -1, lastClickY = -1;
	private BufferedImage layer = null;
	private Color color;
	
	private static long changeLogNumber = 0;
	
	public LightSpotEffectTool(ImageEditor editor, Theme theme) {
		super("Light Spot Effect", editor, theme, false);
		setLayout(new GridBagLayout());
		
		
		JLabel startBrightnessLabel = new JLabel("Starting Brightness (100.0%): ", JLabel.RIGHT);
		JLabel endBrightnessLabel = new JLabel("Ending Brightness (0): ", JLabel.RIGHT);
		JLabel rateLabel = new JLabel("Rate (2.0): ", JLabel.RIGHT);
		
		startingBrightness = new JSlider(0, 510, 510);
		endingBrightness   = new JSlider(0, 510, 0);
		rate = new JSlider(1, 100, 20);	
		
		startingBrightness.addChangeListener(e->{
			startBrightnessLabel.setText("Starting Brightness (" + String.format("%.1f", ((startingBrightness.getValue() - 255) / 255.0)*100) + "%): ");
			updateImage();
		});
		endingBrightness.addChangeListener(e->{
			endBrightnessLabel.setText("Ending Brightness (" + String.format("%.1f", (endingBrightness.getValue() / 510.0)*100) + "%): ");
			updateImage();
		});
		rate.addChangeListener(e->{
			rateLabel.setText("Rate (" +  String.format("%.1f", rate.getValue() / 10.0) + "): ");
			updateImage();
		});
		
		add(startBrightnessLabel, new ChainGBC(0,0).setFill(false));
		add(startingBrightness,   new ChainGBC(1,0).setFill(true, false));
		add(endBrightnessLabel,   new ChainGBC(0,1).setFill(false));
		add(endingBrightness,     new ChainGBC(1,1).setFill(true, false));
		add(rateLabel,            new ChainGBC(0,2).setFill(false));
		add(rate,                 new ChainGBC(1,2).setFill(true, false));
	}

	private void updateImage() {
		if(layer == null)
			return;
		
		int brightness = startingBrightness.getValue() - 255;
		int darkness = 255 - endingBrightness.getValue();
		double rate = this.rate.getValue() / 10.0;
		
		double size = (brightness + darkness) * rate;
		
		int r, g, b;
		
		Graphics imageGraphics = layer.getGraphics();
		
		r = color.getRed() - darkness;
		b = color.getBlue() - darkness;
		g = color.getGreen() - darkness;
		
		r = (r < 0) ? 0 : (r > 255) ? 255 : r;
		g = (g < 0) ? 0 : (g > 255) ? 255 : g;
		b = (b < 0) ? 0 : (b > 255) ? 255 : b;
		
		imageGraphics.setColor(new Color(r,g,b));
		imageGraphics.fillRect(0, 0, layer.getWidth(), layer.getHeight());
		
		for(int count = -darkness; count < brightness; count++){
			r = color.getRed() + count;
			b = color.getBlue() + count;
			g = color.getGreen() + count;
			
			r = (r < 0) ? 0 : (r > 255) ? 255 : r;
			g = (g < 0) ? 0 : (g > 255) ? 255 : g;
			b = (b < 0) ? 0 : (b > 255) ? 255 : b;
			
			size -= rate;
			
			imageGraphics.setColor(new Color(r,g,b));
			imageGraphics.fillOval((int) (lastClickX - (size/2)), (int) (lastClickY - (size/2)), (int) size, (int) size);
		}	
		
		callForRepaint();
		
		new Thread(()-> {
			long logNumber = ++changeLogNumber;
			
			try {
				Thread.sleep(Constants.LIGHT_SPOT_EFFECT_WAIT_TIME);
				
				if(logNumber == changeLogNumber)
					logUndoableChange();
			} catch (Exception e) {}
		}).start();
		
	}
	
	protected void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame) {
		lastClickX = clickX;
		lastClickY = clickY;
		this.layer = layer;
		
		color = new Color(layer.getRGB(lastClickX, lastClickY));
		
		updateImage();
	}



	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {}


	protected void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame) {
		layer = null;
	}
}
