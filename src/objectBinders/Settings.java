package objectBinders;

import java.awt.Dimension;
import java.io.Serializable;

public class Settings implements Serializable{
	private static final long serialVersionUID = -1163643096786559533L;
	
	private Theme theme;
	private Dimension maxPictureSize;
	
	public Settings() {
		theme = Theme.OCEAN_THEME;
		maxPictureSize = new Dimension(720, 1280);
	}

	public final Theme getTheme() {
		return theme;
	}
	public final void setTheme(Theme theme) {
		this.theme = theme;
	}

	public final Dimension getMaxPictureSize() {
		return maxPictureSize;
	}
	public final void setMaxPictureSize(Dimension maxPictureSize) {
		this.maxPictureSize = maxPictureSize;
	}
}
