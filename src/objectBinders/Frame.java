package objectBinders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import globalValues.Constants;

public class Frame {
	private ArrayList<Image> layers;
	
	public Frame(Image image) {
		layers = new ArrayList<>();
		layers.add(image);
	}
	
	public int getNumberOfLayers() {
		return layers.size();
	}
	
	public boolean isEmpty() {
		return layers.isEmpty();
	}
	
	public void addLayer(BufferedImage layer) {
		layers.add(layer);
	}
	
	public Image getLayer(int index) {
		return layers.get(index);
	}
	
	public void removeLayer(int index) {
		layers.remove(index);
	}
	
	public BufferedImage getThumbnail(boolean highlight, int size) {
		if(layers.isEmpty())
			return null;
		
		int overlaySize = size / 5;
		
		BufferedImage thumbnail = new BufferedImage(size, size + ((layers.size() - 1) * overlaySize), Constants.PNG);
		Graphics g = thumbnail.getGraphics();
		
		for(int count = layers.size() - 1; count >= 0; count++) {
			g.drawImage(layers.get(0), 0, (overlaySize * count), size, size, null);
		}	
		
		g.dispose();
		return thumbnail;
	}
}
