package videoTools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import customExceptions.CanceledException;
import jComponents.JProgressDialog;
import objectBinders.AudioInfo;
import objectBinders.Picture;
import objectBinders.VideoSettings;
import tools.MiscTools;

import static globalValues.GlobalVariables.pfk;
import static globalValues.GlobalVariables.dataFile;

public class VideoTools {
	
	private static final Java2DFrameConverter CONVERTER = new Java2DFrameConverter();

	public static void exportImagesAVideo(VideoSettings videoSettings) {
		new Thread(new Runnable() {
			public void run() {
				try {
					ArrayList<FFmpegFrameGrabber> audioGrabbers;
					ArrayList<BufferedImage> frames;
					FFmpegFrameRecorder frameOutput;
					JProgressDialog jpd;
					String outputPath;
					
					if("CANCEL".equals(outputPath = getFileToExportTo()))
						return;
					
					int numberOfPoints = 10 +  pfk.getFrameTimeLine().getFrames().size() + (videoSettings.getAudioSettings().getAudioInfo().size()) + 1;
					jpd = new JProgressDialog("Exporting Video " + JProgressDialog.PERCENT, "Exporting to \n" + outputPath, numberOfPoints);
					
					
					try {
						frames = new ArrayList<>();
						
						for(Picture p : pfk.getFrameTimeLine().getFrames()) 
							frames.add(p.getImage(false));
						
						audioGrabbers = new ArrayList<>();
						
						for(AudioInfo as : videoSettings.getAudioSettings().getAudioInfo())
							audioGrabbers.add(new FFmpegFrameGrabber(as.getFilePath()));	
						
						jpd.moveForward(2);
						
						try {
							for(FFmpegFrameGrabber fffg : audioGrabbers)
								fffg.start();
							
							jpd.moveForward(2);
							
							if(audioGrabbers.size() > 0) {
								frameOutput = new FFmpegFrameRecorder(outputPath, frames.get(0).getWidth(), frames.get(0).getHeight(), audioGrabbers.get(0).getAudioChannels());
								frameOutput.setSampleFormat(audioGrabbers.get(0).getSampleFormat());
								frameOutput.setAudioQuality(videoSettings.getVideoQuality());
								frameOutput.setSampleRate(audioGrabbers.get(0).getSampleRate());
								frameOutput.setAudioBitrate(audioGrabbers.get(0).getAudioBitrate());
							}else
								frameOutput = new FFmpegFrameRecorder(outputPath, frames.get(0).getWidth(), frames.get(0).getHeight());			

							jpd.moveForward(2);
							
							frameOutput.setFrameRate(videoSettings.getFrameRate());
							frameOutput.setVideoCodec(videoSettings.getVideoCodec());
							frameOutput.setVideoBitrate(videoSettings.getVideoBitRate());
							frameOutput.setFormat(videoSettings.getVideoFormat());
							frameOutput.setVideoQuality(videoSettings.getVideoQuality());
							
							jpd.moveForward(2);

							frameOutput.start();
							
							jpd.moveForward(2);

							
							for (int frameCount = 0; frameCount < frames.size(); frameCount++) {
								frameOutput.record(CONVERTER.convert(frames.get(frameCount)));						
								jpd.moveForward();
							}
							
							
							// TODO: COMBINE ALL AUDIO TO ONE FILE THEN ADD
							int[] frameCounts = getAudioFrameCounts(audioGrabbers);
							
							for(int audioGrabberIndex = 0; audioGrabberIndex < audioGrabbers.size(); audioGrabberIndex++) {
								FFmpegFrameGrabber audioGrabber = audioGrabbers.get(audioGrabberIndex);
								double audioFPS = (double) frameCounts[audioGrabberIndex] / (audioGrabber.getLengthInTime() / 1_000_000);
								double ratio =  audioFPS / videoSettings.getFrameRate();
								
								
								
								for (int audioCount = 0; audioCount < frames.size() * ratio; audioCount++) {
									if(videoSettings.getAudioSettings().getAudioInfo(audioGrabberIndex).isFrameCountInBounds((int) (audioCount / ratio))) {
										Frame audioFrame = audioGrabbers.get(audioGrabberIndex).grab();
										if(audioFrame != null) 
											frameOutput.record(audioFrame);
									}
									
								}
								jpd.moveForward();
							}

							
							frameOutput.stop();
							for(FFmpegFrameGrabber fffg : audioGrabbers)
								fffg.stop();
							jpd.moveForward();
							
						} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
							System.out.println("EMPTY CATCH BLOCK: VideoTools.imagesToMp4(ArrayList<BufferedImage>, String, String), FrameGrabber");
							MiscTools.showMessage("ERROR\nFrameGrabber", "ERROR", true);
							e.printStackTrace(System.out);
							
						} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
							System.out.println("EMPTY CATCH BLOCK: VideoTools.imagesToMp4(ArrayList<BufferedImage>, String, String), FrameRecorder");
							MiscTools.showMessage("ERROR\nFrameRecorder", "ERROR", true);
							e.printStackTrace(System.out);
						}
						
					}catch(CanceledException e) {
						System.out.println("CANCELED");
						MiscTools.showMessage("ERROR\nCANCELED", "ERROR", true);
					}				
						
				
				}catch(Exception e) {
					MiscTools.showMessage(e.getMessage(), "ERROR", true);
				}
			}
			
		}).start();
		
	}
	public static int[] getAudioFrameCounts(ArrayList<FFmpegFrameGrabber> audioGrabbers) {
		int[] frameCounts = new int[audioGrabbers.size()];
		Frame frame;
		int count = 0;
		
		for(count = 0; count < frameCounts.length; count++)
			frameCounts[count] = 0;
		
		try {			
			for(count = 0; count < frameCounts.length; count++) {
				audioGrabbers.get(count).restart();
				do{ 
					frame = audioGrabbers.get(count).grab();
					frameCounts[count]++;
				}while(frame != null);	
				
				audioGrabbers.get(count).restart();
			}
		}catch (Exception e) {}	
		
		return frameCounts;
	}
	public static String getFileToExportTo() {
		JFileChooser jfc = new JFileChooser();
		
		jfc.setDialogTitle("Choose File to Export to:");
		jfc.setApproveButtonText("Export");
		jfc.setCurrentDirectory(new File(dataFile.getLastOpenVideoLocation()));
		jfc.setAcceptAllFileFilterUsed(true);
		jfc.addChoosableFileFilter(new FileNameExtensionFilter("Video Files", ".mp4"));

		if (jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
			return "CANCEL";

		File videoFile = jfc.getSelectedFile();
		String outputPath = videoFile.getAbsolutePath();

		if (videoFile.exists())
			if (JOptionPane.showConfirmDialog(null,
					"The file " + videoFile.getName() + " already exists.\nDo you want to replace it?",
					"Do You Want to?", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return "CANCEL";
		
		dataFile.setLastOpenVideoLocation(outputPath);
		return outputPath;
	}

	@Deprecated
	public static void imagesToMp4(ArrayList<BufferedImage> frames, String audioPath, String outputPath,
			VideoSettings settings) {
		FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(audioPath);
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputPath, frames.get(0).getWidth(),
				frames.get(0).getHeight());

		try {
			audioGrabber.start();

			recorder = new FFmpegFrameRecorder(outputPath, frames.get(0).getWidth(), frames.get(0).getHeight(),
					audioGrabber.getAudioChannels());

			recorder.setSampleFormat(audioGrabber.getSampleFormat());
			recorder.setAudioQuality(settings.getVideoQuality());
			recorder.setSampleRate(audioGrabber.getSampleRate());
			recorder.setAudioBitrate(audioGrabber.getAudioBitrate());

			recorder.setFrameRate(settings.getFrameRate());
			recorder.setVideoCodec(settings.getVideoCodec());
			recorder.setVideoBitrate(settings.getVideoBitRate());
			recorder.setFormat(settings.getVideoFormat());
			recorder.setVideoQuality(settings.getVideoQuality());

			recorder.start();

			Frame audioFrame = audioGrabber.grab();

			for (int index = 0; index < frames.size(); index++, audioFrame = audioGrabber.grab()) {
				recorder.record(CONVERTER.convert(frames.get(index)));
				if (audioFrame != null)
					recorder.record(audioFrame);
			}

			recorder.stop();
		} catch (Exception e) {
			System.out.println("EMPTY CATCH BLOCK: VideoTools.imagesToMp4(ArrayList<BufferedImage>, String, String)");
			e.printStackTrace(System.out);
		}
	}
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
