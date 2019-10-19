package mainFrame;

import static globalValues.GlobalVariables.orca;
import static globalValues.GlobalVariables.pfk;
import static globalValues.GlobalVariables.openView;
import static globalValues.GlobalVariables.settings;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jComponents.FrameTimeLine;
import jComponents.pictureEditor.PictureEditor;
import jComponents.pictureEditor.PictureTopBar;
import jComponents.videoEditor.VideoEditor;
import jComponents.videoEditor.VideoTopBar;
import listeners.GlobalFocusListener;
import listeners.OnMainFrameClosed;
import listeners.OnWindowResizedListener;
import objectBinders.Picture;
import tools.MiscTools;

public class PictoralFinKinetic {

	//=======[ TABS ]---------
	private PictureEditor pictureEditor;
	private VideoEditor videoEditor;
	
	private JFrame frame;
	private JPanel mainPanel;
	private FrameTimeLine frameTimeLine;
	
	private VideoTopBar videoTopBar;
	private PictureTopBar pictureTopBar;
	private JTabbedPane tabbedPane;
	
	public PictoralFinKinetic() throws Exception {
		frameTimeLine = new FrameTimeLine();
		frameTimeLine.addFrame(orca);
		
		pfk = this;			
	}
	public void launch() {
		setUpFrame();
		refresh();
		videoEditor.setDividersToDefaultLocations();		
	}
	
	//----------{ SET-UP }-------------------------------------------------------------------------
	public void setUpFrame() {		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle("PictoralFin Kinetic");
		frame.setSize(800, 600);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setIconImage(orca);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new OnMainFrameClosed());
		frame.addComponentListener(new OnWindowResizedListener());	
		
		mainPanel = getMainPanel();
		
		videoTopBar = new VideoTopBar();
		pictureTopBar = new PictureTopBar();
		frame.setJMenuBar(videoTopBar);
		
		frame.add(mainPanel);		
		frame.setVisible(true);
		
		GlobalFocusListener gfl = new GlobalFocusListener();
		for(Component c : MiscTools.getAllSubComponents(frame)) {
			c.setFocusable(true);
			c.addFocusListener(gfl);			
		}
			
	}
	public JPanel getMainPanel() {
		JPanel mainPanel;
		
		mainPanel = new JPanel(new GridLayout(1,0,1,1));		
		
		mainPanel.add(getTabbedPane());
		mainPanel.setBackground(settings.getTheme().getSecondaryBaseColor());
		
		return mainPanel;
	}
	public JTabbedPane getTabbedPane() {
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(settings.getTheme().getTitleFont(), Font.PLAIN, 20));
		tabbedPane.setBackground(settings.getTheme().getPrimaryBaseColor());

		pictureEditor = new PictureEditor();
		videoEditor = new VideoEditor();
		
		tabbedPane.addTab("PictoralFin Kinetic", null, videoEditor);
		tabbedPane.addTab("PictoralFin Static", null, pictureEditor);
		
		tabbedPane.addChangeListener(e -> {
			frame.setJMenuBar((tabbedPane.getSelectedIndex() == 0) ? videoTopBar : pictureTopBar);
			openView = tabbedPane.getSelectedIndex();
			if(tabbedPane.getSelectedIndex() == 1) {				
				videoEditor.detachFrameTimeLine();
				pictureEditor.attachFrameTimeLine();
				frameTimeLine.setBackground(settings.getTheme().getPrimaryBaseColor());
			} else {
				pictureEditor.detachFrameTimeLine();
				videoEditor.attachFrameTimeLine();
				frameTimeLine.setBackground(settings.getTheme().getPrimaryHighlightColor());
			}

			
			refresh();
		});
		
		return tabbedPane;
	}

	
	//----------{ STATIC TOOLS }-------------------------------------------------------------------
	public void refresh(){
		
		if(tabbedPane.getSelectedIndex() == 0)
			videoEditor.refresh();
		else
			pictureEditor.refresh();
		
		frame.repaint();		
		mainPanel.requestFocus();
	}
	public void requestFocus() {
		mainPanel.requestFocus();
	}
	
    //----------{ TOOLS }--------------------------------------------------------------------------
    public void centerImage(){
		//getBottomMenu().setScale((mainPanel.getHeight() / ((double) getLayerPicker().getCurrentImage(false).getHeight() + 500)));
		//setPictureY(75);
		//setPictureX((int) (((getFrame().getWidth())/2)-(((getLayerPicker().getCurrentImage(false).getWidth() * getBottomMenu().getScale()))/2)));
		//refresh();
	}
	public void centerOnPixel(int x, int y){
	//	int screenCenterX = (getFrame().getWidth() / 2);
		//int screenCenterY = (getFrame().getHeight() / 2);
		
	//	int pictureScaledX = (int) (x * getBottomMenu().getScale());
		//int pictureScaledY = (int) (y * getBottomMenu().getScale());
		
	//	setPictureY(screenCenterY - pictureScaledY);
	//	setPictureX(screenCenterX - pictureScaledX);
	//	refresh();
	}
	public int[] getMousePointOnImage(){
    	int x, y;
    	
    	x = (int) ((frame.getMousePosition().getX() - pictureEditor.getPictureX()) / pictureEditor.getScale());
    	y = (int) ((frame.getMousePosition().getY() - pictureEditor.getPictureY()) / pictureEditor.getScale());
    	
    	return new int[]{x,y};
	}
	
	
	//----------{ GETTERS }------------------------------------------------------------------------
	public VideoEditor getVideoEditor() {
		return videoEditor;
	}
	public FrameTimeLine getFrameTimeLine() {
		return frameTimeLine;
	}
	public Picture getCurrentFrame() {
		return frameTimeLine.getCurrentFrame();
	}
	public JFrame getFrame() {
		return frame;
	}
	public PictureEditor getPictureEditor() {
		return pictureEditor;
	}
}
