package playground;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.sound.sampled.AudioFormat;

import objectBinders.RawAudioFile;

public class AudioToByteArray {

	public static void main(String[] args) throws Exception {		
		File audioFile = new File("C:\\Users\\Robots\\Videos\\Grandma's Knitting Class.mp4");
		RawAudioFile rawAudio = new RawAudioFile(audioFile);
	
		
		short[] left, right;
		left  = rawAudio.getChannel(RawAudioFile.LEFT_CHANNEL);
		right = rawAudio.getChannel(RawAudioFile.RIGHT_CHANNEL);
		
		AudioFormat audioFormat = rawAudio.getAudioFormat();
		
		rawAudio.dispose();
		
		final int FADE_IN_LENGTH = 500000;
		
		for(int index = 0; index < FADE_IN_LENGTH; index ++) {
			//System.out.print("BEFORE: R-" + right[index] + ", L-" + left[index]);
			
			left[index]  = (short) (left[index]  * (index / ((double) FADE_IN_LENGTH)));
			right[index] = (short) (right[index] * (index / ((double) FADE_IN_LENGTH) ));
			
			//System.out.println("      AFTER: R-" + right[index] + ", L-" + left[index]);
		}
		
		RawAudioFile copy = new RawAudioFile(audioFormat, left, right);
		
		copy.getPlayableClip().start();
		
		try {
			Thread.sleep(100000);
		} catch (Exception e) {
			
		}
	}
	
	public static BufferedImage createAudioVisualization(RawAudioFile rawAudio) {
		final int WIDTH = 10000;
		final int HEIGHT = 100;
		
		BufferedImage i = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		
		Graphics2D g = (Graphics2D) i.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, i.getWidth(), i.getHeight());
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.BLUE);
		short[] channel1 = rawAudio.getChannel(RawAudioFile.LEFT_CHANNEL);
		
		for(int index = 0; index < WIDTH; index ++) {
			g.drawOval(index,  (int) ((HEIGHT/2.0) + (HEIGHT/2.0) * (channel1[index] / ((double) Short.MAX_VALUE))), 1, 1);
		}
		
		g.dispose();
		
		return i;
	}
	
	public static void playAudio(byte[] byteArray) throws Exception {
		
	}

}
