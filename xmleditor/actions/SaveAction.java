/**
 * 
 */
package xmleditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import xmleditor.Resources;
import xmleditor.gui.editor.EditorWindow;
import xmleditor.xml.XmlDocument;

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
		XmlDocument xmlDocument = editorWindow.getOpenXml();
		
		if (xmlDocument.isSavedOnDisk()) {
			xmlDocument.save();
		} else {
			// perform a SaveAsAction
			super.actionPerformed(e);
		}
	}
}

