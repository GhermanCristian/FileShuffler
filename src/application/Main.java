package application;
	
import java.io.File;
import java.util.List;
import java.util.Vector;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class Main extends Application {
	private File selectedDirectory = null;
	
	private final int WINDOW_WIDTH = 500;
	private final int WINDOW_HEIGHT = 400;
	private final int GAP = 20;
	private final int DEFAULT_ADDED_DIGITS = 4;
	private final int LATERAL_BUTTON_WIDTH = 180;
	private final int CENTRAL_BUTTON_WIDTH = 100;
	
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
	
	private int selectDigitCountButtonAction() {
		final int SELECT_DIGITS_WINDOW_HEIGHT = 175;
		
		Stage currentStage = new Stage();
		Scene currentScene;
		VBox layout = new VBox(GAP);
		Slider slider = new Slider();
		Label filenameExampleLabel = new Label("Example: 0123_Undeva-n Balkani.mp3"); // 4 digits of 1 because that's the default value
		Button selectButton = new Button("Select");
		
		Vector<Integer> selectedDigits = new Vector<Integer>();
		selectedDigits.add(DEFAULT_ADDED_DIGITS);
		
		selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	selectedDigits.setElementAt((int)slider.getValue(), 0);
            	currentStage.close();
            }
        });
		
		slider.setMin(1);
		slider.setMax(10);
		slider.setValue(DEFAULT_ADDED_DIGITS);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);
		slider.setBlockIncrement(1);
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				String example = new String("Example: ");
				selectedDigits.setElementAt(new_val.intValue(), 0);
				for (int i = 0; i < new_val.intValue(); i++) { 
					// new_val should always be of type int, but we need to convert to int explicitly
					example += Integer.toString(i);
				}
				example += "_Undeva-n Balkani.mp3";
				filenameExampleLabel.setText(example);
			}
        });
		
		layout.getChildren().addAll(slider, filenameExampleLabel, selectButton);
		currentScene = new Scene(layout, WINDOW_WIDTH, SELECT_DIGITS_WINDOW_HEIGHT);
		currentStage.setTitle("Select no. of digits");
		currentStage.setScene(currentScene);
		currentStage.setAlwaysOnTop(true);
		currentStage.setOnCloseRequest(event -> {selectedDigits.setElementAt(DEFAULT_ADDED_DIGITS, 0);});
		currentStage.showAndWait();
		
		return selectedDigits.get(0);
	}
	
	private void setAllCheckedValue(ListView<ListViewItem> listView, boolean value) {
		List<ListViewItem> items = listView.getItems();
    	for (ListViewItem crtItem : items) {
    		crtItem.setCheckedValue(value);
    	}
    	listView.setItems(FXCollections.observableList(items)); // this is done to update the list view
	}
	
	@Override
	public void start(Stage primaryStage) {
		VBox mainProgramLayout;
		Scene mainScene;
		
		GridPane buttonAreaLayout1 = new GridPane();
		Button addDigitsButton;
		Button removeDigitsButton;
		Button selectFolderButton;
		Button selectAllFilesButton;
		Button deselectAllFilesButton;
		
		ListView<ListViewItem> listView = new ListView<ListViewItem>();
		listView.setOrientation(Orientation.VERTICAL); // the orientation would've been vertical by default, but it's good to be safe
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listView.setCellFactory(CheckBoxListCell.forListView(new Callback<ListViewItem, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(ListViewItem item) {
                return item.isCheckedProperty();
            }
        }));
	
		mainProgramLayout = new VBox(GAP);
		mainScene = new Scene(mainProgramLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		mainProgramLayout.getChildren().add(listView);
		
		addDigitsButton = new Button("Add leading digit group");
		addDigitsButton.setMinWidth(LATERAL_BUTTON_WIDTH);
		removeDigitsButton = new Button("Remove leading digit group");
		removeDigitsButton.setMinWidth(LATERAL_BUTTON_WIDTH);
		selectFolderButton = new Button("Select folder");
		selectFolderButton.setMinWidth(CENTRAL_BUTTON_WIDTH);
		selectAllFilesButton = new Button("Select all files");
		selectAllFilesButton.setMinWidth(LATERAL_BUTTON_WIDTH);
		deselectAllFilesButton = new Button("Deselect all files");
		deselectAllFilesButton.setMinWidth(LATERAL_BUTTON_WIDTH);
		
		addDigitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	List<ListViewItem> items = listView.getItems();
            	int selectedDigits = selectDigitCountButtonAction();
            	for (ListViewItem crtItem : items) {
            		crtItem.addLeadingDigits(selectedDigits);
            	}
            	listView.setItems(FXCollections.observableList(items)); // this is done to update the list view
            }
        });
		removeDigitsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	List<ListViewItem> items = listView.getItems();
            	for (ListViewItem crtItem : items) {
            		crtItem.removeLeadingDigits();
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
		selectAllFilesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	setAllCheckedValue(listView, true); // the files are selected => checked value = true
            }
        });
		
		deselectAllFilesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	setAllCheckedValue(listView, false); // the files are selected => checked value = true
            }
        });
		
		GridPane.setConstraints(addDigitsButton, 0, 0);
		GridPane.setHalignment(addDigitsButton, HPos.CENTER);
		GridPane.setConstraints(removeDigitsButton, 2, 0);
		GridPane.setHalignment(removeDigitsButton, HPos.CENTER);
		GridPane.setConstraints(selectFolderButton, 1, 0);
		GridPane.setHalignment(selectFolderButton, HPos.CENTER);
		GridPane.setConstraints(selectAllFilesButton, 0, 1);
		GridPane.setHalignment(selectAllFilesButton, HPos.LEFT);
		GridPane.setConstraints(deselectAllFilesButton, 2, 1);
		GridPane.setHalignment(selectAllFilesButton, HPos.RIGHT);
		buttonAreaLayout1.getStyleClass().add("gridpane");
		buttonAreaLayout1.getChildren().addAll(addDigitsButton, removeDigitsButton, selectFolderButton, selectAllFilesButton, deselectAllFilesButton);
		mainProgramLayout.getChildren().add(buttonAreaLayout1);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("File Shuffler");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
