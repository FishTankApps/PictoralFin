package utilities;

import java.io.File;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

public class AudioUtil {

	private AudioUtil() {}
	
	public static File convertAudioFileToMP3(File toConvert) {
		try {
			if(toConvert == null || toConvert.getName().contains("mp3"))
				return toConvert;
			File target = File.createTempFile(toConvert.getName().split("\\.")[0], ".mp3");

			// Audio Attributes
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("libmp3lame");
			audio.setBitRate(128000);
			audio.setChannels(2);
			audio.setSamplingRate(44100);

			// Encoding attributes
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setFormat("mp3");
			attrs.setAudioAttributes(audio);

			// Encode
			Encoder encoder = new Encoder();
			encoder.encode(new MultimediaObject(toConvert), target, attrs);
			
			return target;
		} catch (Exception e) {
			System.out.println("Empty Catch Block: AudioUtil.convertAudioFileToMp3()");
			e.printStackTrace();
		}
		
		return null;		
	}

	public static File extractAudioFromVideo(File videoFile) {
		try {
			File target = File.createTempFile(videoFile.getName().split("\\.")[0], "PictoralFinAudio");

			AudioAttributes audioAttributes = new AudioAttributes();
			audioAttributes.setCodec("libmp3lame")
			    .setBitRate(new Integer(128000))
			    .setChannels(new Integer(2))
			    .setSamplingRate(new Integer(44100));
			
			EncodingAttributes encodingAttributes = new EncodingAttributes();
			encodingAttributes.setFormat("mp3")
			    .setAudioAttributes(audioAttributes);

			Encoder encoder = new Encoder();
			encoder.encode(new MultimediaObject(videoFile), target, encodingAttributes); 
			
			return target;
		} catch (Exception e) {
			System.out.println("Empty Catch Block: AudioUtil.extractAudioFromVideo()");
			e.printStackTrace();
		}
		
		return null;
		
	}
}
