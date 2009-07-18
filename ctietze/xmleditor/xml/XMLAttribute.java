/**
 * 
 */
package ctietze.xmleditor.xml;

/**
 * Attributes of XML tags are represented as a special kind of node in the tree
 * which may not have child nodes.
 * 
 * @author Christian Tietze
 * TODO! get/setComment shouldn't be available here -- abstract a 3rd class from node + attrib?
 */
public class XMLAttribute extends XMLNode {
	/**
	 * Construct a new instance based on XMLNode but changes
	 * <code>allowsChildren</code> to <code>false</code>.
	 * 
	 * @param name	Name of the attribute
	 */
	public XMLAttribute(String name) {
		super(name);
		
		this.allowsChildren = false;
	}
	
	/**
	 * Constructs a new instance with <code>allowsChildren=false</code>
	 * and a value assigned from the start.
	 * 
	 * @param name		Name of the attribute
	 * @param value		Text contents of the attribute
	 */
	public XMLAttribute(String name, String value) {
		this(name);
		
		setValue(value);
	}
	
	//
	// Derived from DefaultMutableTreeNode
	//
	
	/**
	 * Used to modify <code>allowsChildren</code> which is not permitted
	 * for any XML Attribute representation.
	 * 
	 * @param allowsChildren	Must be <code>false</code>
	 */
	@Override
	public void setAllowsChildren(boolean allowsChildren) {
		if (allowsChildren != false) {
			throw new IllegalArgumentException("changing allowsChildren not permitted");
		}
		
		super.setAllowsChildren(allowsChildren);
	}
	
	/**
	 * Returns the attribute name and its value as a String.
	 * 
	 * @return <code>name="value"</code>
	 */
	@Override
	public String generateXMLStructure() {
		return getName() + "=\"" + getValue() + "\"";
	}
	
	public String generateXMLStructure(int depth) {
		return generateXMLStructure();
	}
}
