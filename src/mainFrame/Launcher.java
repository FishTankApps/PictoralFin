package mainFrame;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import java.io.File;

public class Launcher {
	
	public static void main(String[] filePaths) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			PictoralFin pfk = new PictoralFin();

			if (filePaths.length > 0) {
				PictureImporter pictureImporter = pfk.getPictureImporter();
				
				File[] files = new File[filePaths.length];
				for (int index = 0; index < files.length; index++)
					files[index] = new File(filePaths[index]);

				pictureImporter.importPictures(files);
			}
			
			pfk.launch();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
