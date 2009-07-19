/**
 * 
 */
package ctietze.xmleditor.actions;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.dialogs.StackTraceErrorDialog;
import ctietze.xmleditor.gui.editor.EditorWindow;
import ctietze.xmleditor.xml.XMLDocument;


/**
 * Extends <code>SaveAsAction</code> to fall back to the file chosing if
 * necessary.
 * <p>
 * "CTRL-S" is assigned as a shortcut.
 * 
 * @author Christian Tietze
 * @version 1.0 2007-02-27
 */
public class SaveAction extends SaveAsAction {
	/** The localized name of this action */
	private static final String ACTION_NAME = 
		Resources.getString("action.save.name");
	
	/** The localized tooltip of this action */
	private static final String ACTION_TOOLTIP = 
		Resources.getString("action.save.tooltip");

	public SaveAction(EditorWindow editor) {
		super(editor);

		putValue(Action.NAME, ACTION_NAME);
		putValue(Action.SHORT_DESCRIPTION, ACTION_TOOLTIP);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
	}

	public void actionPerformed(ActionEvent e) {
		XMLDocument xmlDocument = editorWindow.getXmlDocument();
		
		if (xmlDocument.wasAlreadySavedOnDisk()) {
			try {
				xmlDocument.save();
			} catch (IOException ex) {
				// TODO refactor: error message in external class
				JOptionPane.showMessageDialog(editorWindow, ERROR_SAVE_DOCUMENT_CHANGES_TEXT, 
						ERROR_SAVE_DOCUMENT_CHANGES_TITLE, JOptionPane.ERROR_MESSAGE);

				StackTraceErrorDialog.makeNew(editorWindow, ex);
			}
		} else {
			// perform a SaveAsAction
			super.actionPerformed(e);
		}
	}
}

