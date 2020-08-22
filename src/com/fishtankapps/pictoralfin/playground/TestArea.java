package com.fishtankapps.pictoralfin.playground;


import javax.swing.JFrame;

import com.fishtankapps.pictoralfin.utilities.Utilities;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class TestArea {

	public static void main(String[] args) throws Exception {

		 
		JFXPanel panel = new JFXPanel();
		 
		 final ColorPicker colorPicker = new ColorPicker();
	        colorPicker.setValue(Color.RED);
	
	 
	        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) { 
	            	System.out.println(Utilities.jfxColorToAwtColor(colorPicker.getValue()));
	            }
	        });
	 
	        FlowPane root = new FlowPane();
	        root.setPadding(new Insets(10));
	        root.setHgap(10);
	        root.getChildren().addAll(colorPicker);
	 
	        Scene scene = new Scene(root, 400, 300);
	 
	        
	        panel.setScene(scene);
	        
	        JFrame frame = new JFrame();
	        frame.setSize(400, 300);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        frame.setLocationRelativeTo(null);
	        
	        frame.add(panel);
	        
	        frame.setVisible(true);
	}
	
}
