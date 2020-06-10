package utilities;

import java.io.File;

import javax.sound.sampled.AudioFormat;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import jTimeLine.AudioClip;
import objectBinders.RawAudioFile;

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
			audio.setSamplingRate(new Integer(Constants.DEFAULT_SAMPLE_RATE));

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
	
	public static File extractAudioFromVideo(File videoFile) {
		return convertAudioFileToWAV(videoFile);
	}
	
	public static RawAudioFile combineAudioClips(long lengthInMilli, AudioClip[] clips) {		
		if(clips == null || clips.length == 0)
			return null;
		
		short[] leftChannel, rightChannel;
		int[] sampleCountPerClip = new int[clips.length];
		
		for(int count = 0; count < sampleCountPerClip.length; count++)
			sampleCountPerClip[count] = 0;
		
		AudioFormat format = clips[0].getAudioClipData().getRawAudioFile().getAudioFormat();
		
		float samplesPerMilli = format.getSampleRate() / 1000;
		int length = (int) (samplesPerMilli * lengthInMilli);
		
		leftChannel = new short[length];
		rightChannel = new short[length];
		
		long currentMillis = 0;
		for(int count = 0; count < length; count++) {
			leftChannel[count] = 0;
			rightChannel[count] = 0;
			
			currentMillis = (long) (count / samplesPerMilli);
			
			for(int clipIndex = 0; clipIndex < clips.length; clipIndex++) {
				if(currentMillis >= clips[clipIndex].getStartTime() && currentMillis <= clips[clipIndex].getEndTime()) {					
					leftChannel[count]  += clips[clipIndex].getAudioClipData().getRawAudioFile().getChannel(RawAudioFile.LEFT_CHANNEL) [sampleCountPerClip[clipIndex]];
					rightChannel[count] += clips[clipIndex].getAudioClipData().getRawAudioFile().getChannel(RawAudioFile.RIGHT_CHANNEL)[sampleCountPerClip[clipIndex]++];
				}
			}
		}
		
		return new RawAudioFile(clips[0].getAudioClipData().getRawAudioFile().getAudioFormat(), leftChannel, rightChannel);		
	}
}
