package jComponents.videoEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import customExceptions.FeatureNotSupportedException;
import exteneralDriveOpenner.JDriveExplorer;
import interfaces.SettingsPanel;
import mainFrame.PictoralFin;

public class VideoEditorSettingsPanel extends JPanel {

	private static final long serialVersionUID = 1584668485922313959L;

	private JDriveExplorer driveExplorer;
	private FileImportPanel fileImportPanel;

	public VideoEditorSettingsPanel(PictoralFin pictoralFin) {	
		super(new BorderLayout());
		
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());		
		setMinimumSize(new Dimension(500, 50));
		
		fileImportPanel = new FileImportPanel(pictoralFin);
		add(fileImportPanel, BorderLayout.CENTER);
		
		try {
			driveExplorer = new JDriveExplorer(pictoralFin.getSettings().getTheme());
			add(driveExplorer, BorderLayout.NORTH);
		} catch (FeatureNotSupportedException e) {
			JOptionPane.showMessageDialog(null, "External Drive Not Supported");
		}
		
	}
	
	public JDriveExplorer getJDriveExplorer() {
		return driveExplorer;
	}
	
	public void attachSettingsPanel(SettingsPanel panel) {
		
		dettachSettingsPanel();
		add(panel, BorderLayout.SOUTH);
		
		revalidate();
		repaint();
	}
	
	public void dettachSettingsPanel() {
		for(Component c : getComponents())
			if(c instanceof SettingsPanel)
				remove(c);
		repaint();
	}
}
