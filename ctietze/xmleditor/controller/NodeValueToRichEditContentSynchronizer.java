/**
 * 
 */
package ctietze.xmleditor.controller;

import javax.swing.JEditorPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLNode;

/**
 * Reacts to <code>TreeSelectionEvent</code>s to synchronize the text in the
 * editor's RichEdit text box with the text value of the currently selected
 * node.
 * <p>
 * Instead of syncing text, the XML structure of the current node will also
 * be assigned to the RichEdit as soon as the selected node has no content on
 * its own. 
 * 
 * @author Christian Tietze
 */
public class NodeValueToRichEditContentSynchronizer implements TreeSelectionListener, TreeModelListener{
	
	/** Text to display when no node is selected */
	private static final String DEFAULT_RICH_EDIT_TEXT = 
		Resources.getString("richedit.default");
	
	/** The RichEdit with text contents of XML nodes in it */
	private JEditorPane richEdit = null;
	
	private EditorWindow editorWindow = null;
	
	/**
	 * Creates a fresh instance which knows the RichEdit component to 
	 * display text values of XML nodes and attributes.
	 * 
	 * @param editPane	RichEdit to put text into
	 */
	public NodeValueToRichEditContentSynchronizer(EditorWindow editor) {
		this.editorWindow = editor;
		this.richEdit = editor.getRichEdit();
	}
	
	/**
	 * Handles events of changed tree selections. Depending on the kind
	 * of node which is selected, the <code>richEdit</code> will display
	 * the text value of a newly selected node while the previously edited
	 * content will be stored in the last selected node before refreshing.
	 * 
	 * @param e		Event which includes the old and new node
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Object rootNode = null;
		Object oldNode  = null;
		Object newNode  = null;
		
		// Root node may not be resolved if a new tree is generated!
		if (e.getNewLeadSelectionPath() != null) {
			newNode  = e.getNewLeadSelectionPath().getLastPathComponent();
			rootNode = e.getNewLeadSelectionPath().getPathComponent(0);
		}
		
		// Store data of previous selected node (if any)
		if (e.getOldLeadSelectionPath() != null) {
			oldNode = e.getOldLeadSelectionPath().getLastPathComponent();
			
			if (oldNode != null && oldNode != rootNode
					&& ((XMLNode) oldNode).canValueBeAssigned()) {
				((XMLNode) oldNode).setValue(richEdit.getText());
			}
		}
		
		// Show data of newly selected node (if any) and set editability of
		// <code>richEdit</code> appropriately
		if (newNode != null) {
			insertNodeText(newNode, rootNode);
		}
		
		// If the root node couldn't be resolved (i.e. new tree generated)
		// then reset RichEdit
		// TODO reset this in SaveAction when implemented
		if (rootNode == null) {
			richEdit.setText(DEFAULT_RICH_EDIT_TEXT);
			richEdit.setEnabled(false);
		}
	}

	/**
	 * Automatically update the RichEdit on node insertion, because the RichEdit
	 * may display the XML Structure
	 */
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		Object changedParentNode 	= e.getTreePath().getLastPathComponent();
		Object rootNode 			= e.getTreePath().getPathComponent(0);
		
		insertNodeText(changedParentNode, rootNode);
	}
		
	/**
	 * Insert the node's contents or the underlying XML Structure into the 
	 * <code>richEdit</code>.
	 * 
	 * @param sourceNode	Node from which the content should be grabbed 
	 * @param rootNode		Root of the tree to compare against
	 */
	private void insertNodeText(Object sourceNode, Object rootNode){
		if (sourceNode != rootNode
				&& ((XMLNode) sourceNode).canValueBeAssigned()) {
			// Enable editing for non-root nodes which can have values
			richEdit.setText(((XMLNode) sourceNode).getValue());
			richEdit.setEnabled(true);
		} else {
			// Disable text editing for root node and nodes with children
			// Show underlying XML structure
			richEdit.setText(((XMLNode) sourceNode).generateXMLStructure());
			richEdit.setEnabled(false);
		}
		
		richEdit.moveCaretPosition(0);
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
		
		editorWindow.getXmlTree().setEnabled(enableMenus);
		// TODO this isn't really the appropriate place
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {		
	}
	
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
	}
}
