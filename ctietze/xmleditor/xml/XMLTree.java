/**
 * 
 */
package ctietze.xmleditor.xml;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ctietze.xmleditor.Resources;

/**
 * Provides access to some convenience methods.
 * 
 * @author Christian Tietze
 * TODO cleanup unused methods
 */
public class XMLTree extends JTree {
	
	/** Root node content */
	private static final String TOP_NODE_NAME = Resources.getString("xmltree.root_name");
	
	public static XMLNode createEmptyRootNode() {
		return new XMLNode(TOP_NODE_NAME);
	}
	
	public XMLTree() {
		super(new DefaultTreeModel(null));
	}
	
	/**
	 * Expands all nodes in the tree.
	 */
	public void expandAll() {
		expandAllNodes(true);
	}
	
	/**
	 * Collapses all nodes in the tree.
	 */
	public void collapseAll() {
		expandAllNodes(false);
	}
	
	/**
	 * Expand or collapse all nodes from root to every leaf.
	 * 
	 * @param expand	<code>true</code> expands, <code>false</code> collapses
	 */
    private void expandAllNodes(boolean expand) {
        TreeNode root = (TreeNode) getModel().getRoot();
    
        // Traverse tree from root
        expandAllNodes(new TreePath(root), expand);
    }
    
    /**
	 * Expand or collapse all nodes from <code>parent</code> on.
	 * 
	 * @param parent	<code>TreePath</code> from which the tree will be
	 * 					traversed and expanded/collapsed
	 * @param expand	<code>true</code> expands, <code>false</code> collapses
	 */
    private void expandAllNodes(TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAllNodes(path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            expandPath(parent);
        } else {
            collapsePath(parent);
        }
    }
    
}
