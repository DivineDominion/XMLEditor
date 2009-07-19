package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLTree;

public abstract class AbstractEditMenuAction extends AbstractEditorAction implements TreeSelectionListener, FocusListener, TreeModelListener {
	/** Last selected node in the tree */
	protected TreePath lastPath = null;
	
	public AbstractEditMenuAction(EditorWindow editorWindow, String name) {
		super(editorWindow, name);
	}
	
	/**
	 * Will be called when lots of nodes are modified.  In this case, I 
	 * assume there'll be more open/close actions which are handled here.
	 * In any other case (node with lots of childs deleted, I think) this
	 * method will do no harm as well.
	 */
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// for all ADDs and REMOVE
		// but shouldn't be for EXPAND, COLLAPSE
		setEnabled(false);
	}
	
	/**
	 * Selection has changed. Adjust the menu items according to the
	 * newly selected node.
	 *  
	 * @see #changeEditMenuAccordingToSelection(XMLNode)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		XMLTree tree = editorWindow.getXmlTree();
		//System.out.println(e.getPath() + " --- " + e.getNewLeadSelectionPath() + " --- " + e.getOldLeadSelectionPath() + " --- " + editorWindow.getXmlTree().getSelectionPath());
		
		if(tree.getSelectionCount() == 1) {
			lastPath = tree.getSelectionPath(); // TODO try e.getNewLeadSelectionPath(), seems to work as well
		} else {
			lastPath = null;
		}
		
		if (lastPath != null) {
			XMLNode selectedNode = (XMLNode) lastPath.getLastPathComponent();
			
			setEnabled(doesSelectionFitForEnabling(selectedNode));
		} else {
			setEnabled(false);
		}
	}

	/**
	 * During {@link #focusLost(FocusEvent) focusLost} all actions were \
	 * disabled;  enablind them has to be selection-sensitive.
	 * 
	 * @see #changeEditMenuAccordingToSelection(XMLNode)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource().equals(editorWindow.getXmlTree())
				&& editorWindow.getXmlTree().getSelectionCount() == 1) {
			XMLNode selectedNode = (XMLNode) lastPath.getLastPathComponent();
			
			setEnabled(doesSelectionFitForEnabling(selectedNode));
		}
	}
	
	/**
	 * Basically disables all important edit actions when the tree loses 
	 * focus to the RichEdit.  When the menu is selected, the items should
	 * stay enabled.  This is so to prevent shortcuts being hit when one
	 * is editing the contents.  
	 */
	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource().equals(editorWindow.getXmlTree())
				&& e.getOppositeComponent() != null
				&& e.getOppositeComponent().equals(editorWindow.getRichEdit())) {
			// Tree-Modifier shortcuts shouldn't work when rich edit has
			// focus (but they should if the menu has)
			setEnabled(false);
		}
	}
	
	/**
	 * add child  --> selectedNode.canChildNodesBeAdded()
	 * add attrib --> selectedNode.canAttributesBeAdded()
	 * del node   --> true if != null
	 * expand & collapse --> ALWAYS true
	 */
	protected abstract boolean doesSelectionFitForEnabling(XMLNode selectedNode);

	@Override
	public void treeNodesChanged(TreeModelEvent e) {	
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
	}

}
