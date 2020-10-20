package com.fishtankapps.pictoralfin.jComponents.videoEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import com.fishtankapps.pictoralfin.globalToolKits.GlobalImageKit;
import com.fishtankapps.pictoralfin.globalToolKits.GlobalListenerToolKit;
import com.fishtankapps.pictoralfin.interfaces.Themed;
import com.fishtankapps.pictoralfin.jComponents.JProgressDialog;
import com.fishtankapps.pictoralfin.jTimeLine.AudioClip;
import com.fishtankapps.pictoralfin.mainFrame.PictoralFin;
import com.fishtankapps.pictoralfin.mainFrame.StatusLogger;
import com.fishtankapps.pictoralfin.objectBinders.Theme;
import com.fishtankapps.pictoralfin.utilities.BufferedImageUtil;
import com.fishtankapps.pictoralfin.utilities.ChainGBC;
import com.fishtankapps.pictoralfin.utilities.Utilities;

public class FileImportPanel extends JPanel implements Themed {

	private static final long serialVersionUID = -8225882191341895849L;

	private JButton importPicture, importAudio, importVideo;
	private JPanel fileDropPanel;
	private PictoralFin pictoralFin;
	
	public FileImportPanel(PictoralFin pictoralFin) {
		this.pictoralFin = pictoralFin;
		
		Font font = new Font(pictoralFin.getSettings().getTheme().getPrimaryFont(), Font.BOLD + Font.ITALIC, 12);
		
		importPicture = new JButton(" Import Pictures");
		importAudio   = new JButton(" Import Audio Files");
		importVideo   = new JButton(" Import Videos");
		
		importPicture.addActionListener(GlobalListenerToolKit.onAddPictureRequest);
		importAudio.addActionListener(GlobalListenerToolKit.onAddAudioRequest);
		importVideo.addActionListener(GlobalListenerToolKit.onAddVideoRequest);
		
		importPicture.setIcon(new ImageIcon(BufferedImageUtil.resizeBufferedImage(GlobalImageKit.pictureIcon, 25, 25, BufferedImage.SCALE_FAST)));
		importPicture.setFont(font);
		importAudio.setIcon(new ImageIcon(BufferedImageUtil.resizeBufferedImage(GlobalImageKit.audioIcon, 25, 25, BufferedImage.SCALE_FAST)));
		importAudio.setFont(font);
		importVideo.setIcon(new ImageIcon(BufferedImageUtil.resizeBufferedImage(GlobalImageKit.videoIcon, 25, 25, BufferedImage.SCALE_FAST)));
		importVideo.setFont(font);
		
		fileDropPanel = new JPanel(new BorderLayout());
		fileDropPanel.setBackground(pictoralFin.getSettings().getTheme().getSecondaryBaseColor());
		fileDropPanel.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 1.5f, 4, 6, false));
		
		JLabel dropHereLabel = new JLabel("Or Drop Files Here", JLabel.CENTER);
		
		dropHereLabel.setFont(new Font(pictoralFin.getSettings().getTheme().getPrimaryFont(), Font.BOLD + Font.ITALIC, 24));
		fileDropPanel.add(dropHereLabel);

		fileDropPanel.setTransferHandler(new TransferHandler() {

			private static final long serialVersionUID = 2945973300282668610L;

			public boolean canImport(TransferHandler.TransferSupport support) {          
	            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
	        }
			
	        public boolean importData(TransferHandler.TransferSupport support) {
	            
	            Transferable t = support.getTransferable();
	            try {
	            	List<?> l = (List<?>) t.getTransferData(DataFlavor.javaFileListFlavor);
	            	new Thread(() -> {
			            try {
			            	
			                JProgressDialog dialog = new JProgressDialog("Importing Files " + JProgressDialog.PERCENT, "Importing...", l.size() + 1);
			                			                
			                int importCount = 0;
			                StatusLogger.logStatus("Importing Files (" +importCount + "/" + l.size() + ")");
			                File file;
			                boolean imported;
			                for(Object object : l) {
			                	if(object instanceof File) {
			                		file = (File) object;
			                		imported = false;
			                				
			                		for(String imageFileExtension : Utilities.getCompatibleImageFiles()) {
			                			if(file.getName().endsWith(imageFileExtension.substring(1))) {			                				
			                				pictoralFin.getTimeLine().addFrame(ImageIO.read(file));		
			                				imported = true;
			                				break;
			                			}
			                		}
			                		
			                		if(!imported)
				                		for(String audioFileExtension : Utilities.getCompatibleAudioFormats()) {
				                			if(file.getName().endsWith(audioFileExtension.substring(1))) {			                				
				                				pictoralFin.getTimeLine().addAudioClip(new AudioClip(file, pictoralFin.getTimeLine()));
				                				imported = true;
				                				break;
				                			}
				                		}
			                		
			                		dialog.moveForward();
			                		StatusLogger.logStatus("Importing Files (" + ++importCount + "/" + l.size() + ")");
			                	}
			                }
			                
			                dialog.close();
			                
			            } catch (Exception e) {
			            	e.printStackTrace();
			            }			            
	            	}).start();
	            } catch (Exception e) {
	            	
	            }
	            
	            
	            

	            return true;
	        }
		});
		
		setLayout(new GridBagLayout());
		add(importPicture, new ChainGBC(0, 0).setFill(false).setWidthAndHeight(1, 1).setPadding(5));
		add(importAudio,   new ChainGBC(1, 0).setFill(false).setWidthAndHeight(1, 1).setPadding(5));
		add(importVideo,   new ChainGBC(2, 0).setFill(false).setWidthAndHeight(1, 1).setPadding(5));
		add(fileDropPanel, new ChainGBC(0, 1).setFill(true, false).setWidthAndHeight(3, 1).setPadding(5));
		
		setBackground(pictoralFin.getSettings().getTheme().getPrimaryBaseColor());
	}
		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int height = importVideo.getHeight() + 20 + fileDropPanel.getHeight();
		
		g.setColor(pictoralFin.getSettings().getTheme().getSecondaryBaseColor());
		
		if(pictoralFin.getSettings().getTheme().isSharp())
			g.fillRect(0, (getHeight() - height) / 2, getWidth(), height);
		else
			g.fillRoundRect(0, (getHeight() - height) / 2, getWidth(), height, 40, 40);
	}
	
	public void applyTheme(Theme theme) {
		
	}	
}
