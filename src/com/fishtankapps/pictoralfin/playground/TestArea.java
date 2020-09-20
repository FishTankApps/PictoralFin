package com.fishtankapps.pictoralfin.playground;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.fishtankapps.pictoralfin.utilities.VideoUtil;

public class TestArea {

	public static void main(String[] args) throws Exception {
		
		
		BufferedImage i = new BufferedImage(7200, 4200, BufferedImage.TYPE_4BYTE_ABGR);
		
		Graphics2D g = (Graphics2D) i.getGraphics();
		
		g.setPaint(new GradientPaint(1000,1000, new Color(6, 115, 115), 6000,3000, new Color(117,247,247)));
		
		g.fillRect(0, 0, 7200, 4200);
		
		ImageIO.write(i, "png", new File("C:\\Users\\Whitaker\\Pictures\\Others\\test.png"));
		
		//Launcher.extractFFmpeg();


		
		//while(true);
		//convertVideo(new File("C:\\Users\\Whitaker\\Videos\\Farmer Throwing Seed.mp4"), "avi");
		//videoToAudio(new File("C:\\Users\\Whitaker\\Videos\\Farmer Throwing Seed.mp4"));
    	//videoToPictures(new File("C:\\Users\\Whitaker\\Videos\\Farmer Throwing Seed.mp4"), new File("C:\\Users\\Whitaker\\Videos\\V2P"));

	}

	// Add Audio to Video: 
	// ffmpeg -i "Path to Video" -i "Path to Audio" -y -vcodec libx264 
	// -crf 20 -c copy -map 0:v:0 -map 1:a:0 "Path to Output"
	// The higher the crf, the small the file, and lower the quality.
	
	public static void videoToPictures(File videoFile, File outputFolder) {
		if (videoFile == null || !videoFile.exists())
			return;

		outputFolder.mkdirs();

		String command = VideoUtil.ffmpegExeicutable.getAbsolutePath() + " -i \"" + videoFile.getAbsolutePath()
				+ "\" -vsync 0 \"" + outputFolder.getAbsolutePath() + "\\out%d.jpg\"";

		try {
			System.out.println(command);
			Process tim = Runtime.getRuntime().exec(command);
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(tim.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(tim.getErrorStream()));

			String outTemp, errorTemp;

			while (true) {
				outTemp = stdInput.readLine();
				errorTemp = stdError.readLine();

				if (outTemp != null)
					System.out.println(outTemp);

				if (errorTemp != null)
					System.out.println(errorTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File videoToAudio(File videoFile) {
		if (videoFile == null || !videoFile.exists())
			return null;

		File outputFile = new File(videoFile.getParent() + "\\" + videoFile.getName() + ".mp3");

		String command = VideoUtil.ffmpegExeicutable.getAbsolutePath() + " -i \"" + videoFile + "\" -vsync 0 -y \"" + outputFile + "\"";

		try {
			System.out.println(command);
			Process ffmpeg = Runtime.getRuntime().exec(command);

			ffmpeg.waitFor();
			
			System.out.println("FFmpeg Done. Exit Code: " + ffmpeg.exitValue());
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputFile;
	}

	public static File convertVideo(File videoFile, String extension) {
		if (videoFile == null || !videoFile.exists())
			return null;

		File outputFile = new File(videoFile.getParent() + "\\" + videoFile.getName() + "." + extension);

		String command = VideoUtil.ffmpegExeicutable.getAbsolutePath() + " -i \"" + videoFile + "\" -crf 10 \"" + outputFile + "\"";

		try {
			System.out.println(command);
			Process tim = Runtime.getRuntime().exec(command);
			tim.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputFile;
	}
}

/*
 * JFXPanel panel = new JFXPanel();
 * 
 * final ColorPicker colorPicker = new ColorPicker();
 * colorPicker.setValue(Color.RED);
 * 
 * 
 * colorPicker.setOnAction(new EventHandler<ActionEvent>() {
 * 
 * @Override public void handle(ActionEvent event) {
 * System.out.println(Utilities.jfxColorToAwtColor(colorPicker.getValue())); }
 * });
 * 
 * FlowPane root = new FlowPane(); root.setPadding(new Insets(10));
 * root.setHgap(10); root.getChildren().addAll(colorPicker);
 * 
 * Scene scene = new Scene(root, 400, 300);
 * 
 * 
 * panel.setScene(scene);
 * 
 * JFrame frame = new JFrame(); frame.setSize(400, 300);
 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 * 
 * frame.setLocationRelativeTo(null);
 * 
 * frame.add(panel);
 * 
 * frame.setVisible(true);
 */