package com.fishtankapps.pictoralfin.utilities;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.fishtankapps.pictoralfin.customExceptions.CanceledException;
import com.fishtankapps.pictoralfin.jComponents.JProgressDialog;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.DataFile;
import com.fishtankapps.pictoralfin.objectBinders.RawAudioFile;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class VideoUtil {
	// EXPORTING VIDEO:

	public static File ffmpegExeicutable = new File("resources/ffmpeg.exe");
	public static File ffprobeExeicutable = new File("resources/ffprobe.exe");
	
	private static File outputFile = null;

	public static void generateAndSaveVideo(PictoralFin pictoralFin) {
		
		Platform.runLater(() -> {
			try {
				generateAndSaveVideoThreaded(pictoralFin);
			} catch (CanceledException e) {
				StatusLogger.logPrimaryStatus("Export Canceled");

				if (outputFile != null)
					outputFile.delete();
			}

		});
	}

	public static void generateAndSaveVideoThreaded(PictoralFin pictoralFin) {
		
		File videoFile = getFileToExportTo(pictoralFin.getConfiguration().getDataFile());

		if (videoFile == null)
			return;

		outputFile = videoFile;
		
		// Assignment: + 10  points for resizing images,
		//             + 2   points for duration calculations 
		//             + 10  points for generating sound file
        //             + 100 points for exporting images (1 per percent)
        //             + 100 points for executing FFmpeg (1 per percent)
		//          -------------------------------------------------------
		//             = 222 points total
		JProgressDialog progressDialog = new JProgressDialog("Exporting - " + JProgressDialog.PERCENT, "Exporting Project to File...", 222);
		int previousPercent = 0;
		int newPercent = 0;
		
		StatusLogger.logPrimaryStatus("Resizing Images...");
		Dimension pictureSize = getOptimalPictureSize(pictoralFin);		
		ArrayList<BufferedImage> frames = resizeImages(pictoralFin, pictureSize);		
		progressDialog.moveForward(10);
		
		StatusLogger.logPrimaryStatus("Getting Frame Durrations...");
		long[] framesDurrationArray = new long[pictoralFin.getTimeLine().numberOfFrame()];
		for (int count = 0; count < framesDurrationArray.length; count++)
			framesDurrationArray[count] = pictoralFin.getTimeLine().getFrames()[count].getDuration();

		long shortestframeDurration = Utilities.findGCDofArray(framesDurrationArray);
		int framesPerSecond = (int) ((1000.0 / shortestframeDurration) * 1000);
		progressDialog.moveForward(2);
		
		StatusLogger.logPrimaryStatus("Generating Sound File...");
		RawAudioFile rawAudioFile = AudioUtil.combineAudioClips(pictoralFin.getTimeLine().getVideoDurration(), pictoralFin.getTimeLine().getAudioClips());
		File audioFile = null;
		if(rawAudioFile != null)
			audioFile = rawAudioFile.createTempWavFile("VideoAudio");
		progressDialog.moveForward(10);
		
		StatusLogger.logPrimaryStatus("Exporting Frames - 0%");
		FileUtils.deleteFolder(new File(FileUtils.pictoralFinTempFolder + "/VideoFrames"));
		
		File imageFile;
		int imageIndex = 1;
		int neededNumberOfDigits = (int) (Math.floor(Math.log10(frames.size())) + 1);		
		try {	
			
			for(int frameCount = 0; frameCount < frames.size(); frameCount++) {
				
				for(int loopCount = 0; loopCount < (framesDurrationArray[frameCount] / shortestframeDurration); loopCount++) {
					imageFile = FileUtils.createTempFile("frame-" + String.format("%0" + neededNumberOfDigits + "d", imageIndex++), ".bmp", "VideoFrames", false);			
					ImageIO.write(frames.get(frameCount), "bmp", imageFile);
				}
				
				newPercent = (int) ((((double) frameCount) / frames.size()) * 100);
				progressDialog.moveForward(newPercent - previousPercent);
				
				previousPercent = newPercent;
				
				StatusLogger.logPrimaryStatus("Exporting Frames - " + newPercent + "%");
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error exporting the frames\nto the temp folder.\nMessage:\n" + e.getMessage() + "\n\n Aborting Export.", "Error Export Images", JOptionPane.ERROR_MESSAGE);
			System.out.println("There was an error writing frames to a temp file: ");
			e.printStackTrace();
			
			outputFile = null;
			return;
		}
		
		
		previousPercent = 0;
		newPercent = 0;
		StatusLogger.logPrimaryStatus("Executing FFmpeg - 0%");
		try {
			String ffmpegCommand = ffmpegExeicutable.getPath() + " -r " + framesPerSecond + "/1000 -i \"" + FileUtils.pictoralFinTempFolder.getAbsolutePath() 
							+ "/VideoFrames/Frame-%0\"" + neededNumberOfDigits + "d.bmp ";
			
			if(audioFile != null)
				ffmpegCommand += " -i \"" + audioFile.getAbsolutePath() + "\" ";
			
			
			ffmpegCommand += " -c:v libx264 -y -start_number 0 -vf \"format=yuv420p\" \"" + outputFile.getAbsolutePath() + "\"";
			
			Process p = Runtime.getRuntime().exec(ffmpegCommand);
			
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line = null;
			while ((line = errorReader.readLine()) != null) {
				if(line.startsWith("frame")) {
					String currentFrame = line.substring(line.indexOf('=') + 1, line.indexOf("fps")).trim();
					
					try {
						newPercent = (int) ((((double) Integer.parseInt(currentFrame)) / (imageIndex - 1)) * 100);
						progressDialog.moveForward(newPercent - previousPercent);
						
						previousPercent = newPercent;
						StatusLogger.logPrimaryStatus("Executing FFmpeg - " + newPercent + "%");
					} catch (NumberFormatException e) {
						System.err.println("Error with current frame conversion! String = \"" + currentFrame + "\" " + e.getMessage());
					}
				}
			}		  
			
			p.waitFor();
			
			if(p.exitValue() != 0) {
				System.err.println("There was an error exicuting FFmpeg!!!");
				throw new Exception();
			}
				
		} catch (CanceledException e) {
			throw e;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error exicuting FFmpeg.\n\n Aborting Export.", "Error Exicuting FFmpeg", JOptionPane.ERROR_MESSAGE);
		}
		
		progressDialog.close();
		StatusLogger.logPrimaryStatus("Done!");

	}

	private static File getFileToExportTo(DataFile dataFile) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setInitialDirectory(new File(dataFile.getLastOpenVideoLocation()).getParentFile());
		fileChooser.setTitle("Export Video File");
		
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Video Files", "*.mp4", "*.flv", "*.mov", "*.avi", "*.wmv"),
				new ExtensionFilter("All Files", "*"));
		
		File videoFile = fileChooser.showSaveDialog(null);

		dataFile.setLastOpenVideoLocation(videoFile.getAbsolutePath());
		return videoFile;
	}

	private static Dimension getOptimalPictureSize(PictoralFin pictoralFin) {
		int width = 0, height = 0;

		for (com.fishtankapps.pictoralfin.objectBinders.Frame f : pictoralFin.getTimeLine().getFrames()) {
//			f.loadImagesUnThreaded();
			if (f.getLayer(0).getWidth() > width)
				width = f.getLayer(0).getWidth();

			if (f.getLayer(0).getHeight() > height)
				height = f.getLayer(0).getHeight();
		}

		return new Dimension(width, height);
	}

	private static ArrayList<BufferedImage> resizeImages(PictoralFin pictoralFin, Dimension newSize) {
		ArrayList<BufferedImage> resizedImages = new ArrayList<>();

		for (com.fishtankapps.pictoralfin.objectBinders.Frame f : pictoralFin.getTimeLine().getFrames()) {
			BufferedImage i = new BufferedImage((int) newSize.getWidth(), (int) newSize.getHeight(), Constants.IMAGE_TYPE);
			BufferedImage frame = f.getLayer(0);
			i.getGraphics().drawImage(frame, i.getWidth() / 2 - frame.getWidth() / 2,
					i.getHeight() / 2 - frame.getHeight() / 2, frame.getWidth(), frame.getHeight(), null);
			
			resizedImages.add(i);
		}

		return resizedImages;
	}
	
	
	// IMPORTING VIDEO:
	public static File[] videoToImageFiles(String videoPath) {
		
		try {
			int frameCount = getVideoFrameCount(videoPath);
			
			StatusLogger.logSecondaryStatus("Extracting Frames... (0/" + frameCount +")");
			File outputFolder = new File(FileUtils.pictoralFinTempFolder.getAbsolutePath() + "/VideoToPictures");
			
			if(outputFolder.exists()) {
				for(File f : outputFolder.listFiles())
					f.delete();
			} else {
				outputFolder.mkdirs();
			}
			
			String ffmpegCommand = ffmpegExeicutable.getPath() + " -i \"" + videoPath 
					+ "\" -y \"" + outputFolder + "/frame-%05d.bmp\"";
			
			Process p = Runtime.getRuntime().exec(ffmpegCommand);
			
			BufferedReader errorReader = new BufferedReader( new InputStreamReader(p.getErrorStream()));
			
			String line = null;
			while ((line = errorReader.readLine()) != null) {
				if(line.startsWith("frame")) {
					StatusLogger.logSecondaryStatus("Extracting Frames - (" + line.substring(6, line.indexOf("fps")).trim() +"/" + frameCount + ")");
				}
			}		  
			
			p.waitFor();

			return outputFolder.listFiles();
			
		} catch (Exception e) {
			
		}
		
		return null;
	}
	
	public static int getVideoFrameCount(String videoPath) {
		try {
			String ffprobeCommand = ffprobeExeicutable.getAbsolutePath() + " -v error -select_streams v:0 -show_entries stream=nb_frames "
					+ "-of default=nokey=1:noprint_wrappers=1 \"" + videoPath + "\"";
			
			Process ffprobeProcess = Runtime.getRuntime().exec(ffprobeCommand);
			
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(ffprobeProcess.getInputStream()));
			
			ffprobeProcess.waitFor();
			
			return Integer.parseInt(inputReader.readLine());	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}	
	public static int getVideoFramesPerSecond(String videoPath) {
		try {
			String ffprobeCommand = ffprobeExeicutable.getAbsolutePath() + " -v error -select_streams v -of default=noprint_wrappers=1:nokey=1 -show_entries stream=r_frame_rate \"" + videoPath + "\"";
			
			Process ffprobeProcess = Runtime.getRuntime().exec(ffprobeCommand);
			
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(ffprobeProcess.getInputStream()));
			
			ffprobeProcess.waitFor();
			
			String line = inputReader.readLine();
			
			double denominator = Integer.parseInt(line.substring(0, line.indexOf('/')));
			double numerator =   Integer.parseInt(line.substring(line.indexOf('/') + 1, line.length()));
			
			return (int) ((numerator / denominator) * 1000);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}

	public static File extractAudioFromVideo(File videoFile) {
		return AudioUtil.extractAudioFromVideo(videoFile);
	}
}

