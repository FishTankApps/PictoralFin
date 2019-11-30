package JTimeLine;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import objectBinders.Frame;

public class FrameTimeLine extends JPanel {
	
	private static final long serialVersionUID = -8764321615928981018L;
	private ArrayList<Frame> frames;
	private JScrollPane scrollPane;
	private JPanel buttonsPanel;
	
	int thumbnailSize = 100;
	
	
	public FrameTimeLine() {
		frames = new ArrayList<>();
		
		buttonsPanel = new JPanel();
		scrollPane = new JScrollPane(buttonsPanel);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(50);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);	
		scrollPane.setPreferredSize(new Dimension(1000, 700));
		this.setPreferredSize(new Dimension(1000, 700));
		this.add(scrollPane);		
	}
	
	public void addFrame(Frame frame) {
		frames.add(frame);
		
		JFrameButton newButton = new JFrameButton(frame);
		newButton.setPreferredHeight(300);
		buttonsPanel.add(newButton);
	}

	public Frame getFrame(int index) {
		return frames.get(index);
	}
	
	public void removeFrame(int index) {
		frames.remove(index);	
		buttonsPanel.remove(index);
	}
	
	public void onJFrameButtonClicked() {
		System.out.println("Clicked");
	}
}
