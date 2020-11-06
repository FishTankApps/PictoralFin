package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.utilities.Constants;

public class ImageTopBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private JMenu fileMenu, exportMenu, editMenu;
	private JMenuItem importLayers, importFrame, saveProject, saveProjectAs, openProject, 
	                  exportFrameToFile, exportLayersToFolder, undo, redo, preferences;
	
	public ImageTopBar(PictoralFin pictoralFin){
		
		//---------{FILE MENU}-------------------------------------------------
		fileMenu = new JMenu(" File ");
		exportMenu = new JMenu("Export...");
				
		importLayers = new JMenuItem("Import Layer");
		importLayers.addActionListener(e->pictoralFin.getImageEditor().getLayerSelecter().addLayerToFrame());
		importLayers.setAccelerator(KeyStroke.getKeyStroke('I', Constants.CTRL));
		
		importFrame = new JMenuItem("Import Frame");
		importFrame.addActionListener(e->Frame.importFrame(pictoralFin));
		importFrame.setAccelerator(KeyStroke.getKeyStroke('I', Constants.CTRL_SHIFT));
		
		saveProject = new JMenuItem("Save Project");
		saveProject.addActionListener(e->pictoralFin.saveProject());
		saveProject.setAccelerator(KeyStroke.getKeyStroke('S', Constants.CTRL));
		
		saveProjectAs = new JMenuItem("Save Project As");
		saveProjectAs.addActionListener(e->pictoralFin.saveProjectAs());
		saveProjectAs.setAccelerator(KeyStroke.getKeyStroke('S', Constants.CTRL_SHIFT));
		
		openProject = new JMenuItem("Open Project");
		openProject.addActionListener(e->pictoralFin.openProject(null));
		openProject.setAccelerator(KeyStroke.getKeyStroke('O', Constants.CTRL));
		
		exportFrameToFile = new JMenuItem("Export Frame to File");
		exportFrameToFile.setAccelerator(KeyStroke.getKeyStroke('E', Constants.CTRL));
		exportFrameToFile.addActionListener(e->pictoralFin.getTimeLine().getCurrentFrame().exportToFile(pictoralFin));
		
		exportLayersToFolder = new JMenuItem("Export Layers to Folder");
		exportLayersToFolder.setAccelerator(KeyStroke.getKeyStroke('E', Constants.CTRL_SHIFT));
		exportLayersToFolder.addActionListener(e->pictoralFin.getTimeLine().getCurrentFrame().exportToFolder(pictoralFin));
		
		fileMenu.add(importLayers);	
		fileMenu.add(importFrame);	
		
		
		fileMenu.addSeparator();
		
		fileMenu.add(openProject);		
		fileMenu.add(saveProject);
		fileMenu.add(saveProjectAs);
		
		
		fileMenu.addSeparator();
		
		fileMenu.add(exportMenu);
			exportMenu.add(exportFrameToFile);
			exportMenu.add(exportLayersToFolder);	
			
		//---------{EDIT MENU}-------------------------------------------------
		editMenu = new JMenu(" Edit ");
			
		undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke('Z', Constants.CTRL));
		undo.addActionListener(e->pictoralFin.getImageEditor().undo());
		
		redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke('Y', Constants.CTRL));
		redo.addActionListener(e->pictoralFin.getImageEditor().redo());
		
		preferences = new JMenuItem("Prefereces");
		preferences.setAccelerator(KeyStroke.getKeyStroke('P', Constants.CTRL));
		preferences.addActionListener(e->pictoralFin.getConfiguration().openConfigurationEditor());
		
		editMenu.addSeparator();
		editMenu.add(undo);
		editMenu.add(redo);
		
		editMenu.addSeparator();
		editMenu.add(preferences);		
		
		
		add(fileMenu);
		add(editMenu);
	}
}
