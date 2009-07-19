package ctietze.xmleditor.actions;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLAttribute;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLTree;
import ctietze.xmleditor.xml.XMLDocument;

/**
 * Creates a new blank XML Document while asking the user to save changes to his
 * efforts first.
 * 
 * @author Christian Tietze
 */
public class NewTreeAction extends AbstractUnsavedChangesAction {

	private static final String ACTION_NAME = Resources.getString("gui.menu.file.new");

	/**
	 * Mainly sets up CTRL+N as shortcut.
	 * 
	 * @param editor	<code>EditorWindow</code> to manipulate
	 */
	public NewTreeAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
	}
	
	/**
	 * Assigns a new instance of {@link DefaultTreeModel} with merely a root 
	 * node to the editor's XML tree.
	 */
	@Override
	protected void doPerformAction() {
		XMLNode rootNode = XMLTree.createEmptyRootNode();
		
		DefaultTreeModel treeModel = (DefaultTreeModel) editorWindow.getXmlTree().getModel();
		treeModel.setRoot(rootNode);
		
		editorWindow.setXmlDocument(new XMLDocument(rootNode));
		
		//editorWindow.getXmlTree().setEnabled(true);
		editorWindow.getXmlTree().requestFocusInWindow();
	}
}
