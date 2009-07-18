package ctietze.xmleditor.actions;

import javax.swing.Action;
import javax.swing.KeyStroke;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.gui.editor.EditorWindow;


/**
 * Invoked by pressing CTRL+Q, closing the window or chosing the menu action.
 * 
 * @author Christian Tietze
 */
public class QuitAction extends AbstractUnsavedChangesAction {

	private static final String ACTION_NAME = Resources.getString("gui.menu.file.quit");

	public QuitAction(EditorWindow editor) {
		super(editor, ACTION_NAME);

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
	}

	/**
	 * Closes the window and terminates program execution when invoked.
	 */
	@Override
	protected void doPerformAction() {
		editorWindow.setVisible(false);
		editorWindow.dispose();
		System.exit(0);
	}
}
