package objectBinders;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Frame implements Serializable {
	
	private static final long serialVersionUID = -6947541644075351604L;

	private transient ArrayList<BufferedImage> layers;
	
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
	
	public ArrayList<BufferedImage> getLayers(){
		return layers;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void removeLayer(int index) {
		layers.remove(index);
	}


	private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(layers.size()); // how many images are serialized?
        for (BufferedImage eachImage : layers) {
            ImageIO.write(eachImage, "png", out); // png is lossless
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCount = in.readInt();
        layers = new ArrayList<BufferedImage>(imageCount);
        for (int i=0; i<imageCount; i++) {
        	layers.add(ImageIO.read(in));
        }
    }
}
