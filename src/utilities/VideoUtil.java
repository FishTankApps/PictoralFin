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

import jComponents.JVideoFileChooser;
import jTimeLine.AudioClip;
import mainFrame.PictoralFin;
import objectBinders.DataFile;

public class VideoUtil {
	
	private static final Java2DFrameConverter CONVERTER = new Java2DFrameConverter();

	public static void generateAndSaveVideo(PictoralFin pictoralFin) {
		File videoFile = getFileToExportTo(pictoralFin.getDataFile());
		
		if(videoFile == null)
			return;
		
		Dimension pictureSize = getOptimalPictureSize(pictoralFin);
		ArrayList<BufferedImage> frames = resizeImages(pictoralFin, pictureSize);
		FFmpegFrameRecorder frameRecorder = new FFmpegFrameRecorder(videoFile, (int) pictureSize.getWidth(), (int) pictureSize.getHeight());

		long[] framesDurrationArray = new long[pictoralFin.getTimeLine().numberOfFrame()];		
		for(int count = 0; count < framesDurrationArray.length; count++)
			framesDurrationArray[count] = pictoralFin.getTimeLine().getFrames()[count].getDuration();
		
		try {
			long shortestframeDurration = Utilities.findGCDofArray(framesDurrationArray);
			double framesPerSecond = 1000.0 / shortestframeDurration;
			
			String videoExtension = videoFile.getName().split("\\.")[1];
			frameRecorder.setVideoCodec((videoExtension.equals("mp4")) ? 13 : 22);
			frameRecorder.setFormat(videoExtension);
			frameRecorder.setFrameRate(framesPerSecond);
			
			AudioClip[] clips = pictoralFin.getTimeLine().getAudioClips();
			if(clips != null && clips.length > 0) {
				frameRecorder.setSampleRate(44100);
				frameRecorder.setAudioBitrate(128000);
				frameRecorder.setAudioChannels(2);
			}
			
			frameRecorder.start();
			
			int frameCount = writeImages(frameRecorder, frames, framesDurrationArray, shortestframeDurration, framesPerSecond);
			writeAudio(frameRecorder, clips, frameCount);
			
			frameRecorder.stop();
			JOptionPane.showMessageDialog(null, "Export Complete", "Done", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was error exporting the video.\n"+e.getMessage(), "Error Exporting", JOptionPane.ERROR_MESSAGE);
			System.out.println("Error Exporting Video:");
			e.printStackTrace();
		}
		
	}	
	
	private static File getFileToExportTo(DataFile dataFile) {
		JVideoFileChooser jfc = new JVideoFileChooser();
		
		jfc.setDialogTitle("Choose File to Export to:");
		jfc.setApproveButtonText("Export");
		jfc.setCurrentDirectory(new File(dataFile.getLastOpenVideoLocation()));
		
		if(jfc.showOpenDialog(null) == JVideoFileChooser.CANCEL_OPTION) {
			return null;
		}

		File videoFile = jfc.getSelectedFile();
		String outputPath = videoFile.getAbsolutePath();
		
		//TODO - Fix this:
		if (!outputPath.split("\\.")[1].equals(jfc.getSelectedVideoFormat())) {
			String message = " The file " + videoFile.getName() + 
					" does not have the right extension (" + jfc.getSelectedVideoFormat() + 
					")\n Would you like to renamed the file to:\n" + videoFile.getName().split("\\.")[0] + "." + jfc.getSelectedVideoFormat() ;
			if (JOptionPane.showConfirmDialog(null, message, "File Extension Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE)
					== JOptionPane.YES_OPTION) {
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
		
		for(objectBinders.Frame f : pictoralFin.getTimeLine().getFrames()) {
			if(f.getLayer(0).getWidth() > width) 
				width = f.getLayer(0).getWidth();
			
			if(f.getLayer(0).getHeight() > height) 
				height = f.getLayer(0).getHeight();
		}
		
		return new Dimension(width, height);
	}
	
	private static ArrayList<BufferedImage> resizeImages(PictoralFin pictoralFin, Dimension newSize){
		ArrayList<BufferedImage> resizedImages = new ArrayList<>();
		
		for(objectBinders.Frame f : pictoralFin.getTimeLine().getFrames()) {
			BufferedImage i = new BufferedImage((int) newSize.getWidth(), (int) newSize.getHeight(), 6);
			BufferedImage frame = f.getLayer(0);
			i.getGraphics().drawImage(frame, i.getWidth()/2-frame.getWidth()/2, i.getHeight()/2-frame.getHeight()/2, frame.getWidth(), frame.getHeight(),null);
			
			resizedImages.add(i);
		}
		
		return resizedImages;
	}
	
	private static int writeImages(FFmpegFrameRecorder frameRecorder, ArrayList<BufferedImage> images, 
			long[] frameDurrations, long shortestframeDurration, double framesPerSecond) throws Exception {
		
		int count = 0;
		int frameCount = 0;
		for (BufferedImage image : images) {
			for(int loop = 0; loop < (frameDurrations[count] / shortestframeDurration); loop++)	{		
				frameRecorder.record(CONVERTER.convert(BufferedImageUtil.setBufferedImageType(image, Constants.IMAGE_TYPE)));
				frameCount++;
			}
			count++;
		}
		return frameCount;
	}
	
	private static void writeAudio(FFmpegFrameRecorder frameRecorder, AudioClip[] audioClips, int imageFrameCount) {		
		if(audioClips == null || audioClips.length == 0)
			return;			
		
		for(AudioClip audioClip : audioClips) {
			try {
				File audioFile = AudioUtil.convertAudioFileToMP3(audioClip.getAudioFile());
				if(audioFile == null)
					break;
				
				FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(audioFile);
				int numOfFrames = getAudioFrameCounts(frameGrabber);
				double framesPerSecond = (double) numOfFrames / (frameGrabber.getLengthInTime() / 1_000_000);
				double audioFramesPerImageFrames =  framesPerSecond / frameRecorder.getFrameRate();				
				
				System.out.println("\n\n---{ Audio File: " + audioClip.getName() + "}--");
				System.out.println("Length in time:    " + frameGrabber.getLengthInTime());
				System.out.println("Number of frames:  " + numOfFrames);
				System.out.println("Frames per second: " + framesPerSecond);		
				System.out.println("A-Frames/I-Frame:  " + audioFramesPerImageFrames);		
				
				frameGrabber.start();
				for (int audioCount = 0; audioCount < imageFrameCount * audioFramesPerImageFrames; audioCount++) {
					Frame audioFrame = frameGrabber.grab();
					if(audioFrame != null) 
						frameRecorder.record(audioFrame);
				}
				
				frameGrabber.stop();
			} catch (Exception e) {
				System.out.println("Empty Catch Block: VideoUtil.writeAudio()");
				e.printStackTrace();
			}
		}
	}
	
	private static int getAudioFrameCounts(FFmpegFrameGrabber audioGrabbers) {
		Frame frame;
		int count = 0;
		
		try {			
			audioGrabbers.restart();
			do { 
				frame = audioGrabbers.grab();
				count++;
			} while(frame != null);	
			
			audioGrabbers.restart();
		}catch (Exception e) {
			System.out.println("Empty Catch Block: VideoUtil.getAudioFrameCount()");
			e.printStackTrace();
		}	
		
		return count;
    }
	
//	public static void exportImagesAVideo(VideoSettings videoSettings) {
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					ArrayList<FFmpegFrameGrabber> audioGrabbers;
//					ArrayList<BufferedImage> frames;
//					FFmpegFrameRecorder frameOutput;
//					JProgressDialog jpd;
//					String outputPath;
//					
//					if("CANCEL".equals(outputPath = getFileToExportTo()))
//						return;
//					
//					int numberOfPoints = 10 +  pfk.getFrameTimeLine().getFrames().size() + (videoSettings.getAudioSettings().getAudioInfo().size()) + 1;
//					jpd = new JProgressDialog("Exporting Video " + JProgressDialog.PERCENT, "Exporting to \n" + outputPath, numberOfPoints);
//					
//					
//					try {
//						frames = new ArrayList<>();
//						
//						for(Picture p : pfk.getFrameTimeLine().getFrames()) 
//							frames.add(p.getImage(false));
//						
//						audioGrabbers = new ArrayList<>();
//						
//						for(AudioInfo as : videoSettings.getAudioSettings().getAudioInfo())
//							audioGrabbers.add(new FFmpegFrameGrabber(as.getFilePath()));	
//						
//						jpd.moveForward(2);
//						
//						try {
//							for(FFmpegFrameGrabber fffg : audioGrabbers)
//								fffg.start();
//							
//							jpd.moveForward(2);
//							
//							if(audioGrabbers.size() > 0) {
//								frameOutput = new FFmpegFrameRecorder(outputPath, frames.get(0).getWidth(), frames.get(0).getHeight(), audioGrabbers.get(0).getAudioChannels());
//								frameOutput.setSampleFormat(audioGrabbers.get(0).getSampleFormat());
//								frameOutput.setAudioQuality(videoSettings.getVideoQuality());
//								frameOutput.setSampleRate(audioGrabbers.get(0).getSampleRate());
//								frameOutput.setAudioBitrate(audioGrabbers.get(0).getAudioBitrate());
//							}else
//								frameOutput = new FFmpegFrameRecorder(outputPath, frames.get(0).getWidth(), frames.get(0).getHeight());
//
//							jpd.moveForward(2);
//							
//							frameOutput.setFrameRate(videoSettings.getFrameRate());
//							frameOutput.setVideoCodec(videoSettings.getVideoCodec());
//							frameOutput.setVideoBitrate(videoSettings.getVideoBitRate());
//							frameOutput.setFormat(videoSettings.getVideoFormat());
//							frameOutput.setVideoQuality(videoSettings.getVideoQuality());
//							
//							jpd.moveForward(2);
//
//							frameOutput.start();
//							
//							jpd.moveForward(2);
//
//							
//							for (int frameCount = 0; frameCount < frames.size(); frameCount++) {
//								frameOutput.record(CONVERTER.convert(frames.get(frameCount)));						
//								jpd.moveForward();
//							}
//							
//							
//							// TODO: COMBINE ALL AUDIO TO ONE FILE THEN ADD
//							int[] frameCounts = getAudioFrameCounts(audioGrabbers);
//							
//							for(int audioGrabberIndex = 0; audioGrabberIndex < audioGrabbers.size(); audioGrabberIndex++) {
//								FFmpegFrameGrabber audioGrabber = audioGrabbers.get(audioGrabberIndex);
//								double audioFPS = (double) frameCounts[audioGrabberIndex] / (audioGrabber.getLengthInTime() / 1_000_000);
//								double ratio =  audioFPS / videoSettings.getFrameRate();
//								
//								
//								
//								for (int audioCount = 0; audioCount < frames.size() * ratio; audioCount++) {
//									if(videoSettings.getAudioSettings().getAudioInfo(audioGrabberIndex).isFrameCountInBounds((int) (audioCount / ratio))) {
//										Frame audioFrame = audioGrabbers.get(audioGrabberIndex).grab();
//										if(audioFrame != null) 
//											frameOutput.record(audioFrame);
//									}
//									
//								}
//								jpd.moveForward();
//							}
//
//							
//							frameOutput.stop();
//							for(FFmpegFrameGrabber fffg : audioGrabbers)
//								fffg.stop();
//							jpd.moveForward();
//							
//						} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
//							System.out.println("EMPTY CATCH BLOCK: VideoTools.imagesToMp4(ArrayList<BufferedImage>, String, String), FrameGrabber");
//							Utilities.showMessage("ERROR\nFrameGrabber", "ERROR", true);
//							e.printStackTrace(System.out);
//							
//						} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
//							System.out.println("EMPTY CATCH BLOCK: VideoTools.imagesToMp4(ArrayList<BufferedImage>, String, String), FrameRecorder");
//							Utilities.showMessage("ERROR\nFrameRecorder", "ERROR", true);
//							e.printStackTrace(System.out);
//						}
//						
//					}catch(CanceledException e) {
//						System.out.println("CANCELED");
//						Utilities.showMessage("ERROR\nCANCELED", "ERROR", true);
//					}				
//						
//				
//				}catch(Exception e) {
//					Utilities.showMessage(e.getMessage(), "ERROR", true);
//				}
//			}
//			
//		}).start();
//		
//	}
//	public static int[] getAudioFrameCounts(ArrayList<FFmpegFrameGrabber> audioGrabbers) {
//		int[] frameCounts = new int[audioGrabbers.size()];
//		Frame frame;
//		int count = 0;
//		
//		for(count = 0; count < frameCounts.length; count++)
//			frameCounts[count] = 0;
//		
//		try {			
//			for(count = 0; count < frameCounts.length; count++) {
//				audioGrabbers.get(count).restart();
//				do{ 
//					frame = audioGrabbers.get(count).grab();
//					frameCounts[count]++;
//				}while(frame != null);	
//				
//				audioGrabbers.get(count).restart();
//			}
//		}catch (Exception e) {}	
//		
//		return frameCounts;
//	}
	
	
	
	public static ArrayList<BufferedImage> mp4ToPictures(String mp4Path) {
		FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(mp4Path);
		ArrayList<BufferedImage> toReturn = new ArrayList<>();

		try {
			frameGrabber.start();

			for (int frameCount = 0; frameCount < frameGrabber.getLengthInFrames(); frameCount++)
				toReturn.add(CONVERTER.convert(frameGrabber.grab()));

			frameGrabber.stop();
		} catch (Exception e) {
			System.out.println("EMPTY CATCH BLOCK: VideoTools.mp4ToPictures(String, int)");
			e.printStackTrace(System.out);
		}

		return toReturn;
	}
}
