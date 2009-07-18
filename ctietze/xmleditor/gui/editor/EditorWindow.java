/*
 * 
 */
package ctietze.xmleditor.gui.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.actions.CloseAction;
import ctietze.xmleditor.actions.NewDummyTreeAction;
import ctietze.xmleditor.actions.NewTreeAction;
import ctietze.xmleditor.actions.QuitAction;
import ctietze.xmleditor.actions.SaveAction;
import ctietze.xmleditor.actions.SaveAsAction;
import ctietze.xmleditor.controller.NodeValueToRichEditContentSynchronizer;
import ctietze.xmleditor.xml.XMLAttribute;
import ctietze.xmleditor.xml.XMLNode;
import ctietze.xmleditor.xml.XMLTree;
import ctietze.xmleditor.xml.XmlDocument;

/**
 * The main frame to display and edit XML files.
 * 
 * @author Christian Tietze
 */
public class EditorWindow extends JFrame {

	/** Default frame title */
	private static final String WINDOW_TITLE = "XML Editor";
	
	/** Default frame size */
	private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(600, 400);
	
	/** Text to display when no node is selected */
	private static final String DEFAULT_RICH_EDIT_TEXT = 
		Resources.getString("richedit.default");
	
	/** The "File" Menu */
	private JMenu fileMenu = null;
	
	/** The "Edit" Menu */
	private JMenu editMenu = null;
	
	/** The default <code>NewTreeAction</code> (empties the tree) */
	private NewTreeAction newAction = null;
	
	/** 
	 * The default <code>NewDummyTreeAction</code> (empties the tree and 
	 * generates a stub.
	 */
	private NewDummyTreeAction newDummyAction = null;
	
	/** The default <code>SaveAction</code> */
	private SaveAction saveAction = null;
	
	/** The default <code>SaveAsAction</code>*/
	private SaveAsAction saveAsAction = null;
	
	/** The default <code>CloseAction</code> */
	private CloseAction closeAction = null;
	
	/** The default <code>QuitAction</code> */
	private QuitAction quitAction = null;
	
	/** Tree-representation of currently opened XML code */
	private XMLTree xmlTree = null;
	
	/** RichEdit which shows node values */
	private JEditorPane richEdit = null;
	
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
		setMinimumSize(EditorWindow.DEFAULT_FRAME_SIZE);
		
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
		    	// TODO calling QuitAction manually is dirty; look for solution
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
		
		JScrollPane richEditView = new JScrollPane(
				getRichEdit(),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		Dimension minSize = new Dimension(200, 100);
		xmlTreeView.setSize(minSize);
		xmlTreeView.setMinimumSize(minSize);
				
		JSplitPane splitView = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				xmlTreeView, richEditView);
		
		splitView.setOneTouchExpandable(false);
		
		setLayout(new GridLayout(0, 1));
		add(splitView);
		
		pack();
		
