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
	
	public static RawAudioFile combineAudioClips(long outputLengthInMilli, AudioClip[] audioClips) {		
		if(audioClips == null || audioClips.length == 0)
			return null;
		
		short[] leftChannel, rightChannel;
		int[] sampleCountPerClip = new int[audioClips.length];
		
		for(int setToZeroCounter = 0; setToZeroCounter < sampleCountPerClip.length; setToZeroCounter++)
			sampleCountPerClip[setToZeroCounter] = 0;
		
		AudioFormat audioFormat = audioClips[0].getAudioClipData().getRawAudioFile().getAudioFormat();
		
		float samplesPerMilli = audioFormat.getSampleRate() / 1000;
		int lengthInSamples = (int) (samplesPerMilli * outputLengthInMilli);
		
		leftChannel = new short[lengthInSamples];
		rightChannel = new short[lengthInSamples];
		
		long currentMillis = 0;
		int leftValueTemp, rightValueTemp;
		for(int sampleIndex = 0; sampleIndex < lengthInSamples; sampleIndex++) {
			leftChannel[sampleIndex] = 0;
			rightChannel[sampleIndex] = 0;
			
			currentMillis = (long) (sampleIndex / samplesPerMilli);
			
			for(int clipIndex = 0; clipIndex < audioClips.length; clipIndex++) {
				
				// Make sure all the audioClip is at the right time:
				//  Must be:   After start time
				//             Before end time
				//             More audio data
				if(     currentMillis >= audioClips[clipIndex].getStartTime() && 
						currentMillis <= audioClips[clipIndex].getEndTime()   &&
						sampleCountPerClip[clipIndex] < audioClips[clipIndex].getAudioClipData().getRawAudioFile().getNumberOfSamples()) {	
					
					leftValueTemp  = audioClips[clipIndex].getAudioClipData().getRawAudioFile()
							.getChannel(RawAudioFile.LEFT_CHANNEL) [sampleCountPerClip[clipIndex]]  + leftChannel[sampleIndex];
					
					rightValueTemp = audioClips[clipIndex].getAudioClipData().getRawAudioFile()
							.getChannel(RawAudioFile.RIGHT_CHANNEL)[sampleCountPerClip[clipIndex]] + rightChannel[sampleIndex];
					
					leftChannel[sampleIndex]   = (leftValueTemp  > Short.MAX_VALUE) ? Short.MAX_VALUE : ((leftValueTemp  < Short.MIN_VALUE) ? Short.MIN_VALUE : (short) leftValueTemp);
					rightChannel[sampleIndex]  = (rightValueTemp > Short.MAX_VALUE) ? Short.MAX_VALUE : ((rightValueTemp < Short.MIN_VALUE) ? Short.MIN_VALUE : (short) rightValueTemp);
					sampleCountPerClip[clipIndex]++;
				}
			}
		}
		
		return new RawAudioFile(audioClips[0].getAudioClipData().getRawAudioFile().getAudioFormat(), leftChannel, rightChannel);		
	}
}
