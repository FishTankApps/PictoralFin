package jComponents.videoEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import mainFrame.PictoralFin;
import utilities.Constants;
import utilities.VideoUtil;

public class VideoTopBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private JMenu fileMenu, importFiles, exportProject;
	private JMenuItem importFrames, importAudio,   importVideo, importOtherFiles, 
					  saveProject,  saveProjectAs, openProject, exportAsVideo;
	
	public VideoTopBar(PictoralFin pictoralFin){
		
		//---------{FILE MENU}-------------------------------------------------
		fileMenu = new JMenu("File");
		importFiles = new JMenu("Import...");		
		
		importFrames = new JMenuItem("Import Frame(s)");
		importFrames.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddPictureRequest);
		
		importAudio = new JMenuItem("Import Audio(s)");
		importAudio.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddAudioRequest);
		
		importVideo = new JMenuItem("Import Video(s)");
		importVideo.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddVideoRequest);
		
		importOtherFiles = new JMenuItem("Import Other File(s)");
		//importOtherFiles.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddPictureRequest);
			
		
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
		
		fileMenu.add(importFiles);
			importFiles.add(importFrames);
			importFiles.add(importAudio);
			importFiles.add(importVideo);
			importFiles.add(importOtherFiles);
		
		fileMenu.addSeparator();
		
		fileMenu.add(openProject);		
		fileMenu.add(saveProject);
		fileMenu.add(saveProjectAs);
		
		fileMenu.addSeparator();
		
		fileMenu.add(exportProject);
			exportProject.add(exportAsVideo);
		
		
		//---------{TOOLS MENU}-------------------------------------------------		
		add(fileMenu);
	}
	
	class OnItemClicked implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			//if(arg0.getSource() == importFrames)
				//PictureImporter.importPictures(pfk.getFrameTimeLine());
			//else if(arg0.getSource() == exportAsVideo)
			//	VideoTools.exportImagesAVideo(pfk.getVideoEditor().generateSettings());
		}	
	}
}
