package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Main extends Application {
	private File selectedDirectory = null;
	
	private void displayDirectoryContent() {
		if (selectedDirectory != null) {
	    	File[] files = selectedDirectory.listFiles();
	    	if (files != null) {
	    		for (File crtFile : files) {
	    			if (crtFile.isFile() == true) {
	    				System.out.println(crtFile);
	    			}
	    		}
	    	}
	    }
	}
	
	@Override
	public void start(Stage primaryStage) {
		StackPane directoryChooserLayout;
		Button selectFolderButton;
		Scene directoryChooserScene;
		
		directoryChooserLayout = new StackPane();
		selectFolderButton = new Button("Choose folder");
		selectFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage directoryChooserStage = new Stage();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose directory");
                
                primaryStage.setAlwaysOnTop(false);
                directoryChooserStage.setAlwaysOnTop(true);
                
                selectedDirectory = directoryChooser.showDialog(directoryChooserStage);
                displayDirectoryContent();
                
                primaryStage.setAlwaysOnTop(true);
                directoryChooserStage.setAlwaysOnTop(false);
                directoryChooserLayout.getChildren().remove(selectFolderButton); // I should set a new scene
            }
        });
		
		directoryChooserLayout.getChildren().add(selectFolderButton);
		directoryChooserScene = new Scene(directoryChooserLayout, 400, 400);
		
		directoryChooserScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(directoryChooserScene);
		primaryStage.setTitle("File Shuffler");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
