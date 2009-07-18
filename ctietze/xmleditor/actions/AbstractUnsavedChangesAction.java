
package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;


/**
 * Shall be implemented by any <code>Action</code> which requires changes to
 * the XML file to be made before continuing, e.g. quitting, opening a new
 * file or creating a blank one. 
 * <p>
 * Implementations must override <code>doPerformAction</code>. This method will
 * be executed when the user saved successfully or declines saving.
 * <p>
 * <code>AbstractUnsavedChangesAction</code> inherits <code>SaveAction</code>
 * to simplify the call to actually save the changes.
 * 
 * @author Christian Tietze
 *
 */
public abstract class AbstractUnsavedChangesAction extends SaveAction {

	private static final String SAVE_DOCUMENT_CHANGES_TITLE = 
		Resources.getString("dialog.save_document_changes.title");
	private static final String SAVE_DOCUMENT_CHANGES_TEXT = 
		Resources.getString("dialog.save_document_changes.text");

	/**
	 * Creates a new instance with a specified name and a long description.
	 * 
	 * @param editor 		<code>EditorWindow</code> to manipulate
	 * @param name 			Caption of the action (for buttons)
	 * @param description 	Description text
	 */
	protected AbstractUnsavedChangesAction(EditorWindow editor,
			String name, String description) {
		super(editor);
		
		// Override "Save"/"Save As" restriction
		setEnabled(true);

		putValue(Action.NAME, name);
		putValue(Action.SHORT_DESCRIPTION, description);
	}
	
	/**
	 * Creates a new instance with a name only.
	 * 
	 * @param editor 	<code>EditorWindow</code> to manipulate
	 * @param name 		Caption of the action (for buttons)
	 */
	protected AbstractUnsavedChangesAction(EditorWindow editor,
			String name) {
		this(editor, name, null);
	}
	
	/**
	 * Perform confirmation check if necessary or continue with execution
	 * of the action right ahead.
	 * @param e			<code>ActionEvent</code> which triggered execution
	 */
	public final void actionPerformed(ActionEvent e) {
		if (editorWindow.hasOpenedDocument()) {
			checkDocumentChanges(e);
		} else {
			doPerformAction();
		}
	}

	/**
	 * Fragt den Nutzer, ob bei ungespeicherten &Auml;nderungen an der aktuell
	 * ge&ouml;ffneten Karte gespeichert werden soll.
	 * <p>
	 * Wenn ja, wird die Karte gespeichert und fortgefahren.<br/>
	 * Wenn nein, wird die KArte zur&uuml;ckgesetzt und fortgefahren.
	 * 
	 * @param e 	<code>ActionEvent</code> 
	 */
	private void checkDocumentChanges(ActionEvent e) {
		if (isDocumentOpenAndUnsaved()) {
			askForSaving(e);
		} else {
			doPerformAction();
		}
	}
	
	/**
	 * Checks whether a document is open in the <code>EditorWindow</code>
	 * and if it actually has unsaved changes.
	 * 
	 * @return <code>true</code> if an open document has unsaved changes
	 * @see EditorWindow#hasOpenedDocument()
	 * @see ctietze.xmleditor.xml.XmlDocument#hasUnsavedChanges()
	 */
	private boolean isDocumentOpenAndUnsaved() {
		return (editorWindow.hasOpenedDocument() 
				&& editorWindow.getOpenXml().hasUnsavedChanges());
	}
	
	/**
	 * Displays a confirmation dialog and performs the desired action
	 * if saving succeeds or no saving should be done.
	 * 
	 * @param e <code>ActionEvent</code> for <code>SaveAction</code>
	 * @see SaveAction#actionPerformed(ActionEvent)
	 */
	private void askForSaving(ActionEvent e) {
		// Ask for saving before continuing
		int choice = JOptionPane.showConfirmDialog(
				editorWindow,
				SAVE_DOCUMENT_CHANGES_TEXT, SAVE_DOCUMENT_CHANGES_TITLE,
				JOptionPane.YES_NO_CANCEL_OPTION);

		switch (choice) {
		case JOptionPane.YES_OPTION:
			// Perform <code>SaveAction</code>
			super.actionPerformed(e);
			
			// TODO can SaveAction fail and should execution be aborted then?
			
			doPerformAction();
			break;
		case JOptionPane.NO_OPTION:
			// Continue execution without saving
			doPerformAction();
			
			break;
		}
		// TODO refactor: doPerformAction() outside switch-case
	}

	/**
	 * Executes inherited actions. Will be executed whenever user wants to
	 * save and did so successfully or if he doesn't want to save at all but
	 * insists on continuation.
	 * <p>
	 * {@inheritDoc}
	 */
	protected abstract void doPerformAction();
}
