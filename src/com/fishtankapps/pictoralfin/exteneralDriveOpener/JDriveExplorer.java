//package com.fishtankapps.pictoralfin.exteneralDriveOpener;
//
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.GridBagLayout;
//import java.io.File;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import javax.swing.Box;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTree;
//import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.tree.TreeSelectionModel;
//
//import com.fishtankapps.pictoralfin.objectBinders.Theme;
//import com.fishtankapps.pictoralfin.utilities.ChainGBC;
//import com.fishtankapps.pictoralfin.utilities.Utilities;
//
//import be.derycke.pieter.com.COMException;
//import jmtp.PortableDevice;
//import jmtp.PortableDeviceFolderObject;
//import jmtp.PortableDeviceObject;
//import jmtp.PortableDeviceStorageObject;
//import jmtp.PortableDeviceToHostImpl32;
//
//@Deprecated
//public class JDriveExplorer extends JPanel {
//
//	private static final long serialVersionUID = 151806570734071938L;
//
//	private ExternalDriveManager driveManager;
//	private DefaultMutableTreeNode rootNode;
//	
//	private JLabel currentDir;
//	private JButton refresh, scan;
//	
//	private JCheckBox enableAutoImport;
//	
//	private JTree driveTree;
//	private boolean enabled = true;
//	private boolean hasSelectedFolder = false;
//	private boolean scanning = false;
//	private boolean ignoreScan = false;
//	
//	private PortableDeviceObject[] previousScanFiles = null;
//	private PortableDeviceToHostImpl32 importer = new PortableDeviceToHostImpl32();
//	private ScheduledThreadPoolExecutor eventPool;
//	
//	private Theme theme;
//	
//	public JDriveExplorer(Theme theme) {
//		this.theme = theme;
//		
//		setBackground(theme.getPrimaryBaseColor());		
//		setLayout(new GridBagLayout());
//		
//		driveManager = new ExternalDriveManager();
//		
//		currentDir = new JLabel("No Folder Selected");
//		currentDir.setFont(new Font(theme.getPrimaryFont(), Font.BOLD, 15));
//		refresh = new JButton("Rescan for Devices");
//		refresh.setFont(currentDir.getFont());
//		refresh.addActionListener(e -> refreshTree());
//		refresh.setPreferredSize(new Dimension(300, 50));
//		
//		scan = new JButton("Begin Scanning for New Files");
//		scan.setFont(refresh.getFont());
//		scan.addActionListener(e -> {
//			if(!enabled || !hasSelectedFolder) {
//				System.out.println("Not enabled/No Selected Folder");
//				return;
//			}			
//			
//			if(scanning) {
//				scanning = false;
//				scan.setText("Begin Scanning for New Files");
//			} else {
//				int choice = JOptionPane.showConfirmDialog(null, "Would you like to import the current files?", "Import Files", JOptionPane.INFORMATION_MESSAGE);
//				
//				if(choice == JOptionPane.CANCEL_OPTION)
//					return;
//				
//				ignoreScan = JOptionPane.YES_OPTION != choice;
//				scanning = true;
//				scan.setText("Stop Scanning");
//			}
//		});
//		
//		rootNode = new DefaultMutableTreeNode("External Drives");
//		driveTree = new JTree(rootNode);
//		driveTree.setFont(new Font(theme.getPrimaryFont(), Font.PLAIN, 12));
//		driveTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//		driveTree.setBackground(theme.getSecondaryBaseColor());
//		driveTree.addTreeSelectionListener(e -> {
//			
//			if(driveTree.getSelectionPath() != null) {
//				String path = "External Drives";
//				for(int count = 1; count < driveTree.getSelectionPath().getPath().length; count ++)
//					path += " >> " + driveTree.getSelectionPath().getPath()[count];
//				
//				currentDir.setText("Path: " + path);
//				hasSelectedFolder = true;
//			} else {
//				currentDir.setText("No Folder Selected");
//				hasSelectedFolder = false;
//			}
//			repaint();
//		});
//		
//		JScrollPane treeScrollPane = new JScrollPane(driveTree);
//		treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		treeScrollPane.setPreferredSize(new Dimension(1000, 100));	
//		
//		enableAutoImport = new JCheckBox("Auto Import", false);
//		enableAutoImport.setFont(currentDir.getFont());
//		enableAutoImport.setBackground(theme.getSecondaryHighlightColor());
//		enableAutoImport.addChangeListener(e->{
//			enabled = enableAutoImport.isSelected();
//			
//			currentDir.setVisible(enabled);
//			refresh.setVisible(enabled);	
//			scan.setVisible(enabled);
//			treeScrollPane.setVisible(enabled);
//			
//			repaint();
//		});
//		
//		enabled = false;
//		
//		currentDir.setVisible(enabled);
//		refresh.setVisible(enabled);	
//		scan.setVisible(enabled);
//		treeScrollPane.setVisible(enabled);
//		
//		add(enableAutoImport, 			new ChainGBC(0,0).setFill(false, false).setPadding(10).setWidthAndHeight(1, 1));		
//		add(refresh,          			new ChainGBC(1,0).setFill(false, false).setPadding(10).setWidthAndHeight(1, 1));
//		add(scan,             			new ChainGBC(2,0).setFill(false, false).setPadding(10).setWidthAndHeight(1, 1));
//		add(Box.createHorizontalGlue(), new ChainGBC(3,0).setFill(true,  false).setPadding(10).setWidthAndHeight(1, 1));
//		add(currentDir,       			new ChainGBC(0,1).setFill(true,  false).setPadding(10).setWidthAndHeight(4, 1));
//		add(treeScrollPane,   			new ChainGBC(0,2).setFill(true,  true ).setPadding(10).setWidthAndHeight(4, 2));
//		
//		refreshTree();
//		
//		
//		eventPool = new ScheduledThreadPoolExecutor(1);
//		eventPool.scheduleAtFixedRate(new ScanningRunnable(), 0, 4, TimeUnit.SECONDS);
//		Thread[] listOfThreads = new Thread[Thread.activeCount()];
//		Thread.enumerate(listOfThreads);
//	}
//	
//	public void scanTargetFolder() {
//		if(!enabled || !hasSelectedFolder) {
//			System.out.println("Not enabled/No Selected Folder");
//			return;
//		}
//		
//		driveManager.refresh();
//		
//		// GET TARGET DIR
//		PortableDeviceObject targetDir = null;
//		for(PortableDevice device : driveManager.getDevices()) {
//			targetDir = getTargetFolderObject(device);
//			
//			// CHECK IF TARGET DIR WAS FOUND
//			if(targetDir == null) 
//				continue;
//
//			// GET TARGET DIR CHILDERN
//			PortableDeviceObject[] targetDirChildern = getTargetFolderChildern((PortableDeviceObject) targetDir);
//			
//			// REPORT NEW CHILDREN
//			if(!ignoreScan) {
//				for(PortableDeviceObject newFile : targetDirChildern) {
//					boolean isNewFile = true;
//					
//					if(previousScanFiles != null) 
//						for(PortableDeviceObject oldFile : previousScanFiles) 
//							isNewFile = isNewFile && !newFile.getName().equals(oldFile.getName());				
//					
//					if(isNewFile) {
//						try {
//							importer.copyFromPortableDeviceToHost(newFile.getID(), "C:/Users/" + System.getProperty("user.name") + "/Pictures", (PortableDevice) device);
//							
//							File importedPicture = new File("C:/Users/" + System.getProperty("user.name") + "/Pictures/" + newFile.getName());
//							Utilities.getPictoralFin(this).getPictureImporter().simpleImportPicture(importedPicture);
//							importedPicture.delete();
//							
//							Utilities.playSound("PictureImported.wav");
//						} catch (COMException e) {
//							e.printStackTrace();
//						}			
//					}				
//				}
//			}			
//			ignoreScan = false;
//			previousScanFiles = targetDirChildern;
//			
//			break;
//		}
//		
//				
//	}
//	
//	private PortableDeviceObject[] getTargetFolderChildern(PortableDeviceObject targetDir) {
//		PortableDeviceObject[] files = null;
//		
//		if(targetDir instanceof PortableDeviceStorageObject) {
//			PortableDeviceStorageObject targetFolder = (PortableDeviceStorageObject) targetDir;;
//			files = targetFolder.getChildObjects();
//			
//		} else if (targetDir instanceof PortableDeviceFolderObject) {
//			
//			PortableDeviceFolderObject targetFolder = (PortableDeviceFolderObject) targetDir;;
//			files = targetFolder.getChildObjects();
//		}
//		
//		return files;
//	}
//	private PortableDeviceObject getTargetFolderObject(PortableDeviceObject root) {
//		Object[] path = driveTree.getSelectionPath().getPath();
//		
//		PortableDeviceObject currentDir = root;
//		for(int count = 0; count < path.length; count++) {
//			if(currentDir instanceof PortableDeviceStorageObject) {
//				for(PortableDeviceObject deviceObject : ((PortableDeviceStorageObject) currentDir).getChildObjects()) {
//					if(deviceObject.getName().equals(path[count].toString())) {
//						currentDir = deviceObject;
//						continue;
//					}
//				}
//				
//			} else if (currentDir instanceof PortableDeviceFolderObject) {
//				for(PortableDeviceObject deviceObject : ((PortableDeviceFolderObject) currentDir).getChildObjects()) {
//					if(deviceObject.getName().equals(path[count].toString())) {
//						currentDir = deviceObject;
//						continue;
//					}
//				}
//			}
//		}
//		
//		if(currentDir == root) currentDir = null;
//		
//		return currentDir;
//	}
//	private PortableDeviceObject getTargetFolderObject(PortableDevice device) {
//		for(PortableDeviceObject rootObject : device.getRootObjects()) {
//			PortableDeviceObject result = getTargetFolderObject(rootObject);
//			
//			if(result != null)
//				return result;
//		}
//		
//		return null;
//	}
//	
//	public void refreshTree() {
//		driveManager.refresh();	
//		rootNode.removeAllChildren();
//		
//		for(PortableDevice device : driveManager.getDevices()) {	
//			
//			for(PortableDeviceObject rootObject : device.getRootObjects()) {
//				DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode(device.getRootObjects()[0].getParent().getName());
//				rootNode.add(deviceNode);
//				addNode(deviceNode, rootObject);
//			}
//		}
//		
//		if(rootNode.isLeaf())
//			rootNode.setUserObject("No External Drives");
//		else
//			rootNode.setUserObject("External Drives");
//		
//		driveTree.collapseRow(0);
//		driveTree.repaint();
//		repaint();
//	}
//	private void addNode(DefaultMutableTreeNode parent, PortableDeviceObject file) {
//		if(file.getName().charAt(0) != '.') {
//			DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
//			if(file.getName().contains("app") || file.getName().contains("Android") || file.getName().contains("temp"))
//				return;
//		
//			
//			if(file instanceof PortableDeviceStorageObject) {
//				parent.add(node);
//				for(PortableDeviceObject childFile : ((PortableDeviceStorageObject) file).getChildObjects())
//					addNode(node, childFile);
//			}
//			
//			if(file instanceof PortableDeviceFolderObject) {
//				parent.add(node);
//				for(PortableDeviceObject childFile : ((PortableDeviceFolderObject) file).getChildObjects())
//					addNode(node, childFile);
//			}
//		}
//	}
//
//	public void closeDevices() {
//		driveManager.close();
//		eventPool.shutdown();
//	}
//
//	private class ScanningRunnable implements Runnable {
//		public void run() {
//			if (scanning) {
//				Utilities.playSound("SonarPing.wav");
//				scanTargetFolder();	
//			}
//		}		 
//	}
//
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		g.setColor(theme.getPrimaryHighlightColor());
//		if(theme.isSharp())
//			g.fillRect(0, 0, getWidth(), getHeight());
//		else
//			g.fillRoundRect(0, 0, getWidth(), getHeight() + 30, 30, 30);
//	}
//}
