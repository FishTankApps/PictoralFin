package mainFrame;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import objectBinders.DataFile;
import pictureTools.PictureImporter;

import static globalValues.GlobalVariables.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Launcher {
	
	public static void main(String[] filePaths) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			orca = generateOrcaIcon();

			dataFile = new DataFile();
			
			PictoralFinKinetic pfk = new PictoralFinKinetic();

			if (filePaths.length > 0) {
				File[] files = new File[filePaths.length];
				for (int index = 0; index < files.length; index++)
					files[index] = new File(filePaths[index]);

				PictureImporter.importPictures(files, pfk.getFrameTimeLine());
			}
			
			pfk.launch();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	public static BufferedImage generateOrcaIcon() {	

		try {
			return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("Kinetic Icon.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		};
		return null;
	}
}
