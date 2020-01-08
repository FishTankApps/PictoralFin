package JTimeLine;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import mainFrame.PictoralFin;
import objectBinders.Frame;

public class JTimeLine extends JPanel {
	
	public static final int NEXT_FRAME = -1;
	
	private static final long serialVersionUID = -7284429791726637894L;
	
	private FrameTimeLine frameTimeLine;
	private JScrollPane scrollPane;
	private JPanel timeLinePanel;

	
	public JTimeLine(PictoralFin pictoralFin) {		

		frameTimeLine = new FrameTimeLine(pictoralFin);		
		
		timeLinePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		timeLinePanel.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		scrollPane = new JScrollPane(timeLinePanel);		
		scrollPane.getHorizontalScrollBar().setUnitIncrement(50);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
		
		
		timeLinePanel.add(frameTimeLine);

		
		add(scrollPane);
	}
	
	public Frame getCurrentFrame() {
		return frameTimeLine.getSelectedFrame();
	}
	
	public void setSelectedIndex(int index) {
		frameTimeLine.setSelectedFrame(index);
	}
	
	public void addOnFrameSelectionChangeListener(OnFrameSelectionChangedListener listener) {
		frameTimeLine.addOnFrameSelectionChangedListener(listener);
	}
	
	public void updateSizes() {
		frameTimeLine.setHeight(250);
		
		JSplitPane jsp = ((JSplitPane) getParent());
		
		Dimension parentDimension = getParent().getSize();
		Dimension preferedSize = new Dimension(parentDimension.width, 
				parentDimension.height - jsp.getDividerLocation() - scrollPane.getHorizontalScrollBar().getHeight());

		frameTimeLine.setHeight((int) (preferedSize.height / 1.7));
		scrollPane.setPreferredSize(preferedSize);		
		setPreferredSize(preferedSize);
	
		setMinimumSize(new Dimension(1, 100));
		
		revalidate();
		repaint();
	}
	
	public void addFrame(Frame frame) {
		frameTimeLine.addFrame(frame);
	}
	public void addFrame(BufferedImage image) {
		frameTimeLine.addFrame(new Frame(image));
	}
	public void removeFrame(int index) {
		frameTimeLine.removeFrame(index);
	}
}
