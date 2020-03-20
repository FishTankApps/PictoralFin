package projectFileManagement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import jTimeLine.AudioClip;
import mainFrame.PictoralFin;
import objectBinders.Frame;
import utilities.BufferedImageUtil;
import utilities.Constants;
import utilities.FileUtils;

public class PictoralFinProjectManager {

	private PictoralFinProjectManager() {}
	
	private static ArrayList<File> tempFiles = new ArrayList<>();
	
	public static File saveProject(PictoralFin pictoralFin, String filePath) {
		
		try {
			int index = 0;
			String fileName;
			ProjectInfo projectInfo = new ProjectInfo(pictoralFin);			
			
			File saveLocation;
			if(filePath == null) {
				JFileChooser jfc = new JFileChooser();
				
				jfc.setDialogTitle("Save Project As");
				jfc.setAcceptAllFileFilterUsed(true);
				jfc.addChoosableFileFilter(new FileNameExtensionFilter("Project Files", "pfp"));
				
				if(jfc.showSaveDialog(null) == JFileChooser.CANCEL_OPTION)
					return null;

					
				filePath = jfc.getSelectedFile().getPath();	
			}
			
			if(filePath.contains(".")) 
				filePath = filePath.split("\\.")[0];			
			
			saveLocation = new File(filePath);
			saveLocation.mkdirs();
			fileName = saveLocation.getName() + ".pfp";
			
			ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(new File(saveLocation.getAbsoluteFile() + "/PROJECT-INFO.info")));
			objectOutput.writeObject(projectInfo);			
			objectOutput.close();
			
			index = 0;
			if(pictoralFin.getTimeLine().getAudioClips() != null)
				for(AudioClip clip : pictoralFin.getTimeLine().getAudioClips())
					FileUtils.copyFile(clip.getAudioFile(), saveLocation.getAbsolutePath() + "/audiotrack-" + (index++) + ".audio");			
			
			index = 0;
			if(pictoralFin.getTimeLine().getFrames() != null)
				for(Frame frame : pictoralFin.getTimeLine().getFrames()) {
					File frameFolder = new File(saveLocation.getAbsolutePath() + "/Frame-" + index++ + "/");
					frameFolder.mkdirs();
					
					int layerIndex = 0;
					for(BufferedImage i : frame.getLayers()) {
						i = BufferedImageUtil.setBufferedImageType(i, Constants.PNG);
						File imageOutput = new File(frameFolder.getAbsoluteFile() + "/Layer-" + (layerIndex++) + ".layer");
						ImageIO.write(i, "png", imageOutput);
					}
				}
			
			FileUtils.zipFolder(saveLocation, saveLocation.getParent() + "\\" + fileName);
			FileUtils.deleteFolder(saveLocation);
			return new File(saveLocation.getParent() + "\\" + fileName);
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error saving the project:\n" + e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
			System.out.println("Error in PictoralFinProjectManager.saveProject(): ");
			e.printStackTrace();
		}
		
		return null;
	}
		
	public static File openProject(PictoralFin pictoralFin, String filePath) {
		try {
			
			if(!pictoralFin.getTimeLine().isEmpty()) {
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to save the current project?", "Save", JOptionPane.INFORMATION_MESSAGE);
				
				if(choice == JOptionPane.YES_OPTION) {
					if(!pictoralFin.saveProject())
						return pictoralFin.openProject;
				} else if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
					return null;
			
				pictoralFin.getTimeLine().empty();
			}
			
			int index = 0;
			String path;			
			File openLocation;
			if(filePath == null) {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Open Project");
				jfc.setAcceptAllFileFilterUsed(true);
				jfc.addChoosableFileFilter(new FileNameExtensionFilter("Project Files", "pfp"));
				if(jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
					return null;				
				
				filePath = jfc.getSelectedFile().getPath();	
			}
			
			openLocation = new File(filePath);
			path = openLocation.getParent() + "\\PROJECT-TEMP";
			
			FileUtils.unzipFolder(openLocation, path);
			
			ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(new File(path + "/PROJECT-INFO.info")));
			ProjectInfo projectInfo = (ProjectInfo) objectInput.readObject();		
			objectInput.close();
			
			ArrayList<AudioInfo> audioInfo = projectInfo.getAudioInfo();
			index = 0;
			if(!audioInfo.isEmpty())
				for(AudioInfo info : audioInfo) {
					File audioFile = new File(path + "\\audiotrack-" + (index++) + ".audio");

					AudioClip audioClip = new AudioClip(audioFile, pictoralFin.getTimeLine());
					audioClip.setStartTime(info.getStartTime());
					audioClip.setEndTime(info.getEndTime());
					audioClip.setVolume(info.getVolume());
					audioClip.setName(info.getName());
					
					pictoralFin.getTimeLine().addAudioClip(audioClip);					
				}
			
			ArrayList<FrameInfo> frameInfo = projectInfo.getFrameInfo();
			index = 0;
			if(!frameInfo.isEmpty())
				for(FrameInfo info : frameInfo) {
					Frame frame = new Frame(info.getDuration());
					
					File layerFolder = new File(path+"\\Frame-" + (index++));
					for(File layer : layerFolder.listFiles())
						frame.addLayer(ImageIO.read(layer));
					
					pictoralFin.getTimeLine().addFrame(frame);
				}
			tempFiles.add(new File(path));
			
			return openLocation;
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "There was an error openning the project:\n" + e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
			System.out.println("Error in PictoralFinProjectManager.saveProject(): ");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void clearTempFiles() {
		for(File tempFile : tempFiles)
			FileUtils.deleteFolder(tempFile);
	}
}
