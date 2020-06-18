package mainFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import globalToolKits.GlobalImageKit;
import globalToolKits.GlobalListenerToolKit;
import interfaces.Closeable;
import jComponents.videoEditor.VideoEditor;
import jComponents.videoEditor.VideoTopBar;
import jTimeLine.JTimeLine;
import listeners.OnMainFrameClosed;
import listeners.OnWindowResizedListener;
import objectBinders.DataFile;
import objectBinders.Settings;
import projectFileManagement.PictoralFinProjectManager;
import utilities.AudioImporter;
import utilities.BufferedImageUtil;
import utilities.ChainGBC;
import utilities.PictureImporter;
import utilities.Utilities;
import utilities.VideoImporter;

public class PictoralFin extends JFrame implements Closeable {
	
	private static final long serialVersionUID = 6656205076381846860L;
	
	public static enum EditorMode {
		STATIC, KINETIC, WAVE;
	}
	
	// =======[ TABS ]---------
	// private PictureEditor pictureEditor;
	private VideoEditor videoEditor;
	private Settings settings;
	private DataFile dateFile;
	public File openProject = null;

	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private JTimeLine timeLine;
	private JSplitPane verticalSplitPane;
	private JProgressBar memoryUsageBar;
	private JLabel statusLabel;
	
	private GlobalListenerToolKit globalListenerToolKit;
	private GlobalImageKit globalImageKit;

	public ArrayList<String> flags;
	
