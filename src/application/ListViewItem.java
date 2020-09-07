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
    	System.out.println(newFileName);
    	File newFile = newFileName.toFile();
    	this.crtFile.renameTo(newFile);
    	crtFile = newFile;
    }
    
    public void removeLeadingDigits(int leadingDigits) {
    	if (this.isChecked.get() == false) {
    		return; // only modifiy the checked files
    	}
    	
    	String oldName = this.crtFile.getName();
    	
    	if (leadingDigits + 1 > oldName.length()) {
    		return; // in case the string is not long enough there's no need to even check for the leading digits
    	}
    	
    	for (int position = 0; position < leadingDigits; position++) {
    		if (oldName.charAt(position) < '0' || oldName.charAt(position) > '9') {
    			return; // the first "leadingDigits" characters are not all digits
    		}
    	}
    	if (oldName.charAt(leadingDigits) != '_') {
    		return; // the first character after the leading digits is not the separator (underscore)
    	}
    	
    	Path newFileName = Paths.get(this.crtFile.getParent(), oldName.substring(leadingDigits + 1));
    	System.out.println(newFileName);
    	File newFile = newFileName.toFile();
    	this.crtFile.renameTo(newFile);
    	crtFile = newFile;
    }
}
