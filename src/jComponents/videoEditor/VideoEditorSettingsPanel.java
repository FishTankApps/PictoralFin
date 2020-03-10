package jComponents.videoEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import customExceptions.FeatureNotSupportedException;
import exteneralDriveOpenner.JDriveExplorer;
import interfaces.SettingsPanel;
import objectBinders.Theme;

public class VideoEditorSettingsPanel extends JPanel {

	private static final long serialVersionUID = 1584668485922313959L;

	private JDriveExplorer driveExplorer;

	public VideoEditorSettingsPanel(Theme theme) {	
		super(new BorderLayout());
		
		setBackground(theme.getPrimaryBaseColor());		
		setMinimumSize(new Dimension(500, 50));
		
		try {
			driveExplorer = new JDriveExplorer(theme);
			add(driveExplorer, BorderLayout.NORTH);
		} catch (FeatureNotSupportedException e) {
			JOptionPane.showMessageDialog(null, "External Drive Not Supported");
		}
		
	}
	
	public JDriveExplorer getJDriveExplorer() {
		return driveExplorer;
	}
	
	public void attachSettingsPanel(SettingsPanel panel) {
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
