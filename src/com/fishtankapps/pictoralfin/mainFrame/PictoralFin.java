package com.fishtankapps.pictoralfin.mainFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.globalToolKits.GlobalListenerToolKit;
import com.fishtankapps.pictoralfin.interfaces.Closeable;
import com.fishtankapps.pictoralfin.jComponents.JProgressDialog;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageEditor;
import com.fishtankapps.pictoralfin.jComponents.pictureEditor.ImageTopBar;
import com.fishtankapps.pictoralfin.jComponents.videoEditor.VideoEditor;
import com.fishtankapps.pictoralfin.jComponents.videoEditor.VideoTopBar;
import com.fishtankapps.pictoralfin.jTimeLine.JTimeLine;
import com.fishtankapps.pictoralfin.listeners.OnMainFrameClosed;
import com.fishtankapps.pictoralfin.listeners.OnWindowResizedListener;
import com.fishtankapps.pictoralfin.objectBinders.Frame;
import com.fishtankapps.pictoralfin.projectFileManagement.PictoralFinProjectManager;
import com.fishtankapps.pictoralfin.utilities.AudioImporter;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.JokeFactory;
import com.fishtankapps.pictoralfin.utilities.PictureImporter;
import com.fishtankapps.pictoralfin.utilities.Utilities;
import com.fishtankapps.pictoralfin.utilities.VideoImporter;

public class PictoralFin extends JFrame implements Closeable {
	
	private static final long serialVersionUID = 6656205076381846860L;
	
	public static enum EditorMode {
		STATIC, KINETIC, WAVE;
	}
	
	private VideoEditor videoEditor;
	private ImageEditor imageEditor;
	private PictoralFinConfiguration configuration;
	public File openProject = null;

	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private JTimeLine timeLine;
	private JSplitPane verticalSplitPane;
	private JProgressBar memoryUsageBar;
	private JLabel statusLabel, memoryLabel;
	
	private VideoTopBar videoTopBar;
	private ImageTopBar imageTopBar;

	public ArrayList<String> flags;
	
	public PictoralFin() {
		flags = new ArrayList<>();
		configuration = PictoralFinConfiguration.openConfiguration();
		StatusLogger.logger = new StatusLogger(this);
		JProgressDialog.configuration = configuration;
		//FrameManager.frameManager = new FrameManager(this);
		
		GlobalListenerToolKit.initialize(this);
		JokeFactory.initialize(this);
		
		try {
			GlobalImageKit.initialize();
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
			
			if(!configuration.getMessagesToNotShow().contains(parts[0])) {
				boolean dontShowAgain = Utilities.showDoNotShowAgainDialog(parts[1], parts[0], false);
				
				if(dontShowAgain)
					configuration.addMessageToNotShow(parts[0]);
			}
		}
		flags.clear();
		setStatus("GUI Launched...");
	}
	
	// ----------{ SET-UP }-------------------------------------------------------------------------
	private void setUpFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("PictoralFin");
		setSize(800, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setIconImage(GlobalImageKit.pictoralFinIcon);
		setLocationRelativeTo(null);
		addWindowListener(new OnMainFrameClosed(this));
		addComponentListener(new OnWindowResizedListener(this));

		videoTopBar = new VideoTopBar(this);
		imageTopBar = new ImageTopBar(this);
		
		setJMenuBar(videoTopBar);
		
		mainPanel = createMainPanel();

		add(mainPanel);
	}

	private JPanel createMainPanel() {
		JPanel mainPanel;
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(configuration.getTheme().getSecondaryBaseColor());
		timeLine = new JTimeLine(this);			

		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
				e -> refreshSizes());

		verticalSplitPane.setTopComponent(createTabbedPane());
		verticalSplitPane.setBottomComponent(timeLine);

		verticalSplitPane.setOneTouchExpandable(false);
		verticalSplitPane.setBackground(configuration.getTheme().getSecondaryBaseColor());
		verticalSplitPane.setForeground(Color.RED);
		
