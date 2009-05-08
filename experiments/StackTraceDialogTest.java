/**
 * Tests the <code>StackTraceErrorDialog</code> dialog.
 */

package experiments;

import xmleditor.gui.dialogs.StackTraceErrorDialog;

public class StackTraceDialogTest {
	public static void main(String[] args) {
		try {
			throw new NullPointerException("usuck");
		}
		catch(Exception ex) {
			StackTraceErrorDialog.makeNew(null, ex);
		}
	}
}
