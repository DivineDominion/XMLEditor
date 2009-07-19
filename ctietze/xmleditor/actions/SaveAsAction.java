package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.dialogs.StackTraceErrorDialog;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLDocument;


/**
 * "Save As ..."-Action in the file menu.
 * <p>
 * Has shortcut "CTRL+SHIFT+S" and knows the <code>EditorWindow</code> to
 * manipulate.
 * <p>
 * Thanks to <em>Adam&nbsp;Turk&nbsp;(<code>aturk@biggeruniverse.com</code>)</em> 
 * and Bjorn&nbsp;Lindeijer&nbsp;(<code>b.lindeijer@xs4all.nl</code>) for their
 * great source inspirations.
 * 
 * @author Christian Tietze
 * @version 1.0 2007-02-27
 */
public class SaveAsAction extends AbstractEditorAction implements TreeModelListener {

	private static final String ACTION_NAME = Resources.getString("action.save_as.name");
	private static final String ACTION_TOOLTIP = Resources.getString("action.save_as.tooltip");

	protected static final String ERROR_SAVE_DOCUMENT_CHANGES_TITLE = 
		Resources.getString("error.save_document_changes.title");
	protected static final String ERROR_SAVE_DOCUMENT_CHANGES_TEXT = 
		Resources.getString("error.save_document_changes.text");
	
	/**
	 * Creates a new instance which shows a file choser dialog.
	 * 
	 * @param editor Window, which shows the file to be saved
	 */
	public SaveAsAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.SHORT_DESCRIPTION, ACTION_TOOLTIP);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));

		// Disabled by default if no document is open
		setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		XMLDocument document = null;
		
		JFileChooser fileChooser = XMLDocument.getXmlFileChooser();
		int result = fileChooser.showSaveDialog(editorWindow);

		if (result == JFileChooser.APPROVE_OPTION) {
			if (editorWindow.hasOpenedDocument()) {
				 document = editorWindow.getXmlDocument();
			} else {
				document = new XMLDocument((XMLNode) editorWindow.getXmlTree().getModel().getRoot());
			}
			
			try {
				// Add .xml if necessary
				File file = fileChooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".xml")) {
					file = new File(file.getAbsolutePath() + ".xml");
				}
				
				document.saveAs(file);
			} catch (Exception ex) {
				// TODO refactor: error message in external class
				JOptionPane.showMessageDialog(editorWindow, ERROR_SAVE_DOCUMENT_CHANGES_TEXT, 
						ERROR_SAVE_DOCUMENT_CHANGES_TITLE, JOptionPane.ERROR_MESSAGE);

				StackTraceErrorDialog.makeNew(editorWindow, ex);
			}
			
			editorWindow.setXmlDocument(document);
		}
	}
	
	/**
	 * Will be called when lots of nodes are modified.  In this case, I 
	 * assume there'll be more open/close actions which are handled here.
	 * In any other case (node with lots of childs deleted, I think) this
	 * method will do no harm as well.
	 * 
	 * Override in {@link QuitAction}!
	 */
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// This is the condition for affected root nodes.
		// Root node affected?  Content change (close, new, open, ...)!
		boolean enableMenus = (e.getTreePath() != null && e.getChildIndices() == null);
		
		setEnabled(enableMenus);
	}
	
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