package mainFrame;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import utilities.PictureImporter;

import java.io.File;

public class Launcher {
	
	public static void main(String[] filePaths) {

		try {
			System.out.println("-- Setting Look And Feel --");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			System.out.println("-- Setting Up PictoralFin --");
			PictoralFin pfk = new PictoralFin();

			System.out.println("-- Importing Pictures --");
			if (filePaths.length > 0) {
				PictureImporter pictureImporter = pfk.getPictureImporter();
				
				File[] files = new File[filePaths.length];
				for (int index = 0; index < files.length; index++)
					files[index] = new File(filePaths[index]);

				pictureImporter.importPictures(files);
			}
			
			System.out.println("-- Launching PictoralFin --");
			pfk.launch();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
