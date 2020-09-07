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
    	//System.out.println(this.crtFile.getParent() + "\\" + leadingString + this.crtFile.getName());
    	this.crtFile.renameTo(newFileName.toFile());
    }
    
    public void removeLeadingDigits(int leadingDigits) {
    	;
    }
}
