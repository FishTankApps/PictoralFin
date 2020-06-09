package utilities;

import java.io.File;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.xuggler.IStreamCoder;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import mainFrame.StatusLogger;

public class AudioUtil {

	private AudioUtil() {}

	public static File convertAudioFileToWAV(File toConvert) {

		try {			
			File target = File.createTempFile(toConvert.getName(), ".wav");
			target.deleteOnExit();
			
			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Tempfile created at: " + target.getAbsolutePath());
			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Created AudioAttributes... ");
			
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("pcm_s16le");
			audio.setBitRate(new Integer(16));
			audio.setChannels(new Integer(2));
			audio.setSamplingRate(new Integer(48000));

			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Created EncodingAttributes... ");
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setFormat("wav");
			attrs.setAudioAttributes(audio);

			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Created Encoder... ");
			Encoder encoder = new Encoder();
			
			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Coverting... ");
			encoder.encode(toConvert, target, attrs);
			
			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Coverting Completed without errors!");
			
			return target;
		} catch (Exception e) {
			System.out.println("Empty Catch Block: AudioUtil.convertAudioFileToWAV()");
			e.printStackTrace();
		}

		return null;
	}

	public static File convertAudioFileToMP3(File toConvert) {
		try {

			File target = File.createTempFile(toConvert.getName().split("\\.")[0], ".mp3");
			target.deleteOnExit();
			Utilities.debug("AudioUtil.convertToMP3() - Tempfile created at: " + target.getAbsolutePath());
			Utilities.debug("AudioUtil.convertToMP3() - Creating Readers and Writers...");

			StatusLogger.logStatus("Converting Audio File... (Creating Readers/Writers)");
			IMediaReader mediaReader = ToolFactory.makeReader(toConvert.getPath());
			IMediaWriter mediaWriter = ToolFactory.makeWriter(target.getPath(), mediaReader);

			Utilities.debug("AudioUtil.convertToMP3() - Adding Listeners...");
			StatusLogger.logStatus("Converting Audio File... (Adding Listeners)");
			mediaReader.addListener(mediaWriter);

			mediaWriter.addListener(new MediaListenerAdapter() {
				@Override
				public void onAddStream(IAddStreamEvent event) {
					IStreamCoder streamCoder = event.getSource().getContainer().getStream(event.getStreamIndex())
							.getStreamCoder();
					streamCoder.setBitRate(Constants.DEFAULT_KBS);
					streamCoder.setBitRateTolerance(0);
					streamCoder.setChannels(2);
				}
			});

			StatusLogger.logStatus("Converting Audio File... (Converting)");
			Utilities.debug("AudioUtil.convertToMP3() - Converting...");

			while (mediaReader.readPacket() == null)
				Utilities.doNothing();

			StatusLogger.logStatus("Done Converting Audio File!");

			Utilities.debug("AudioUtil.convertToMP3() - Converting Complete!");

			return target;

		} catch (Exception e) {
			System.out.println("Empty Catch Block: AudioUtil.convertAudioFileToMp3()");
			e.printStackTrace();
		}

		return null;
	}

	public static File extractAudioFromVideo(File videoFile) {
		return convertAudioFileToWAV(videoFile);
	}
}
