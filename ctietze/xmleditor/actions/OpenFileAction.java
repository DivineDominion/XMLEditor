/**
 * 
 */
package ctietze.xmleditor.actions;

import java.io.File;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLTree;
import ctietze.xmleditor.xml.XMLDocument;

/**
 * Opens a file on disk and loads its contents into the editor.
 * <p>
 * Bound to CTRL-O.
 * 
 * @author Christian Tietze
 */
public class OpenFileAction extends AbstractUnsavedChangesAction {

	private static final String ACTION_NAME = Resources.getString("gui.menu.file.open");
	
	public OpenFileAction(EditorWindow editor) {
		super(editor, ACTION_NAME);
		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
	}
	
	@Override
	protected void doPerformAction() {
		XMLDocument document = null;
		
		JFileChooser fileChooser = XMLDocument.getXmlFileChooser();
		int result = fileChooser.showOpenDialog(editorWindow);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			document = new XMLDocument(fileChooser.getSelectedFile());
			
			DefaultTreeModel treeModel = (DefaultTreeModel) editorWindow.getXmlTree().getModel();
			treeModel.setRoot(document.getRootNode());
			
			editorWindow.setXmlDocument(document);
		}
	}
}
