package jTimeLine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;

public class AudioClipData implements Serializable {

	private static final long serialVersionUID = 9080986784077301257L;
	private static short audioFileCount = 0;
	
	private String name;
	private long startTime, length;
	private double volume = 1.0;	

    private byte[] audioData;
    public AudioClipData(File audioFile) {
    	try {
    		FileInputStream fileInput = new FileInputStream(audioFile);
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            audioData = new byte[2048];

            for (int readNum; (readNum = fileInput.read(audioData)) != -1;) {
                byteArrayStream.write(audioData, 0, readNum);
            }
            
            fileInput.close();

            audioData = byteArrayStream.toByteArray();
            byteArrayStream.close();
    	} catch (Exception e) {
    		System.out.println("Empty Catch Block: AudioClipData.AudioClipData(File);");
    		e.printStackTrace();
    	}
        
    }
    
    public File generateAudioTempFile() {    	
    	try {
    		File tempFile = File.createTempFile("PictoralFinAudioFile" + audioFileCount++, ".mp3");
    		tempFile.deleteOnExit();

			FileOutputStream out = new FileOutputStream(tempFile);
			out.write(audioData);
			out.close();
			
			return tempFile;
		} catch (Exception e) {
			System.out.println("Empty Catch Block AudioClipData.generateAudioTempFile(): ");
			e.printStackTrace();
		}
    	
		return null;    	
    }

    public InputStream getAudioInputStream(){
        return new ByteArrayInputStream(audioData);
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
}
