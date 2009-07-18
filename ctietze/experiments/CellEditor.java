package ctietze.experiments;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeCellEditor.DefaultTextField;

/**
 * Adapted from {@link http://www.java2s.com/Code/JavaAPI/javax.swing/JTreesetCellEditorTreeCellEditoreditor.htm}
 */
public class CellEditor {

	public static void main(final String args[]) {
		JFrame frame = new JFrame("Editable Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JTree tree = new JTree();
		tree.setEditable(true);

		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
		/*String elements[] = { "A", "B", "C", "D" };
    	JComboBox comboBox = new JComboBox(elements);
    	comboBox.setEditable(true);
    	TreeCellEditor comboEditor = new DefaultCellEditor(comboBox);
    	TreeCellEditor editor = new DefaultTreeCellEditor(tree, renderer, comboEditor);*/

		// TODO extend DefaultCellEditor: should expand/narrow TextField when typing
		// TODO extend DefaultCellEditor: should expand/narrow according to window size
		// TODO extend DefaultCellEditor: multiline support!

		DefaultTreeCellEditor editor = new DefaultTreeCellEditor(tree, renderer);

		JTextField textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
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
					size.height = tree.getRowHeight(); // !!!
					field.setSize(size);
				}
			}
		});
		
		DefaultCellEditor realEditor = new DefaultCellEditor(textField);
		
		/**
		 * This editor sucks because the textfield width is TOO DAMN WIDE
		 */
		class MongoEditor extends DefaultTreeCellEditor implements KeyListener {
			public MongoEditor(JTree tree, DefaultTreeCellRenderer renderer, TreeCellEditor realEditor) {
				super(tree, renderer, realEditor);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e != null && e.getSource() instanceof JTree) {
					setTree((JTree)e.getSource());

					// adjust width according to text entered? DefaultCellEditor.getcomponent needed
					//SwingUtilities.computeStringWidth(textField.getFontMetrics(textField.getFont()), "")
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						// invoke editing
						//((DefaultCellEditor) tree.getCellEditor()).actionPerformed(null);
						tree.startEditingAtPath(lastPath);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		}
		
		MongoEditor mongoEditor = new MongoEditor(tree, renderer, realEditor);
		/*{
			protected boolean canEditImmediately(EventObject event) {
				System.out.println(event.getSource());

				return (super.canEditImmediately(event) 
						|| (event instanceof KeyEvent 
								&& ((KeyEvent) event).getKeyCode() == KeyEvent.VK_ENTER));
			}
		};*/

		class TreeKeyAdapter extends KeyAdapter implements TreeSelectionListener {
			JTree tree = null;
			TreePath lastPath = null;
			
			public TreeKeyAdapter(JTree tree) {
				super();
				
				setTree(tree);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e != null) {
					setTree((JTree)e.getSource());

					// adjust width according to text entered? DefaultCellEditor.getcomponent needed
					//SwingUtilities.computeStringWidth(textField.getFontMetrics(textField.getFont()), "")
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						// invoke editing
						//((DefaultCellEditor) tree.getCellEditor()).actionPerformed(null);
						tree.startEditingAtPath(lastPath);
					}
				}
			}

			private void setTree(JTree tree) {
				this.tree = tree;
			}
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if(tree != null) {
					if(tree.getSelectionCount() == 1) {
						lastPath = tree.getSelectionPath();
					} else {
						lastPath = null;
					}
				}
			}
		}
		
		TreeKeyAdapter adapter = new TreeKeyAdapter(tree);
		
		// bind all things together
		
		tree.setCellEditor(editor);
		
		//tree.setCellEditor(realEditor);
		//tree.addKeyListener(adapter);
		//tree.getSelectionModel().addTreeSelectionListener(adapter);

		JScrollPane scrollPane = new JScrollPane(tree);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setSize(300, 150);
		frame.setVisible(true);
	}

}