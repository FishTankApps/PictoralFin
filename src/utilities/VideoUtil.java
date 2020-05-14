package utilities;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import customExceptions.CanceledException;
import jComponents.JProgressDialog;
import jComponents.JVideoFileChooser;
import jTimeLine.AudioClip;
import mainFrame.PictoralFin;
import mainFrame.StatusLogger;
import objectBinders.DataFile;

public class VideoUtil {	
	// EXPORTING VIDEO:
	
	private static int stepsCompleted = 0;
	private static int totalSteps = 0;
	private static File outputFile = null;
	
	private static JProgressDialog progressDialog = null;
	
	public static void generateAndSaveVideo(PictoralFin pictoralFin) {
		new Thread(()->{
				try {
					generateAndSaveVideoThreaded(pictoralFin);
				} catch (CanceledException e) {
					StatusLogger.logStatus("Export Canceled");
					
					if(outputFile != null)
						outputFile.delete();
				}
				
			}).start();
	}
	public static void generateAndSaveVideoThreaded(PictoralFin pictoralFin) {
		File videoFile = getFileToExportTo(pictoralFin.getDataFile());

		if (videoFile == null)
			return;

		outputFile = videoFile;
		
		StatusLogger.logStatus("Setting Up Varibles...");
		
		Dimension pictureSize = getOptimalPictureSize(pictoralFin);
		ArrayList<BufferedImage> frames = resizeImages(pictoralFin, pictureSize);
		FFmpegFrameRecorder frameRecorder = new FFmpegFrameRecorder(videoFile, (int) pictureSize.getWidth(),
				(int) pictureSize.getHeight());

		long[] framesDurrationArray = new long[pictoralFin.getTimeLine().numberOfFrame()];
		for (int count = 0; count < framesDurrationArray.length; count++)
			framesDurrationArray[count] = pictoralFin.getTimeLine().getFrames()[count].getDuration();

		try {		
			StatusLogger.logStatus("Applying Video Settings...");
			long shortestframeDurration = Utilities.findGCDofArray(framesDurrationArray);
			double framesPerSecond = 1000.0 / shortestframeDurration;		
			
			// Set Up Video Output
			String videoExtension = videoFile.getName().split("\\.")[1];
			frameRecorder.setVideoCodec((videoExtension.equals("mp4")) ? 13 : 22);
			frameRecorder.setFormat(videoExtension);
			frameRecorder.setFrameRate(framesPerSecond);
			frameRecorder.setVideoQuality(1.0);

			// Set Up Audio Output
			AudioClip[] clips = pictoralFin.getTimeLine().getAudioClips();
			if (clips != null && clips.length > 0) {
				frameRecorder.setSampleRate(44100);
				frameRecorder.setAudioBitrate(128000);
				frameRecorder.setAudioChannels(2);
				frameRecorder.setAudioQuality(1.0);
			}

			frameRecorder.start();		
			
			// Set Up JProgressDialog
			StatusLogger.logStatus("Estimating Work Load...");
			
			stepsCompleted = 0;
			totalSteps = 0;
			int framesToWrite = getImageFramesToWrite(frames, framesDurrationArray, shortestframeDurration, framesPerSecond);
			totalSteps += framesToWrite;
			totalSteps += getAudioFramesToWrite(frameRecorder, clips, framesToWrite);
			
			progressDialog = new JProgressDialog("Exporting Project... " + JProgressDialog.PERCENT, "Writing to Video File...", totalSteps);
			
			StatusLogger.logStatus("Writting Data... (0/" + totalSteps+")");
			
			// Write Data
			writeImages(frameRecorder, frames, framesDurrationArray, shortestframeDurration, framesPerSecond);
			writeAudio(frameRecorder, clips, framesToWrite);

			StatusLogger.logStatus("Saving File... (" + ++stepsCompleted + "/" + totalSteps+")");
			frameRecorder.stop();
			
			progressDialog.close();
			progressDialog = null;
			outputFile = null;
			
			StatusLogger.logStatus("Export Finished!");
		} catch (Exception e) {
			if(e instanceof CanceledException)
				throw (CanceledException) e;
			
			JOptionPane.showMessageDialog(null, "There was error exporting the video.\n" + e.getMessage(),
					"Error Exporting", JOptionPane.ERROR_MESSAGE);
			System.out.println("Error Exporting Video:");
			e.printStackTrace();
		}

	}

