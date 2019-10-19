package interfaces;

import java.util.ArrayList;

public abstract class Undoable <E extends Undoable<E>> {

	private ArrayList<E> history = new ArrayList<E>();
	private int currentVersion = -1;
	
	public Undoable() {
		
	}
	
	protected abstract void override(E instance);
	protected abstract E clone();
	
	public void saveInstance() {
		currentVersion++;
		
		while (currentVersion < history.size())
			history.remove(history.size() - 1);
		
		history.add(clone());		
	}
	
	public boolean undo() {
		if(currentVersion == 0)
			return false;
		
		override(history.get(--currentVersion));
		return true;
	}

	public boolean redo() {
		if(currentVersion == history.size() - 1)
			return false;
		
		override(history.get(++currentVersion));		
		return true;
	}
}
