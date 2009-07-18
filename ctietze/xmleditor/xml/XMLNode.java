/**
 * 
 */
package ctietze.xmleditor.xml;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ctietze.xmleditor.actions.SaveAction;

/**
 * A generic XML node in the document. Provides all tools to create a fully
 * functional tree consisting of XML nodes and their children, tag names and
 * text values.
 * 
 * @author Christian Tietze
 */
public class XMLNode extends DefaultMutableTreeNode {
	
	/** Textual value of the node */
	protected String value = null;
	
	/** <!-- Comment --> assigned to this tag */
	protected String comment = null;
	
	protected static final Pattern REGEX_VALID_NAME = Pattern.compile(
			"^[a-zA-Z]+[:]?[a-zA-Z0-9_-]*[a-zA-Z0-9]*$");
	
	
	/**
	 * Root level node, i.e. has no parent from the start.
	 * 
	 * @throws IllegalArgumentException if <code>name</code> is 
	 *         <code>null</code> or an empty <code>String</code>.
	 */
	public XMLNode(String name) 
	throws IllegalArgumentException {
		super();
		
		setName(name);
		allowsChildren = true;
	}
	
	public XMLNode(String name, String value)
	throws IllegalArgumentException{
		this(name);
		
		setValue(value);
	}
	
	public XMLNode(String name, String value, String comment)
	throws IllegalArgumentException{
		this(name, value);
		
		setComment(comment);
	}
	
