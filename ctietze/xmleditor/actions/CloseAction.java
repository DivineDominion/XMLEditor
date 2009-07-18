package ctietze.xmleditor.actions;

import javax.swing.Action;
import javax.swing.KeyStroke;

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
	 * the tree here and the RichEdit via 
	 * {@link NodeValueToRichEditContentSynchronizer}. 
	 */
	@Override
	protected void doPerformAction() {
		editorWindow.getXmlTree().setModel(null); //XMLTree.createEmptyTreeModel()
		
		editorWindow.getXmlTree().setEnabled(false);
		// rich edit is automatically reset due to adapters
	}

}
