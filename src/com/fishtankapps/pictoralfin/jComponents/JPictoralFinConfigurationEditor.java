package com.fishtankapps.pictoralfin.jComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.tree.DefaultMutableTreeNode;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFinConfiguration;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;

public class JPictoralFinConfigurationEditor extends JFrame{
	private static final long serialVersionUID = 8811707125716275839L;

	private JTree preferencesTree;
	private DefaultMutableTreeNode treeRoot, general, advanced, apperance, ignoredMessages, memoryManagment;
	private JSplitPane splitPane;
	private JPanel preferencesPanel, apperancePanel, ignoredMessagesPanel, applyClosePanel, memoryManagmentPanel, generalPanel;
	private PictoralFinConfiguration configuration;
	
	public JPictoralFinConfigurationEditor(PictoralFinConfiguration c) {
		configuration = c.copyConfiguration();
		
		setSize(600, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Preferences");
		setIconImage(GlobalImageKit.pictoralFinIcon);

		general = new DefaultMutableTreeNode("General");
		advanced = new DefaultMutableTreeNode("Advanced");
		ignoredMessages = new DefaultMutableTreeNode("Ignored Messages");
		memoryManagment = new DefaultMutableTreeNode("Memory Managment");
		apperance = new DefaultMutableTreeNode("Apperance");
		
		treeRoot = new DefaultMutableTreeNode("Preferences");
		
		preferencesTree = new JTree(treeRoot);
		
		preferencesTree.addTreeSelectionListener(e->updatePreferencesPanel(e.getSource()));
		
		treeRoot.add(general);
			general.add(apperance);
			general.add(ignoredMessages);
		treeRoot.add(advanced);
			advanced.add(memoryManagment);
		
		preferencesPanel = new JPanel(new BorderLayout());
		
		applyClosePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton applyAndClose = new JButton("Apply and Close");
		JButton cancel = new JButton("Cancel");
		
		cancel.addActionListener(e-> dispose());
		applyAndClose.addActionListener(e->{c.applyConfiguration(configuration); dispose();});
		
		applyClosePanel.add(applyAndClose);
		applyClosePanel.add(cancel);
		
		preferencesPanel.add(applyClosePanel, BorderLayout.SOUTH);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);	
		
		splitPane.setLeftComponent(preferencesTree);
		splitPane.setRightComponent(preferencesPanel);
		
		add(splitPane);
		
		this.addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}