	private static File getFileToExportTo(DataFile dataFile) {
		JVideoFileChooser jfc = new JVideoFileChooser();

		jfc.setDialogTitle("Choose File to Export to:");
		jfc.setApproveButtonText("Export");
		jfc.setCurrentDirectory(new File(dataFile.getLastOpenVideoLocation()));

		if (jfc.showOpenDialog(null) == JVideoFileChooser.CANCEL_OPTION) {
			return null;
		}

		File videoFile = jfc.getSelectedFile();
		String outputPath = videoFile.getAbsolutePath();

		// TODO - Fix this:
		if (!outputPath.split("\\.")[1].equals(jfc.getSelectedVideoFormat())) {
			String message = " The file " + videoFile.getName() + " does not have the right extension ("
					+ jfc.getSelectedVideoFormat() + ")\n Would you like to renamed the file to:\n"
					+ videoFile.getName().split("\\.")[0] + "." + jfc.getSelectedVideoFormat();
			if (JOptionPane.showConfirmDialog(null, message, "File Extension Error", JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
				outputPath = outputPath.split("\\.")[0];
				outputPath += "." + jfc.getSelectedVideoFormat();
			}
		}

		videoFile = new File(outputPath);

		if (videoFile.exists())
			if (JOptionPane.showConfirmDialog(null,
					"The file " + videoFile.getName() + " already exists.\nDo you want to replace it?",
					"Do You Want to?", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return null;

		dataFile.setLastOpenVideoLocation(outputPath);
		return videoFile;
	}

	private static Dimension getOptimalPictureSize(PictoralFin pictoralFin) {
		int width = 0, height = 0;

		for (objectBinders.Frame f : pictoralFin.getTimeLine().getFrames()) {
			if (f.getLayer(0).getWidth() > width)
				width = f.getLayer(0).getWidth();

			if (f.getLayer(0).getHeight() > height)
				height = f.getLayer(0).getHeight();
		}

		return new Dimension(width, height);
	}

	private static ArrayList<BufferedImage> resizeImages(PictoralFin pictoralFin, Dimension newSize) {
		ArrayList<BufferedImage> resizedImages = new ArrayList<>();

		for (objectBinders.Frame f : pictoralFin.getTimeLine().getFrames()) {
			BufferedImage i = new BufferedImage((int) newSize.getWidth(), (int) newSize.getHeight(), 6);
			BufferedImage frame = f.getLayer(0);
			i.getGraphics().drawImage(frame, i.getWidth() / 2 - frame.getWidth() / 2,
					i.getHeight() / 2 - frame.getHeight() / 2, frame.getWidth(), frame.getHeight(), null);

			resizedImages.add(i);
		}

		return resizedImages;
	}

	private static void writeImages(FFmpegFrameRecorder frameRecorder, ArrayList<BufferedImage> images,
			long[] frameDurrations, long shortestframeDurration, double framesPerSecond) throws Exception {
		StatusLogger.logStatus("Recording Images... (" + stepsCompleted + "/" + totalSteps+")");
		
		Java2DFrameConverter converter = new Java2DFrameConverter();
		int count = 0;
		for (BufferedImage image : images) {
			for (int loop = 0; loop < (frameDurrations[count] / shortestframeDurration); loop++) {
				frameRecorder.record(converter.convert(BufferedImageUtil.setBufferedImageType(image, Constants.IMAGE_TYPE)));
				StatusLogger.logStatus("Recording Images... (" + ++stepsCompleted + "/" + totalSteps+")");
				progressDialog.moveForward();
			}
			count++;
		}
	}

	private static void writeAudio(FFmpegFrameRecorder frameRecorder, AudioClip[] audioClips, int imageFrameCount) {
		if (audioClips == null || audioClips.length == 0)
			return;
		
		for (AudioClip audioClip : audioClips) {
			try {
				File audioFile = AudioUtil.convertAudioFileToMP3(audioClip.getAudioFile());
				if (audioFile == null)
					break;

				FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(audioFile);
				int numOfFrames = getAudioFrameCounts(frameGrabber);
				double framesPerSecond = (double) numOfFrames / (frameGrabber.getLengthInTime() / 1_000_000);
				double audioFramesPerImageFrames = framesPerSecond / frameRecorder.getFrameRate();

				frameGrabber.start();
				for (int audioCount = 0; audioCount < imageFrameCount * audioFramesPerImageFrames; audioCount++) {
					Frame audioFrame = frameGrabber.grab();
					if (audioFrame != null) {
						frameRecorder.record(audioFrame);
					}
					StatusLogger.logStatus("Recording Audio... (" + ++stepsCompleted + "/" + totalSteps+")");
					progressDialog.moveForward();
				}

				frameGrabber.stop();
			} catch (Exception e) {
				System.out.println("Empty Catch Block: VideoUtil.writeAudio()");
				e.printStackTrace();
			}
		}
	}
	
	private static int getImageFramesToWrite(ArrayList<BufferedImage> images, long[] frameDurrations, 
			long shortestframeDurration, double framesPerSecond) {
			int count = 0;
			int frameCount = 0;
			for (@SuppressWarnings("unused") BufferedImage image : images) {
				for (int loop = 0; loop < (frameDurrations[count] / shortestframeDurration); loop++) {
					frameCount++;
				}
				count++;
			}
			return frameCount;
	}
	
	private static int getAudioFramesToWrite(FFmpegFrameRecorder frameRecorder, AudioClip[] audioClips, int imageFrameCount) {
		if (audioClips == null || audioClips.length == 0)
			return 0;
		
		int count = 0;
		
		for (AudioClip audioClip : audioClips) {
			try {
				File audioFile = AudioUtil.convertAudioFileToMP3(audioClip.getAudioFile());
				if (audioFile == null)
					break;

				FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(audioFile);
				int numOfFrames = getAudioFrameCounts(frameGrabber);
				double framesPerSecond = (double) numOfFrames / (frameGrabber.getLengthInTime() / 1_000_000);
				double audioFramesPerImageFrames = framesPerSecond / frameRecorder.getFrameRate();

				count += imageFrameCount * audioFramesPerImageFrames;

				frameGrabber.stop();
			} catch (Exception e) {
				System.out.println("Empty Catch Block: VideoUtil.getAudioFramesToWrite()");
				e.printStackTrace();
			}
		}
		
		return count;
	}

	private static int getAudioFrameCounts(FFmpegFrameGrabber audioGrabbers) {
		Frame frame;
		int count = 0;

		try {
			audioGrabbers.restart();
			do {
				frame = audioGrabbers.grab();
				count++;
			} while (frame != null);

			audioGrabbers.restart();
		} catch (Exception e) {
			System.out.println("Empty Catch Block: VideoUtil.getAudioFrameCount()");
			e.printStackTrace();
		}

		return count;
	}

	
	// IMPORTING VIDEO:
	public static ArrayList<BufferedImage> videoToPictures(String mp4Path) {
		StatusLogger.logStatus("Openning Video File...");
		Java2DFrameConverter converter = new Java2DFrameConverter();
		FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(mp4Path);
		ArrayList<BufferedImage> toReturn;
		Frame frame;

		try {
			
			// TODO: Get this working
			int count = 1;
			frameGrabber.start();
			toReturn = new ArrayList<BufferedImage>(frameGrabber.getLengthInFrames());
			
			System.out.println("Num Of Frames: " + frameGrabber.getLengthInFrames());
			System.out.println("Frames Rate:   " + frameGrabber.getFrameRate());
			
			for (int i = 0; i < frameGrabber.getLengthInFrames(); i += frameGrabber.getFrameRate()) {
				frame = frameGrabber.grabImage();
				
				BufferedImage bi = converter.convert(frame);
				toReturn.add(bi);			
				
				StatusLogger.logStatus("Getting Frames... (" + count++ + "/" + frameGrabber.getLengthInFrames() + ")");
			}
			
			frameGrabber.stop();
			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
