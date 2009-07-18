/**
 * 
 */
package ctietze.xmleditor.controller;

import javax.swing.JEditorPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.xml.XMLNode;

/**
 * Reacts to <code>TreeSelectionEvent</code>s to synchronize the text in the
 * editor's RichEdit text box with the text value of the currently selected
 * node.
 * 
 * @author Christian Tietze
 */
public class NodeValueToRichEditContentSynchronizer implements TreeSelectionListener {
	
	/** Text to display when no node is selected */
	private static final String DEFAULT_RICH_EDIT_TEXT = 
		Resources.getString("richedit.default");
	
	/** The RichEdit with text contents of XML nodes in it */
	private JEditorPane richEdit = null;
	
	/**
	 * Creates a fresh instance which knows the RichEdit component to 
	 * display text values of XML nodes and attributes.
	 * 
	 * @param editPane	RichEdit to put text into
	 */
	public NodeValueToRichEditContentSynchronizer(JEditorPane editPane) {
		this.richEdit = editPane;
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
			
			if (oldNode != null && oldNode != rootNode) {
				((XMLNode)oldNode).setValue(richEdit.getText());
			}
		}
		
		// Show data of newly selected node (if any) and set editability of
		// <code>richEdit</code> appropriately
		if (newNode != null) {
			if (newNode != rootNode
					&& ((XMLNode)newNode).canValueBeAssigned()) {
				// Enable editing for non-root nodes which can have values
				richEdit.setText(((XMLNode)newNode).getValue());
				richEdit.setEnabled(true);
			} else {
				// Disable text editing for root node and nodes with children
				// Show underlying XML structure
				richEdit.setText(((XMLNode)newNode).generateXMLStructure());
				richEdit.setEnabled(false);
			}
		}
		
		// If the root node couldn't be resolved (i.e. new tree generated)
		// then reset RichEdit
		// TODO reset this in SaveAction when implemented
		if (rootNode == null) {
			richEdit.setText(DEFAULT_RICH_EDIT_TEXT);
			richEdit.setEnabled(false);
		}
	}
	
}
