package jComponents.videoEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import customExceptions.FeatureNotSupportedException;
import exteneralDriveOpenner.JDriveExplorer;
import interfaces.SettingsPanel;
import mainFrame.PictoralFin;
import utilities.BufferedImageUtil;

public class VideoEditorSettingsPanel extends JPanel {

	private static final long serialVersionUID = 1584668485922313959L;

	private JLabel nothingToEdit;
	
	private JDriveExplorer driveExplorer;
	private FileImportPanel fileImportPanel;
	private JTabbedPane tabbedPane;
	private JTextArea notes;

	private JPanel importPanel;
	private JPanel settingsPanel;
	
	public VideoEditorSettingsPanel(PictoralFin pictoralFin) {	
		super(new GridLayout(1,1,0,0));
		
		BufferedImage fileImportBackground = pictoralFin.getGlobalImageKit().readImage("FileImportBackground.png");
		BufferedImage objectEditorBackground = pictoralFin.getGlobalImageKit().readImage("ObjectEditorBackground.png");
		BufferedImageUtil.applyColorThemeToImage(fileImportBackground, pictoralFin.getSettings().getTheme());
		BufferedImageUtil.applyColorThemeToImage(objectEditorBackground, pictoralFin.getSettings().getTheme());
		
		
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());		
		setMinimumSize(new Dimension(500, 50));
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(pictoralFin.getSettings().getTheme().getTitleFont(), Font.PLAIN, 20));
		tabbedPane.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		
		//TODO: Rest of background Images!
		
		
		importPanel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 1L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.drawImage(fileImportBackground, 0, 0, getWidth(), getHeight(), null);
			}
			
		};
		fileImportPanel = new FileImportPanel(pictoralFin);

		importPanel.add(fileImportPanel, BorderLayout.NORTH);
		importPanel.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		settingsPanel = new JPanel(new BorderLayout()){
			private static final long serialVersionUID = 1L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.drawImage(objectEditorBackground, 0, 0, getWidth(), getHeight(), null);
			}
			
		};		
		settingsPanel.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		try {
			driveExplorer = new JDriveExplorer(pictoralFin.getSettings().getTheme());
			importPanel.add(driveExplorer, BorderLayout.SOUTH);
		} catch (FeatureNotSupportedException e) {
			pictoralFin.flags.add("Auto Import Not Supported$The Auto Import (External Drive) feature is not supported.\nIt must be run on 32-bit JVM.");
			
			JLabel driveExplorerNotAvilable = new JLabel("Auto Import is not supported on your JVM.", JLabel.CENTER);
			driveExplorerNotAvilable.setFont(new Font(pictoralFin.getSettings().getTheme().getPrimaryFont(), Font.BOLD, 30));
			driveExplorerNotAvilable.setForeground(pictoralFin.getSettings().getTheme().getPrimaryHighlightColor());
			
			importPanel.add(driveExplorerNotAvilable, BorderLayout.SOUTH);
		}
		
		notes = new JTextArea();
		notes.setToolTipText("Type any notes you want to keep with your project here! (To-dos, general notes, scenes, etc.)");
		notes.setFont(new Font("Ariel", Font.PLAIN, 20));
		
		JPanel notePanel = new JPanel(new GridLayout(1,1,1,1)); // Used to adding Padding		
		notePanel.setBorder(new EmptyBorder(30, 30, 30, 30));
		notePanel.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		notePanel.add(notes);
		
		tabbedPane.addTab("Import Files", importPanel);
		tabbedPane.addTab("Object Editor", settingsPanel);
		tabbedPane.addTab("Note Pad", notePanel);
		
		add(tabbedPane);	
		
		nothingToEdit = new JLabel("No Editable Object is Selected", JLabel.CENTER);
		nothingToEdit.setFont(new Font(pictoralFin.getSettings().getTheme().getPrimaryFont(), Font.BOLD, 30));
		nothingToEdit.setForeground(pictoralFin.getSettings().getTheme().getPrimaryHighlightColor());
		
		settingsPanel.add(nothingToEdit);
	}
	
	public JDriveExplorer getJDriveExplorer() {
		return driveExplorer;
	}
	
	public void attachSettingsPanel(SettingsPanel panel) {
		
		settingsPanel.removeAll();
		settingsPanel.add(panel, BorderLayout.SOUTH);
		
		revalidate();
		repaint();
	}
	
	public void dettachSettingsPanel() {		
		settingsPanel.removeAll();		
		settingsPanel.add(nothingToEdit);
		repaint();
	}

	public String getNotes() {
		return notes.getText();
	}
	
	public void setNotes(String notes) {
		this.notes.setText(notes);
	}
	
}
