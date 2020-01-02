package mainFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import JTimeLine.JTimeLine;
import interfaces.Themed;
import jComponents.videoEditor.VideoEditor;
import jComponents.videoEditor.VideoTopBar;
import listeners.GlobalFocusListener;
import listeners.OnMainFrameClosed;
import listeners.OnWindowResizedListener;
import objectBinders.Settings;
import objectBinders.Theme;
import utilities.Utilities;

public class PictoralFin extends JFrame {
	
	private static final long serialVersionUID = 6656205076381846860L;
	
	// =======[ TABS ]---------
	// private PictureEditor pictureEditor;
	private VideoEditor videoEditor;
	private Settings settings;

	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private JTimeLine timeLine;
	private JSplitPane horizontalSplitPane;

	public PictoralFin() {
		settings = new Settings();
		settings.setTheme(Theme.RED_METAL_THEME);
		setUpFrame();
		
		this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	refreshSizes();
            }
        });
	}

	void launch() {
		setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				horizontalSplitPane.setDividerLocation(0.75);
			}
		});		
	}
	
	// ----------{ SET-UP }-------------------------------------------------------------------------
	private void setUpFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("PictoralFin");
		setSize(800, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setIconImage(getOrcaIcon());
		setLocationRelativeTo(null);
		addWindowListener(new OnMainFrameClosed());
		addComponentListener(new OnWindowResizedListener());

		this.setJMenuBar(new VideoTopBar());
		
		mainPanel = createMainPanel();

		add(mainPanel);

		GlobalFocusListener gfl = new GlobalFocusListener();
		for (Component c : Utilities.getAllSubComponents(this)) {
			c.setFocusable(true);
			c.addFocusListener(gfl);
			
			if(c instanceof Themed)
				((Themed) c).applyTheme(settings.getTheme());
		}

	}

	private JPanel createMainPanel() {
		JPanel mainPanel;

		mainPanel = new JPanel(new GridLayout(1, 0, 1, 1));

		timeLine = new JTimeLine(settings.getTheme());			

		horizontalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		horizontalSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
				e -> refreshSizes());

		horizontalSplitPane.setTopComponent(createTabbedPane());
		horizontalSplitPane.setBottomComponent(timeLine);

		horizontalSplitPane.setOneTouchExpandable(false);
		horizontalSplitPane.setBackground(settings.getTheme().getSecondaryHighlightColor());
		horizontalSplitPane.setForeground(Color.RED);
		
		mainPanel.add(horizontalSplitPane);

		return mainPanel;
	}

	private JTabbedPane createTabbedPane() {

		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(settings.getTheme().getTitleFont(), Font.PLAIN, 20));
		tabbedPane.setBackground(settings.getTheme().getPrimaryBaseColor());

		videoEditor = new VideoEditor();

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

	public JTimeLine getTimeLine() {
		return timeLine;
	}
	public PictureImporter getPictureImporter() {
		return new PictureImporter(this);
	}
	public Settings getSettings() {
		return settings;
	}

	private void refreshSizes() {
		timeLine.updateSizes();

		horizontalSplitPane.revalidate();
		horizontalSplitPane.repaint();
	}



	public static BufferedImage getOrcaIcon() {	

		try {
			return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("Kinetic Icon.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		};
		return null;
	}
}
