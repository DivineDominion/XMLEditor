/**
 * 
 */
package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.TreeModelEvent;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLNode;

/**
 * Expands all nodes in the whole {@link XMLTree}.
 * <p>
 * Bound to CTRL-[.
 * 
 * @author Christian Tietze
 */
public class CollapseAllAction extends AbstractEditMenuAction {
	
	private static final String ACTION_NAME = Resources.getString("gui.menu.edit.collapse_all");

	public CollapseAllAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control ]"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		editorWindow.getXmlTree().collapseAll();
	}
	
	/**
	 * Will be called when lots of nodes are modified.  In this case, I 
	 * assume there'll be more open/close actions which are handled here.
	 * In any other case (node with lots of childs deleted, I think) this
	 * method will do no harm as well.
	 */
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// This is the condition for affected root nodes.
		// Root node affected?  Content change (close, new, open, ...)!
		boolean enableMenus = (e.getTreePath() != null && e.getChildIndices() == null);
		
		setEnabled(enableMenus);
	}

	@Override
	protected boolean doesSelectionFitForEnabling(XMLNode selectedNode) {
		// Selection doesnt matter
		return true;
	}
}