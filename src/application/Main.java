package application;
	
import java.io.File;
import java.util.List;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Main extends Application {
	private File selectedDirectory = null;
	
	private List<File> getFilesInDirectory() {
		List<File> filesInDirectory = new Vector<File>();
		if (selectedDirectory != null) {
	    	File[] allFiles = selectedDirectory.listFiles();
	    	if (allFiles != null) {
	    		for (File crtFile : allFiles) { // make a lambda for this
	    			if (crtFile.isFile() == true) {
	    				filesInDirectory.add(crtFile);
	    			}
	    		}
	    	}
	    }
		return filesInDirectory;
	}
	
	private void clickedButtonAction(VBox mainProgramLayout, ListView<File> listView) {
		Stage directoryChooserStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        
		ObservableList<File> listItems;
        
        directoryChooser.setTitle("Choose directory");
        directoryChooserStage.setAlwaysOnTop(true);
        selectedDirectory = directoryChooser.showDialog(directoryChooserStage);
        directoryChooserStage.setAlwaysOnTop(false);
        
        listItems = FXCollections.observableList(getFilesInDirectory());
		listView.setItems(listItems);
		mainProgramLayout.getChildren().add(listView);
	}
	
	@Override
	public void start(Stage primaryStage) {
		StackPane directoryChooserLayout;
		Button selectFolderButton;
		Scene directoryChooserScene;
		
		VBox mainProgramLayout;
		Scene mainScene;
		
		ListView<File> listView = new ListView<File>();
		listView.setOrientation(Orientation.VERTICAL); // the orientation would've been vertical by default, but it's good to be safe
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		mainProgramLayout = new VBox(20); // gap = 20px
		mainScene = new Scene(mainProgramLayout, 400, 400);
		
		directoryChooserLayout = new StackPane();
		selectFolderButton = new Button("Choose folder");
		selectFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	primaryStage.setAlwaysOnTop(false); // the setAlwaysOnTop stuff might not be that useful, but we keep it for now
                clickedButtonAction(mainProgramLayout, listView);
                primaryStage.setAlwaysOnTop(true);
                primaryStage.setScene(mainScene); // after we click the button, we move to the next scene
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
