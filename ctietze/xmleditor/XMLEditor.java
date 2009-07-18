/**
 * 
 */

package ctietze.xmleditor;

import java.util.Locale;

import ctietze.xmleditor.gui.editor.EditorWindow;


/**
 * Provides a program starting point. <code>XMLEditor</code> initializes
 * the GUI.
 * 
 * @author Christian Tietze
 */
public class XMLEditor {
	
	/**
	 * To be run on program execution.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		// Load English locale by default 
		Locale.setDefault(Locale.ENGLISH);
		
		// Currency-safe GUI invocation (from Java Swing Tutorial)
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EditorWindow.createAndShow();
			}
		});
	}
}
