/**
 * 
 */
package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.tree.*;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLNode;

/**
 * Adds a default child node.
 * <p>
 * Bound to CTRL-ENTER.
 * 
 * @author Christian Tietze
 */
public class AddChildNodeAction extends AbstractEditMenuAction {
	
	private static final String ACTION_NAME = Resources.getString("gui.menu.edit.add_child_node");
	
	private static final String DEFAULT_NODE_NAME = Resources.getString("xmltree.default_node");

	public AddChildNodeAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl ENTER"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (lastPath != null) {
			XMLNode selectedNode = (XMLNode) lastPath.getLastPathComponent();
			XMLNode newNode = new XMLNode(DEFAULT_NODE_NAME);
			
			DefaultTreeModel treeModel = (DefaultTreeModel) editorWindow.getXmlTree().getModel();
			
			treeModel.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
		}
	}

	@Override
	protected boolean doesSelectionFitForEnabling(XMLNode selectedNode) {
		return selectedNode.canChildNodesBeAdded();
	}
}
