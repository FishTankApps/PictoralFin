package objectBinders;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import utilities.AudioUtil;
import utilities.FileUtils;

public class RawAudioFile {

	public final static int MONO_CHANNEL = 1;
	public final static int LEFT_CHANNEL = 1;
	public final static int RIGHT_CHANNEL = 2;

	private byte[] rawAudioData;
	private short[] shortChannel1, shortChannel2;
	private int numOfChannels;
	private AudioFormat format;

	public RawAudioFile(String path) throws IOException, UnsupportedAudioFileException {
		this(new File(path));
	}

	public RawAudioFile(File file) throws IOException, UnsupportedAudioFileException {
		file = AudioUtil.convertAudioFileToWAV(file);

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		DataInputStream dis = new DataInputStream(audioInputStream);

		format = audioInputStream.getFormat();

		numOfChannels = format.getChannels();

		rawAudioData = new byte[(int) (audioInputStream.getFrameLength() * format.getFrameSize())];

		dis.readFully(rawAudioData);
		dis.close();

		if (numOfChannels == 1) {

			shortChannel1 = new short[rawAudioData.length / 2];
			shortChannel2 = null;

			for (int index = 0; index < rawAudioData.length; index += 2) {
				shortChannel1[index / 2] = (short) ((rawAudioData[index] & 0xff) | (rawAudioData[index + 1] << 8));
			}

		} else if (numOfChannels == 2) {

			shortChannel1 = new short[rawAudioData.length / 4];
			shortChannel2 = new short[rawAudioData.length / 4];

			for (int index = 0; index < rawAudioData.length; index += 4) {
				shortChannel1[index / 4] = (short) ((rawAudioData[index] & 0xff) | (rawAudioData[index + 1] << 8));
				shortChannel2[index / 4] = (short) ((rawAudioData[index + 2] & 0xff) | (rawAudioData[index + 3] << 8));
			}
		} else {
			throw new RuntimeException("Unable to work with more than two channels!");
		}
	}

	public RawAudioFile(AudioFormat audioFormat, short[]... channels) {
		format = audioFormat;
		numOfChannels = channels.length;

		if (numOfChannels <= 0 || numOfChannels > 2)
			throw new RuntimeException("Invalid number of channels: " + channels.length);

		if (numOfChannels == 1) {
			shortChannel1 = channels[0];
			shortChannel2 = null;

			rawAudioData = new byte[shortChannel1.length * 2];

			for (int index = 0; index < rawAudioData.length; index += 2) {
				rawAudioData[index] = (byte) (shortChannel1[index / 2] & 0xff);
				rawAudioData[index + 1] = (byte) ((shortChannel1[index / 2] >> 8) & 0xff);
			}
		} else if (numOfChannels == 2) {
			shortChannel1 = channels[0];
			shortChannel2 = channels[1];

			rawAudioData = new byte[shortChannel1.length * 4];

			for (int index = 0; index < rawAudioData.length; index += 4) {
				rawAudioData[index] = (byte) (shortChannel1[index / 4] & 0xff);
				rawAudioData[index + 1] = (byte) ((shortChannel1[index / 4] >> 8) & 0xff);
				rawAudioData[index + 2] = (byte) (shortChannel2[index / 4] & 0xff);
				rawAudioData[index + 3] = (byte) ((shortChannel2[index / 4] >> 8) & 0xff);
			}
		}
	}

	public RawAudioFile(AudioFormat audioFormat, byte[] rawAudioData) {
		this.rawAudioData = rawAudioData;
		this.format = audioFormat;

		numOfChannels = format.getChannels();

		if (numOfChannels == 1) {

			shortChannel1 = new short[rawAudioData.length / 2];
			shortChannel2 = null;

			for (int index = 0; index < rawAudioData.length; index += 2) {
				shortChannel1[index / 2] = (short) ((rawAudioData[index] & 0xff) | (rawAudioData[index + 1] << 8));
			}

		} else if (numOfChannels == 2) {

			shortChannel1 = new short[rawAudioData.length / 4];
			shortChannel2 = new short[rawAudioData.length / 4];

			for (int index = 0; index < rawAudioData.length; index += 4) {
				shortChannel1[index / 4] = (short) ((rawAudioData[index] & 0xff) | (rawAudioData[index + 1] << 8));
				shortChannel2[index / 4] = (short) ((rawAudioData[index + 2] & 0xff) | (rawAudioData[index + 3] << 8));
			}
		} else {
			throw new RuntimeException("Unable to work with more than two channels!");
		}
	}
	
	public byte[] getRawAudioData() {
		return rawAudioData;
	}

	public int getNumberOfChannels() {
		return numOfChannels;
	}

	public int getNumberOfSamples() {
		if(shortChannel1 != null)
			return shortChannel1.length;
		
		return -1;
	}
	
	
	public AudioFormat getAudioFormat() {
		return format;
	}

	public short[] getChannel(int index) {
		if (index > numOfChannels)
			throw new RuntimeException("Their are only " + numOfChannels + " channel(s).");

		if (index == MONO_CHANNEL)
			return shortChannel1;
		else if (index == RIGHT_CHANNEL)
			return shortChannel2;

		return null;
	}

	public File createTempWavFile(String name) {
		try {
			File output = FileUtils.createTempFile(name, ".wav");
			output.deleteOnExit();

			ByteArrayInputStream bais = new ByteArrayInputStream(rawAudioData);
			AudioInputStream audioInputStream = new AudioInputStream(bais, format, rawAudioData.length);
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, output);
			audioInputStream.close();

			return output;
		} catch (Exception e) {
			System.err.println("Empty catch block: RawAudioFile.createTempWavFile():");
			e.printStackTrace();
		}

		return null;
	}

	public void dispose() {
		rawAudioData = null;
		shortChannel1 = null;
		shortChannel2 = null;
		numOfChannels = -1;

		System.gc();
	}

	public Clip getPlayableClip() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(format, rawAudioData, 0, rawAudioData.length);

			return clip;
		} catch (Exception e) {
			System.out.println("Empty Catch Block - RawAudioFile.getPlayableClip(): \n" + e.getMessage());
		}
		return null;
	}
}