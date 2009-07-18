package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.dialogs.StackTraceErrorDialog;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XmlDocument;


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
public class SaveAsAction extends AbstractEditorAction {

	/** <code>true</code> if user clicks on "cancel" */
	private boolean savingCancelled = false;

	private static final String ACTION_NAME = Resources.getString("action.save_as.name");
	private static final String ACTION_TOOLTIP = Resources.getString("action.save_as.tooltip");


	private static final String ERROR_SAVE_DOCUMENT_CHANGES_TITLE = 
		Resources.getString("error.save_document_changes.title");
	private static final String ERROR_SAVE_DOCUMENT_CHANGES_TEXT = 
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
		XmlDocument document = editorWindow.getOpenXml();
		
		// TODO suggest old name?
		savingCancelled = false;
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(editorWindow);

		switch (result) {
		case JFileChooser.APPROVE_OPTION:
			try {
				document.saveAs(fileChooser.getSelectedFile());
			} catch (Exception ex) {
				// TODO refactor: error message in external class
				JOptionPane.showMessageDialog(editorWindow, ERROR_SAVE_DOCUMENT_CHANGES_TEXT, 
						ERROR_SAVE_DOCUMENT_CHANGES_TITLE, JOptionPane.ERROR_MESSAGE);

				StackTraceErrorDialog.makeNew(editorWindow, ex);
			}
			break;
		case JFileChooser.CANCEL_OPTION:
			savingCancelled = true;
		}
	}

	public boolean isSavingCancelled() {
		return savingCancelled;
	}
}