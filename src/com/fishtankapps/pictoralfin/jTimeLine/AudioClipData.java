package com.fishtankapps.pictoralfin.jTimeLine;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Clip;

import com.fishtankapps.pictoralfin.objectBinders.RawAudioFile;

public class AudioClipData implements Serializable {

	private static final long serialVersionUID = 9080986784077301257L;
	
	private String name;
	private long startTime, length;
	private double volume = 1.0;	

	private transient RawAudioFile rawAudioFile;
    
    public AudioClipData(File audioFile) {
    	try {
    		rawAudioFile = new RawAudioFile(audioFile);
    	} catch (Exception e) {
    		System.out.println("Empty Catch Block: AudioClipData.AudioClipData(File);");
    		e.printStackTrace();
    		
    		throw new RuntimeException(e.getMessage());
    	}
        
    }
    
    public File generateAudioTempFile() {
    	return  rawAudioFile.createTempWavFile(name);	
    }

    public Clip getAudioClip() {
    	return rawAudioFile.getPlayableClip();
    }
    
    public RawAudioFile getRawAudioFile() {
    	return rawAudioFile;
    }
    
	public String getName() {
		return name;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getLength() {
		return length;
	}

	public double getVolume() {
		return volume;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		
        out.writeInt(rawAudioFile.getRawAudioData().length);
        
        out.write(rawAudioFile.getRawAudioData());
        
        AudioFormat format = rawAudioFile.getAudioFormat();
        
        out.writeFloat(format.getSampleRate());
        out.writeInt(format.getSampleSizeInBits());
        out.writeInt(format.getChannels());
        out.writeBoolean(format.isBigEndian());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        int dataLength = in.readInt();
        byte[] audioData = new byte[dataLength];

        in.readFully(audioData);
        
        float sampleRate = in.readFloat();
        int sampleSizeInBits = in.readInt();
        int channels = in.readInt();
        boolean bigEndian = in.readBoolean();
        
        rawAudioFile = new RawAudioFile(new AudioFormat(sampleRate, sampleSizeInBits, channels, true, bigEndian), audioData);
    }
}
