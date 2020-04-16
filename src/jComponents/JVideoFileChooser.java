package jComponents;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JVideoFileChooser extends JFileChooser {

	private static final long serialVersionUID = -254760104682778702L;
	private JComboBox<String> videoFormat;

	PropertyChangeListener onNewItemSelected = new PropertyChangeListener() {

		public void propertyChange(final PropertyChangeEvent pe) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				protected Void doInBackground() {
					if (getSelectedFile() != null) {
						String[] split = getSelectedFile().getName().split("\\.");
						if (split != null && split.length > 1) {
							if (split[1].equals("mp4") || split[1].equals("flv")) {
								videoFormat.setSelectedItem(split[1]);
								repaint();
							}
						}
					}

					return null;
				}
			};
			worker.execute();
		}
	};

	public JVideoFileChooser() {
		setAcceptAllFileFilterUsed(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Video files", "mp4", "flv");
		addChoosableFileFilter(filter);
		setFileFilter(filter);

		JPanel labelPanel = ((JPanel) ((JPanel) ((JPanel) getComponent(2)).getComponent(2)).getComponent(0));
		labelPanel.add(new Box.Filler(new Dimension(1, 12), new Dimension(1, 12), new Dimension(1, 12)));
		labelPanel.add(new JLabel("Video Format:"));

		JPanel comboBoxPanel = ((JPanel) ((JPanel) ((JPanel) getComponent(2)).getComponent(2)).getComponent(2));

		videoFormat = new JComboBox<>();
		videoFormat.addItem("mp4");
		videoFormat.addItem("flv");

		comboBoxPanel.add(new Box.Filler(new Dimension(1, 8), new Dimension(1, 8), new Dimension(1, 8)));
		comboBoxPanel.add(videoFormat);
	    addPropertyChangeListener(onNewItemSelected);
	}

	public String getSelectedVideoFormat() {
		return (String) videoFormat.getSelectedItem();
	}
}
