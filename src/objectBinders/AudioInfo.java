package objectBinders;

import java.io.File;

public class AudioInfo {
	public static final int TILL_THE_END = -1;
	
	private int startingFrame, endingFrame;
	private String fileName, filePath;
	
	public AudioInfo() {
		startingFrame = 0;
		endingFrame = 0;
		this.fileName = "";
	}
	public AudioInfo(File file, int startingFrame) {
		endingFrame = TILL_THE_END;
		this.startingFrame = startingFrame;		
		this.fileName = file.getName();
		this.filePath = file.getAbsolutePath();
	}
	public AudioInfo(File file, int startingFrame, int endingFrame) {
		this.endingFrame = endingFrame;		
		this.startingFrame = startingFrame;		
		this.fileName = file.getName();
		this.filePath = file.getAbsolutePath();
	}
	
	
	public final int getStartingFrame() {
		return startingFrame;
	}
	public final int getEndingFrame() {
		return endingFrame;
	}
	public final String getFileName() {
		return fileName;
	}
	public final String getFilePath() {
		return filePath;
	}
	
	public final void setStartingFrame(int startingFrame) {
		this.startingFrame = startingFrame;
	}
	public final void setEndingFrame(int endingFrame) {
		this.endingFrame = endingFrame;
	}
	public final void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public final void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public final boolean isFrameCountInBounds(int frameCount) {
		return (startingFrame <= frameCount) && (endingFrame >= frameCount);
	}
	
	@Override 
	public String toString() {
		return fileName;
	}
}
