package ctietze.xmleditor.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.filechooser.FileFilter; 

import javax.swing.JFileChooser;

public class XMLDocument {
	private static final String XML_HEADER = 
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n";
	
	private File file = null;
	
	private XMLNode rootNode = null;
	
	private static JFileChooser fileChooser = null;
	
	/**
	 * This <code>JFileChooser</code> is used for opening and saving.  This
	 * method is a convenience method since it remedies the initialization
	 * and adds a custom file filter as well.
	 * 
	 * @return {@link #fileChooser} instance
	 */
	public static JFileChooser getXmlFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			for (FileFilter filter : fileChooser.getChoosableFileFilters()) {
				fileChooser.removeChoosableFileFilter(filter);
			}
			fileChooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}

					return f.getName().toLowerCase().endsWith(".xml");
				}

				public String getDescription() { 
					return "XML files";
				}  
			});
		}
		return fileChooser;
	}
	
	/**
	 * Loads an XML Document from <code>file</code>. The file's contents are 
	 * parsed instantly.
	 * 
	 * @param file	File to read contents from
	 */
	public XMLDocument(File file) {
		this.file = file;
		
		rootNode = XMLParser.parseXmlFromFile(file);
	}
	
	public XMLDocument(XMLNode rootNode) {
		this.rootNode = rootNode;
	}
	
	public void save() throws FileNotFoundException, IOException {
		if (file == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!file.exists()) {
			throw new FileNotFoundException ("File does not exist: " + file);
		}
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Should not be a directory: " + file);
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException("Is not a file: " + file);
		}
		if (!file.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: " + file);
		}
		
		writeXmlContent(this.file);
	}

	public void saveAs(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Should not be a directory: " + file);
		}
		if (file.exists() && !file.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: " + file);
		}
		
		if (!file.exists()) {
			file.createNewFile();
		}

		writeXmlContent(file);
		
		// Use new file
		this.file = file;
	}
	
	private void writeXmlContent(File file) throws IOException {
		String xmlString = rootNode.generateXMLStructure();

		Writer output = new BufferedWriter(new FileWriter(file));
		
		try {
			output.write(XML_HEADER);
			output.write(xmlString);
		} finally {
			output.close();
		}
	}

	public boolean wasAlreadySavedOnDisk() {
		return (getFile() != null);
	}
	
	public XMLNode getRootNode() {
		return rootNode;
	}
	
	public File getFile() {
		return file;
	}
	
	public boolean hasUnsavedChanges() {
		// TODO implement a simple check
		return false;
	}
}
