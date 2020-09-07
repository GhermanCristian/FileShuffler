package application;
	
import java.io.File;
import java.util.List;
import java.util.Vector;

import com.sun.glass.events.WindowEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
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
	
	private void selectFolderButtonAction(VBox mainProgramLayout, ListView<ListViewItem> listView) {
		Stage directoryChooserStage = new Stage();
		DirectoryChooser directoryChooser = new DirectoryChooser();
        
		ObservableList<ListViewItem> listItems;
        
		directoryChooser.setTitle("Choose directory");
        directoryChooserStage.setAlwaysOnTop(true); // not sure the "setAlwaysOnTop" is really useful but it's staying here for now
        selectedDirectory = directoryChooser.showDialog(directoryChooserStage);
        directoryChooserStage.setAlwaysOnTop(false);
        
        List<File> filesInDirectory = getFilesInDirectory();
        List<ListViewItem> listViewItems = new Vector<ListViewItem>();
        for (File crtFile : filesInDirectory) {
        	listViewItems.add(new ListViewItem(crtFile, false));
        }
        listItems = FXCollections.observableList(listViewItems);
		listView.setItems(listItems);
	}
	
	@Override
	public void start(Stage primaryStage) {
		VBox mainProgramLayout;
		Scene mainScene;
		
		HBox buttonAreaLayout;
		Button addDigitsButton;
		Button removeDigitsButton;
		Button selectFolderButton;
		
		ListView<ListViewItem> listView = new ListView<ListViewItem>();
		listView.setOrientation(Orientation.VERTICAL); // the orientation would've been vertical by default, but it's good to be safe
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listView.setCellFactory(CheckBoxListCell.forListView(new Callback<ListViewItem, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(ListViewItem item) {
                return item.isCheckedProperty();
            }
        }));
	
		mainProgramLayout = new VBox(20); // gap = 20px
		mainScene = new Scene(mainProgramLayout, 400, 400);
		mainProgramLayout.getChildren().add(listView);
		
		buttonAreaLayout = new HBox(20); // gap = 20px
		addDigitsButton = new Button("Add leading digits");
		removeDigitsButton = new Button("Remove leading digits");
		selectFolderButton = new Button("Select folder");
		
		addDigitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	List<ListViewItem> items = listView.getItems();
            	for (ListViewItem crtItem : items) {
            		crtItem.addLeadingDigits(4); // for now it's a default value, but the user will be able to change it
            	}
            	listView.setItems(FXCollections.observableList(items)); // this is done to update the list view
            }
        });
		
		removeDigitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	List<ListViewItem> items = listView.getItems();
            	for (ListViewItem crtItem : items) {
            		crtItem.removeLeadingDigits(4); // for now it's a default value, but the user will be able to change it
            	}
            	listView.setItems(FXCollections.observableList(items)); // this is done to update the list view
            }
        });
		
		selectFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	primaryStage.setAlwaysOnTop(false); // the setAlwaysOnTop stuff might not be that useful, but we keep it for now
                selectFolderButtonAction(mainProgramLayout, listView);
                primaryStage.setAlwaysOnTop(true);
            }
        });
		
		buttonAreaLayout.getChildren().addAll(addDigitsButton, removeDigitsButton, selectFolderButton);
		mainProgramLayout.getChildren().add(buttonAreaLayout);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("File Shuffler");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