	/**
	 * Sets a new node name. The name is the XML tag in a saved document.
	 * 
	 * @param name New name for this node
	 * @see DefaultMutableTreeNode#setUserObject(Object)
	 */
	public void setName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name is null/empty");
		} else if (!isNameValid(name)) {
			throw new IllegalArgumentException("name is of an invalid form");
		}
		
		super.setUserObject(name);
	}
	
	/**
	 * The name of a node is the tag which will be stored in an XML file.
	 * 
	 * @return Name of this node
	 */
	public String getName() {
		return (String)getUserObject();
	}
	
	/**
     * Sets the user object for this node to <code>userObject</code>.
     * Prefer {@link #setName(String)}!
     *
     * @param userObject	the Object that constitutes this node's 
     *                          user-specified data
     * @see	#setName
     * @throws IllegalArgumentException If <code>userObject</code> is not a String
     * @deprecated
     */
	@Override
    public void setUserObject(Object userObject) {
    	if (!(userObject instanceof String)) {
    		throw new IllegalArgumentException("userObject is not a String");
    	}
    	
    	super.setUserObject(userObject);
    }
    
    
	/**
	 * Sets the text value of this node to the provided value.
	 * 
	 * @param value Text, which replaces any old value.
	 */
	public void setValue(String value) {
		this.value = value;
		
		if (value.length() == 0) {
			this.value = null;
		}
	}
	
	/**
	 * Returns text value of this node.
	 * 
	 * @return Text value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Exports the value, i.e. wraps it in CDATA if neccessary.
	 * 
	 * @return Value, ready to be saved in an XML document
	 */
	private String exportValue() {
		System.out.println(cdataNeeded() + "    " + value );
		if (cdataNeeded()) {
			return "<![CDATA[" + getValue() + "]]>";
		}
		return getValue();	
	}
	
	/**
	 * Decides whether the value should be wrapped in CDATA-tags to prevent 
	 * parsing problems.
	 * 
	 * @return 	<code>true</code> if special characters are used or the
	 * 			value is too large
	 */
	private boolean cdataNeeded() {
		return (getValue().length() > 500 
				|| Pattern.compile(".*?(<|>|\\n).*", Pattern.DOTALL).matcher(getValue()).matches());
	}
	
	/**
	 * Computes whether the node has a value or not.
	 * 
	 * @return <code>true</code>, if {@link value} is not <code>null</code>
	 */
	public boolean hasValue() {
		return value != null;
	}
	
	/**
	 * Changes the assigned comment's text
	 * 
	 * @param comment New comment text
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Returns assigned comment
	 * 
	 * @return Comment text, if any, or <code>null</code>
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns all child {@link XMLAttribute}s as an array.
	 * 
	 * @return Array of child XMLAttributes.
	 */
	public XMLAttribute[] getChildXMLAttributes() {
		XMLAttribute[] arr = null;
		
		if (hasAttributes()) {
			arr = new XMLAttribute[getAttributeCount()];
			int i = 0;
			
			for (Object child : children.toArray()) {
				if (child instanceof XMLAttribute) {
					arr[i] = (XMLAttribute) child;
					i++;
				}
			}
		}
		
		return arr;
	}
	
	/**
	 * Returns all child <code>XMLNodes</code> which are not 
	 * {@link XMLAttribute}s as an array.
	 * 
	 * @return Array of child bare XMLNodes.
	 */
	public XMLNode[] getChildXMLNodes() {
		XMLNode[] arr = null;
		
		if (hasChildNodes()) {
			arr = new XMLNode[getChildNodeCount()];
			int i = 0;
			
			for (Object child : children.toArray()) {
				if (child instanceof XMLNode 
						&& !(child instanceof XMLAttribute)) {
					arr[i] = (XMLNode) child;
					i++;
				}
			}
		}
		
		return arr;
	}
	/**
	 * Tests a new <code>name</code> against a regular expression.
	 * 
	 * @param name	New name to test
	 * @return <code>true</code> if the name is a valid tag name
	 * @see #REGEX_VALID_NAME
	 */
	protected boolean isNameValid(String name) {
        return REGEX_VALID_NAME.matcher(name).find();
    }
	
	/**
	 * Text values can only be added if the node has no text content and
	 * vice versa.
	 * 
	 * @return <code>true</code>, if no sub-nodes are present and they
	 * 		   cannot be added.
	 */
	public boolean canValueBeAssigned() {
		return !getAllowsChildren() || this.getChildNodeCount() == 0;
	}
	
	public boolean canChildNodesBeAdded() {
		return !hasValue();
	}
	
	/**
	 * Computes the amount of child XMLNodes which are not XMLAttributes.
	 *  
	 * @return Amount of child XMLNodes
	 */
	public int getChildNodeCount() {
		return getChildCount() - getAttributeCount();
	}
	
	/**
	 * Checks whether this node has child nodes.  Only nodes which are not
	 * {@link XMLAttribute}s will be checked.
	 * 
	 * @return 	<code>true</code>, if at least one child (no XMLAttribute!) 
	 * 			exists
	 */
	public boolean hasChildNodes() {
		return getChildNodeCount() > 0;
	}
	
	/**
	 * Count all children which are <code>XMLAttribute</code>s.
	 * 
	 * @return Amount of child XMLAttributes
	 */
	public int getAttributeCount() {
		int count = 0;
		if (children != null) {
			for (Object node : children.toArray()) {
				if (node instanceof XMLAttribute) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Checks whether child nodes exist and if there are any 
	 * {@link XMLAttribute}s included.
	 * 
	 * @return <code>true</code>, when at least one XMLAttribute is a child
	 */
	public boolean hasAttributes() {
		return getAttributeCount() > 0;
	}
	
	/**
	 * Adds an <code>XMLNode</code> (which may be a {@link XMLAttribute} as
	 * well!) to the set of child nodes.
	 * <p>
	 * Before adding <code>XMLNodes</code> blind, check whether
	 * non-<code>XMLAttributes</code> can be added.  This is not the case when
	 * text value is assigned to this node already.
	 *  
	 * @param newChild	New child node which should be added
	 * @throws 	<code>RuntimeException</code> if newChild is no XMLAttribute and 
	 * 			this node is not empty.
	 */
	public void add(XMLNode newChild) {
		
		if (newChild instanceof XMLAttribute
				|| canChildNodesBeAdded()) {
			// let newChild be added only if:
			//  - node is an XMLAttribute, or
			//  - node is an XMLNode and this node has no content yet
			super.add(newChild);
		} else {
			throw new RuntimeException("newChild is not an Attribute and can not " +
					"be added to a non-empty node " + this.toString());
		}
	}
	
	/**
	 * 
	 * @param depth
	 * @return Generates XML structure below this 
	 */
	public String generateXMLStructure(int depth) {
		StringBuffer buffer = new StringBuffer();
		
		// <name [attr=val [attrib=val ...]]>
		
		buffer.append(indent(depth));
		buffer.append("<" + getName());
		
		if (hasAttributes()) {
			for (XMLAttribute attrib : getChildXMLAttributes()) {
				buffer.append(" " + attrib.generateXMLStructure());
			}
		}
		
		if (!hasChildNodes()) {
			// without childs ...
			if (!hasValue()) {
				// ... and without value == single tag
				buffer.append(" />\n");
			} else {
				// ... but with text content; wrap in CDATA to be sure
				buffer.append(">");
				buffer.append(exportValue());
				buffer.append("</" + getName() + ">\n");
			}				
		} else {
			buffer.append(">\n");
			
			// add all child nodes recursively
			for (XMLNode node : getChildXMLNodes()) {
				buffer.append(node.generateXMLStructure(depth + 1));
			}
			
			// terminate XML tag
			buffer.append(indent(depth));
			buffer.append("</" + getName() + ">\n");
		}
		
		return buffer.toString();
	}
	
	/**
	 * Assumes this node is the lowest level node, i.e. no indentation
	 * shall be used for the structured output.
	 * 
	 * @return Generated XML structure below this node
	 * @see #generateXMLStructure(int)
	 */
	public String generateXMLStructure() {
		return generateXMLStructure(0);
	}
	
	/**
	 * Creates a String containing whitespace only to indent code output.
	 * 
	 * @param depth	Indentation-level
	 * @return String with lots of whitespaces
	 */
	private String indent(int depth) {
		StringBuffer buf = new StringBuffer();
		while ((--depth) >= 0) {
			buf.append("   ");
		}
		return buf.toString();
	}
	
	/*
	 * TODO for debugging: should adding MutableTreeNode raise a warning?
	public void add(MutableTreeNode newChild) {
		System.out.println("add(MutableTreeNode) used");
		super.add(newChild);
		
	}
	*/
	
	/*public String toString() {
		return "<!-- " + getComment() +" --> " + getName() + ": " + getValue();
	}*/
}
