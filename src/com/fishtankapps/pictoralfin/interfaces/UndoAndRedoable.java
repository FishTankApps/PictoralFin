package com.fishtankapps.pictoralfin.interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.fishtankapps.pictoralfin.utilities.FileUtils;

public abstract class UndoAndRedoable<E> implements Serializable, Cloneable {

	private static final long serialVersionUID = 3553145605525597592L;
	private transient ArrayList<File> history;
	private transient int currentVersion = -1;

	public final void logUndoableChange() {
		if(history == null)
			history = new ArrayList<>();
		
		while (currentVersion + 1 < history.size())
			history.remove(history.size() - 1);

		currentVersion++;

		File temp = FileUtils.createTempFile("UndoHistoryFrame(v_" + currentVersion + ")", ".frame");

		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp));
			out.writeObject(this);
			out.close();

			history.add(temp);
		} catch (Exception e) {
			System.err.println("Error saving frame to file: ");
			e.printStackTrace();
		}
	}
	
	public final boolean undo() {
		if(history == null)
			history = new ArrayList<>();
		
		if(currentVersion == 0)
			return false;
			
		try {
			ObjectInputStream out = new ObjectInputStream(new FileInputStream((history.get(--currentVersion))));
			
			@SuppressWarnings("unchecked")
			E previousVersion = (E) out.readObject();			
			out.close();

			override(previousVersion);
						
			return true;
		} catch (Exception e) {
			System.err.println("Frame.undo() - Error recalling frame from file: ");
			e.printStackTrace();
		}	
		
		return false;
	}

	public final boolean redo() {
		if(history == null)
			history = new ArrayList<>();
		
		if(currentVersion == history.size() - 1)
			return false;
		
		try {
			ObjectInputStream out = new ObjectInputStream(new FileInputStream((history.get(++currentVersion))));
			
			@SuppressWarnings("unchecked")
			E previousVersion = (E) out.readObject();			
			out.close();

			override(previousVersion);
			
			return true;
		} catch (Exception e) {
			System.err.println("Frame.redo() - Error recalling frame from file: ");
			e.printStackTrace();
		}	
				
		return true;
	}
	
	protected abstract void override(E previousVersion);
}
