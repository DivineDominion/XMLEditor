package experiments;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

/**
 * Adapted from {@link http://www.java2s.com/Code/JavaAPI/javax.swing/JTreesetCellEditorTreeCellEditoreditor.htm}
 */
public class CellEditor {

  public static void main(final String args[]) {
    JFrame frame = new JFrame("Editable Tree");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JTree tree = new JTree();
    tree.setEditable(true);

    DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
    /*String elements[] = { "A", "B", "C", "D" };
    JComboBox comboBox = new JComboBox(elements);
    comboBox.setEditable(true);
    TreeCellEditor comboEditor = new DefaultCellEditor(comboBox);
    TreeCellEditor editor = new DefaultTreeCellEditor(tree, renderer, comboEditor);*/
    //JTextArea textArea = new JTextArea();
    
    TreeCellEditor editor = new DefaultTreeCellEditor(tree, renderer);
    tree.setCellEditor(editor);
    
    class TreeKeyAdapter extends KeyAdapter {
  	  JTree tree = null;
  	  
  	  public TreeKeyAdapter(JTree tree) {
  		  super();
  		  this.tree = tree;
  	  }

  	  public void keyPressed(KeyEvent e) {
  		  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
  			  // invoke editing
  			  ((DefaultTreeCellEditor) tree.getCellEditor()).actionPerformed(null);
  		  }
  	  }
    }
    
    tree.addKeyListener(new TreeKeyAdapter(tree));

    JScrollPane scrollPane = new JScrollPane(tree);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.setSize(300, 150);
    frame.setVisible(true);
  }
  
  
}