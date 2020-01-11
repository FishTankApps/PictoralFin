package objectBinders;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utilities.BufferedImageUtil;

public class Frame {
	private ArrayList<BufferedImage> layers;
	
	/**
	 * Duration of the frame in Milliseconds.
	 */
	private int duration;
	
	public Frame() {
		this(100);
	}
	
	public Frame(int duration) {
		this.duration = duration;
		layers = new ArrayList<>();
	}
	
	public Frame(BufferedImage image) {
		this(image, 100);
	}
	
	public Frame(BufferedImage image, int duration) {
		this.duration = duration;
		
		layers = new ArrayList<>();
		
		if(image.getType() != BufferedImage.TYPE_4BYTE_ABGR)
			layers.add(BufferedImageUtil.setBufferedImageType(image, BufferedImage.TYPE_4BYTE_ABGR));
		else
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
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public BufferedImage getLayer(int index) {
		return layers.get(index);
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void removeLayer(int index) {
		layers.remove(index);
	}
}
