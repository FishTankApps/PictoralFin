package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.fishtankapps.pictoralfin.listeners.LayerMouseListener;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;

public class ImageEditorToolKitPanel extends JPanel implements Scrollable  {

	private static final long serialVersionUID = -2646271125412362864L;

	private ImageEditorTool toolInFocus = null;
	private JPanel favoritesPanel, editorsPanel, effectsPanel;
	
	public ImageEditorToolKitPanel(PictoralFin pictoralFin, ImageEditor imageEditor) {
		super(new GridLayout(1, 1, 0, 0));
		setMinimumSize(new Dimension(250, 100));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(pictoralFin.getConfiguration().getTheme().getTitleFont(), Font.PLAIN, 20));
		tabbedPane.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
				
		favoritesPanel = new JPanel(new GridBagLayout());
		favoritesPanel.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		
		editorsPanel = new JPanel(new GridBagLayout());
		editorsPanel.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		
		effectsPanel = new JPanel(new GridBagLayout());
		effectsPanel.setBackground(pictoralFin.getConfiguration().getTheme().getPrimaryBaseColor());
		
		tabbedPane.addTab("Favorites", favoritesPanel);
		tabbedPane.addTab("Editors", editorsPanel);
		tabbedPane.addTab("Effects", effectsPanel);
		
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
		
	public void addInstalledEditors(ImageEditor imageEditor, Theme theme) {
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
				 
			     Constructor<?> constructor = classToLoad.getConstructor(ImageEditor.class, Theme.class);
			     ImageEditorTool instance = (ImageEditorTool) constructor.newInstance(imageEditor, theme);
					
				 editorsPanel.add(instance,  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));	
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error loading an Image Editor:\n" + name + 
					"\nError Message: \n" + e.getMessage(), "Error Loading Editor", JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
		}
		
		editorsPanel.add(Box.createHorizontalGlue(), new ChainGBC(0, heightIndex++).setFill(true, true));
	}

	public void addInstalledEffects(ImageEditor imageEditor, Theme theme) {
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
				 
			     Constructor<?> constructor = classToLoad.getConstructor(ImageEditor.class, Theme.class);
			     ImageEditorTool instance = (ImageEditorTool) constructor.newInstance(imageEditor, theme);
					
			     effectsPanel.add(instance,  new ChainGBC(0, heightIndex++).setFill(true, false).setPadding(10));	
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error loading an Image Effect:\n" + name + 
					"\nError Message: \n" + e.getMessage(), "Error Loading Editor", JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
		}
		
		
		effectsPanel.add(Box.createHorizontalGlue(), new ChainGBC(0, heightIndex++).setFill(true, true));
	}
	
	
	public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
       return 10;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width) - 10;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}