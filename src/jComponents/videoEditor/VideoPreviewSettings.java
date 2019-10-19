package jComponents.videoEditor;

import static globalValues.GlobalVariables.framesPerSecond;
import static globalValues.GlobalVariables.settings;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;

import tools.MiscTools;

public class VideoPreviewSettings extends JPanel {

	private static final long serialVersionUID = 1584668485922313959L;

	private JLabel framesPerSecondLabel;
	private JPanel framesPerSecondPanel;
	private JSlider framesPerSecondSlider;
	private AudioBoard audioBoard;

	public VideoPreviewSettings() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBackground(settings.getTheme().getPrimaryBaseColor());
		this.setMinimumSize(new Dimension(350, 500));

		audioBoard = new AudioBoard();

		setUpFramesPerSecondStuff();

		this.add(framesPerSecondPanel, MiscTools.generateGBC(0, 0, 1, 1));
		this.add(audioBoard, MiscTools.generateGBC(0, 1, 1, 1));
	}

	public void setUpFramesPerSecondStuff() {
		framesPerSecondLabel = new JLabel("Frames Per Second (10):");
		framesPerSecondLabel.setHorizontalTextPosition(JLabel.RIGHT);
		framesPerSecondLabel.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 15));

		framesPerSecondSlider = new JSlider(1, 250, 10);
		framesPerSecondSlider.addChangeListener(e -> {
			framesPerSecond = framesPerSecondSlider.getValue();
			framesPerSecondLabel.setText("Frames Per Second (" + framesPerSecond + "):");
		});
		
		framesPerSecondSlider.setBackground(settings.getTheme().getSecondaryHighlightColor());

		SpringLayout sl = new SpringLayout();
		framesPerSecondPanel = new JPanel(sl) {
			private static final long serialVersionUID = 2426242311828689171L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(settings.getTheme().getPrimaryHighlightColor());
				g.fillRoundRect(0, 0, getWidth()-2, getHeight(), 10, 15);
			}
		};
		
		sl.putConstraint(SpringLayout.WEST, framesPerSecondSlider, 5, SpringLayout.EAST, framesPerSecondLabel);
		sl.putConstraint(SpringLayout.NORTH, framesPerSecondLabel, 11, SpringLayout.NORTH, framesPerSecondPanel);
		sl.putConstraint(SpringLayout.WEST, framesPerSecondLabel, 10, SpringLayout.WEST, framesPerSecondPanel);
		sl.putConstraint(SpringLayout.NORTH, framesPerSecondSlider, 5, SpringLayout.NORTH, framesPerSecondPanel);
		sl.putConstraint(SpringLayout.EAST, framesPerSecondSlider, -10, SpringLayout.EAST, framesPerSecondPanel);
		
		framesPerSecondPanel.setBackground(settings.getTheme().getPrimaryBaseColor());
		
		framesPerSecondPanel.add(framesPerSecondLabel);
		framesPerSecondPanel.add(framesPerSecondSlider);
	}

	public AudioBoard getAudioBoard() {
		return audioBoard;
	}
	
	public void refresh() {
		this.setPreferredSize(this.getParent().getSize());
		framesPerSecondPanel.setPreferredSize(new Dimension(this.getParent().getSize().width - 10, 40));
		audioBoard.refresh();
		
		revalidate();
		repaint();
	}
}
