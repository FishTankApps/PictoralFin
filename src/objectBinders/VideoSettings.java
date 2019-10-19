package objectBinders;

import org.bytedeco.javacpp.avcodec;

public class VideoSettings {
	public static final int MP4 = 1;
	
	private int frameRate;
	private int videoBitRate;
	private int videoQuality;
	private int videoFormat;
	private AudioSettings audioSettings;
	
	public VideoSettings() {
		this.frameRate = 30;
		this.videoBitRate = 9000;
		this.videoQuality = 0;
		this.videoFormat = MP4;
		this.audioSettings = new AudioSettings();
	}
	public VideoSettings(int frameRate, int videoBitRate, int videoQuality, int videoFormat, AudioSettings audioSettings) {
		this.frameRate = frameRate;
		this.videoBitRate = videoBitRate;
		this.videoQuality = videoQuality;
		this.videoFormat = videoFormat;
		this.audioSettings = audioSettings;
	}
	
	public final int getFrameRate() {
		return frameRate;
	}
	public final int getVideoBitRate() {
		return videoBitRate;
	}
	public final int getVideoQuality() {
		return videoQuality;
	}
	public final int getVideoCodec() {
		if(videoFormat == MP4)
			return avcodec.AV_CODEC_ID_MPEG4;		
		else
			return -1;
	}
	public final String getVideoFormat() {
		if(videoFormat == MP4)
			return "mp4";		
		else
			return "";
	}
	public final AudioSettings getAudioSettings() {
		return audioSettings;
	}
	
	public final VideoSettings setFrameRate(int frameRate) {
		this.frameRate = frameRate;
		return this;
	}
	public final VideoSettings setVideoBitRate(int videoBitRate) {
		this.videoBitRate = videoBitRate;
		return this;
	}
	public final VideoSettings setVideoQuality(int videoQuality) {
		this.videoQuality = videoQuality;
		return this;
	}
	public final VideoSettings setVideoFormat(int videoFormat) {
		this.videoFormat = videoFormat;
		return this;
	}
	public final VideoSettings setAudioSettings(AudioSettings audioInfo) {
		this.audioSettings = audioInfo;		
		return this;
	}
}
