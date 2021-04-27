package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.fishtankapps.pictoralfin.jComponents.JScrollablePanel;
import com.fishtankapps.pictoralfin.listeners.LayerMouseListener;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.DataFile;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;

public class ImageEditorToolKitPanel extends JPanel {

	private static final long serialVersionUID = -2646271125412362864L;

	private ImageEditorTool toolInFocus = null;
	private JPanel favoritesPanel, editorsPanel, effectsPanel;
	private ArrayList<Class<?>> imageEditorTools, imageEffectTools;

	public ImageEditorToolKitPanel(PictoralFin pictoralFin, ImageEditor imageEditor) {
		super(new GridLayout(1, 1, 0, 0));
		setMinimumSize(new Dimension(250, 100));
		
		
		imageEditorTools = new ArrayList<>();
		imageEffectTools = new ArrayList<>();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(pictoralFin.getConfiguration().getTheme().getTitleFont(), Font.PLAIN, 20));
		
		favoritesPanel = new JScrollablePanel(new GridBagLayout());		
		editorsPanel = new JScrollablePanel(new GridBagLayout());		
		effectsPanel = new JScrollablePanel(new GridBagLayout());
		
		favoritesPanel.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		editorsPanel.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		effectsPanel.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		
		JScrollPane favoritesScroller = new JScrollPane(favoritesPanel);
		JScrollPane editorsScroller = new JScrollPane(editorsPanel);
		JScrollPane effectsScroller = new JScrollPane(effectsPanel);
		
		effectsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		editorsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		favoritesScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		effectsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		favoritesScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		effectsScroller.getViewport().setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		editorsScroller.getViewport().setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		favoritesScroller.getViewport().setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		
		tabbedPane.addTab("Favorites", favoritesScroller);
		tabbedPane.addTab("Editors", editorsScroller);
		tabbedPane.addTab("Effects", effectsScroller);
		
		add(tabbedPane);
		
		imageEditor.getImagePreview().addLayerMouseListener(new LayerMouseListener() {

				public void onMousePressed(int xOnImage, int yOnImage, BufferedImage layer) {
					if(toolInFocus != null)
						toolInFocus.onMousePressed(xOnImage, yOnImage, layer, imageEditor.getSelectedFrame());
					
					pictoralFin.repaint();
				}
	
				public void onMouseReleased(int xOnImage, int yOnImage, BufferedImage layer) {
					if(toolInFocus != null)
						toolInFocus.onMouseReleased(xOnImage, yOnImage, layer, imageEditor.getSelectedFrame());	
					
					pictoralFin.repaint();
				}
		
			});
		
		addInstalledEditors(imageEditor, pictoralFin.getConfiguration().getTheme());
		addInstalledEffects(imageEditor, pictoralFin.getConfiguration().getTheme());
		
		updateFavoritesPanel(imageEditor, pictoralFin);
		
		setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
	}
	
	public void requestToolFocus(ImageEditorTool toolInFocus) {
		if(this.toolInFocus != null) {
			this.toolInFocus.isFocusedTool = false;
			toolInFocus.removeClearImageFromImagePreview();
		}
		
		this.toolInFocus = toolInFocus;
		this.toolInFocus.isFocusedTool = true;
		
		repaint();
	}
	
	public void dropToolFocus() {
		this.toolInFocus = null;		
		repaint();
	}
		
	
	private void addInstalledEditors(ImageEditor imageEditor, Theme theme) {
		int heightIndex = 0;
		
		String name = "";
		
		try {
		    URL[] classes = {new File("resources").toURI().toURL()};
		    URLClassLoader child = new URLClassLoader(classes, this.getClass().getClassLoader());
			File folder = new File("resources/imageEditorTools");
			
			for(File imageEditorFile : folder.listFiles()) {
				
				 name = imageEditorFile.getName().substring(0, imageEditorFile.getName().indexOf('.'));
				 
				 if(name.contains("$"))
					 continue;
				 
				 Class<?> classToLoad = Class.forName(name, true, child);	  
				 imageEditorTools.add(classToLoad);
				 
			     Constructor<?> constructor = classToLoad.getConstructor(ImageEditor.class, Theme.class);
			     ImageEditorTool imageEditorTool = (ImageEditorTool) constructor.newInstance(imageEditor, theme);
				
				 editorsPanel.add(imageEditorTool,  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error loading an Image Editor:\n" + name + 
					"\nError Message: \n" + e.getMessage(), "Error Loading Editor", JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
		}
	}

	private void addInstalledEffects(ImageEditor imageEditor, Theme theme) {
		int heightIndex = 0;
		
		String name = "";
		
		try {
		    URL[] classes = {new File("resources").toURI().toURL()};
		    URLClassLoader child = new URLClassLoader(classes, this.getClass().getClassLoader());
			File folder = new File("resources/imageEffectTools");
			
			for(File imageEditorFile : folder.listFiles()) {
				
				 name = imageEditorFile.getName().substring(0, imageEditorFile.getName().indexOf('.'));
				 
				 if(name.contains("$"))
					 continue;
				 
				 Class<?> classToLoad = Class.forName(name, true, child);	  
				 
				 imageEffectTools.add(classToLoad);
				 
			     Constructor<?> constructor = classToLoad.getConstructor(ImageEditor.class, Theme.class);
			     ImageEditorTool imageEditorTool = (ImageEditorTool) constructor.newInstance(imageEditor, theme);
					
			     effectsPanel.add(imageEditorTool,  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));		
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error loading an Image Effect:\n" + name + 
					"\nError Message: \n" + e.getMessage(), "Error Loading Editor", JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
		}
	}
	
	public void updateFavoritesPanel(ImageEditor imageEditor, PictoralFin pictoralFin) {
		int heightIndex = 0;		
		DataFile dataFile = pictoralFin.getConfiguration().getDataFile();
		Theme theme = pictoralFin.getConfiguration().getTheme();
		
		
		favoritesPanel.removeAll();	
		
		JLabel editorsLabel = new JLabel("Editors: ", JLabel.LEFT);
		editorsLabel.setFont(new Font(theme.getTitleFont(), Font.PLAIN, 25));
		editorsLabel.setForeground(theme.getSecondaryBaseColor());
		
		favoritesPanel.add(editorsLabel, new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
		
		for(Class<?> toolClass : imageEditorTools) {		     
			try {
				Constructor<?> constructor = toolClass.getConstructor(ImageEditor.class, Theme.class);
				
				ImageEditorTool imageEditorTool = (ImageEditorTool) constructor.newInstance(imageEditor, theme);
					
				if(dataFile.isImageEditorToolAFavorite(imageEditorTool.getToolName()))
					 favoritesPanel.add((ImageEditorTool) constructor.newInstance(imageEditor, theme), 
							 new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
			}catch (Exception e) {e.printStackTrace();}		    
		}
		
		
		JLabel effectsLabel = new JLabel("Effects: ", JLabel.LEFT);
		effectsLabel.setFont(new Font(theme.getTitleFont(), Font.PLAIN, 25));
		effectsLabel.setForeground(theme.getSecondaryBaseColor());
		
		favoritesPanel.add(effectsLabel, new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
		
		for(Class<?> toolClass : imageEffectTools) {		     
			try {
				Constructor<?> constructor = toolClass.getConstructor(ImageEditor.class, Theme.class);
				
				ImageEditorTool imageEffectTool = (ImageEditorTool) constructor.newInstance(imageEditor, theme);
					
				if(dataFile.isImageEditorToolAFavorite(imageEffectTool.getToolName()))
					 favoritesPanel.add((ImageEditorTool) constructor.newInstance(imageEditor, theme), 
							 new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));
			}catch (Exception e) {e.printStackTrace();}		    
		}
	}
	
}