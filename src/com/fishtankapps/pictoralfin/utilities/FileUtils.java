package com.fishtankapps.pictoralfin.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import com.fishtankapps.pictoralfin.objectBinders.MediaFileType;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;

public class FileUtils {

	private FileUtils() {
	}

	public static void copyFile(File toCopy, String newLocation) throws IOException {
		File copied = new File(newLocation);
		copied.createNewFile();

		InputStream in = new BufferedInputStream(new FileInputStream(toCopy));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(copied));

		byte[] buffer = new byte[1024];
		int lengthRead;
		while ((lengthRead = in.read(buffer)) > 0) {
			out.write(buffer, 0, lengthRead);
			out.flush();
		}

		in.close();
		out.close();

		if (!copied.exists())
			throw new IOException("New file did not exist");

	}

	public static void deleteFolder(File folder) {
		if (folder.isDirectory())
			for (File children : folder.listFiles())
				deleteFolder(children);
		
		folder.delete();
	}

	public static File pictoralFinTempFolder = null;

	public static File createTempFile(String name, String suffix, String folder, boolean addUniqueNumberToName) {
		try {
			if(pictoralFinTempFolder == null) {			
				File locator = File.createTempFile("LocatorFile", "locator");				
				pictoralFinTempFolder = new File(locator.getParent() + "\\PictoralFinTemp");
				pictoralFinTempFolder.mkdirs();
				
				pictoralFinTempFolder.deleteOnExit();
				locator.delete();
			}
			
			File parentFolder = new File(pictoralFinTempFolder.getPath() + "\\" + folder);
			parentFolder.mkdirs();
			
			File temp;
			if(addUniqueNumberToName) {
				temp = File.createTempFile(name, suffix, parentFolder);
				temp.deleteOnExit();
			} else {
				temp = new File(parentFolder.getPath() + "\\" + name + suffix);
				temp.createNewFile();
				temp.deleteOnExit();
			}
			
			
			return temp;
			
		} catch (Exception e) {}
		
		return null;
	}
	
	public static void deleteTempFolder() {
		if(pictoralFinTempFolder != null)
			deleteFolder(pictoralFinTempFolder);
		
		pictoralFinTempFolder = null;
	}
	
	
	public static MediaFileType getMediaFileType(File file) {
		String[] brokenFileName = file.getName().split("\\.");
		
		if(brokenFileName.length <= 1)
			return MediaFileType.NONE;
		
		String fileExtension = brokenFileName[brokenFileName.length - 1];	
		
		for(String imageFileExtension : getCompatibleImageFiles()) 
			if(imageFileExtension.contains(fileExtension))
				return MediaFileType.IMAGE;
		
		if(fileExtension.equalsIgnoreCase("pff"))
			return MediaFileType.FRAME;
		
		try {
			if(isVideoFile(file))
				return MediaFileType.VIDEO;
			
		} catch (Exception e) {
			e.printStackTrace();			
			Utilities.showDoNotShowAgainDialog("There was an error exicuting FFprobe\nto probe for video streams.\nError Message:\n" + e.getMessage(), "Error Probing for Video Streams", true);
			
			return MediaFileType.NONE;
		}
		
		
		
		try {			
			if(isAudioFile(file))
				return MediaFileType.AUDIO;
			
		} catch (Exception e) {
			e.printStackTrace();			
			Utilities.showDoNotShowAgainDialog("There was an error exicuting FFprobe\nto probe for audio streams.\nError Message:\n" + e.getMessage(), "Error Probing for Audio Streams", true);
		}
		
		
		
		return MediaFileType.NONE;
	}
	
	public static String[] getCompatibleImageFiles() {
		String[] compatibleImages = ImageIO.getReaderFileSuffixes();

		for (int index = 0; index < compatibleImages.length; index++)
			compatibleImages[index] = "*." + compatibleImages[index];

		return compatibleImages;

	}
	
	public static boolean isVideoFile(File file) throws Exception {
		String ffprobeCommand = VideoUtil.ffprobeExeicutable.getAbsolutePath() + " -show_streams -select_streams v -loglevel error \"" + file.getAbsolutePath() + "\"";
		
		Process ffprobeProcess = Runtime.getRuntime().exec(ffprobeCommand);
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(ffprobeProcess.getInputStream()));
		
		ffprobeProcess.waitFor();
		
		int outputLineCount = 0;
		
		while(inputReader.readLine() != null)
			outputLineCount++;
		
		return outputLineCount > Constants.MIN_NUMBER_OF_VIDEO_LINES;
	}
	
	public static boolean isAudioFile(File file) throws Exception {
		String ffprobeCommand = VideoUtil.ffprobeExeicutable.getAbsolutePath() + " -show_streams -select_streams a -loglevel error \"" + file.getAbsolutePath() + "\"";
		
		Process ffprobeProcess = Runtime.getRuntime().exec(ffprobeCommand);
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(ffprobeProcess.getInputStream()));
		
		ffprobeProcess.waitFor();
		
		int outputLineCount = 0;
		
		while(inputReader.readLine() != null)
			outputLineCount++;
		
		return outputLineCount > Constants.MIN_NUMBER_OF_VIDEO_LINES;
	}

	private static String[] compatibleAudioFormats = null;
	public static String[] getCompatibleAudioFormats() {
		if(compatibleAudioFormats == null) {
			try {
				ArrayList<String> returnArrayList = new ArrayList<>();
				String ffmpegCommand = VideoUtil.ffmpegExeicutable.getPath() + " -hide_banner -formats";

				Process p = Runtime.getRuntime().exec(ffmpegCommand);

				BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

				String line = null;
				while ((line = errorReader.readLine()) != null) {
					if(line.length() > 5) {
						String extension = line.substring(4);
						extension = extension.substring(0, extension.indexOf(' '));
						
						if(extension.length() > 1) {
							returnArrayList.add(extension);
						}
					}
					
				}
				
				compatibleAudioFormats = returnArrayList.toArray(new String[returnArrayList.size()]);
				
			} catch (Exception e) {
				compatibleAudioFormats = null;
			}
		}		
		

		return compatibleAudioFormats;
	}

	
	
	public static void zipFolder(File folderLocation, String zipFilePath) {
		SOURCE_FOLDER = folderLocation.getAbsolutePath();
		ArrayList<String> fileList = new ArrayList<>();
		for (File file : folderLocation.listFiles())
			generateFileList(file, fileList);

		zipIt(zipFilePath, fileList);
	}
	public static void unzipFolder(File zipLocation, String folderFilePath) {
		extract(zipLocation, new File(folderFilePath));
	}
	
	private static void extract(File zipFile, File target) {
		ZipInputStream zip = null;
		try {
			zip = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry entry;

			while ((entry = zip.getNextEntry()) != null) {
				File file = new File(target, entry.getName());

				if (!file.toPath().normalize().startsWith(target.toPath())) {
					throw new IOException("Bad zip entry");
				}

				if (entry.isDirectory()) {
					file.mkdirs();
					continue;
				}

				byte[] buffer = new byte[4096];
				file.getParentFile().mkdirs();
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				int count;

				while ((count = zip.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}

				out.close();
			}
			zip.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String SOURCE_FOLDER;
	private static void generateFileList(File node, ArrayList<String> fileList) {

		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), fileList);
			}
		}
	}
	private static String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}
	private static void zipIt(String zipFile, ArrayList<String> fileList) {
		byte[] buffer = new byte[1024];
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			FileInputStream in = null;

			for (String file : fileList) {
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zos.closeEntry();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