	public PictoralFin() {
		flags = new ArrayList<>();
		settings = Settings.openSettings();
		dateFile = DataFile.openDataFile();
		StatusLogger.logger = new StatusLogger(this);
		
		globalListenerToolKit = new GlobalListenerToolKit(this);
		
		try {
			globalImageKit = new GlobalImageKit();
		} catch (IOException e) {
			throw new RuntimeException("ERROR LOADING PICTURES");
		}
		
		ScheduledThreadPoolExecutor threadpool = new ScheduledThreadPoolExecutor(1);
		threadpool.scheduleAtFixedRate(()->{updateMemoryUsage();}, 2, 1, TimeUnit.SECONDS);
		Thread[] listOfThreads = new Thread[Thread.activeCount()];
		Thread.enumerate(listOfThreads);
		
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
				verticalSplitPane.setDividerLocation(0.75);
			}
		});		

		for(String flag : flags) {
			String[] parts = flag.split("\\$");
			
			if(!settings.getMessagesToNotShow().contains(parts[0])) {
				boolean dontShowAgain = Utilities.showDoNotShowAgainDialog(parts[1], parts[0], false);
				
				if(dontShowAgain)
					settings.addMessageToNotShow(parts[0]);
			}
		}
		flags.clear();
		setStatus("Launch Complete");
	}
	
	// ----------{ SET-UP }-------------------------------------------------------------------------
	private void setUpFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("PictoralFin");
		setSize(800, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setIconImage(globalImageKit.pictoralFinIcon);
		setLocationRelativeTo(null);
		addWindowListener(new OnMainFrameClosed(this));
		addComponentListener(new OnWindowResizedListener(this));

		setJMenuBar(new VideoTopBar(this));
		
		mainPanel = createMainPanel();

		add(mainPanel);
	}

	private JPanel createMainPanel() {
		JPanel mainPanel;
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(settings.getTheme().getSecondaryBaseColor());
		timeLine = new JTimeLine(this);			

		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
				e -> refreshSizes());

		verticalSplitPane.setTopComponent(createTabbedPane());
		verticalSplitPane.setBottomComponent(timeLine);

		verticalSplitPane.setOneTouchExpandable(false);
		verticalSplitPane.setBackground(settings.getTheme().getSecondaryBaseColor());
		verticalSplitPane.setForeground(Color.RED);
		
		JLabel memoryLabel = new JLabel("  Memory Usage:");
		memoryLabel.setFont(new Font(settings.getTheme().getPrimaryFont(), Font.BOLD, 11));
		
		memoryUsageBar = new JProgressBar();
		memoryUsageBar.setMinimumSize(new Dimension(500, 10));
		memoryUsageBar.setMinimum(0);
		memoryUsageBar.setMaximum((int)  Runtime.getRuntime().totalMemory());
		memoryUsageBar.setValue(  (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		
		statusLabel = new JLabel("Status: Launching");
		statusLabel.setFont(memoryLabel.getFont());
		
		mainPanel.add(verticalSplitPane,          (new ChainGBC(0,0)).setFill(true)       .setPadding(0).setWidthAndHeight(4, 1));
		mainPanel.add(statusLabel,                (new ChainGBC(0,1)).setFill(false)      .setPadding(10, 10, 0, 0).setWidthAndHeight(1, 1));
		mainPanel.add(Box.createVerticalStrut(1), (new ChainGBC(1,1)).setFill(true, false).setPadding(0).setWidthAndHeight(1, 1));
		mainPanel.add(memoryLabel,                (new ChainGBC(2,1)).setFill(false)      .setPadding(10, 10, 0, 0).setWidthAndHeight(1, 1));
		mainPanel.add(memoryUsageBar,             (new ChainGBC(3,1)).setFill(false)      .setPadding(10, 10, 0, 0).setWidthAndHeight(1, 1));
		
		
		return mainPanel;
	}

	private JTabbedPane createTabbedPane() {

		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(settings.getTheme().getTitleFont(), Font.PLAIN, 20));
		tabbedPane.setBackground(settings.getTheme().getPrimaryBaseColor());

		videoEditor = new VideoEditor(settings.getTheme(), this);

		ImageIcon kineticIcon = null, staticIcon = null, audioIcon = null;
		try {
			kineticIcon = new ImageIcon(globalImageKit.videoIcon);
			staticIcon  = new ImageIcon(BufferedImageUtil.resizeBufferedImage(globalImageKit.pictureIcon, 33, 33, BufferedImage.SCALE_FAST));
			audioIcon  = new ImageIcon(BufferedImageUtil.resizeBufferedImage(globalImageKit.audioIcon, 33, 33, BufferedImage.SCALE_FAST));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		tabbedPane.addTab("  Kinetic", kineticIcon, videoEditor);
		tabbedPane.addTab("  Static", staticIcon, new JButton("TEST"));
		tabbedPane.addTab("  Wave", audioIcon, new JButton("TEST"));

		tabbedPane.addChangeListener(e -> {
			videoEditor.pausePreview();
			
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

		});

		return tabbedPane;
	} 
	
	public void setCurrentMode(EditorMode mode) {
		if(mode == EditorMode.STATIC) 
			tabbedPane.setSelectedIndex(1);
		else if (mode == EditorMode.KINETIC)
			tabbedPane.setSelectedIndex(0);
		
	}
	
	public JTimeLine getTimeLine() {
		return timeLine;
	}
	public PictureImporter getPictureImporter() {
		return new PictureImporter(this);
	}
	public AudioImporter getAudioImporter() {
		return new AudioImporter(this);
	}
	public VideoImporter getVideoImporter() {
		return new VideoImporter(this);
	}
	public Settings getSettings() {
		return settings;
	}
	public DataFile getDataFile() {
		return dateFile;
	}
		
	void setStatus(String status) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText("Status: " + status);
				statusLabel.repaint();
			}
		});	
	}
	public void setOpenProjectFile(File projectFile) {
		openProject = projectFile;
		
		if(openProject != null)
			setTitle("Pictoral Fin ~ " + openProject.getName());
	}
	
	public void openProject(String filePath) {
		PictoralFinProjectManager.openProject(this, filePath);	
	}
	
	public void saveProject() {
		PictoralFinProjectManager.saveProject(PictoralFin.this, (openProject == null) ? null : openProject.getAbsolutePath());	
	}
	public void saveProjectAs() {
		PictoralFinProjectManager.saveProject(PictoralFin.this, null);
	}
	public VideoEditor getVideoEditor() {
		return videoEditor;
	}
	
	private void updateMemoryUsage(){
		if(Runtime.getRuntime().freeMemory() < 10000)
			System.gc();
		
		memoryUsageBar.setMaximum((int)  Runtime.getRuntime().totalMemory());
		memoryUsageBar.setValue(  (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));		
		
		memoryUsageBar.repaint();
	}
	
	private void refreshSizes() {
		timeLine.updateSizes();

		verticalSplitPane.revalidate();
		verticalSplitPane.repaint();
	}

	public GlobalListenerToolKit getGlobalListenerToolKit() {
		return globalListenerToolKit;
	}
	public GlobalImageKit getGlobalImageKit() {
		return globalImageKit;
	}

	@Override
	public void close() {
		for(Component c : Utilities.getAllSubComponents(this)) {
			if(c instanceof Closeable)
				((Closeable) c).close();
		}
	}
}