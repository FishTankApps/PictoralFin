package utilities;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import mainFrame.PictoralFin;

public class Utilities {
	private Utilities(){}
	
	public static final String getFileName(String filePath){
		int startIndex = filePath.lastIndexOf("\\") + 1;
		
		if(startIndex == 0)
			startIndex = filePath.lastIndexOf("/");
		
		int endIndex = filePath.lastIndexOf(".");
		
		String fileName = filePath.substring(startIndex, endIndex);
		
		return fileName;
	}
	public static final String getFileNameAndExtension(String filePath){
		int startIndex = filePath.lastIndexOf("\\") + 1;
		
		if(startIndex == 0)
			startIndex = filePath.lastIndexOf("/");
		
		String fileName = filePath.substring(startIndex, filePath.length());
		
		return fileName;
	}
	public static final ArrayList<Component> getAllSubComponents(Container toSearch){
		ArrayList<Component> arrayListOfComponents = new ArrayList<>();
		
		for(Component c : toSearch.getComponents()){
			arrayListOfComponents.add(c);
			
			if(c instanceof Container) 
				arrayListOfComponents.addAll(getAllSubComponents((Container) c));		
		}
		
		return arrayListOfComponents;
	}
	
	public static final void fillArray(int[][] array, int value){
		for(int x = 0; x < array.length; x++)
			for(int y = 0; y < array[0].length; y++)
				array[x][y] = value;
	}
	public static final void fillArray(boolean[][] array, boolean value){
		for(int x = 0; x < array.length; x++)
			for(int y = 0; y < array[0].length; y++)
				array[x][y] = value;
	}
	public static final void fillArray(boolean[] array, boolean value){
		for(int x = 0; x < array.length; x++)
				array[x] = value;
		}

	public static final void showMessage(String message, String title, boolean alert){
		JOptionPane.showMessageDialog(null, message, title, (alert) ? JOptionPane.ERROR_MESSAGE: JOptionPane.INFORMATION_MESSAGE);
	}

	public static final GridBagConstraints generateGBC(int x, int y, int width, int height){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		
		gbc.insets = new Insets(2,2,2,2);
		gbc.anchor = GridBagConstraints.CENTER;// or NORTH, EAST, SOUTH, WEST, SOUTHWEST etc.
		gbc.fill   = GridBagConstraints.BOTH;
		
		return gbc;
	}
	public static final GridBagConstraints generateGBC(int x, int y, int width, int height, int leftSpacing, int rightSpacing){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		
		gbc.insets = new Insets(2,leftSpacing,2,rightSpacing);
		gbc.anchor = GridBagConstraints.CENTER;// or NORTH, EAST, SOUTH, WEST, SOUTHWEST etc.
		gbc.fill   = GridBagConstraints.BOTH;
		
		return gbc;
	}
	public static final GridBagConstraints generateGBC(int x, int y, int width, int height, int leftSpacing, int rightSpacing, int southSpacing){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		
		gbc.insets = new Insets(2,leftSpacing,southSpacing,rightSpacing);
		gbc.anchor = GridBagConstraints.CENTER;// or NORTH, EAST, SOUTH, WEST, SOUTHWEST etc.
		gbc.fill   = GridBagConstraints.BOTH;
		
		return gbc;
	}

	public static PictoralFin getPictoralFin(Component childComponent) {		
		Container container = childComponent.getParent();
		
		while(true)
			if(container instanceof PictoralFin)
				return (PictoralFin) container;
		
			else 
				container = container.getParent();
	}
}


