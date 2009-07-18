/**
 * 
 */
package ctietze.xmleditor.xml;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import ctietze.xmleditor.Resources;

/**
 * Provides access to some convenience methods.
 * 
 * @author Christian Tietze
 */
public class XMLTree extends JTree {
	
	/** Root node content */
	private static final String TOP_NODE_NAME = Resources.getString("xmltree.root_name");
	
	/**
	 * Creates a new <code>TreeModel</code> with merely a root node with
	 * this program's default name.
	 * 
	 * @return		"Empty" TreeModel with only a root node
	 */
	public static DefaultTreeModel createEmptyTreeModel() {
		// TODO! XMLRootNode with unique icon
		return new DefaultTreeModel(new DefaultMutableTreeNode(TOP_NODE_NAME));
	}
	
	/**
	 * Creates a new <code>TreeModel</code> with <code>node</code> as the
	 * root.
	 * 
	 * @param node 	Root node
	 * @return		TreeModel with root node which contains all child data
	 */
	public static DefaultTreeModel createTreeModel(XMLNode node) {
		return new DefaultTreeModel(node);
	}
	
	/**
	 * Creates a new <code>TreeModel</code> with the default root node
	 * containing <code>nodes</code> objects at first level.
	 * 
	 * @param nodes		First level of the XML tree
	 * @return			TreeModel with root node containing children
	 */
	public static DefaultTreeModel createTreeModelWith(XMLNode[] nodes) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(TOP_NODE_NAME);
		
		for (XMLNode node : nodes) {
			root.add(node);
		}
		
		return new DefaultTreeModel(root);
	}
	
	public XMLTree() {
		super(new DefaultTreeModel(null));
	}
	
	public XMLTree(XMLNode rootNode) {
		super(rootNode);
	}
}
