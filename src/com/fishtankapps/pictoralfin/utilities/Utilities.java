package com.fishtankapps.pictoralfin.utilities;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;

public class Utilities {
	private Utilities() {
	}

	public static final void debug(String message) {
		if (Constants.DEBUG)
			System.out.println(message);
	}

	public static final void writeToLogFile(String logName, String logText) {
		Calendar calendar = new GregorianCalendar();
		File file = new File("resources/logs/" + logName + " " + calendar.get(Calendar.MONTH) + "." 
				+ calendar.get(Calendar.DATE) + "." + calendar.get(Calendar.YEAR) + " " 
				+ calendar.get(Calendar.HOUR_OF_DAY) + "." + calendar.get(Calendar.MINUTE) 
				+ "." + calendar.get(Calendar.SECOND) + ".txt");
		
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			PrintWriter writer = new PrintWriter(file);
			writer.print(logText);
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static final String stackTraceToString(Exception e) {
		String returnString = "";
		
		for(StackTraceElement element : e.getStackTrace())
			returnString += "\n" + element.toString();
		
		return returnString;
	}
	
	public static final ArrayList<Component> getAllSubComponents(Container toSearch) {
		ArrayList<Component> arrayListOfComponents = new ArrayList<>();

		for (Component c : toSearch.getComponents()) {
			arrayListOfComponents.add(c);

			if (c instanceof Container)
				arrayListOfComponents.addAll(getAllSubComponents((Container) c));
		}

		return arrayListOfComponents;
	}

	public static final void showMessage(String message, String title, boolean alert) {
		JOptionPane.showMessageDialog(null, message, title,
				(alert) ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
	}

	public static final boolean showDoNotShowAgainDialog(String message, String title, boolean alert) {
		JCheckBox doNotShowAgain;
		JOptionPane pane = new JOptionPane("", (alert) ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.OK_OPTION, null, new Object[] { "OK" });

		pane.setMessage(message);

		doNotShowAgain = new JCheckBox("Don't show again");
		doNotShowAgain.setAlignmentX(JCheckBox.RIGHT_ALIGNMENT);

		((JPanel) pane.getComponent(1)).add(doNotShowAgain, 0);

		JDialog dialog = pane.createDialog(title);
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL); // MODELESS = not on top
		dialog.setVisible(true);

		return doNotShowAgain.isSelected();
	}

	public static final PictoralFin getPictoralFin(Component childComponent) {
		Container container = childComponent.getParent();

		while (true)
			if (container instanceof PictoralFin)
				return (PictoralFin) container;

			else
				container = container.getParent();
	}

	public static final String formatFrameLength(long durration) {
		int minutes = (int) (durration / 60_000);
		int seconds = (int) ((durration - (60_000 * minutes)) / 1_000);
		int millis = (int) ((durration - (1_000 * seconds) - (60_000 * minutes)));

		return minutes + ":" + ((seconds < 10) ? "0" : "") + seconds + "."
				+ ((millis < 100) ? ((millis < 10) ? "00" : "0") : "") + millis;
	}

	public static final java.awt.Color invertColor(java.awt.Color c) {
		return new java.awt.Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}

	public static final java.awt.Color jfxColorToAwtColor(javafx.scene.paint.Color color) {
		return new java.awt.Color((int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}

	public static final void playSound(String fileName) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(Utilities.class.getResourceAsStream(fileName));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			System.out.println("There was an Error playing the sound: " + fileName + "\n" + e.getMessage());
		}
	}

	public static final long findGCDofArray(long arr[]) {
		long result = arr[0];
		for (int i = 1; i < arr.length; i++) {
			result = gcd(arr[i], result);

			if (result == 1)
				return 1;
		}

		return result;
	}

	private static final long gcd(long a, long b) {
		if (a == 0)
			return b;
		return gcd(b % a, a);
	}

	
	/**
	 * Use in while loops so it rechecks the case.
	 */
	public static final void doNothing() {
		try {
			Thread.sleep(0, 1);
		} catch (Exception e) {

		}
	}
}
