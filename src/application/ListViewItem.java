package application;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

class ListViewItem{
	private File crtFile;
	private BooleanProperty isChecked = new SimpleBooleanProperty();
	
	public ListViewItem(File crtFile, boolean isChecked) {
		this.crtFile = crtFile;
		this.isChecked.set(isChecked);
	}
	
	public final BooleanProperty isCheckedProperty() {
		return this.isChecked;
	}
	
	public void setCheckedValue(boolean value) {
		this.isChecked.set(value);
	}
	
	public void reverseCheckedValue() {
		this.isChecked.set(!this.isChecked.get());
	}
	
	public final File getFile() {
		return this.crtFile;
	}
	
    @Override
    public String toString() {
    	// we only display the actual name of the file, not the entire path
    	return this.crtFile.getName();
    }
    
    public void addLeadingDigits(int leadingDigits) {
    	if (this.isChecked.get() == false) {
    		return; // only modifiy the checked files
    	}
    	
    	String leadingString = new String();
    	for (int i = 0; i < leadingDigits; i++) {
    		leadingString += Integer.toString((int)(Math.random() * 10)); // probably there's a cleaner way of doing this
    	}
    	leadingString += "_";
    	
    	Path newFileName = Paths.get(this.crtFile.getParent(), leadingString + this.crtFile.getName());
    	File newFile = newFileName.toFile();
    	this.crtFile.renameTo(newFile);
    	crtFile = newFile;
    }
    
    public void removeLeadingDigits() {
    	if (this.isChecked.get() == false) {
    		return; // only modifiy the checked files
    	}
    	
    	String oldName = this.crtFile.getName();
    	int leadingDigits = 0;
    	
    	while (leadingDigits < oldName.length()) {
    		char currentCharacter = oldName.charAt(leadingDigits);
    		if (currentCharacter < '0' || currentCharacter > '9') {
    			break;
    		}
    		leadingDigits++;
    	}
    	
    	if (leadingDigits >= oldName.length() || oldName.charAt(leadingDigits) != '_') {
    		return; 
    		// the leading digits format is: ddddddd_
    		// so if there's no _ => invalid format => we don't remove anything
    	}
    	
    	Path newFileName = Paths.get(this.crtFile.getParent(), oldName.substring(leadingDigits + 1));
    	File newFile = newFileName.toFile();
    	this.crtFile.renameTo(newFile);
    	crtFile = newFile;
    }
}
