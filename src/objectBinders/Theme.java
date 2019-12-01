package objectBinders;

import java.awt.Color;
import java.io.Serializable;

public class Theme implements Serializable{

	private static final long serialVersionUID = -1417979899096675597L;

	public static Theme OCEAN_THEME = new Theme(new Color(25,25,25), new Color(255,255,250), new Color(93,137,170),  new Color(0,187,179), "Papyrus", "Blackadder ITC", false);
	public static Theme RED_METAL_THEME = new Theme(new Color(169,169,169), new Color(192,192,192), new Color(240,26,26),  new Color(135,23,23), "BankGothic Lt BT", "Magneto", true);
	
	private Color primaryHighlightColor, secondaryHighlightColor, secondaryBaseColor, primaryBaseColor;
	private String primaryFont, titleFont;
	private boolean sharp;
	
	
	public Theme(Color primaryBaseColor, Color secondaryBaseColor, Color primaryHighlightColor, Color secondaryHighlightColor, String primaryFont, boolean sharp) {
		this.primaryHighlightColor = primaryHighlightColor;
		this.secondaryHighlightColor = secondaryHighlightColor;
		this.secondaryBaseColor = secondaryBaseColor;
		this.primaryBaseColor = primaryBaseColor;
		this.primaryFont = primaryFont;
		this.titleFont = primaryFont;
		this.sharp = sharp;
	}
	public Theme(Color primaryBaseColor, Color secondaryBaseColor, Color primaryHighlightColor, Color secondaryHighlightColor,  String primaryFont, String titleFont, boolean sharp) {
		this.primaryHighlightColor = primaryHighlightColor;
		this.secondaryHighlightColor = secondaryHighlightColor;
		this.secondaryBaseColor = secondaryBaseColor;
		this.primaryBaseColor = primaryBaseColor;
		this.primaryFont = primaryFont;
		this.titleFont = titleFont;
		this.sharp = sharp;
	}
	
	public final Color getPrimaryHighlightColor() {
		return primaryHighlightColor;
	}
	public final Color getSecondaryHighlightColor() {
		return secondaryHighlightColor;
	}
	public final Color getSecondaryBaseColor() {
		return secondaryBaseColor;
	}
	public final Color getPrimaryBaseColor() {
		return primaryBaseColor;
	}
	
	public final String getPrimaryFont() {
		return primaryFont;
	}
	public final String getTitleFont() {
		return titleFont;
	}
	
	public final boolean isSharp() {
		return sharp;
	}
}
