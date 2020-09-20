package com.fishtankapps.pictoralfin.objectBinders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.fishtankapps.pictoralfin.interfaces.UndoAndRedoable;
import com.fishtankapps.pictoralfin.jComponents.ImageChooser;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;
import com.fishtankapps.pictoralfin.utilities.Constants;
import com.fishtankapps.pictoralfin.utilities.FileUtils;
import com.fishtankapps.pictoralfin.utilities.Utilities;

import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Frame extends UndoAndRedoable<Frame> {

	private static final long serialVersionUID = -6947541644075351604L;
	private static final String IMAGE_OUTPUT_FILE_FORMAT = "PNG";
	
	private transient ArrayList<BufferedImage> layers;
	private transient File imageStash = null;
	public static transient int averageStashAndLoadTime = -1;
	private static transient int stashAndLoadTimeCount = 0;

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
		layers = new ArrayList<>();
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
		//logUndoableChange();
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
	private transient boolean currentlyReadingOrWritingToFile = false;
	
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
					imageStash = FileUtils.createTempFile("StashedFrame", ".frame", "FrameStash", false);
		
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

	
	
	protected void override(Frame previousVersion) {
		this.layers.clear();
		
		for(BufferedImage layer : previousVersion.layers)
			this.layers.add(layer);
		
		this.duration = previousVersion.duration;
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

		
	
	public void exportToFile(PictoralFin pictoralFin) {
		
		Platform.runLater(() -> {
			
				FileChooser fileChooser = new FileChooser();
				
				fileChooser.setInitialDirectory(new File(pictoralFin.getDataFile().getLastOpenedPictureLocation()).getParentFile());
				fileChooser.setTitle("Export Frame To...");
				fileChooser.setInitialFileName("Frame-"+pictoralFin.getTimeLine().getMilliAtFrame(this)+"mls");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Frame File", ".pff"),
														 new ExtensionFilter("All Files", "*"));
				
				File selectedFile = fileChooser.showSaveDialog(null);
				
				if(selectedFile != null) {
					
					String fileName = selectedFile.getName();
					
					if(!fileName.endsWith(".pff")) {
						String[] brokenFileName = fileName.split("\\.");
						
						if(brokenFileName.length >= 2) {
							int result = JOptionPane.showConfirmDialog(null, "The selected file does not have\nthe perfered extension: .pff\n"
									+ "Would you like to rename the file to: " + brokenFileName[0] + ".pff ?", "Rename File?", JOptionPane.YES_NO_CANCEL_OPTION);
						
							if(result == JOptionPane.OK_OPTION) {
								selectedFile = new File(selectedFile.getParent() + "/" + brokenFileName[0] + ".pff");
							} else if (result == JOptionPane.CANCEL_OPTION)
								return;
						}
					}
					
					try {
						ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(selectedFile));
						output.writeObject(this);
						
						output.close();
						
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "There was an error writing file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});		
	}
	
	public void exportToFolder(PictoralFin pictoralFin) {
		Platform.runLater(() -> {
			
			DirectoryChooser directoryChooser = new DirectoryChooser();
			
			directoryChooser.setInitialDirectory(new File(pictoralFin.getDataFile().getLastOpenedPictureLocation()).getParentFile());
			directoryChooser.setTitle("Export Layers To...");
			
			File selectedDirectory = directoryChooser.showDialog(null);
			
			if(selectedDirectory != null) {
				
				if(selectedDirectory.list().length > 0) {
					int result = JOptionPane.showConfirmDialog(null, "The selected folder contains files.\nWould you like to delete these files?", "Clear folder", JOptionPane.YES_NO_CANCEL_OPTION);
				
					if(result == JOptionPane.YES_OPTION) {
						FileUtils.deleteFolder(selectedDirectory);
					} else if (result == JOptionPane.CANCEL_OPTION) {
						return;
					}
				}
				
				selectedDirectory.mkdirs();
				
				try {
					
					int layerIndex = 0;
					for(BufferedImage layer : layers)
						ImageIO.write(layer, "PNG", new File(selectedDirectory.getAbsolutePath() + "/Layer-" + layerIndex++ + ".png"));
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "There was an error writing file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public static void addLayerToFrame(PictoralFin pictoralFin, Frame frame) {
		Platform.runLater(() -> {
				File newLayer = ImageChooser.openFile(pictoralFin);
		
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
			});
	}

	public static void importFrame(PictoralFin pictoralFin) {
		Platform.runLater(() -> {
			File lastOpened = new File(pictoralFin.getDataFile().getLastOpenedPictureLocation());
			FileChooser fileChooser = new FileChooser();		
			
			fileChooser.setTitle("Import Images");
			fileChooser.setInitialDirectory((lastOpened.isDirectory()) ? lastOpened : lastOpened.getParentFile());			
			
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Pictoral Fin Frame File", "*.pff"),
					new ExtensionFilter("All Files", "*"));

			File selectedFile = fileChooser.showOpenDialog(null);
			
			if(selectedFile != null) {
				pictoralFin.getDataFile().setLastOpenedPictureLocation(selectedFile.getAbsolutePath());
				
				try {
					ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(selectedFile));
					Frame frame = (Frame) inputStream.readObject();
					
					inputStream.close();
					
					pictoralFin.getTimeLine().addFrame(frame);
					pictoralFin.repaint();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "There was an error reading file:" + selectedFile.getName() + "\nError:\n" + e.getMessage(), "Error Reading File", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
