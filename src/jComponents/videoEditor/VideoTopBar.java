package jComponents.videoEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import mainFrame.PictoralFin;
import utilities.Constants;

public class VideoTopBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private JMenu fileMenu;
	private JMenuItem importFrames;
	
	public VideoTopBar(PictoralFin pictoralFin){
		
		//---------{FILE MENU}-------------------------------------------------
		fileMenu = new JMenu("File");
		
		
		importFrames = new JMenuItem("Import Frame(s)");
		importFrames.addActionListener(pictoralFin.getGlobalListenerToolKit().onAddPictureRequest);
		importFrames.setAccelerator(KeyStroke.getKeyStroke('I', Constants.CTRL));
			
		
		fileMenu.add(importFrames);
		fileMenu.addSeparator();
		
		//---------{TOOLS MENU}-------------------------------------------------		
		add(fileMenu);
	}
	
	class OnItemClicked implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			//if(arg0.getSource() == importFrames)
				//PictureImporter.importPictures(pfk.getFrameTimeLine());
			//else if(arg0.getSource() == exportAsVideo)
			//	VideoTools.exportImagesAVideo(pfk.getVideoEditor().generateSettings());
		}	
	}
}
