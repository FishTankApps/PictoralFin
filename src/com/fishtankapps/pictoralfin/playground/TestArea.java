package com.fishtankapps.pictoralfin.playground;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class TestArea {

	public static void main(String[] args) throws Exception {
		System.out.println("File system roots returned by   FileSystemView.getFileSystemView():");
	      FileSystemView fsv = FileSystemView.getFileSystemView();
	      File[] roots = fsv.getRoots();
	      for (int i = 0; i < roots.length; i++)
	      {
	        System.out.println("Root: " + roots[i]);
	      }

	      System.out.println("Home directory: " + fsv.getHomeDirectory());

	      System.out.println("File system roots returned by File.listRoots():");

	      File[] f = File.listRoots();
	      for (int i = 0; i < f.length; i++)
	      {
	        System.out.println("Drive: " + f[i]);
	        System.out.println("Display name: " + fsv.getSystemDisplayName(f[i]));
	        System.out.println("Is drive: " + fsv.isDrive(f[i]));
	        System.out.println("Is floppy: " + fsv.isFloppyDrive(f[i]));
	        System.out.println("Readable: " + f[i].canRead());
	        System.out.println("Writable: " + f[i].canWrite());
	      }
	}
}

/*
 * JFXPanel panel = new JFXPanel();
 * 
 * final ColorPicker colorPicker = new ColorPicker();
 * colorPicker.setValue(Color.RED);
 * 
 * 
 * colorPicker.setOnAction(new EventHandler<ActionEvent>() {
 * 
 * @Override public void handle(ActionEvent event) {
 * System.out.println(Utilities.jfxColorToAwtColor(colorPicker.getValue())); }
 * });
 * 
 * FlowPane root = new FlowPane(); root.setPadding(new Insets(10));
 * root.setHgap(10); root.getChildren().addAll(colorPicker);
 * 
 * Scene scene = new Scene(root, 400, 300);
 * 
 * 
 * panel.setScene(scene);
 * 
 * JFrame frame = new JFrame(); frame.setSize(400, 300);
 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 * 
 * frame.setLocationRelativeTo(null);
 * 
 * frame.add(panel);
 * 
 * frame.setVisible(true);
 */