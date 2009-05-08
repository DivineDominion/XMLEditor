/**
 * 
 */

package xmleditor.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;

import xmleditor.gui.editor.EditorWindow;

/**
 * A Bridge between real <code>Action</code>s and <code>AbstractAction</code>s
 * which takes care of <code>EditorWindow</code> assignment.
 * 
 * @author Christian Tietze
 */
public abstract class AbstractEditorAction extends AbstractAction {
	/**
	 * Controlled {@link xmleditor.gui.EditorWindow}
	 */
	protected EditorWindow editorWindow;

	/**
	 * Creates a new instance and saves a reference to the <code>EditorWindow</code>
	 * which is to be controlled.
	 * 
	 * @param editor 	<code>EditorWindow</code> to control
	 * @throws InvalidArgumentException If <code>editor</code> is <code>null</code>
	 */
	public AbstractEditorAction(EditorWindow editor) 
	throws IllegalArgumentException {
		if (editor == null) {
			throw new IllegalArgumentException("editor is null");
		}

		this.editorWindow = editor;
	}

	/**
	 * Creates a new instance and saves a reference to the <code>EditorWindow</code>
	 * which is to be controlled. A <code>name</code> for the Action is stored
	 * as well. This will be printed in menus and buttons. 
	 * 
	 * @param editor <code>EditorWindow</code> to control
	 * @param name Caption to be shown
	 * @throws IllegalArgumentException If <code>editor</code> is <code>null</code>
	 */
	public AbstractEditorAction(EditorWindow editor, String name)
	throws IllegalArgumentException {
		this(editor);

		putValue(Action.NAME, name);
	}

}
