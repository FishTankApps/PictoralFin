package objectBinders;

import java.awt.Color;
import java.io.Serializable;

public class Theme implements Serializable{

	private static final long serialVersionUID = -1417979899096675597L;

	public static Theme OCEAN_THEME = new Theme(new Color(25,25,25), new Color(255,255,250), new Color(93,137,170),  new Color(0,187,179), "Papyrus", "Blackadder ITC");
	public static Theme RED_METAL_THEME = new Theme(new Color(169,169,169), new Color(192,192,192), new Color(240,26,26),  new Color(135,23,23), "BankGothic Lt BT", "Magneto");
	
	private Color primaryHighlightColor, secondaryHighlightColor, secondaryBaseColor, primaryBaseColor;
	private String primaryFont, titleFont;
	
	
	public Theme(Color primaryBaseColor, Color secondaryBaseColor, Color primaryHighlightColor, Color secondaryHighlightColor, String primaryFont) {
		this.primaryHighlightColor = primaryHighlightColor;
		this.secondaryHighlightColor = secondaryHighlightColor;
		this.secondaryBaseColor = secondaryBaseColor;
		this.primaryBaseColor = primaryBaseColor;
		this.primaryFont = primaryFont;
		this.titleFont = primaryFont;
	}
	public Theme(Color primaryBaseColor, Color secondaryBaseColor, Color primaryHighlightColor, Color secondaryHighlightColor,  String primaryFont, String titleFont) {
		this.primaryHighlightColor = primaryHighlightColor;
		this.secondaryHighlightColor = secondaryHighlightColor;
		this.secondaryBaseColor = secondaryBaseColor;
		this.primaryBaseColor = primaryBaseColor;
		this.primaryFont = primaryFont;
		this.titleFont = titleFont;
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
	
	
	public final void setPrimaryHighlightColor(Color primaryHighlightColor) {
		this.primaryHighlightColor = primaryHighlightColor;
	}
	public final void setSecondaryHighlightColor(Color secondaryHighlightColor) {
		this.secondaryHighlightColor = secondaryHighlightColor;
	}
	public final void setSecondaryBaseColor(Color secondaryBaseColor) {
		this.secondaryBaseColor = secondaryBaseColor;
	}
	public final void setPrimaryBaseColor(Color primaryBaseColor) {
		this.primaryBaseColor = primaryBaseColor;
	}
	
	public final void setPrimaryFont(String primaryFont) {
		this.primaryFont = primaryFont;
	}
	public final void setTitleFont(String titleFont) {
		this.titleFont = titleFont;
	}
}
