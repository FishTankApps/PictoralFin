package jComponents;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import static globalValues.GlobalVariables.dataFile;

public class JFileChooserWithImagePreview extends JFrame {
	private static final long serialVersionUID = 1L;
	JLabel img;
	JFileChooser jf = new JFileChooser();
	PropertyChangeListener onNewItemSelected = new PropertyChangeListener() {

		public void propertyChange(final PropertyChangeEvent pe) {
			SwingWorker<Image, Void> worker = new SwingWorker<Image, Void>() {
				protected Image doInBackground() {
					if (pe.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
						File f = jf.getSelectedFile();
						try {
							FileInputStream fin = new FileInputStream(f);
							BufferedImage bim = ImageIO.read(fin);

							return bim.getScaledInstance(175, 175, BufferedImage.SCALE_FAST);
						} catch (Exception e) {
							img.setText(" Not valid image/Unable to read");
						}
					}
					return null;
				}

				protected void done() {
					try {
						Image i = get(1L, TimeUnit.NANOSECONDS);
						if (i == null)
							return;
						img.setIcon(new ImageIcon(i));
					} catch (Exception e) {
						img.setText(" Error occured.");
					}
				}
			};
			worker.execute();
		}
	};
	
	public File[] openFiles() {
		File lastOpened = new File(dataFile.getLastOpenedPictureLocation());
		
		img = new JLabel();
		img.setPreferredSize(new Dimension(175, 175));

		jf.setMultiSelectionEnabled(true);
		jf.setAccessory(img);
		jf.setDialogTitle("Import Images");
		jf.setCurrentDirectory((lastOpened.isDirectory()) ? lastOpened : lastOpened.getParentFile());

		jf.setAcceptAllFileFilterUsed(true);
		jf.addChoosableFileFilter(new FileNameExtensionFilter("Importable files", "pfkp", "jpg", "jpeg", "png"));
		jf.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
		jf.addChoosableFileFilter(new FileNameExtensionFilter("Project files", "pfkp"));
		

		jf.addPropertyChangeListener(onNewItemSelected);

		if(jf.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
			return null;
		
		dataFile.setLastOpenedPictureLocation(jf.getSelectedFile().getAbsolutePath());
		return jf.getSelectedFiles();
	}
}
