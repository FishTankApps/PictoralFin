package com.fishtankapps.pictoralfin.jComponents.pictureEditor;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public class ImageEditorToolPopUpMenu {

	private JPopupMenu menu;
	private ImageEditorTool tool;
	
	private ActionListener ADD_TO_FAVORITES = e->{
		PictoralFin pictoralFin = Utilities.getPictoralFin(tool);
		
		pictoralFin.getConfiguration().getDataFile().addFavoriteImageEditorTool(tool.getToolName());		
		pictoralFin.getImageEditor().getImageEditorToolkitPanel().updateFavoritesPanel(pictoralFin.getImageEditor(), pictoralFin);
	};
	
	private ActionListener REMOVE_FROM_FAVORITES = e->{
		PictoralFin pictoralFin = Utilities.getPictoralFin(tool);
		
		pictoralFin.getConfiguration().getDataFile().removeFavoriteImageEditorTool(tool.getToolName());		
		pictoralFin.getImageEditor().getImageEditorToolkitPanel().updateFavoritesPanel(pictoralFin.getImageEditor(), pictoralFin);
	};
	
	public ImageEditorToolPopUpMenu(ImageEditorTool tool, int x, int y) {		
		this.tool = tool;
		
		menu = new JPopupMenu();

		if(!Utilities.getPictoralFin(tool).getConfiguration().getDataFile().isImageEditorToolAFavorite(tool.getToolName())) {			
			JMenuItem addToFavorites = new JMenuItem("Add to Favorites");
			addToFavorites.addActionListener(ADD_TO_FAVORITES);			
			menu.add(addToFavorites);	
		} else {
			JMenuItem removeFromFavorites = new JMenuItem("Remove to Favorites");
			removeFromFavorites.addActionListener(REMOVE_FROM_FAVORITES);			
			menu.add(removeFromFavorites);	
		}
		
		
		menu.show(tool, x, y);
	}
}
