package objectBinders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import jComponents.JFileChooserWithImagePreview;
import mainFrame.PictoralFin;
import utilities.BufferedImageUtil;
import utilities.Constants;
import utilities.FileUtils;
import utilities.Utilities;

public class Frame implements Serializable {

	private static final long serialVersionUID = -6947541644075351604L;
	private static final String IMAGE_OUTPUT_FILE_FORMAT = "PNG";
	
	private transient ArrayList<BufferedImage> layers;
	private transient ArrayList<File> history;
	private transient File imageStash = null;
	public static transient int averageStashAndLoadTime = -1;
	private static transient int stashAndLoadTimeCount = 0;

	private transient int currentVersion = -1;

	/**
	 * Duration of the frame in Milliseconds.
	 */
	private long duration;

	public Frame() {
		this(100);
		new Thread(()->logUndoableChange()).start();
	}

	public Frame(long duration) {
		this.duration = duration;
		layers = new ArrayList<>(2);
		history = new ArrayList<>();
	}

	public Frame(BufferedImage image) {
		this(image, 100);
		new Thread(()->logUndoableChange()).start();
	}

	public Frame(BufferedImage image, int duration) {
		this(duration);

		layers.add(BufferedImageUtil.setBufferedImageType(image, Constants.IMAGE_TYPE));
		new Thread(()->logUndoableChange()).start();
	}

	public int getNumberOfLayers() {
		return layers.size();
	}

	public boolean isEmpty() {
		return layers.isEmpty();
	}

	public void addLayer(BufferedImage layer) {
		layers.add(BufferedImageUtil.setBufferedImageType(layer, Constants.IMAGE_TYPE));
	
		logUndoableChange();
	}

	public void setDuration(long duration) {
		this.duration = duration;
		logUndoableChange();
	}

	public BufferedImage getLayer(int index) {
		return layers.get(index);
	}

	public ArrayList<BufferedImage> getLayers() {
		return layers;
	}

	public long getDuration() {
		return duration;
	}

	public void removeLayer(int index) {
		layers.remove(index);
		logUndoableChange();
	}

	@Deprecated
	private boolean currentlyReadingOrWritingToFile = false;
	