		// only possible after "packing" because else scroll bar has no height
		// +4 seems to be necessary for borders. I don't know for sure.
		richEditView.setMinimumSize(new Dimension(richEditView.getMinimumSize().width,
				richEditView.getHorizontalScrollBar().getHeight() 
				+ getRichEdit().getPreferredSize().height + 4));
	}
	
	/**
	 * Generates the file menu if necessary.
	 * 
	 * @return {@link fileMenu} instance
	 */
	public JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu("File");
			
			fileMenu.add(getNewAction());
			fileMenu.add(getNewDummyAction());
			fileMenu.add("Open ..."); 		// TODO implement "File - Open"
			fileMenu.addSeparator();
			fileMenu.add(getSaveAction());
			fileMenu.add(getSaveAsAction());
			fileMenu.addSeparator();
			fileMenu.add(getCloseAction()); 			// TODO implement "File - Close"
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
	 * Generates a new <code>NewTreeAction</code> if necessary.
	 * 
	 * @return {@link #newAction} instance
	 */
	public Action getNewAction() {
		if (newAction == null) {
			newAction = new NewTreeAction(this);
		}
		return newAction;
	}
	
	/**
	 * Generates a new <code>NewDummyTreeAction</code> if necessary.
	 * 
	 * @return {@link #newDummyAction} instance
	 */
	public Action getNewDummyAction() {
		if (newDummyAction == null) {
			newDummyAction = new NewDummyTreeAction(this);
		}
		return newDummyAction;
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
	 * 
	 * @return {@link #saveAsAction} instance.
	 */
	public Action getSaveAsAction() {
		if (saveAsAction == null) {
			saveAsAction = new SaveAsAction(this);
		}
		return saveAsAction;
	}
	
	/**
	 * Generates the "Close" <code>Action</code> if necessary.
	 * 
	 * @return {@link #closeAction} instance.
	 */
	public Action getCloseAction() {
		if (closeAction == null) {
			closeAction = new CloseAction(this);
		}
		return closeAction;
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
	
	/**
	 * Generates the RichEdit (EditorStyle) pane to modify values of nodes.
	 * 
	 * @return {@link #richEdit} instance
	 */
	public JEditorPane getRichEdit() {
		if (richEdit == null) {
			richEdit = new JEditorPane();
			richEdit.setText(DEFAULT_RICH_EDIT_TEXT);
			richEdit.setEnabled(false);
			richEdit.addKeyListener(new TreeEditingKeyAdapter());
		}
		return richEdit;
	}

	/**
	 * Generates the XML Tree View if necessary.
	 * 
	 * @return {@link #xmlTree} instance
	 */
	public XMLTree getXmlTree() {
		if (xmlTree == null) {
			xmlTree = new XMLTree(); 
			
			XMLTreeCellRenderer renderer = new XMLTreeCellRenderer();
			
			// allow only single row selection
			xmlTree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			xmlTree.setCellRenderer(renderer);
			
			xmlTree.setEnabled(false); // resizing of panes doesn't work then :(
			xmlTree.setEditable(true);
			
			xmlTree.addTreeSelectionListener(new NodeValueToRichEditContentSynchronizer(getRichEdit()));
			
			TreeEditingKeyAdapter adapter = new TreeEditingKeyAdapter();
			
			// TODO refactor CellEditor(textField) 
			JTextField textField = new JTextField();
			/*textField.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					keyTyped(e);
				}
				
				public void keyPressed(KeyEvent e) {
					keyTyped(e);
				}
				public void keyTyped(KeyEvent e) {
					if (e != null && e.getSource() instanceof JTextField) {
						JTextField field = (JTextField)e.getSource();
						Dimension size = field.getPreferredSize();
						size.height = xmlTree.getRowHeight(); // !!!
						field.setSize(size);
					}
				}
			});*/
			
			// TODO cell editor needs to check for tag-name validity
			xmlTree.setCellEditor(new DefaultCellEditor(textField));
			//xmlTree.setCellEditor(new DefaultTreeCellEditor(xmlTree, renderer));
			
			xmlTree.addKeyListener(adapter);
			xmlTree.addTreeSelectionListener(adapter);
		}
		return xmlTree;
	}
	
	/**
	 * Adapter which responds to XML Tree shortcuts to edit nodes and their
	 * values. 
	 * 
	 * @author Christian Tietze
	 */
	class TreeEditingKeyAdapter extends KeyAdapter implements TreeSelectionListener {
		TreePath lastPath = null;
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				//XMLTree tree = (XMLTree) e.getSource();
				
				int modifierMask = KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;

				if ((e.getModifiersEx() & modifierMask) == KeyEvent.SHIFT_DOWN_MASK ) {
					// SHIFT+ENTER without CTRL/ALT pressed
					// should invoke switching between tree and editor
					if (e.getSource().equals(getXmlTree()) 
							&& getRichEdit().isEnabled()) {
						getRichEdit().requestFocusInWindow();
					} else if (e.getSource().equals(getRichEdit()) 
							&& getXmlTree().isEnabled()) {
						getXmlTree().requestFocusInWindow();
					}
				} else if ((e.getModifiersEx() & modifierMask) == KeyEvent.CTRL_DOWN_MASK) {
					// TODO! ctrl+enter should add new node beneath
				} else if ((e.getModifiersEx() & modifierMask) == KeyEvent.ALT_DOWN_MASK) {
					// TODO! alt+enter should add new attribute beneath
				} else if ((e.getModifiersEx() & modifierMask) == 0) {
					// ENTER without any modifier pressed
					// should invoke node editing
					if (e.getSource() instanceof XMLTree) {
						System.out.println(lastPath);
						getXmlTree().startEditingAtPath(lastPath);
					}
				}
			}
		}
		
		/**
		 * Invoked when the selected node has changed, 
		 */
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			XMLTree tree = getXmlTree();
			
			if(tree.getSelectionCount() == 1) {
				lastPath = tree.getSelectionPath();
			} else {
				lastPath = null;
			}
		}
	}
	
	/**
	 * Customized {@link DefaultTreeCellRenderer} to display tree node
	 * icons appropriate to their kind: document root, xml node, attribute.
	 * 
	 * @author Christian Tietze
	 */
	class XMLTreeCellRenderer extends DefaultTreeCellRenderer {
		private final Icon TAG_ICON = Resources.getIcon("tag.png");
		private final Icon ATTRIB_ICON = Resources.getIcon("attrib.png");
		
		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean sel,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(
					tree, value, sel,
					expanded, leaf, row,
					hasFocus);
			
			if (value instanceof XMLAttribute) {
				setIcon(ATTRIB_ICON);
			} else if (value instanceof XMLNode) {
				setIcon(TAG_ICON);
			}

			return this;
		}
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
