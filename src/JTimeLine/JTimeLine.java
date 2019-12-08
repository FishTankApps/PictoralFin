package JTimeLine;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import objectBinders.Frame;
import objectBinders.Theme;

public class JTimeLine extends JPanel {
	
	public static final int NEXT_FRAME = -1;
	
	private static final long serialVersionUID = -7284429791726637894L;
	
	private FrameTimeLine frameTimeLine;
	private JScrollPane scrollPane;
	private JPanel timeLinePanel;
	
	public JTimeLine(Theme theme) {		
		frameTimeLine = new FrameTimeLine(theme);		
		
		timeLinePanel = new JPanel();
		timeLinePanel.setBackground(theme.getPrimaryBaseColor());
		scrollPane = new JScrollPane(timeLinePanel);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(50);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		timeLinePanel.add(frameTimeLine);

		
		add(scrollPane);
	}
	
	public void setSelectedIndex(int index) {
		frameTimeLine.setSelectedFrame(index);
	}
	
	public void updateSizes() {
		frameTimeLine.setHeight(250);
		this.setPreferredSize(getParent().getSize());
		scrollPane.setPreferredSize(getParent().getSize());
		
		revalidate();
		repaint();
	}
	
	public void addFrame(Frame frame) {
		frameTimeLine.addFrame(frame);
	}
}
