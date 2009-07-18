package ctietze.xmleditor.gui.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * Ein kleiner Nachrichtendialog, der Exceptions der Liste im Detail anzeigt.
 * <p>
 * Dieser Dialog zeigt den Titel der Exception und den kompletten Stack&nbsp;Trace, 
 * &auml;hnlich der Konsole.
 * <p>
 * Der <code>StackTraceErrorDialog</code> wurde entwickelt, um Fehler bei der 
 * Listenverarbeitung zu melden, die bei der Benutzung durchaus h&auml;ufiger 
 * auftreten k&ouml;nnen.
 * <br/>
 * Nat&uuml;rlich ist es technisch durchaus m&ouml;glich, diesen Dialog auch anderswo 
 * zu verwenden.
 * <p>
 * Zum Verarbeiten der Knopfdruck-Events wurde der <code>{@link ActionListener}</code>
 * hier direkt hinein implementiert.
 * 
 * @author Christian Tietze
 * @version 1.2 2007-02-24
 */
public class StackTraceErrorDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -3136911312604675634L;

	private JPanel inhaltPanel = null;

	/**
	 * Label, dass nur aus dem Error-Icon von Swing besteht.
	 */
	private JLabel errorIcon = null;

	/**
	 * Seperater Fehlertext (Label nie mehrzeilig), der mehrzeilig
	 * den Fehlertext anzeigt
	 */
	private JTextArea errorTextArea = null;

	/**
	 * Scrollbereich um die Stack Trace-Fehlerausgabe.
	 * 
	 * @see #stackTraceTextArea
	 */
	private JScrollPane stackTraceScrollPane = null;

	/**
	 * Textfeld, das den Fehler zum eigenen Zur&uuml;ckvervolgen anzeigt,
	 * &auml;hnlich der Konsolenausgabe.
	 * 
	 * @see #stackTraceScrollPane
	 */
	private JTextArea stackTraceTextArea = null;

	/**
	 * Knopf zum Schlie&szlig;en des Fensters
	 */
	private JButton okButton = null;

	/**
	 * Aufgetretener Fehler, dessen Details angezeigt werden sollen. 
	 */
	private Exception exception = null;

	/**
	 * Erstellt einen neuen <code>StackTraceErrorDialog</code> und
	 * initialisiert alle Komponenten.
	 *  
	 * @param owner &Üuml;bergeordnetes, besitzendes Fenster
	 * @param exception Fehler, der angezeigt werden soll
	 */
	public StackTraceErrorDialog(Frame owner, Exception exception) {
		super(owner);

		this.exception = exception;
		
		// Ausgabe in Konsole auf jeden Fall _auch_ vornehmen
		exception.printStackTrace();

		initialize();
	}

	/**
	 * Factory-Funktion, die eine neue Instanz von <code>StackTraceErrorDialog</code>
	 * erstellt und anzeigt. Das besitzende Fenster wird dabei blockiert.
	 * 
	 * @param owner &Üuml;bergeordnetes, besitzendes Fenster
	 * @param exception Fehler, der angezeigt werden soll
	 */
	public static void makeNew(JFrame owner, Exception exception) {
		StackTraceErrorDialog dialog = new StackTraceErrorDialog(owner, exception);
		dialog.setVisible(true);
	}

	/**
	 * Initialisiert den Dialog.
	 * <p>
	 * Der Titel und die Ma&szlig;e werden beispielsweise gesetzt. Au&szlig;erdem
	 * wird die Methode zum Zusammenstellen der einzelnen Komponenten
	 * aufgerufen. 
	 */
	private void initialize() {
		setTitle("Ein Fehler ist aufgetreten");

		setSize(400, 550);
		zentrieren();

		setModal(true);
		setResizable(false);

		setContentPane(getInhaltPanel());
	}

	/**
	 * Stellt den gesamten Inhalt zusammen.
	 * <p>
	 * Das Layout wird erstellt und mit Details und den einzelnen Inhalten
	 * gef&uuml;llt.
	 * 
	 * @return Gesamter Inhalt des Fensters
	 * @see #getErrorIcon()
	 * @see #getErrorTextArea()
	 * @see #getStackTraceTextArea()
	 * @see #getOkButton()
	 */
	private JPanel getInhaltPanel() {
		if (inhaltPanel == null) {
			GridBagConstraints gridBagErrorIcon = new GridBagConstraints();
			gridBagErrorIcon.gridx = 0;
			gridBagErrorIcon.gridy = 0;
			gridBagErrorIcon.anchor = GridBagConstraints.WEST;

			GridBagConstraints gridBagErrorText = new GridBagConstraints();
			gridBagErrorText.gridx = 1;
			gridBagErrorText.gridy = 0;
			gridBagErrorText.anchor = GridBagConstraints.NORTHWEST;
			gridBagErrorText.fill = GridBagConstraints.BOTH;

			GridBagConstraints gridBagTrace = new GridBagConstraints();
			gridBagTrace.gridx = 0;
			gridBagTrace.gridy = 1;
			gridBagTrace.gridwidth = 2;
			gridBagTrace.fill = GridBagConstraints.BOTH;
			gridBagTrace.weightx = 1.0;
			gridBagTrace.weighty = 1.0;

			GridBagConstraints gridBagOK = new GridBagConstraints();
			gridBagOK.gridx = 0;
			gridBagOK.gridy = 2;
			gridBagOK.gridwidth = 2;
			gridBagOK.ipadx = 28; // Innenabstand vom Knopftext zum Knopfrand
			gridBagOK.anchor = GridBagConstraints.CENTER;

			// Alle drei Zeilen 12px vom Rand und voneinander entfernen -> luftig
			gridBagErrorIcon.insets = new Insets(12, 12, 12, 12);
			gridBagErrorText.insets = new Insets(12, 12, 12, 12);
			gridBagTrace.insets = new Insets(0, 12, 0, 12);
			gridBagOK.insets = new Insets(12, 12, 12, 12);

			// Layout einf�gen
			inhaltPanel = new JPanel();
			inhaltPanel.setLayout(new GridBagLayout());

			inhaltPanel.add(getErrorIcon(), gridBagErrorIcon);
			inhaltPanel.add(getErrorTextArea(), gridBagErrorText);
			inhaltPanel.add(getStackTraceScrollPane(), gridBagTrace);
			inhaltPanel.add(getOkButton(), gridBagOK);
		}

		return inhaltPanel;
	}

	/**
	 * Initialisiert ggf. das Label bzw. Warn-Icon und liefert es zur&uuml;ck.
	 * 
	 * @return Sch&ouml;n buntes Icon zum dramatisieren der Szene
	 * @see com.sun.java.swing.plaf.metal.icons.ocean
	 */
	private JLabel getErrorIcon() {
		if (errorIcon == null) {
			errorIcon = new JLabel();
			errorIcon.setIcon(new ImageIcon(MetalLookAndFeel.class
					.getResource("icons/ocean/warning.png")));
		}

		return errorIcon;
	}

	/**
	 * Initialisiert ggf. die TextArea und liefert sie zur&uuml;k.
	 * <p>
	 * Sie beinhaltet dabei den Fehlertext, der das Problem beschreibt,
	 * das auftrat.
	 * 
	 * @return Fehlertext, der den Fehler beschreibt
	 */
	private JTextArea getErrorTextArea() {
		if (errorTextArea == null) {
			errorTextArea = new JTextArea();
			errorTextArea.setText(exception.getMessage());
			errorTextArea.setBackground(this.getInhaltPanel()
					.getBackground());
			errorTextArea.setEditable(false);
			errorTextArea.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
			errorTextArea.setWrapStyleWord(true);
			errorTextArea.setLineWrap(true);
		}

		return this.errorTextArea;
	}

	/**
	 * Initialisiert ggf. die ScrollPane um die Stack Trace-Textbox 
	 * und liefert deren Instanz zur&uuml;ck.
	 * 
	 * @return JScrollPane
	 * @see #getStackTraceTextArea()
	 */
	private JScrollPane getStackTraceScrollPane() {
		if (this.stackTraceScrollPane == null) {
			this.stackTraceScrollPane = new JScrollPane();
			this.stackTraceScrollPane.setViewportView(this
					.getStackTraceTextArea());
		}

		return this.stackTraceScrollPane;
	}

	/**
	 * Initialisiert ggf. die Textbox mit dem Stack Trace der &uuml;bergebenen
	 * Exception und liefert sie zur&uuml;ck. 	
	 * 	
	 * @return TextArea mit dem gesamten Stack Trace als Inhalt	
	 */
	private JTextArea getStackTraceTextArea() {
		if (this.stackTraceTextArea == null) {
			Font font = new Font("Courier New", Font.PLAIN, 14);

			stackTraceTextArea = new JTextArea();

			stackTraceTextArea.setFont(font);
			stackTraceTextArea.setForeground(Color.RED);
			stackTraceTextArea.setEditable(false);

			stackTraceTextArea.setText(getExceptionStackTrace());

			// Setzt die Scrollbar zur�ck
			resetStackTraceScrollBar();
		}

		return stackTraceTextArea;
	}

	/**
	 * Initialisiert den OK-Knopf, wenn n&ouml;tig, und liefert seine Instanz
	 * zur&uuml;ck.	
	 * 	
	 * @return OK-Knopf	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(this);
		}

		return okButton;
	}

	/**
	 * Zentriert den Dialog auf dem Bildschirm.
	 */
	private void zentrieren() {
		/*
	 Rectangle r = this.getBounds();

	 int w = (int) r.getWidth();
	 int h = (int) r.getHeight();

	 int screenW = (int) this.getToolkit().getScreenSize().getWidth();
	 int screenH = (int) this.getToolkit().getScreenSize().getHeight();

	 int x = (screenW - w) / 2;
	 int y = (screenH - h) / 2;

	 this.setBounds(new Rectangle(x, y, w, h));
		 */

		// Automatisches zentrieren. Ist bisschen k�rzer als das oben :)
		setLocationRelativeTo(null);
	}

	/**
	 * Formatiert die einzelnen Zeilen des Stack Trace zu einem gro&szlig;en Block,
	 * der dann in der TextArea gezeigt werden kann.
	 * 
	 * @return Formatierter, ggf. mehrzeiliger Stack Trace
	 */
	private String getExceptionStackTrace() {
		final String newline = System.getProperty("line.separator");
		String traceText = 
			"(is a " + exception.getClass().getName() + ")" + newline + newline 
			+ "Stack Trace:" + newline;
		

		for (StackTraceElement elem : exception.getStackTrace()) {
			traceText += "  " + elem.getClassName() + "."
			+ elem.getMethodName() + "(" + elem.getFileName() + ":"
			+ elem.getLineNumber() + ")" + newline;
		}

		return traceText;
	}

	/**
	 * L&auml;sst die vertikale Scrollbar wieder nach oben rutschen, falls sie durch 
	 * gro&szlig;en Inhalt aktiviert wurde, damit der Blick anfangs auch auf den Ursprung
	 * des Fehlers liegt.
	 */
	private void resetStackTraceScrollBar() {
		JScrollBar vertical = stackTraceScrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMinimum());
	}

	/**
	 * Verarbeitet den einzigen m&ouml;glichen Knopfdruck des Fensters
	 * und schlie&szlig;t, wenn er den auftrat, das Fensterchen.
	 * 
	 * @since 1.2
	 * @param event Aufgetretenes Event
	 */
	public final void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(getOkButton())) {
			setVisible(false);
		}
	}
}
