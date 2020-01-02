package mainFrame;

import static globalValues.GlobalVariables.orca;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import JTimeLine.JTimeLine;
import jComponents.videoEditor.VideoEditor;
import listeners.GlobalFocusListener;
import listeners.OnMainFrameClosed;
import listeners.OnWindowResizedListener;
import objectBinders.Frame;
import objectBinders.Settings;
import tools.MiscTools;

public class PictoralFin {

	// =======[ TABS ]---------
	// private PictureEditor pictureEditor;
	private VideoEditor videoEditor;
	private Settings settings;

	private JFrame frame;
	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private JTimeLine timeLine;
	private JSplitPane horizontalSplitPane;

	public PictoralFin() {
		settings = new Settings();
		setUpFrame();
	}

	// ----------{ SET-UP
	// }-------------------------------------------------------------------------
	public void setUpFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle("PictoralFin");
		frame.setSize(800, 600);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setIconImage(orca);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new OnMainFrameClosed());
		frame.addComponentListener(new OnWindowResizedListener());

		mainPanel = getMainPanel();

		frame.add(mainPanel);

		GlobalFocusListener gfl = new GlobalFocusListener();
		for (Component c : MiscTools.getAllSubComponents(frame)) {
			c.setFocusable(true);
			c.addFocusListener(gfl);
		}

	}

	public JPanel getMainPanel() {
		JPanel mainPanel;

		mainPanel = new JPanel(new GridLayout(1, 0, 1, 1));

		timeLine = new JTimeLine(settings.getTheme());

		for (int count = 0; count < 20; count++)
			timeLine.addFrame(new Frame(orca));

		horizontalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		horizontalSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
				e -> onDividerLocationChange());

		horizontalSplitPane.setTopComponent(getTabbedPane());
		horizontalSplitPane.setBottomComponent(timeLine);

		horizontalSplitPane.setOneTouchExpandable(false);
		horizontalSplitPane.setBackground(settings.getTheme().getSecondaryHighlightColor());

		mainPanel.add(horizontalSplitPane);

		return mainPanel;
	}

	public JTabbedPane getTabbedPane() {

		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(settings.getTheme().getTitleFont(), Font.PLAIN, 20));
		tabbedPane.setBackground(settings.getTheme().getPrimaryBaseColor());

		videoEditor = new VideoEditor(settings.getTheme());

		ImageIcon kineticIcon = null, staticIcon = null;
		try {
			kineticIcon = new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("VideoIcon.jpg")));
			staticIcon  = new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("PictureIcon.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		tabbedPane.addTab("  Kinetic", kineticIcon, videoEditor);
		tabbedPane.addTab("  Static", staticIcon, null);

		tabbedPane.addChangeListener(e -> {
//			frame.setJMenuBar((tabbedPane.getSelectedIndex() == 0) ? videoTopBar : pictureTopBar);
//			openView = tabbedPane.getSelectedIndex();
//			if (tabbedPane.getSelectedIndex() == 1) {
//				videoEditor.detachFrameTimeLine();
//				pictureEditor.attachFrameTimeLine();
//				frameTimeLine.setBackground(settings.getTheme().getPrimaryBaseColor());
//			} else {
//				pictureEditor.detachFrameTimeLine();
//				videoEditor.attachFrameTimeLine();
//				frameTimeLine.setBackground(settings.getTheme().getPrimaryHighlightColor());
//			}
//
//			refresh();
		});

		return tabbedPane;
	}

	public void launch() {
		frame.setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				horizontalSplitPane.setDividerLocation(0.75);
			}
		});		
	}

	public void onDividerLocationChange() {
		timeLine.updateSizes();

		horizontalSplitPane.revalidate();
		horizontalSplitPane.repaint();
	}
}
