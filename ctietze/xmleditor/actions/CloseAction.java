package ctietze.xmleditor.actions;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.controller.NodeValueToRichEditContentSynchronizer;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLTree;

/**
 * Closes the currently open XML document in the editor window if the user
 * proceeds (i.e. saves or refuses saving).
 * <p>
 * Bound to CTRL+W.
 * 
 * @author Christian Tietze
 */
public class CloseAction extends AbstractUnsavedChangesAction {

	private static final String ACTION_NAME = Resources.getString("gui.menu.file.close");

	public CloseAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
	}
	
	/**
	 * Clears the XMLTree and the RichEdit contents. Both are disabled,
	 * since listeners check for changes.
	 * 
	 * @see NodeValueToRichEditContentSynchronizer 
	 */
	@Override
	protected void doPerformAction() {
		DefaultTreeModel treeModel = (DefaultTreeModel) editorWindow.getXmlTree().getModel();;
		treeModel.setRoot(null);
		editorWindow.setXmlDocument(null);
	}

}
