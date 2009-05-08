/**
 * 
 */
package xmleditor.gui.editor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.tree.*;

import xmleditor.actions.QuitAction;
import xmleditor.actions.SaveAction;
import xmleditor.actions.SaveAsAction;
import xmleditor.xml.XmlDocument;

/**
 * The main window to display XML files.
 * 
 * @author Christian Tietze
 */
public class EditorWindow extends JFrame {

	/** Default frame title */
	private static final String WINDOW_TITLE = "XML Editor";
	
	/** Default frame size */
	private static final Dimension DEFAULT_SIZE = new Dimension(500, 300);
		
	/** The "File" Menu */
	private JMenu fileMenu = null;
	
	/** The "Edit" Menu */
	private JMenu editMenu = null;
	
	/** The default <code>QuitAction</code> */
	private QuitAction quitAction = null;
	
	/** The default <code>SaveAction</code> */
	private SaveAction saveAction = null;
	
	/** The default <code>SaveAsAction</code>*/
	private SaveAsAction saveAsAction = null;
	
	/** Tree-representation of currently opened XML code */
	private JTree xmlTree = null;
	
	/**
	 * Factory method which creates a plain window and shows it by default.
	 * 
	 * @return New {@link EditorWindow} instance
	 */
	public static EditorWindow createAndShow() {
		EditorWindow window = new EditorWindow();
		
		window.setVisible(true);
		
		return window;
	}
	
	/**
	 * Sets up the frame and initializes its contents.
	 */
	public EditorWindow() {
		super();
		
		setTitle(EditorWindow.WINDOW_TITLE);
		setSize(EditorWindow.DEFAULT_SIZE); 
		
		setupMenu();
		setupContent();
		setupFrameClosing();
		
		setLocationRelativeTo(null); // center on screen
	}
	
	/**
	 * Removes Java's default close operation. This classe's {@link quitAction}
	 * will be used instead.
	 */
	private void setupFrameClosing() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Create inner Class (WindowAdapter) just for closing
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	// Execute quit action manually
		    	getQuitAction().actionPerformed(null);
		    }
		});
	}
	
	/**
	 * Sets up the whole menu bar.
	 */
	private void setupMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.add(getFileMenu());
		menuBar.add(getEditMenu());
		
		setJMenuBar(menuBar);
	}
	
	/**
	 * Initializes the frame contents.
	 */
	private void setupContent() {		
		JScrollPane xmlTreeView = new JScrollPane(
				getXmlTree(),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		setLayout(new GridLayout(0, 1));
		add(xmlTreeView);
	}
	
	/**
	 * Generates the file menu if necessary.
	 * 
	 * @return {@link fileMenu} instance
	 */
	public JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu("File");
			
			fileMenu.add("New"); 			// TODO implement "File - New"
			fileMenu.add("Open ..."); 		// TODO implement "File - Open"
			fileMenu.addSeparator();
			fileMenu.add(getSaveAction());
			fileMenu.add(getSaveAsAction());
			fileMenu.addSeparator();
			fileMenu.add("Close"); 			// TODO implement "File - Close"
			fileMenu.add(getQuitAction());			
		}
		return fileMenu;
	}
	
	public JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu("Edit");
			
			editMenu.add("Expand All");		// TODO implement "Edit - Expand All"
			editMenu.add("Collapse All");	// TODO implement "Edit - Collapse All"
			
		}
		return editMenu;
	}
	
	/**
	 * Generates a new <code>SaveAction</code> if necessary.
	 * 
	 * @return {@link #saveAction} instance
	 */
	public Action getSaveAction() {
		if (saveAction == null) {
			saveAction = new SaveAction(this);
		}
		return saveAction;
	}
	
	/**
	 * Generates a new <code>SaveAsAction</code> if necessary.
	 * @return {@link #saveAsAction} instance
	 */
	public Action getSaveAsAction() {
		if (saveAsAction == null) {
			saveAsAction = new SaveAsAction(this);
		}
		return saveAsAction;
	}
	
	/**
	 * Generates the "Quit" <code>Action</code> if necessary.
	 * 
	 * @return {@link #quitAction} instance.
	 */
	public Action getQuitAction() {
		if (quitAction == null) {
			quitAction = new QuitAction(this);
		}
		return quitAction;
	}

	public JTree getXmlTree() {
		if (xmlTree == null) {
			DefaultMutableTreeNode top = new DefaultMutableTreeNode("XML Document");
			
			top.add(createDummyNodes());
			
			xmlTree = new JTree(top);
			
			// allow only single row selection
			xmlTree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			xmlTree.setCellEditor(
					new DefaultTreeCellEditor(xmlTree, (DefaultTreeCellRenderer) xmlTree.getCellRenderer()));
		}
		return xmlTree;
	}
	
	private MutableTreeNode createDummyNodes() {
		DefaultMutableTreeNode xml1 = new DefaultMutableTreeNode("html");
		DefaultMutableTreeNode xml2 = new DefaultMutableTreeNode("body");
		DefaultMutableTreeNode xml3a = new DefaultMutableTreeNode("h1");
		DefaultMutableTreeNode xml3b = new DefaultMutableTreeNode("p");
		DefaultMutableTreeNode attrib3a = new DefaultMutableTreeNode("class");
		DefaultMutableTreeNode attrib3aval = new DefaultMutableTreeNode("page_title");
		DefaultMutableTreeNode xml3aval = new DefaultMutableTreeNode("News");
		DefaultMutableTreeNode xml3bval = new DefaultMutableTreeNode("lorem ipsum dolor sit amet ...");
		
		attrib3a.add(attrib3aval);
		xml3a.add(attrib3a);
		xml3a.add(xml3aval);
		xml3b.add(xml3bval);
		
		xml2.add(xml3a);
		xml2.add(xml3b);
		
		xml1.add(xml2);
		
		return xml1;
	}

	public XmlDocument getOpenXml() {
		// TODO return opened document
		return null;
	}
	
	/**
	 * Returns whether a file is loaded into the editor.
	 * 
	 * @return <code>true</code> if a XML document is opened in the editor
	 */
	public boolean hasOpenedDocument() {
		return getOpenXml() != null;
	}
}
