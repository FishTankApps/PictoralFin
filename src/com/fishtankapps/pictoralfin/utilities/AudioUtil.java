package com.fishtankapps.pictoralfin.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.sound.sampled.AudioFormat;

import com.fishtankapps.pictoralfin.jTimeLine.AudioClip;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.RawAudioFile;

public class AudioUtil {

	private AudioUtil() {}
	
	private static PictoralFin pictoralFin;
	
	public static void passPictoralFin(PictoralFin pictoralFin) {
		AudioUtil.pictoralFin = pictoralFin;
	}

	public static File convertAudioFileToWAV(File toConvert) {
		try {
			StatusLogger.logStatus("Converting File " + toConvert.getName());
			File target = FileUtils.createTempFile(toConvert.getName(), ".wav", "AudioConversion", true);
			target.deleteOnExit();
			
			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Tempfile created at: " + target.getAbsolutePath());
			
			String command = VideoUtil.ffmpegExeicutable.getAbsolutePath() + " -i \"" + toConvert 
					+ "\" -vsync 0 -ab: 16k -ac 2 -ar " + pictoralFin.getConfiguration().getAudioSampleRate() + " -y \"" + target + "\"";
			
			Utilities.debug("AudioUtil.convertAudioFileToWAV() - Exicuting FFmpeg: " + command);
			
			Process ffmpeg = Runtime.getRuntime().exec(command);
			
			ffmpeg.waitFor();
			
			Utilities.debug("AudioUtil.convertAudioFileToWAV() - FFmpeg Done, Exit Code: " + ffmpeg.exitValue());
			
			if(ffmpeg.exitValue() != 0) {
				System.err.println("FFmpeg Finished with a code of: " + ffmpeg.exitValue());
				System.err.println("\n\n-----{StdErr Out}------");
				
				BufferedReader stdOutput = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));

				String outTemp, errorTemp;

				do {
					errorTemp = stdError.readLine();

					if (errorTemp != null)
						System.err.println(errorTemp);
				} while (errorTemp != null);
				
				
				System.err.println("\n\n-----{StdOut Out}------");
				
				do {
					outTemp = stdOutput.readLine();

					if (outTemp != null)
						System.err.println(outTemp);
				} while (outTemp != null);

			}
			
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
					
					leftValueTemp  = (short) (audioClips[clipIndex].getAudioClipData().getRawAudioFile()
							.getChannel(RawAudioFile.LEFT_CHANNEL) [sampleCountPerClip[clipIndex]] 
							* audioClips[clipIndex].getAudioClipData().getVolume())  + leftChannel[sampleIndex];
					
					rightValueTemp = (short) (audioClips[clipIndex].getAudioClipData().getRawAudioFile()
							.getChannel(RawAudioFile.RIGHT_CHANNEL)[sampleCountPerClip[clipIndex]] 
							* audioClips[clipIndex].getAudioClipData().getVolume()) + rightChannel[sampleIndex];
					
					leftChannel[sampleIndex]   = (leftValueTemp  > Short.MAX_VALUE) ? Short.MAX_VALUE : ((leftValueTemp  < Short.MIN_VALUE) ? Short.MIN_VALUE : (short) leftValueTemp);
					rightChannel[sampleIndex]  = (rightValueTemp > Short.MAX_VALUE) ? Short.MAX_VALUE : ((rightValueTemp < Short.MIN_VALUE) ? Short.MIN_VALUE : (short) rightValueTemp);
					sampleCountPerClip[clipIndex]++;
				}
			}
		}
		
		return new RawAudioFile(audioClips[0].getAudioClipData().getRawAudioFile().getAudioFormat(), leftChannel, rightChannel);		
	}
}
