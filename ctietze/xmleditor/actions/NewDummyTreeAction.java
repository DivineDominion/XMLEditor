package ctietze.xmleditor.actions;

import java.net.URL;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ctietze.experiments.XMLParsingTest;
import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLAttribute;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLParser;
import ctietze.xmleditor.xml.XMLTree;

/**
 * 
 * Responds to CTRL+ALT+N
 * 
 * @author Christian Tietze
 *
 */
public class NewDummyTreeAction extends AbstractUnsavedChangesAction {

	private static final String ACTION_NAME = Resources.getString("gui.menu.file.new_dummies");

	public NewDummyTreeAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt N"));
	}
	
	/**
	 * Assigns the XMLTree a new model with dummy contents.
	 * @see #createDummyNodes() 
	 */
	@Override
	protected void doPerformAction() {
		//XMLNode[] nodes = { createDummyNodes() };
		
		//editorWindow.getXmlTree().setModel(XMLTree.createTreeModelWith(nodes));
		editorWindow.getXmlTree().setModel(XMLTree.createTreeModel(createDummyNodes()));
		
		editorWindow.getXmlTree().setEnabled(true);
		editorWindow.getXmlTree().requestFocusInWindow();
	}
	
	/**
	 * Generates a faux HTML document.
	 * 
	 * @return 	Node with "html" in it
	 */
	private XMLNode createDummyNodes() {
		URL url = Resources.getFileURL("example3.xml");
		XMLNode node = XMLParser.parseXmlFromFile(url);
		return node;
	}
	/*
	private XMLNode createDummyNodes() {
		XMLNode xml1 = new XMLNode("html");
		XMLNode xml2a = new XMLNode("head");
		XMLNode xml2b = new XMLNode("body");
		XMLNode xml3a = new XMLNode("h1");
		xml3a.setValue("News");
		XMLNode xml3b = new XMLNode("p");
		xml3b.setValue("lorem ipsum dolor sit amet ...");
		XMLAttribute attrib3a = new XMLAttribute("class");
		attrib3a.setValue("page_title");
		
		xml3a.add(attrib3a);
		
		xml2b.add(xml3a);
		xml2b.add(xml3b);
		
		xml1.add(xml2a);
		xml1.add(xml2b);
		
		return xml1;
	}
	*/
}
