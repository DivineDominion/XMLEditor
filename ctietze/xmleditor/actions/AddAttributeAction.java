/**
 * 
 */
package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLAttribute;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLTree;

/**
 * Adds a default attribute.
 * <p>
 * Bound to ALT-ENTER.
 * 
 * @author Christian Tietze
 */
public class AddAttributeAction extends AbstractEditMenuAction {
	
	private static final String ACTION_NAME = Resources.getString("gui.menu.edit.add_attribute");
	
	private static final String DEFAULT_ATTRIBUTE_NAME = Resources.getString("xmltree.default_attribute");

	public AddAttributeAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt ENTER"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (lastPath != null) {
			XMLNode selectedNode = (XMLNode) lastPath.getLastPathComponent();
			XMLAttribute newNode = new XMLAttribute(DEFAULT_ATTRIBUTE_NAME);
			
			DefaultTreeModel treeModel = (DefaultTreeModel) editorWindow.getXmlTree().getModel();
			
			treeModel.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
		}
	}

	@Override
	protected boolean doesSelectionFitForEnabling(XMLNode selectedNode) {
		return selectedNode.canAttributesBeAdded();
	}
}