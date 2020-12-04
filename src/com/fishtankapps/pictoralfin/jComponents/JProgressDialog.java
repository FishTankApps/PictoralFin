package com.fishtankapps.pictoralfin.jComponents;

import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.fishtankapps.pictoralfin.customExceptions.CanceledException;
import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFinConfiguration;
import com.fishtankapps.pictoralfin.utilities.JokeFactory;

public class JProgressDialog {

	public static String PERCENT = "@%^#&*)(@$";
	public static String VALUE = "@#)(*^#()*%^";
	public static String VALUE_IN_PARENTHESES = "Q(*%&#Q)(%*&";
	
	private int completedValue, progress = 0;
	private JDialog dialog;
	private JProgressBar jProgressBar;
	private String title;
	
	public static PictoralFinConfiguration configuration; 
	
	public JProgressDialog(String title, String message, int completedValue) {
		this.completedValue = completedValue;
		this.title = title;
		
		JOptionPane pane = new JOptionPane("", JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION, null,
				new Object[] {"Cancel"});
		
		pane.setMessage(message.replace(PERCENT, "0%").replace(VALUE, "0").replace(VALUE_IN_PARENTHESES, 
				"(0/" + completedValue+")") + ((configuration.getShowJokeOnProgressDialog()) ? "\n" + JokeFactory.getJoke() : ""));
		jProgressBar = new JProgressBar(0, completedValue);
		jProgressBar.setValue(0);
		pane.add(jProgressBar, 1);
		
		dialog = pane.createDialog(replaceKeys(title));
		dialog.setIconImage(GlobalImageKit.pictoralFinIcon);
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		
		new Thread(()->
		dialog.setVisible(true)).start();
	}

	public void setIcon(Image i) {
		dialog.setIconImage(i);
	}
	
	public void moveForward() {
		moveForward(1);
	}
	public void moveForward(int amount) {
		setProgress(progress + amount);
	}
	public void setProgress(int progress) {
		this.progress = progress;
		jProgressBar.setValue(progress);
		jProgressBar.repaint();
		
		dialog.setTitle(replaceKeys(title));
		
		if(progress >= completedValue)	
			dialog.dispose();
		
		if(!dialog.isVisible() && progress < completedValue)
			throw new CanceledException();
	}
	
	public void close() {
		setProgress(completedValue + 2);
	}
	
	public String replaceKeys(String string) {
		String percent = String.format("%.1f", (progress / (double) completedValue) * 100);
		return string.replace(PERCENT, percent + "%")
					 .replace(VALUE, progress + "")
					 .replace(VALUE_IN_PARENTHESES, "(" + progress + "/" + completedValue+")");
	}
}