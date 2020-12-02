package com.fishtankapps.pictoralfin.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.fishtankapps.pictoralfin.jComponents.JProgressDialog;
import com.fishtankapps.pictoralfin.jTimeLine.AudioClip;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.objectBinders.MediaFileType;

public class FileImporter {

	private FileImporter() {}
	
	private static PictoralFin pictoralFin = null;
	
	public static void passPictoralFin(PictoralFin pictoralFin) {
		FileImporter.pictoralFin = pictoralFin;
	}
	
	
	public static void importFiles(String... filePaths) {
		ArrayList<File> filesList = new ArrayList<>(filePaths.length);

		for(String filePath : filePaths)
			filesList.add(new File(filePath));
		
		importFiles(filesList);
	}
	
	public static void importFiles(File... files) {
		ArrayList<File> filesList = new ArrayList<>(files.length);

		for(File file : files)
			filesList.add(file);
		
		importFiles(filesList);
	}
	
	public static void importFiles(Collection<File> files) {
		
		int failedCount = 0, importedFilesCount = 0, numOfFrames, frameCount;
		
		String errorMessages = "";
		
		double widthRatio, heightRation, ratio;
		BufferedImage image;
		
		JProgressDialog progressDialog = new JProgressDialog("Importing Files - " + JProgressDialog.PERCENT, 
				"Importing Files...", getNumberOfSteps(files));
		
		for(File fileToImport : files) {	
			MediaFileType fileType = FileUtils.getMediaFileType(fileToImport);
					
			StatusLogger.logPrimaryStatus("Importing File(s): (" + importedFilesCount++ + "/" + files.size() + ")          ");
			
			
			
			
			if(fileType == MediaFileType.NONE) {
				errorMessages += "\n-" + fileToImport.getName() + " (Not a image/video/audio/ffmpeg compatible)"; failedCount++;
				progressDialog.moveForward();
			}
			
			
			
			else if(fileType == MediaFileType.FRAME) {
				try {
					StatusLogger.logSecondaryStatus("Importing Frame");
					ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileToImport));
					Frame frame = (Frame) input.readObject();
					
					input.close();
					
					pictoralFin.getTimeLine().addFrame(frame);
				} catch (FileNotFoundException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (File does not exist)"; failedCount++;	
				} catch (IOException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Error reading file - outdated? corrupt?)"; failedCount++;	
				} catch (ClassNotFoundException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Error openning file - outdated? PictoralFin broke?)"; failedCount++;	
				} catch (Exception e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Unexpected Error: " + e.getMessage() + " )"; failedCount++;	
				}
				
				progressDialog.moveForward();
			}
			
			
			
			
			else if(fileType == MediaFileType.IMAGE) {
				try {
					StatusLogger.logSecondaryStatus("Importing Image");
					image = ImageIO.read(fileToImport);
					
					widthRatio = ((double) image.getWidth()) / pictoralFin.getConfiguration().getMaxPictureSize().getWidth();
					heightRation = ((double) image.getHeight()) / pictoralFin.getConfiguration().getMaxPictureSize().getHeight();
					
					if(image.getWidth() > pictoralFin.getConfiguration().getMaxPictureSize().getWidth() || image.getHeight() > pictoralFin.getConfiguration().getMaxPictureSize().getHeight()) {
						ratio = (widthRatio > heightRation) ? widthRatio : heightRation;								
						image = BufferedImageUtil.resizeBufferedImage(image, (int) (image.getWidth() / ratio), (int) (image.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
					}
					
					if(image == null)
						throw new Exception("No Image - corrupt?");
					
					pictoralFin.getTimeLine().addFrame(image);		
				} catch (FileNotFoundException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (File does not exist)"; failedCount++;
				} catch (IOException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Error reading file - missing? outdated? corrupt?)"; failedCount++;
				} catch (Exception e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Unexpected Error: " + e.getMessage() + " )"; failedCount++;
				}
			
				progressDialog.moveForward();
			}
			
			
			
			
			else if(fileType == MediaFileType.AUDIO) {
				try {
					StatusLogger.logSecondaryStatus("Importing Audio");
					pictoralFin.getTimeLine().addAudioClip(new AudioClip(fileToImport, pictoralFin.getTimeLine()));
				} catch (NullPointerException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Empty File - does not exist? corrupt?)"; failedCount++;
				} catch (RuntimeException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Error reading file - " + e.getMessage() + ")"; failedCount++;
				} catch (Exception e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Unexpected Error: " + e.getMessage() + " )"; failedCount++;
				}
			
				progressDialog.moveForward();
			}
			
			
			
			
			else if(fileType == MediaFileType.VIDEO) {
				try {					
					frameCount = 0;
					StatusLogger.logSecondaryStatus("Importing Video - Extracting Audio");
					File audioFile = VideoUtil.extractAudioFromVideo(fileToImport);
					
					progressDialog.moveForward();
					
					StatusLogger.logSecondaryStatus("Importing Video - Importing Audio");
					if(audioFile != null) {
						AudioClip audioClip = new AudioClip(audioFile, pictoralFin.getTimeLine());
						audioClip.setName(fileToImport.getName() + " Audio");
						audioClip.setLength(audioClip.getMaxLength());
						pictoralFin.getTimeLine().addAudioClip(audioClip);	
					}
					
					progressDialog.moveForward();

					File[] images = VideoUtil.videoToImageFiles(fileToImport.getAbsolutePath());
					numOfFrames = images.length;
					int frameDurration = VideoUtil.getVideoFramesPerSecond(fileToImport.getAbsolutePath());
					
					for(File imageFile : images) {
						try {
							StatusLogger.logSecondaryStatus("Importing Frames - (" + frameCount++ + "/" + numOfFrames + ")");
							image = ImageIO.read(imageFile);
							
							if(image == null)
								continue;
							
							widthRatio = ((double) image.getWidth()) / pictoralFin.getConfiguration().getMaxPictureSize().getWidth();
							heightRation = ((double) image.getHeight()) / pictoralFin.getConfiguration().getMaxPictureSize().getHeight();
							
							if(image.getWidth() > pictoralFin.getConfiguration().getMaxPictureSize().getWidth() || image.getHeight() > pictoralFin.getConfiguration().getMaxPictureSize().getHeight()) {
								ratio = (widthRatio > heightRation) ? widthRatio : heightRation;								
								image = BufferedImageUtil.resizeBufferedImage(image, (int) (image.getWidth() / ratio), (int) (image.getHeight() / ratio), BufferedImage.SCALE_SMOOTH);
							}
							
							pictoralFin.getTimeLine().addFrame(new Frame(image, frameDurration));
							progressDialog.moveForward();
							
						} catch (Exception e) {
							System.out.println("Empty Catch Block VideoImporter.importVideo(File[]): ");
							e.printStackTrace();
						}				
					}
				} catch (NullPointerException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Empty File - does not exist? corrupt?)"; failedCount++;
				} catch (RuntimeException e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Error reading file - " + e.getMessage() + ")"; failedCount++;
				} catch (Exception e) {
					errorMessages += "\n-" + fileToImport.getName() + " (Unexpected Error: " + e.getMessage() + " )"; failedCount++;
				}
			
				progressDialog.moveForward();
			}
							
		} // End of File Loop
		
		progressDialog.close();
		
		if(failedCount > 0) {
			JOptionPane.showMessageDialog(null, "There was an errors importing the following file(s):\n"
					+ errorMessages, "Error Importing " + failedCount + " File(s)", JOptionPane.ERROR_MESSAGE);
		}
		
		StatusLogger.logPrimaryStatus((files.size() - failedCount) + " out of " + files.size() + " File(s) Imported!");
	}
	
	private static int getNumberOfSteps(Collection<File> files) {
		
		int stepCount = 0;
		
		for(File file : files) {
			if(FileUtils.getMediaFileType(file) != MediaFileType.VIDEO)
				stepCount++;
			else {
				stepCount += 3; // Extract Audio = 1, Import Audio = 1, Extract Frames = 1
				stepCount += VideoUtil.getVideoFrameCount(file.getAbsolutePath());
			}
		}
		
		return stepCount;
	}
}
