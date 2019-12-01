package JTimeLine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interfaces.Themed;
import objectBinders.Frame;
import objectBinders.Theme;

public class FrameTimeLine extends JPanel implements Themed {
	
	private static final long serialVersionUID = -8764321615928981018L;
	private ArrayList<Frame> frames;
	private JScrollPane scrollPane;
	private JPanel buttonsPanel;
	private JFrameButton previousSelection;
	private Theme theme;

	int index = 0;	
	
	public FrameTimeLine(Theme theme) {
		this.theme = theme;
		
		frames = new ArrayList<>();
		
		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(theme.getPrimaryBaseColor());
		
		scrollPane = new JScrollPane(buttonsPanel);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(50);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);	
		scrollPane.setPreferredSize(new Dimension(1000, 700));
		scrollPane.setBackground(theme.getPrimaryBaseColor());
		
		setPreferredSize(new Dimension(1000, 700));		
		add(scrollPane);		
		
		this.setBackground(theme.getPrimaryBaseColor());
	}
	
	public void addFrame(Frame frame) {
		frames.add(frame);
		
		JFrameButton newButton = new JFrameButton(frame, theme);
		newButton.setPreferredHeight(200);
		buttonsPanel.add(newButton);
	}

	public Frame getFrame(int index) {
		return frames.get(index);
	}
	
	public void removeFrame(int index) {
		frames.remove(index);	
		buttonsPanel.remove(index);
	}
	
	public void onJFrameButtonClicked(MouseEvent mouseEvent, JFrameButton button) {
		if(previousSelection != null) {
			previousSelection.setSelected(false);
			previousSelection.repaint();
		}
		
		button.setSelected(true);
		previousSelection = button;
		System.out.println((mouseEvent.getButton() == 1) ? "Left Clicked" : "Right Clicked");
	}

	
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	
	public void setHeight(int height) {
		 for(Component c : buttonsPanel.getComponents())
			 if(c instanceof JFrameButton)
				 ((JFrameButton) c).setPreferredHeight(height);
		 
		 buttonsPanel.revalidate();
	}
}
