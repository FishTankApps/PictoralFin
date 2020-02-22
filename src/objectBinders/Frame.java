package objectBinders;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Frame {
	private ArrayList<BufferedImage> layers;
	
	/**
	 * Duration of the frame in Milliseconds.
	 */
	private long duration;
	
	public Frame() {
		this(100);
	}
	
	public Frame(long duration) {
		this.duration = duration;
		layers = new ArrayList<>(2);
	}
	
	public Frame(BufferedImage image) {
		this(image, 100);
	}
	
	public Frame(BufferedImage image, int duration) {
		this(duration);		
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
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public BufferedImage getLayer(int index) {
		return layers.get(index);
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void removeLayer(int index) {
		layers.remove(index);
	}
}
