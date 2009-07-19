package ctietze.xmleditor.actions;

import java.io.File;
import java.net.URL;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import ctietze.experiments.XMLParsingTest;
import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLAttribute;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLParser;
import ctietze.xmleditor.xml.XMLTree;
import ctietze.xmleditor.xml.XMLDocument;

/**
 * Create new tree contents with some XML content to show all features.
 * <p>
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
		XMLNode rootNode = createDummyNodes();
		
		DefaultTreeModel treeModel = (DefaultTreeModel) editorWindow.getXmlTree().getModel();
		treeModel.setRoot(rootNode);
		
		editorWindow.setXmlDocument(new XMLDocument(rootNode));
		
		//editorWindow.getXmlTree().setEnabled(true);
		editorWindow.getXmlTree().requestFocusInWindow();
	}
	
	/**
	 * Generates tree contents from example XML file.
	 * 
	 * @return 	Root node with some example content
	 */
	private XMLNode createDummyNodes() {
		URL url = Resources.getFileURL("example3.xml");
		XMLNode node = XMLParser.parseXmlFromUrl(url);
		return node;
	}
}
