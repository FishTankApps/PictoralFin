import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditorTool;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.LayerButton;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.Theme;

public class LightSpotEffectTool extends ImageEditorTool {
	private static final long serialVersionUID = 4604201034354560301L;


	public LightSpotEffectTool(ImageEditor editor, Theme theme) {
		super("Light Spot Effect", editor, theme, false);
		// TODO Auto-generated constructor stub
	}

	protected void onMousePressed(int clickX, int clickY, BufferedImage layer, Frame frame) {
		final int brightness = 255, darkness = 100, rate = 2;
		
		
		int r, g, b, size = (brightness + darkness) * rate;
		
		Graphics imageGraphics = layer.getGraphics();
		
		Color baseColor = new Color(layer.getRGB(clickX, clickY));
		
		r = baseColor.getRed() - darkness;
		b = baseColor.getBlue() - darkness;
		g = baseColor.getGreen() - darkness;
		
		r = (r < 0) ? 0 : (r > 255) ? 255 : r;
		g = (g < 0) ? 0 : (g > 255) ? 255 : g;
		b = (b < 0) ? 0 : (b > 255) ? 255 : b;
		
		imageGraphics.setColor(new Color(r,g,b));
		imageGraphics.fillRect(0, 0, layer.getWidth(), layer.getHeight());
		
		for(int count = -darkness; count < brightness; count ++){
			r = baseColor.getRed() + count;
			b = baseColor.getBlue() + count;
			g = baseColor.getGreen() + count;
			
			r = (r < 0) ? 0 : (r > 255) ? 255 : r;
			g = (g < 0) ? 0 : (g > 255) ? 255 : g;
			b = (b < 0) ? 0 : (b > 255) ? 255 : b;
			
			size -= rate;
			
			imageGraphics.setColor(new Color(r,g,b));
			imageGraphics.fillOval(clickX - (size/2), clickY - (size/2), size, size);
		}	
		
		logUndoableChange();
	}



	protected void onMouseReleased(int clickX, int clickY, BufferedImage layer, Frame frame) {}


	protected void onLayerSelectionChanged(LayerButton oldFrame, LayerButton newFrame) {}
}