	@Deprecated
	public void stashImages() {		
		if (imageStash != null)
			return;
		
		new Thread(() -> {
				try {					
					while(currentlyReadingOrWritingToFile) Utilities.doNothing();
					
					if (imageStash != null)
						return;
					
					currentlyReadingOrWritingToFile = true;
					imageStash = FileUtils.createTempFile("StashedFrame", ".frame");
		
					ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(imageStash));
					fileOutput.writeInt(layers.size());
		
					new Thread(() -> {
						if(layers.size() == 0)
							return;
						
						BufferedImage thumbnail = BufferedImageUtil.resizeBufferedImage(layers.get(0), 20, 20,
								BufferedImage.SCALE_FAST);
						
						while(currentlyReadingOrWritingToFile) Utilities.doNothing();				
						layers.add(thumbnail);				
					}).start();
					
					for (BufferedImage eachImage : layers) {
						ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						ImageIO.write(eachImage, IMAGE_OUTPUT_FILE_FORMAT, buffer);
						
						buffer.flush();				
						fileOutput.writeInt(buffer.size());
						buffer.writeTo(fileOutput);
					}
					
					layers.clear();			
					currentlyReadingOrWritingToFile = false;					
					
					fileOutput.close();
				} catch (Exception e) {
					System.err.println("Error Stashing Images");
					e.printStackTrace();
				}
			}).start();
	}

	@Deprecated
	public void loadImagesUnThreaded() {
		if (imageStash == null)
			return;


		try {
			while(currentlyReadingOrWritingToFile) Utilities.doNothing();
			
			if (imageStash == null)
				return;
			
			currentlyReadingOrWritingToFile = true;
			
			long startTime = System.currentTimeMillis();
			
			ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(imageStash));

			layers.clear();

			final int imageCount = fileInput.readInt();
			layers = new ArrayList<BufferedImage>(imageCount);
			for (int i = 0; i < imageCount; i++) {
				int size = fileInput.readInt();

				byte[] buffer = new byte[size];
				fileInput.readFully(buffer);

				layers.add(ImageIO.read(new ByteArrayInputStream(buffer)));
			}

			fileInput.close();

			imageStash = null;
			currentlyReadingOrWritingToFile = false;
			
			if(averageStashAndLoadTime == -1) {
				averageStashAndLoadTime = (int) (System.currentTimeMillis() - startTime);
				stashAndLoadTimeCount = 1;
			} else {
				averageStashAndLoadTime = ((averageStashAndLoadTime * stashAndLoadTimeCount) + 
						(int) (System.currentTimeMillis() - startTime)) / ++stashAndLoadTimeCount;
			}
			
		} catch (Exception e) {
			System.err.println("Error Loading Images");
			e.printStackTrace();
		}

	}
	
	@Deprecated
	public void loadImages() {
		if (imageStash == null)
			return;

		new Thread(() -> {
				try {
					while(currentlyReadingOrWritingToFile) Utilities.doNothing();
					
					if (imageStash == null)
						return;
					
					currentlyReadingOrWritingToFile = true;
					
					long startTime = System.currentTimeMillis();
					
					ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(imageStash));
		
					layers.clear();
		
					final int imageCount = fileInput.readInt();
					layers = new ArrayList<BufferedImage>(imageCount);
					for (int i = 0; i < imageCount; i++) {
						int size = fileInput.readInt();
		
						byte[] buffer = new byte[size];
						fileInput.readFully(buffer);
		
						layers.add(ImageIO.read(new ByteArrayInputStream(buffer)));
					}
		
					fileInput.close();
		
					imageStash = null;
					currentlyReadingOrWritingToFile = false;
					
					if(averageStashAndLoadTime == -1) {
						averageStashAndLoadTime = (int) (System.currentTimeMillis() - startTime);
						stashAndLoadTimeCount = 1;
					} else {
						averageStashAndLoadTime = ((averageStashAndLoadTime * stashAndLoadTimeCount) + 
								(int) (System.currentTimeMillis() - startTime)) / ++stashAndLoadTimeCount;
					}
					
				} catch (Exception e) {
					System.err.println("Error Loading Images");
					e.printStackTrace();
				}
			}).start();
	}

	public void logUndoableChange() {
		
		while (currentVersion + 1 < history.size())
			history.remove(history.size() - 1);
		
		currentVersion++;
		
		File temp = FileUtils.createTempFile("UndoHistoryFrame(v_" + currentVersion + ")", ".frame");
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp));
			out.writeObject(this);			
			out.close();
			
			history.add(temp);
		} catch (Exception e) {
			System.err.println("Error saving frame to file: ");
			e.printStackTrace();
		}	
	}

	public boolean undo() {
		if(currentVersion == 0)
			return false;
			
		try {
			ObjectInputStream out = new ObjectInputStream(new FileInputStream((history.get(--currentVersion))));
			Frame previousVersion = (Frame) out.readObject();			
			out.close();

			layers.clear();
			
			for(BufferedImage layer : previousVersion.getLayers())			
				layers.add(layer);
			
			this.duration = previousVersion.duration;
			
			return true;
		} catch (Exception e) {
			System.err.println("Frame.undo() - Error recalling frame from file: ");
			e.printStackTrace();
		}	
		
		return false;
	}

	public boolean redo() {
		if(currentVersion == history.size() - 1)
			return false;
		
		try {
			ObjectInputStream out = new ObjectInputStream(new FileInputStream((history.get(++currentVersion))));
			Frame previousVersion = (Frame) out.readObject();			
			out.close();

			layers.clear();
			
			for(BufferedImage layer : previousVersion.getLayers())			
				layers.add(layer);
			
			this.duration = previousVersion.duration;
			
			return true;
		} catch (Exception e) {
			System.err.println("Frame.redo() - Error recalling frame from file: ");
			e.printStackTrace();
		}	
				
		return true;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeInt(layers.size());
		for (BufferedImage eachImage : layers) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			ImageIO.write(eachImage, IMAGE_OUTPUT_FILE_FORMAT, buffer);
			buffer.flush();
			
			out.writeInt(buffer.size());

			buffer.writeTo(out);
		}
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final int imageCount = in.readInt();
		layers = new ArrayList<BufferedImage>(imageCount);
		for (int i = 0; i < imageCount; i++) {
			int size = in.readInt(); // Read byte count

			byte[] buffer = new byte[size];
			in.readFully(buffer); // Make sure you read all bytes of the image

			layers.add(ImageIO.read(new ByteArrayInputStream(buffer)));
		}
	}

	public static void addLayerToFrame(PictoralFin pictoralFin, Frame frame) {
		File newLayer = JFileChooserWithImagePreview.openFile(pictoralFin);

		if (newLayer != null) {
			try {
				double width, height, ratio;
				BufferedImage image;
				image = ImageIO.read(newLayer);

				width = image.getWidth() / pictoralFin.getSettings().getMaxPictureSize().getWidth();
				height = image.getHeight() / pictoralFin.getSettings().getMaxPictureSize().getHeight();

				if (image.getWidth() > pictoralFin.getSettings().getMaxPictureSize().getWidth()
						&& image.getHeight() > pictoralFin.getSettings().getMaxPictureSize().getHeight()) {
					ratio = (width > height) ? width : height;
					image = BufferedImageUtil.resizeBufferedImage(image, (int) (image.getWidth() / ratio),
							(int) (image.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
				}

				frame.addLayer(image);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error adding layer from file: " + newLayer.getName()
						+ "\nException Message: \n" + e.getMessage(), "Error Adding Layer", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
