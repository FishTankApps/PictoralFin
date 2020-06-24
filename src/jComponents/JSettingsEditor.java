package jComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

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
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.tree.DefaultMutableTreeNode;

import objectBinders.Settings;
import objectBinders.Theme;
import utilities.ChainGBC;

public class JSettingsEditor extends JFrame{
	private static final long serialVersionUID = 8811707125716275839L;

	private JTree settingsTree;
	private DefaultMutableTreeNode treeRoot, general, advanced, apperance, ignoredMessages, memoryManagment;
	private JSplitPane splitPane;
	private JPanel settingsPanel, apperancePanel, ignoredMessagesPanel, applyClosePanel;
	private Settings settings;
	
	
	public JSettingsEditor(Settings s, BufferedImage icon) {
		settings = s.copySettings();
		
		setSize(600, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Preferences");
		setIconImage(icon);

		general = new DefaultMutableTreeNode("General");
		advanced = new DefaultMutableTreeNode("Advanced");
		ignoredMessages = new DefaultMutableTreeNode("Ignored Messages");
		memoryManagment = new DefaultMutableTreeNode("Memory Managment");
		apperance = new DefaultMutableTreeNode("Apperance");
		
		treeRoot = new DefaultMutableTreeNode("Preferences");
		
		settingsTree = new JTree(treeRoot);
		
		settingsTree.addTreeSelectionListener(e->updateSettingsPanel(e.getSource()));
		
		treeRoot.add(general);
			general.add(apperance);
			general.add(ignoredMessages);
		treeRoot.add(advanced);
			advanced.add(memoryManagment);
		
		settingsPanel = new JPanel(new BorderLayout());
		
		applyClosePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton applyAndClose = new JButton("Apply and Close");
		JButton cancel = new JButton("Cancel");
		
		cancel.addActionListener(e-> dispose());
		applyAndClose.addActionListener(e->{s.applySettings(settings); dispose();});
		
		applyClosePanel.add(applyAndClose);
		applyClosePanel.add(cancel);
		
		settingsPanel.add(applyClosePanel, BorderLayout.SOUTH);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);	
		
		splitPane.setLeftComponent(settingsTree);
		splitPane.setRightComponent(settingsPanel);
		
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
	}
	
	private void setUpApperancePanel() {
		apperancePanel = new JPanel(new GridBagLayout());
		apperancePanel.setBorder(BorderFactory.createTitledBorder("Apperance"));
		
		JLabel lookAndFeelLabel = new JLabel("Look And Feels:");
		JComboBox<String> looksAndFeels = new JComboBox<>();		
		for(LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels())
			looksAndFeels.addItem(laf.getClassName());
		
		looksAndFeels.setSelectedItem(settings.getLookAndFeel());
		looksAndFeels.addActionListener(e->settings.setLookAndFeel((String) looksAndFeels.getSelectedItem()));		
		
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
		
		if(settings.getTheme().getPrimaryFont().equals(Theme.OCEAN_THEME.getPrimaryFont()))
			oceanTheme.setSelected(true);
		else if(settings.getTheme().getPrimaryFont().equals(Theme.RED_METAL_THEME.getPrimaryFont()))
			redMetalTheme.setSelected(true);
		//else
		//	custom.setSelected(true);
		
		oceanTheme.addActionListener(e->settings.setTheme(Theme.OCEAN_THEME));
		redMetalTheme.addActionListener(e->settings.setTheme(Theme.RED_METAL_THEME));
		
		apperancePanel.add(lookAndFeelLabel, new ChainGBC(0,0).setFill(false, false).setPadding(10, 10, 50, 10));
		apperancePanel.add(looksAndFeels,    new ChainGBC(1,0).setFill(true,  false).setPadding(10, 10, 50, 10));
		
		JLabel callForRestart = new JLabel("To apply apperence settings, please restart PictoralFin.", JLabel.CENTER);
		
		apperancePanel.add(themePanel,   new ChainGBC(0,1).setFill(true, false).setWidthAndHeight(2, 1).setPadding(10, 10, 10, 10));
		apperancePanel.add(Box.createGlue(),   new ChainGBC(0,2).setFill(true, true).setWidthAndHeight(2, 1).setPadding(10, 10, 10, 10));
		apperancePanel.add(callForRestart,   new ChainGBC(0,3).setFill(true, false).setWidthAndHeight(2, 1).setPadding(10, 10, 10, 10));
	}
	
	private void setUpIgnoredMessagesPanel() {
		ignoredMessagesPanel = new JPanel(new GridBagLayout());
		ignoredMessagesPanel.setBorder(BorderFactory.createTitledBorder("Ignored Messages"));
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for(String message : settings.getMessagesToNotShow())
			model.addElement(message);
		
		JList<String> ignoredMessages = new JList<>(model);
		ignoredMessages.setBorder(BorderFactory.createLineBorder(Color.BLACK.brighter(), 3));
		
		JButton removeFromIgnoredList = new JButton("Remove from Ignore List");
		removeFromIgnoredList.addActionListener(e->{
					String selected = ignoredMessages.getSelectedValue();
					
					if(selected != null) {
						settings.removeMessageToNotShow(selected);
						model.remove(ignoredMessages.getSelectedIndex());
					}
			});
		
		ignoredMessagesPanel.add(ignoredMessages, new ChainGBC(0,0).setFill(true).setPadding(10, 10, 50, 10));
		ignoredMessagesPanel.add(removeFromIgnoredList, new ChainGBC(0,1).setFill(false).setPadding(10));
	}
	
	private void updateSettingsPanel(Object source) {
		if(settingsTree.getSelectionPath() == null)
			return;
		
		Object[] path = settingsTree.getSelectionPath().getPath();
		Object selectedObject = path[path.length - 1];
		
		if(selectedObject.toString().equals("Apperance")) {
			settingsPanel.removeAll();
			settingsPanel.add(apperancePanel, BorderLayout.CENTER);
			settingsPanel.add(applyClosePanel, BorderLayout.SOUTH);
			
			settingsPanel.revalidate();
			settingsPanel.repaint();
		} else if(selectedObject.toString().equals("Ignored Messages")) {
			settingsPanel.removeAll();
			settingsPanel.add(ignoredMessagesPanel, BorderLayout.CENTER);
			settingsPanel.add(applyClosePanel, BorderLayout.SOUTH);
			
			settingsPanel.revalidate();
			settingsPanel.repaint();
		}
	}
}
