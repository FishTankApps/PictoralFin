package mainFrame;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import java.io.File;

public class Launcher {
	
	public static void main(String[] filePaths) {

		try {
			System.out.println("-- Setting Look And Feel --");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			System.out.println("-- Setting Up PictoralFin --");
			PictoralFin pictoralFin = new PictoralFin();

			System.out.println("-- Importing Pictures --");
			if (filePaths.length > 0) {
				File file = new File(filePaths[0]);
				if(file.exists()) {
					if(file.getName().contains(".pfp")) {
						pictoralFin.openProject(file.getAbsolutePath());
					}
				}
			}
			
			System.out.println("-- Launching PictoralFin --");
			pictoralFin.launch();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"There was an error at start up:\n" + e.getMessage() + "\n" + e.getStackTrace(), "FATAL ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
