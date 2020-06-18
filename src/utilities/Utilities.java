package utilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mainFrame.PictoralFin;

public class Utilities {
	private Utilities(){}
	
	public static final void debug(String message) {
		if(Constants.DEBUG)
			System.out.println(message);
	}
	
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
	
	public static final boolean showDoNotShowAgainDialog(String message, String title, boolean alert) {
		JCheckBox doNotShowAgain;
		JOptionPane pane = new JOptionPane("", (alert) ? JOptionPane.ERROR_MESSAGE: JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_OPTION, null, new Object[] {"OK"});

		pane.setMessage(message);

		doNotShowAgain = new JCheckBox("Don't show again");
		doNotShowAgain.setAlignmentX(JCheckBox.RIGHT_ALIGNMENT);
		
		((JPanel) pane.getComponent(1)).add(doNotShowAgain,0);
		
		JDialog dialog = pane.createDialog(title);
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL); // MODELESS = not on top
		dialog.setVisible(true);
		
		return doNotShowAgain.isSelected();
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
	
	public static final PictoralFin getPictoralFin(Component childComponent) {		
		Container container = childComponent.getParent();
		
		while(true)
			if(container instanceof PictoralFin)
				return (PictoralFin) container;
		
			else 
				container = container.getParent();
	}

	public static final String formatFrameLength(long durration) {
		int minutes = (int) (durration / 60_000);
		int seconds = (int) ((durration - (60_000 * minutes)) / 1_000);
		int millis = (int) ((durration - (1_000 * seconds) - (60_000 * minutes)));
		
		return minutes + ":" + ((seconds < 10) ? "0" : "") + seconds + "."
		+  ((millis < 100) ? ((millis < 10) ? "00" : "0") : "") + millis;
	}
	
	public static final Color invertColor(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}

	public static final void playSound(String fileName) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Utilities.class.getResourceAsStream(fileName));
	        Clip clip = AudioSystem.getClip();
	   	    clip.open(audioInputStream); 
	   	    clip.start(); 
		} catch (Exception e) {
			 System.out.println("There was an Error playing the sound: "+ fileName + "\n" + e.getMessage());
		}		
	}

	public static final long findGCDofArray(long arr[]) { 
		long result = arr[0]; 
        for (int i = 1; i < arr.length; i++){ 
            result = gcd(arr[i], result); 
  
            if(result == 1) 
            { 
               return 1; 
            } 
        } 
  
        return result; 
    } 
	
	private static final long gcd(long a, long b)  { 
        if (a == 0) 
            return b; 
        return gcd(b % a, a); 
    } 

	/**
	 * Use in while loops so it rechecks the case.
	 */
	public static final void doNothing() {
		try {
			Thread.sleep(0,1);
		} catch(Exception e) {
			
		}
	}
}


