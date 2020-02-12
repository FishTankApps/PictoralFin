package utilities;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ChainGBC extends GridBagConstraints {
	private static final long serialVersionUID = -665560057212809379L;

	public ChainGBC(int x, int y) {
		this.gridx = x;
		this.gridy = y;
		
		this.gridx = x;
		this.gridy = y;
		this.gridwidth = 1;
		this.gridheight = 1;
		
		this.insets = new Insets(2,2,2,2);
		this.anchor = GridBagConstraints.WEST;// or NORTH, EAST, SOUTH, WEST, SOUTHWEST etc.
		this.fill   = GridBagConstraints.BOTH;
	}
	
	
	public ChainGBC setWidthAndHeight(int width, int height) {
		this.gridwidth = width;
		this.gridheight = height;
		
		return this;
	}
	
	public ChainGBC setPadding(int padding) {
		this.insets = new Insets(padding,padding,padding,padding);
		return this;
	}
	
	public ChainGBC setPadding(int left, int right, int top, int botton) {
		this.insets = new Insets(top,left,botton,right);
		return this;
	}
	
	public ChainGBC setFill(boolean fill) {
		this.weightx = (fill) ? 1 : 0;
		this.weighty = (fill) ? 1 : 0;
		return this;
	}
	
	public ChainGBC setFill(boolean xFill, boolean yFill) {
		this.weightx = (xFill) ? 1 : 0;
		this.weighty = (yFill) ? 1 : 0;
		return this;
	}
}