			public void windowDeactivated(WindowEvent e) {
				dispose();	
			}
		
		});
		
		setVisible(true);
		
		splitPane.setDividerLocation(.25);
		
		setUpPanels();
	}
	
	private void setUpPanels() {
		setUpApperancePanel();
		setUpIgnoredMessagesPanel();
		setUpMemoryManagmentPanel();
		setUpGeneralPanel();
	}
	
	private void setUpApperancePanel() {
		apperancePanel = new JPanel(new GridBagLayout());
		apperancePanel.setBorder(BorderFactory.createTitledBorder("Apperance"));
		
		JLabel lookAndFeelLabel = new JLabel("Look And Feels:");
		JComboBox<String> looksAndFeels = new JComboBox<>();		
		for(LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels())
			looksAndFeels.addItem(laf.getClassName());
		
		looksAndFeels.setSelectedItem(configuration.getLookAndFeel());
		looksAndFeels.addActionListener(e->configuration.setLookAndFeel((String) looksAndFeels.getSelectedItem()));		
		
		JRadioButton oceanTheme = new JRadioButton("Ocean");
		JRadioButton redMetalTheme = new JRadioButton("Red Metal");
		//JRadioButton custom = new JRadioButton("Custom");
		
		ButtonGroup themeButtons = new ButtonGroup();
		themeButtons.add(oceanTheme);
		themeButtons.add(redMetalTheme);
		//themeButtons.add(custom);
		
		JPanel themePanel = new JPanel(new GridBagLayout());
		themePanel.setBorder(BorderFactory.createTitledBorder("Theme"));
		    
		themePanel.add(oceanTheme,    new ChainGBC(0,0).setFill(true, false).setPadding(10));
		themePanel.add(redMetalTheme, new ChainGBC(1,0).setFill(true, false).setPadding(10));
		//themePanel.add(custom,        new ChainGBC(2,0).setFill(true, false).setPadding(10));
		
		if(configuration.getTheme().getPrimaryFont().equals(Theme.OCEAN_THEME.getPrimaryFont()))
			oceanTheme.setSelected(true);
		else if(configuration.getTheme().getPrimaryFont().equals(Theme.RED_METAL_THEME.getPrimaryFont()))
			redMetalTheme.setSelected(true);
		//else
		//	custom.setSelected(true);
		
		oceanTheme.addActionListener(e->configuration.setTheme(Theme.OCEAN_THEME));
		redMetalTheme.addActionListener(e->configuration.setTheme(Theme.RED_METAL_THEME));
		
		apperancePanel.add(lookAndFeelLabel, new ChainGBC(0,0).setFill(false, false).setPadding(10, 10, 50, 10));
		apperancePanel.add(looksAndFeels,    new ChainGBC(1,0).setFill(true,  false).setPadding(10, 10, 50, 10));
		
		JLabel callForRestart = new JLabel("To apply apperence preferences, please restart PictoralFin.", JLabel.CENTER);
		
		apperancePanel.add(themePanel,       new ChainGBC(0,1).setFill(true, false).setWidthAndHeight(2, 1).setPadding(10, 10, 10, 10));
		apperancePanel.add(Box.createGlue(), new ChainGBC(0,2).setFill(true, true).setWidthAndHeight(2, 1).setPadding(10, 10, 10, 10));
		apperancePanel.add(callForRestart,   new ChainGBC(0,3).setFill(true, false).setWidthAndHeight(2, 1).setPadding(10, 10, 10, 10));
	}
	private void setUpIgnoredMessagesPanel() {
		ignoredMessagesPanel = new JPanel(new GridBagLayout());
		ignoredMessagesPanel.setBorder(BorderFactory.createTitledBorder("Ignored Messages"));
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for(String message : configuration.getMessagesToNotShow())
			model.addElement(message);
		
		JList<String> ignoredMessages = new JList<>(model);
		ignoredMessages.setBorder(BorderFactory.createLineBorder(Color.BLACK.brighter(), 3));
		
		JButton removeFromIgnoredList = new JButton("Remove from Ignore List");
		removeFromIgnoredList.addActionListener(e->{
					String selected = ignoredMessages.getSelectedValue();
					
					if(selected != null) {
						configuration.removeMessageToNotShow(selected);
						model.remove(ignoredMessages.getSelectedIndex());
					}
			});
		
		ignoredMessagesPanel.add(ignoredMessages, new ChainGBC(0,0).setFill(true).setPadding(10, 10, 50, 10));
		ignoredMessagesPanel.add(removeFromIgnoredList, new ChainGBC(0,1).setFill(false).setPadding(10));
	}
	private void setUpMemoryManagmentPanel() {
		memoryManagmentPanel = new JPanel(new GridBagLayout());
		memoryManagmentPanel.setBorder(BorderFactory.createTitledBorder("Memory Managment"));
		
		
		
		
		JPanel maxImageDimensions = new JPanel(new GridBagLayout());
		maxImageDimensions.setBorder(BorderFactory.createTitledBorder("Max Image Size: "));
		
		SpinnerNumberModel heightSpinnerModel = new SpinnerNumberModel(configuration.getMaxPictureSize().height, 100, Integer.MAX_VALUE, 100);
		SpinnerNumberModel widthSpinnerModel = new SpinnerNumberModel(configuration.getMaxPictureSize().width, 100, Integer.MAX_VALUE, 100);
		
		JSpinner heightSpinner = new JSpinner(heightSpinnerModel);
		JSpinner widthSpinner  = new JSpinner(widthSpinnerModel);
		
		widthSpinner.addChangeListener(e->configuration.setMaxPictureSize(new Dimension((int) widthSpinnerModel.getValue(), (int) heightSpinnerModel.getValue()))); 
		heightSpinner.addChangeListener(e->configuration.setMaxPictureSize(new Dimension((int) widthSpinnerModel.getValue(), (int) heightSpinnerModel.getValue()))); 
		
		maxImageDimensions.add(new JLabel("Max Image Height:", JLabel.RIGHT), new ChainGBC(0,0).setFill(false, false).setPadding(10));
		maxImageDimensions.add(heightSpinner,                                 new ChainGBC(1,0).setFill(true,  false).setPadding(10));
		maxImageDimensions.add(new JLabel("Max Image Width:", JLabel.RIGHT),  new ChainGBC(0,1).setFill(false, false).setPadding(10));
		maxImageDimensions.add(widthSpinner,                                  new ChainGBC(1,1).setFill(true,  false).setPadding(10));
				
		
		
		
		JPanel audioSampleRatePanel = new JPanel(new GridBagLayout());
		audioSampleRatePanel.setBorder(BorderFactory.createTitledBorder("Audio Sample Rate: "));
		
		SpinnerNumberModel sampleRateSpinnerModel = new SpinnerNumberModel(configuration.getAudioSampleRate(), 1000, Integer.MAX_VALUE, 1000);		
		JSpinner sampleRateSpinner = new JSpinner(sampleRateSpinnerModel);		
		sampleRateSpinner.addChangeListener(e-> configuration.setAudioSampleRate((int) sampleRateSpinnerModel.getValue()));
		
		audioSampleRatePanel.add(new JLabel("Audio Sample Rate: ", JLabel.RIGHT), new ChainGBC(0,0).setFill(false, false).setPadding(10));
		audioSampleRatePanel.add(sampleRateSpinner                              , new ChainGBC(1,0).setFill(true,  false).setPadding(10));
		
		
		
		
		JPanel setToDefaultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton setToDefaults = new JButton("Restore to Defaults");
		setToDefaults.addActionListener(e-> {
				configuration.applyConfiguration(PictoralFinConfiguration.DEFAULT_SETTINGS);
				sampleRateSpinnerModel.setValue(configuration.getAudioSampleRate());
				widthSpinnerModel.setValue(configuration.getMaxPictureSize().width);
				heightSpinnerModel.setValue(configuration.getMaxPictureSize().height);
				
				memoryManagmentPanel.repaint();
			});
		
		setToDefaultPanel.add(setToDefaults);
		
		memoryManagmentPanel.add(maxImageDimensions, new ChainGBC(0,0).setFill(true, false).setPadding(20));
		memoryManagmentPanel.add(audioSampleRatePanel, new ChainGBC(0,1).setFill(true, false).setPadding(20));	
		memoryManagmentPanel.add(Box.createHorizontalGlue(), new ChainGBC(0,2).setFill(true, true).setPadding(20));
		memoryManagmentPanel.add(setToDefaultPanel, new ChainGBC(0,3).setFill(true, false).setPadding(10));	
	}
	private void setUpGeneralPanel() {
		generalPanel = new JPanel(new GridBagLayout());
		generalPanel.setBorder(BorderFactory.createTitledBorder("General"));
		

		JPanel fontPreviewPanel = new JPanel(new GridBagLayout());
		fontPreviewPanel.setBorder(BorderFactory.createTitledBorder("Font Preview Text"));
		
		JLabel fontPreviewEditorLabel = new JLabel("Font Preview:", JLabel.RIGHT);
		JTextField fontPreviewEditor = new JTextField(configuration.getFontPreviewString());
		fontPreviewEditor.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {	
				configuration.setFontPreviewString(fontPreviewEditor.getText());
			}

			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			
		});
		
		
		fontPreviewPanel.add(fontPreviewEditorLabel, new ChainGBC(0,0).setPadding(10, 10, 10, 10).setFill(false, false));
		fontPreviewPanel.add(fontPreviewEditor,      new ChainGBC(1,0).setPadding(10, 10, 10, 10).setFill(true,  false));
		
		
		generalPanel.add(fontPreviewPanel, new ChainGBC(0,0).setPadding(10, 10, 50, 10).setFill(true, false));
		generalPanel.add(Box.createGlue(), new ChainGBC(0,1).setPadding(10, 10, 10, 10).setFill(true, true));
	}
	
	private void updatePreferencesPanel(Object source) {
		if(preferencesTree.getSelectionPath() == null)
			return;
		
		Object[] path = preferencesTree.getSelectionPath().getPath();
		Object selectedObject = path[path.length - 1];
		
		if(selectedObject.toString().equals("Apperance")) {
			preferencesPanel.removeAll();
			preferencesPanel.add(apperancePanel, BorderLayout.CENTER);
			preferencesPanel.add(applyClosePanel, BorderLayout.SOUTH);
			
			preferencesPanel.revalidate();
			preferencesPanel.repaint();
		} else if(selectedObject.toString().equals("Ignored Messages")) {
			preferencesPanel.removeAll();
			preferencesPanel.add(ignoredMessagesPanel, BorderLayout.CENTER);
			preferencesPanel.add(applyClosePanel, BorderLayout.SOUTH);
			
			preferencesPanel.revalidate();
			preferencesPanel.repaint();
		} else if(selectedObject.toString().equals("Memory Managment")) {
			preferencesPanel.removeAll();
			preferencesPanel.add(memoryManagmentPanel, BorderLayout.CENTER);
			preferencesPanel.add(applyClosePanel, BorderLayout.SOUTH);
			
			preferencesPanel.revalidate();
			preferencesPanel.repaint();
		} else if(selectedObject.toString().equals("General")) {
			preferencesPanel.removeAll();
			preferencesPanel.add(generalPanel, BorderLayout.CENTER);
			preferencesPanel.add(applyClosePanel, BorderLayout.SOUTH);
			
			preferencesPanel.revalidate();
			preferencesPanel.repaint();
		}
	}
}
