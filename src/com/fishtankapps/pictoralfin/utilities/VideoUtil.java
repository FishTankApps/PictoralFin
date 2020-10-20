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
import com.fishtankapps.pictoralfin.jComponents.JVideoFileChooser;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.DataFile;
import com.fishtankapps.pictoralfin.objectBinders.RawAudioFile;

public class VideoUtil {
	// EXPORTING VIDEO:

	public static File ffmpegExeicutable = null;
	public static File ffprobeExeicutable = null;
	
	private static File outputFile = null;

	public static void generateAndSaveVideo(PictoralFin pictoralFin) {
		new Thread(() -> {
			try {
				generateAndSaveVideoThreaded(pictoralFin);
			} catch (CanceledException e) {
				StatusLogger.logStatus("Export Canceled");

				if (outputFile != null)
					outputFile.delete();
			}

		}).start();
	}

	public static void generateAndSaveVideoThreaded(PictoralFin pictoralFin) {
		
		File videoFile = getFileToExportTo(pictoralFin.getDataFile());

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
		
		StatusLogger.logStatus("Resizing Images...");
		Dimension pictureSize = getOptimalPictureSize(pictoralFin);		
		ArrayList<BufferedImage> frames = resizeImages(pictoralFin, pictureSize);		
		progressDialog.moveForward(10);
		
		StatusLogger.logStatus("Getting Frame Durrations...");
		long[] framesDurrationArray = new long[pictoralFin.getTimeLine().numberOfFrame()];
		for (int count = 0; count < framesDurrationArray.length; count++)
			framesDurrationArray[count] = pictoralFin.getTimeLine().getFrames()[count].getDuration();

		long shortestframeDurration = Utilities.findGCDofArray(framesDurrationArray);
		int framesPerSecond = (int) ((1000.0 / shortestframeDurration) * 1000);
		progressDialog.moveForward(2);
		
		StatusLogger.logStatus("Generating Sound File...");
		RawAudioFile rawAudioFile = AudioUtil.combineAudioClips(pictoralFin.getTimeLine().getVideoDurration(), pictoralFin.getTimeLine().getAudioClips());
		File audioFile = null;
		if(rawAudioFile != null)
			audioFile = rawAudioFile.createTempWavFile("VideoAudio");
		progressDialog.moveForward(10);
		
		StatusLogger.logStatus("Exporting Frames - 0%");
		FileUtils.deleteFolder(new File(FileUtils.pictoralFinTempFolder + "\\VideoFrames"));
		
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
				
				StatusLogger.logStatus("Exporting Frames - " + newPercent + "%");
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
		StatusLogger.logStatus("Executing FFmpeg - 0%");
		try {
			String ffmpegCommand = ffmpegExeicutable.getPath() + " -r " + framesPerSecond + "/1000 -i \"" + FileUtils.pictoralFinTempFolder.getAbsolutePath() 
							+ "\\VideoFrames\\Frame-%0\"" + neededNumberOfDigits + "d.bmp ";
			
			if(audioFile != null)
				ffmpegCommand += " -i \"" + audioFile.getAbsolutePath() + "\" ";
			
			
			ffmpegCommand += " -c:v libx264 -y -start_number 0 -vf \"fps=10,format=yuv420p\" \"" + outputFile.getAbsolutePath() + "\"";
			
			Process p = Runtime.getRuntime().exec(ffmpegCommand);
			
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line = null;
			while ((line = errorReader.readLine()) != null) {
				if(line.startsWith("frame")) {
					String currentFrame = line.substring(6, line.indexOf("fps")).trim();
					
					try {
						newPercent = (int) ((((double) Integer.parseInt(currentFrame)) / (imageIndex - 1)) * 10);
						progressDialog.moveForward(newPercent - previousPercent);
						
						previousPercent = newPercent;
						StatusLogger.logStatus("Executing FFmpeg - " + newPercent + "%");
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
		StatusLogger.logStatus("Done!");

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
			
			StatusLogger.logStatus("Extracting Frames... (0/" + frameCount +")");
			File outputFolder = new File(FileUtils.pictoralFinTempFolder.getAbsolutePath() + "\\VideoToPictures");
			
			if(outputFolder.exists()) {
				for(File f : outputFolder.listFiles())
					f.delete();
			} else {
				outputFolder.mkdirs();
			}
			
			String ffmpegCommand = ffmpegExeicutable.getPath() + " -i \"" + videoPath 
					+ "\" -y \"" + outputFolder + "\\frame-%05d.bmp\"";
			
			Process p = Runtime.getRuntime().exec(ffmpegCommand);
			
			BufferedReader errorReader = new BufferedReader( new InputStreamReader(p.getErrorStream()));
			
			String line = null;
			while ((line = errorReader.readLine()) != null) {
				if(line.startsWith("frame")) {
					StatusLogger.logStatus("Extracting Frames... (" + line.substring(6, line.indexOf("fps")).trim() +"/" + frameCount + ")");
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

