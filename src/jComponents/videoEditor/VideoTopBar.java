package jComponents.videoEditor;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import mainFrame.PictoralFin;
import utilities.Constants;
import utilities.VideoUtil;

public class VideoTopBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private JMenu fileMenu, importFiles, exportProject, editMenu;
	private JMenuItem importFrames, importAudio,   importVideo,
					  saveProject,  saveProjectAs, openProject, exportAsVideo, preferences;
	
	public VideoTopBar(PictoralFin pictoralFin){
		
		//---------{FILE MENU}-------------------------------------------------
		fileMenu = new JMenu(" File ");
		importFiles = new JMenu("Import...");		
		
		importFrames = new JMenuItem("Import Frame(s)");
		importFrames.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddPictureRequest);
		
		importAudio = new JMenuItem("Import Audio(s)");
		importAudio.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddAudioRequest);
		
		importVideo = new JMenuItem("Import Video(s)");
		importVideo.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddVideoRequest);
			
		
		saveProject = new JMenuItem("Save Project");
		saveProject.addActionListener(e->pictoralFin.saveProject());
		saveProject.setAccelerator(KeyStroke.getKeyStroke('S', Constants.CTRL));
		
		saveProjectAs = new JMenuItem("Save Project As");
		saveProjectAs.addActionListener(e->pictoralFin.saveProjectAs());
		saveProjectAs.setAccelerator(KeyStroke.getKeyStroke('S', Constants.CTRL_SHIFT));
		
		openProject = new JMenuItem("Open Project");
		openProject.addActionListener(e->pictoralFin.openProject(null));
		openProject.setAccelerator(KeyStroke.getKeyStroke('O', Constants.CTRL));
		
		exportProject = new JMenu("Export...");
		exportAsVideo = new JMenuItem("Export Project As Video");
		exportAsVideo.addActionListener(e->VideoUtil.generateAndSaveVideo(pictoralFin));
		
		editMenu = new JMenu(" Edit ");
		
		preferences = new JMenuItem("Prefereces");
		preferences.setAccelerator(KeyStroke.getKeyStroke('P', Constants.CTRL));
		preferences.addActionListener(e->pictoralFin.getSettings().openSettingsEditor(pictoralFin.getGlobalImageKit().pictoralFinIcon));
		
		fileMenu.add(importFiles);
			importFiles.add(importFrames);
			importFiles.add(importAudio);
			importFiles.add(importVideo);
		
		fileMenu.addSeparator();
		
		fileMenu.add(openProject);		
		fileMenu.add(saveProject);
		fileMenu.add(saveProjectAs);
		
		fileMenu.addSeparator();
		
		fileMenu.add(exportProject);
			exportProject.add(exportAsVideo);
			
			
		editMenu.add(preferences);
		
		
		//---------{TOOLS MENU}-------------------------------------------------		
		add(fileMenu);
		add(editMenu);
	}
}
