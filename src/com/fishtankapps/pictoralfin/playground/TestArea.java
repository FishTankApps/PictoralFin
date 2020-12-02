package com.fishtankapps.pictoralfin.playground;

import javax.swing.JFileChooser;

import com.fishtankapps.pictoralfin.utilities.FileUtils;

public class TestArea {

	public static void main(String[] args) throws Exception {
		
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		
		System.out.println(FileUtils.getMediaFileType(chooser.getSelectedFile()));
		
		
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