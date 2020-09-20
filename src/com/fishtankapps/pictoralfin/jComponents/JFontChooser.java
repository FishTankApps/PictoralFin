package com.fishtankapps.pictoralfin.jComponents;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public class JFontChooser extends JFrame{

	private static final long serialVersionUID = 5844485031381749104L;
	
	private JPanel mainPanel;
	
	private Font font;
	private JLabel fontPreview;
	private JButton done;
	
	private boolean fontHasBeenPicked = false;
	
	private JFontChooser(Theme theme, String previewString){
		
		setSize(320, 400);
		setLayout(new GridLayout(1,1,0,0));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Font Chooser");
		setLocationRelativeTo(null);
		addWindowListener(new WindowListener() {

			public void windowClosing(WindowEvent e) {
				font = null;
				fontHasBeenPicked = true;
			}
			
			public void windowOpened(WindowEvent e) {}			
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {				
				font = null;
				fontHasBeenPicked = true;
				
				dispose();
			}		
		});
		
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(theme.getPrimaryBaseColor());
		
		fontPreview = new JLabel("No Font Selected", JLabel.CENTER);
		fontPreview.setForeground(theme.getPrimaryHighlightColor());
		
		JPanel fontPanel = new JPanel();
		fontPanel.setLayout(new GridBagLayout());
		fontPanel.setBackground(theme.getPrimaryBaseColor());
		
		int index = 0;
		for(Font s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
			JButton button = new JButton(s.getName());
			button.setBorderPainted(false);
			button.setFont(s.deriveFont(10f));
			button.setHorizontalTextPosition(JButton.LEFT);
			button.setHorizontalAlignment(JButton.LEFT);
			
			
			button.addActionListener(e->{fontPreview.setFont(font = s.deriveFont(20f)); fontPreview.setText(previewString);});
			fontPanel.add(button, new ChainGBC(0, index++).setFill(true, false).setPadding(0));
		}
		
		JScrollPane pane = new JScrollPane(fontPanel);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setBackground(theme.getPrimaryBaseColor());
		pane.getVerticalScrollBar().setUnitIncrement(10);
		
		done = new JButton("Done");
		done.addActionListener(e->{fontHasBeenPicked = true; dispose();});
		
		mainPanel.add(pane, new ChainGBC(0,0).setFill(true).setPadding(5));
		mainPanel.add(fontPreview, new ChainGBC(0, 1).setFill(true, false).setPadding(5));
		mainPanel.add(done, new ChainGBC(0, 2).setFill(true, false).setPadding(5));
		
		add(mainPanel);
	}
	
	private Font chooseFont() {
		setVisible(true);
		fontHasBeenPicked = false;
		
		while(!fontHasBeenPicked) {Utilities.doNothing();}
		return font;
	}
	
	public static Font showChooserDialog(Theme theme, String preveiwString) {
		return new JFontChooser(theme, preveiwString).chooseFont();
	}
}