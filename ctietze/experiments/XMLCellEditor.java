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

import ctietze.xmleditor.xml.*;

/**
 * Adapted from {@link http://www.java2s.com/Code/JavaAPI/javax.swing/JTreesetCellEditorTreeCellEditoreditor.htm}
 */
public class XMLCellEditor {

	public static void main(final String args[]) {
		JFrame frame = new JFrame("Editable Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTree tree = new JTree();

		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
		DefaultTreeCellEditor editor = new DefaultTreeCellEditor(tree, renderer);

	

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
		tree.addKeyListener(adapter);
		tree.getSelectionModel().addTreeSelectionListener(adapter);

		JScrollPane scrollPane = new JScrollPane(tree);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setSize(300, 150);
		frame.setVisible(true);
	}
	
	public static XMLNode[] dummy(){
		XMLNode xml1 = new XMLNode("html");
		XMLNode xml2a = new XMLNode("head");
		XMLNode xml2b = new XMLNode("body");
		XMLNode xml3a = new XMLNode("h1");
		xml3a.setValue("News");
		XMLNode xml3b = new XMLNode("p");
		xml3b.setValue("lorem ipsum dolor sit amet ...");
		XMLAttribute attrib3a = new XMLAttribute("class");
		attrib3a.setValue("page_title");
		
		xml3a.add(attrib3a);
		
		xml2b.add(xml3a);
		xml2b.add(xml3b);
		
		xml1.add(xml2a);
		xml1.add(xml2b);
		
		XMLNode[] nodes = { xml1 };
		return nodes;
	}

}