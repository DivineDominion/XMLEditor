package ctietze.xmleditor.xml;

import java.io.File;
import java.net.URL;

public class XmlDocument {
	private URL fileUrl = null;
	
	private XMLTree tree = null;
	
	public XmlDocument(URL fileUrl) {
		this.fileUrl = fileUrl;
		
		XMLNode rootNode = XMLParser.parseXmlFromFile(fileUrl);
		this.tree = new XMLTree(rootNode);
	}	
	
	public void save() {
		// TODO! implement saving
	}
	
	public void saveAs(File file) {
		// TODO! implement save-as
	}
	
	public boolean isSavedOnDisk() {
		return (getFile() != null);
	}
	
	public File getFile() {
		// TODO! return file if saved
		return null;
	}
	
	public String getFileName() {
		// TODO! return file name _if already saved on disk_
		return null;
	}
	
	public boolean hasUnsavedChanges() {
		return false;
	}
}
