/**
 * 
 */
package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLTree;

/**
 * Deletes the currently selected node which is tracked as a Listener when
 * the user accepts the dialog which pops up.
 * <p>
 * Bound to BACK_SPACE.
 * 
 * @author Christian Tietze
 */
public class DeleteNodeAction extends AbstractEditMenuAction {
	private static final String ACTION_NAME = 
		Resources.getString("gui.menu.edit.delete_node");

	private static final String CONFIRM_DIALOG_TITLE = 
		Resources.getString("dialog.delete_node.title");
	private static final String CONFIRM_DIALOG_TEXT = 
		Resources.getString("dialog.delete_node.text");
	
	public DeleteNodeAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("BACK_SPACE"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (lastPath != null) {
			XMLNode selectedNode = (XMLNode) lastPath.getLastPathComponent();
			
			int deletionChoice = JOptionPane.showConfirmDialog(editorWindow, 
					CONFIRM_DIALOG_TEXT,
                    CONFIRM_DIALOG_TITLE,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
			
			if (deletionChoice == JOptionPane.YES_OPTION) {
				DefaultTreeModel treeModel = (DefaultTreeModel) editorWindow.getXmlTree().getModel();
				
				treeModel.removeNodeFromParent(selectedNode);
			}
		}
	}


	@Override
	protected boolean doesSelectionFitForEnabling(XMLNode selectedNode) {
		// all we need is ensure something is selected
		return (selectedNode != null 
				&& !editorWindow.getXmlTree().getModel().getRoot().equals(selectedNode));
	}
}