		memoryLabel = new JLabel("Loading...");
		memoryLabel.setFont(new Font(configuration.getTheme().getPrimaryFont(), Font.BOLD, 11));
		
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
		tabbedPane.setFont(new Font(configuration.getTheme().getTitleFont(), Font.PLAIN, 20));
		tabbedPane.setBackground(configuration.getTheme().getPrimaryBaseColor());

		videoEditor = new VideoEditor(configuration.getTheme(), this);
		imageEditor = new ImageEditor(this);
		
		ImageIcon kineticIcon = null, staticIcon = null, audioIcon = null;
		try {
			kineticIcon = new ImageIcon(GlobalImageKit.videoIcon);
			staticIcon  = new ImageIcon(BufferedImageUtil.resizeBufferedImage(GlobalImageKit.pictureIcon, 33, 33, BufferedImage.SCALE_FAST));
			audioIcon  = new ImageIcon(BufferedImageUtil.resizeBufferedImage(GlobalImageKit.audioIcon, 33, 33, BufferedImage.SCALE_FAST));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		tabbedPane.addTab("  Kinetic", kineticIcon, videoEditor);
		tabbedPane.addTab("  Static", staticIcon, imageEditor);
		tabbedPane.addTab("  Wave", audioIcon, new JButton("Yet to come..."));

		tabbedPane.addChangeListener(e -> {
			videoEditor.pausePreview();
			timeLine.getAudioTimeLine().pause();
			setJMenuBar((tabbedPane.getSelectedIndex() == 0) ? videoTopBar : imageTopBar);
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
	public PictoralFinConfiguration getConfiguration() {
		return configuration;
	}
		
	private String primaryStatus = "", secondaryStatus = "";
	void setStatus(String status) {
		primaryStatus = status;
		secondaryStatus = "";
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText("Status: " + primaryStatus);
				statusLabel.repaint();
			}
		});	
	}
	void setSecondaryStatus(String status) {
		secondaryStatus = status;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText("Status: " + primaryStatus + " [" + secondaryStatus + "]");
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
	
	public void openFrameFile(File file) {
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
			Frame frame = (Frame) input.readObject();
			
			input.close();
			
			timeLine.addFrame(frame);
			tabbedPane.setSelectedIndex(1);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error openning the frame file:\n" + file.getName() + "\n\nError Message:\n" + e.getMessage());
		}
	}
	
	public VideoEditor getVideoEditor() {
		return videoEditor;
	}
	public ImageEditor getImageEditor() {
		return imageEditor;
	}
	
	private void updateMemoryUsage(){
		
		double usableMeg =  (Runtime.getRuntime().totalMemory() / 1_000_000.0);
		double usedMeg   = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1_000_000.0);
		
		String usableMemory, usedMemory;
		if(usableMeg > 1000) {
			usableMemory = String.format("%.2f", usableMeg  / 1000.0) + " GB";
			usedMemory =   String.format("%.2f", usedMeg    / 1000.0) + " GB";
		} else {
			usableMemory = String.format("%.2f", usableMeg) + " MB";
			usedMemory =   String.format("%.2f", usedMeg)   + " MB";
		}
		
		memoryLabel.setText("Memory Usage (" + usedMemory + "/" + usableMemory + ") :");
		
		memoryUsageBar.setMaximum((int)  (Runtime.getRuntime().totalMemory() / 1000));
		memoryUsageBar.setValue(  (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000));		
		
		memoryUsageBar.repaint();
	}
	
	private void refreshSizes() {
		timeLine.updateSizes();

		verticalSplitPane.revalidate();
		verticalSplitPane.repaint();
	}

	@Override
	public void close() {
		for(Component c : Utilities.getAllSubComponents(this)) {
			if(c instanceof Closeable)
				((Closeable) c).close();
		}
	}
}