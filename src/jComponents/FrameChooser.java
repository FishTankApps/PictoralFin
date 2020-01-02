package jComponents;

import static globalValues.GlobalVariables.orca;
import static globalValues.GlobalVariables.settings;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import utilities.BufferedImageUtil;
import utilities.Utilities;

public class FrameChooser {
	private OnFrameClickedListener ofcl = new OnFrameClickedListener();
	private ArrayList<BufferedImage> resizedFrames;
	private ArrayList<JButton> buttons;	

	private JPanel mainPanel, buttonsPanel;
	private JButton done, cancel;
	private JScrollPane jsp;
	private JLabel message;
	private JFrame frame;
	
	private boolean isControlDown = false;
	private boolean finished = false;
	private boolean[] choosen;
	
	
	
	public FrameChooser(ArrayList<BufferedImage> frames, String message) {
		resizedFrames = new ArrayList<>();
		choosen = new boolean[frames.size()];
		
		Utilities.fillArray(choosen, false);
		
		
		buttons = new ArrayList<>();
		
		frame = new JFrame();
		frame.setSize(900, 300);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Frame Chooser");
		frame.setIconImage(orca);

		this.message = new JLabel(message);
		this.message.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 15));
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 5));
		buttonsPanel.setBackground(settings.getTheme().getPrimaryBaseColor());

		jsp = new JScrollPane(buttonsPanel);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(650, 130));
		
		done = new JButton("Done");
		done.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 13));
		done.addActionListener(new OnButtonClickedListener());
		
		cancel = new JButton("Cancel");
		cancel.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 13));
		cancel.addActionListener(new OnButtonClickedListener());
		
		
		SpringLayout sl = new SpringLayout();
		mainPanel = new JPanel(sl) {
			private static final long serialVersionUID = 5438355355530853655L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(settings.getTheme().getPrimaryHighlightColor());
				g.fillRoundRect(0, -20, getWidth(), 60, 20, 20);
				
				g.setColor(settings.getTheme().getSecondaryHighlightColor());
				g.fillRoundRect(320, getHeight() - 40, 260, 60, 40, 40);
			}
		};
		mainPanel.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent arg0) {
				isControlDown = arg0.isControlDown();
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		sl.putConstraint(SpringLayout.NORTH, this.message, 10, SpringLayout.NORTH, mainPanel);
		sl.putConstraint(SpringLayout.EAST, this.message, 10, SpringLayout.EAST, mainPanel);
		sl.putConstraint(SpringLayout.WEST, this.message, 10, SpringLayout.WEST, mainPanel);
		
		sl.putConstraint(SpringLayout.NORTH, jsp, 60, SpringLayout.SOUTH, this.message);
		
		sl.putConstraint(SpringLayout.EAST, jsp, -10, SpringLayout.EAST, mainPanel);
		sl.putConstraint(SpringLayout.WEST, jsp, 10, SpringLayout.WEST, mainPanel);
		
		sl.putConstraint(SpringLayout.NORTH, done, 10, SpringLayout.SOUTH, jsp);
		sl.putConstraint(SpringLayout.NORTH, cancel, 10, SpringLayout.SOUTH, jsp);
		
		sl.putConstraint(SpringLayout.SOUTH, done, -5, SpringLayout.SOUTH, mainPanel);
		sl.putConstraint(SpringLayout.SOUTH, cancel, -5, SpringLayout.SOUTH, mainPanel);
		
		sl.putConstraint(SpringLayout.WEST, cancel, 350, SpringLayout.WEST, mainPanel);
		sl.putConstraint(SpringLayout.EAST, done, -350, SpringLayout.EAST, mainPanel);
		
		mainPanel.add(this.message);
		mainPanel.add(jsp);
		mainPanel.add(done);
		mainPanel.add(cancel);
		
		mainPanel.setBackground(settings.getTheme().getPrimaryBaseColor());

		frame.add(mainPanel);	
		frame.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				Utilities.fillArray(choosen, false);
				finished = true;
			}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
			
		});
		
		int index = 0;
		for(BufferedImage bi : frames.toArray(new BufferedImage[frames.size()])) {			
			bi = BufferedImageUtil.resizeBufferedImage(bi, 100, 100, BufferedImage.SCALE_FAST);
			resizedFrames.add(bi);
			
			JButton button = new JButton();
			button.setPreferredSize(new Dimension(100, 100));
			
			Graphics2D g = bi.createGraphics();
			g.setStroke(new BasicStroke(10));
			g.setColor((choosen[index]) ? settings.getTheme().getSecondaryHighlightColor() : settings.getTheme().getPrimaryBaseColor());
			g.drawRoundRect(0, 0, bi.getWidth(), bi.getHeight(), 20, 20);
			g.drawRect(0, 0, bi.getWidth(), bi.getHeight());
			
			button.setIcon(new ImageIcon(bi));
			
			button.addActionListener(ofcl);
			buttonsPanel.add(button);
			buttons.add(button);
			index++;
		}	
	}
	public boolean[] show() {
		frame.setVisible(true);		
		while(!finished) try{Thread.sleep(1);}catch(Exception e){}
		return choosen;
	}
	
	private int getButtonIndex(Object clickedButton) {
		for(JButton button : buttons) 
			if(button == clickedButton)
				return buttons.indexOf(button);
		
		return -1;
	}
	private class OnFrameClickedListener implements ActionListener {
		int startIndex = -1;
		
		public void actionPerformed(ActionEvent arg0) {
			int index = getButtonIndex(arg0.getSource());			
			
			if(isControlDown) {
				if(startIndex == -1) {
					startIndex = index;
				} else {			
					for(int i = startIndex; i <= index; i++) {
						choosen[i] = true;
						refresh(i);
					}
					startIndex = -1;
				}
			} else {
				startIndex = -1;
				choosen[index] = !choosen[index];
			}
			
			
			refresh(index);
			mainPanel.requestFocus();
		}		
	}
	private class OnButtonClickedListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == cancel) 
				Utilities.fillArray(choosen, false);			
			
			frame.setVisible(false);
			finished = true;
		}		
	}
		
	private void refresh(int changedIndex) {
		BufferedImage bi = resizedFrames.get(changedIndex);
		
		Graphics2D g = bi.createGraphics();
		g.setStroke(new BasicStroke(10));
		g.setColor((choosen[changedIndex]) ? settings.getTheme().getSecondaryHighlightColor() : settings.getTheme().getPrimaryBaseColor());
		g.drawRoundRect(0, 0, bi.getWidth(), bi.getHeight(), 20, 20);
		g.drawRect(0, 0, bi.getWidth(), bi.getHeight());
		
		buttons.get(changedIndex).setIcon(new ImageIcon(bi));
		
		mainPanel.repaint();
	}
}
