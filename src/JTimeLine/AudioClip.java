package JTimeLine;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class AudioClip {

	private File audioFile;
	private long startTime, endTime;
	private double volume = 1.0;
	private Clip clip;
	
	public AudioClip(String filePath) {
		this(new File(filePath));
	}
	
	public AudioClip(File audioFile) {
		this.audioFile = audioFile;
		
		startTime = 0;
		endTime = 1000;
		
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
	        clip = AudioSystem.getClip();
	   	    clip.open(audioInputStream);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error Opening Audio file: " + audioFile.getAbsolutePath(), "Error Playing Sound", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	
	public File getAudioFile() {
		return audioFile;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public long getLength() {
		return endTime - startTime;
	}
	public double getVolume() {
		return volume;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public void setLength(long length) {
		endTime = startTime + length;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}

	public Clip getClip() {
		 return clip;
	}
}
