package com.fishtankapps.pictoralfin.jComponents.videoEditor;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalListenerToolKit;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.utilities.Constants;
import com.fishtankapps.pictoralfin.utilities.VideoUtil;

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
		importFrames.addActionListener(GlobalListenerToolKit.onAddPictureRequest);
		importFrames.setAccelerator(KeyStroke.getKeyStroke('I', Constants.CTRL));
		
		importAudio = new JMenuItem("Import Audio(s)");
		importAudio.addActionListener(GlobalListenerToolKit.onAddAudioRequest);
		importAudio.setAccelerator(KeyStroke.getKeyStroke('I', Constants.CTRL_SHIFT));
		
		importVideo = new JMenuItem("Import Video(s)");
		importVideo.addActionListener(GlobalListenerToolKit.onAddVideoRequest);
			
		
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
		exportAsVideo.setAccelerator(KeyStroke.getKeyStroke('E', Constants.CTRL));
		
		editMenu = new JMenu(" Edit ");
		
		preferences = new JMenuItem("Prefereces");
		preferences.setAccelerator(KeyStroke.getKeyStroke('P', Constants.CTRL));
		preferences.addActionListener(e->pictoralFin.getConfiguration().openConfigurationEditor());
		
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
		editMenu.addSeparator();			
		
		//---------{TOOLS MENU}-------------------------------------------------		
		add(fileMenu);
		add(editMenu);
	}
}
