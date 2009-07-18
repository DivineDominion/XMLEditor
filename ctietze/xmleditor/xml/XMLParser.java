/**
 * 
 */
package ctietze.xmleditor.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Christian Tietze
 *
 */
public class XMLParser {
	
	/**
	 * Matches whitespace 
	 *  1) outside of tags which contain text
	 *  2) before or after tags which have no text in between (+ comments)
	 *  3) outside of <![CDATA[ ... ]]> multiline text
	 *  
	 * @see <a href="http://regexadvice.com/forums/thread/39708.aspx">clear whitespace in xml</a>
	 */
	private static String REGEX_WHITESPACE = "\\s*(<([^>]*)>[^<]*</\\2>|<[^>]*>|<!\\[CDATA\\[.*\\]\\]>)\\s*";
	
	/**
	 * Parse an XML file from <code>url</code> and return the document root.
	 * 
	 * @param url	File URL to parse XML from.
	 * @return		Document's root node 
	 */
	public static XMLNode parseXmlFromFile(URL url) {		
		String content = "";
		
		try {
			content = getStringFromFile(url).toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return parseXmlFromString(content);
	}
	
	/**
	 * Parse an XML document contained in a single String and return the 
	 * XML document root.
	 * 
	 * @param content	Whole XML document String
	 * @return			Document's root node
	 */
	public static XMLNode parseXmlFromString(String content) {
		content = cleanDocument(content);
		//content = removeComments(content);
		
		return parseXmlString(content).getFirst();
	}
	
	
	/**
	 * Returns a large String from a file.  Reads the whole file line by line.
	 * 
	 * @param url	File to open and read.
	 * @return		Whole content of the file
	 * @throws IOException
	 */
	private static StringBuffer getStringFromFile(URL url) throws IOException {
		if (url == null) {
			throw new IllegalArgumentException("url is null");
		}
		
		StringBuffer fileString = new StringBuffer();
		InputStream stream = url.openStream();
		InputStreamReader streamReader = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(streamReader);
		
		String line = "";
		
		while ((line = reader.readLine()) != null) {
			fileString.append(line + "\n");
		}
		
		reader.close();
		streamReader.close();
		stream.close();
		
		return fileString;
	}
	
	private static String removeComments(String content) {
		Matcher comments = Pattern.compile("<!--.*?-->\\s*?", Pattern.DOTALL).matcher(content);
		return comments.replaceAll("");
	}
	
	private static String cleanDocument(String content) {
		return removeWhitespace(removeHeader(content));
	}
	
	private static String removeHeader(String content) {
		// Remove header
		Matcher header = Pattern.compile("<\\?.*?\\?>\\s*?").matcher(content);
		return header.replaceFirst("");
	}
	
	private static String removeWhitespace(String content) {
		// FIXME Remove whitespace outside of text content & CDATA only!
		// (?<=(\\]\\]>|</[\\w\\s]+?>|<[\\w\\s]+?/>)) 
		Matcher whitespace = Pattern.compile(REGEX_WHITESPACE, Pattern.DOTALL).matcher(content);
		return whitespace.replaceAll("$1");
	}
	
	
	/**
	 * Parses the <code>content</code> String and transforms it into an 
	 * XMLNode. This new node's contents are parsed as well recursively
	 * so the whole XML tree gets processed.
	 * 
	 * @param content	String which will be parsed
	 * @return			A LinkesList with all nodes on this level containing 
	 * 					their subsequent nodes
	 */
	private static LinkedList<XMLNode> parseXmlString(String content) {
		Matcher matcher = Pattern.compile(TagData.REGEXP, Pattern.DOTALL).matcher(content);
		LinkedList<XMLNode> nodes_in_this_level = null;

		while (matcher.find()) {
			// Create LinkedList if neccessary
			if (nodes_in_this_level == null) {
				nodes_in_this_level = new LinkedList<XMLNode>();
			}

			nodes_in_this_level.add(createNodeFromCurrentMatch(matcher)); 
		}
		
		return nodes_in_this_level;
	}
	
	/**
	 * 
	 * @param matcher
	 * @return
	 */
	private static XMLNode createNodeFromCurrentMatch(Matcher matcher) {
		String[] groups = new String[matcher.groupCount() + 1];
		for (int i = 0; i <= matcher.groupCount(); i++) {
			groups[i] = matcher.group(i); 
		}
		
		return (new TagData(groups)).createNode();
	}
	
	static class TagData {
		/** The killer-RegExp to parse a single tag-expression! */
		public static final String REGEXP =
			// "?:" prevents backreference which isn't needed for delimiters
			"(?:<!--\\s*(.*?)\\s*?-->?)?"
			+ "(" // usual tag style
			+ "<([A-Za-z]\\w*?)\\b([^>]*?)/>"
			+ "|" 
				+ "<([A-Za-z]\\w*?)\\b([^>]*?)>"
				+ "(?:\\Q<![CDATA[\\E(.*?)\\]\\]>|(.*?))"
				+ "</\\5\\s*?>"
			//+ "|" // shorthand <tag/> form
				//+ "<([A-Za-z]\\w*?)\\b([^>]*?)/>"
			+ ")";
		//+ "(?:<!\\[CDATA\\[)?(.*?)(?:\\]\\]>)?" // too lazy for invalid or strange XML
		
		/** A RegExp which splits any attribute from an attribute String */
		private static final String REGEX_ATTRIB = "\\b(\\w+?)\\b\\s*?=\\s*?\"([^\"]*?)\"";
		
		//
		// Constant integers which give groups[]'s indizes a readable name.
		// All other indizes are meaningless or serve only the purpose to
		// group other group's expressions ... yeah.
		//
		
		private static final int COMMENT = 1;
		private static final int OPEN_TAG = 5;
		private static final int SINGLE_TAG = 3;
		private static final int ATTRIB_REGULAR = 6;
		private static final int ATTRIB_SINGLE = 4;		
		private static final int CONTENT = 8;
		private static final int CDATA = 7;
		
		/*
		private static final int COMMENT = 1;
		private static final int OPEN_TAG = 3;
		private static final int SINGLE_TAG = 7;
		private static final int ATTRIB_REGULAR = 4;
		private static final int ATTRIB_SINGLE = 8;		
		private static final int CONTENT = 6;
		private static final int CDATA = 5;
		*/
		
		private static final int ATTRIBUTE_NAME = 1;
		private static final int ATTRIBUTE_VALUE = 2;
		
		/** Array which holds all RegExp matcher's values ("matching groups") */
		private String[] groups = null; 
		
		/**
		 * Initializes all neccessary variables and sets empty (="") items
		 * in <code>groups</code> to <code>null</code> for any other
		 * processing relies on non-empty items.
		 *  
		 * @param groups	Array of matcher values.  The indizes correspond
		 * 					to the matching groups in {@link REGEXP}
		 */
		public TagData(String[] groups) {
			for (int i = 0; i < groups.length; i++) {
				// W3C says: Do not trim! 
				// So assign 'null' if string is empty
				groups[i] = (groups[i] == null)
						? null
						: ((groups[i].trim().length() == 0) ? null : groups[i]);
			}
			
			this.groups = groups;
		}
		
		public boolean hasClosingTag() {
			return groups[SINGLE_TAG] == null;
		}
		
		public boolean hasAttributes() {
			return getAttributes() != null;
		}
		
		public boolean hasComments() {
			return getComments() != null;
		}
		
		public boolean contentIsCdata() {
			return getTextContent() == null && getCdata() != null;
		}
		
		public boolean isTextContentEmpty() { 
			return getTextContent() == null;
		}
		
		/**
		 * Distinguishes between single-tag and closing-tag nodes and returns
		 * the appropriate RegExp Matching Group which contains the name.
		 * 
		 * @return The name of this tag
		 */
		public String getName() {
			if (hasClosingTag()) {
				return groups[OPEN_TAG];
			}
			
			return groups[SINGLE_TAG];
		}
		
		/**
		 * Distinguishes between single-tag and closing-tag nodes and returns
		 * the appropriate RegExp Matching Group which contains all attributes.
		 * 
		 * @return The whole attribute String, unparsed
		 */
		public String getAttributes() {
			if (hasClosingTag()) {
				return groups[ATTRIB_REGULAR];
			}
			
			return groups[ATTRIB_SINGLE];
		}
		
		public String getCdata() {
			return groups[CDATA];
		}
		
		public String getTextContent() {
			return groups[CONTENT];
		}
		
		public String getComments() {
			return groups[COMMENT];
		}
		
		public String toString() {
			StringBuffer buf = new StringBuffer();
			for (String elem : groups) {
				buf.append(elem + "\n");
			}
			return buf.toString();
		}
		
		/**
		 * Creates the final {@link XMLNode} which contains all attributes,
		 * all child-nodes or CDATA/text content and all comments.
		 * @return
		 */
		public XMLNode createNode() {
			XMLNode node = new XMLNode(getName());
			
			// Text content is only possible if it's an ordinary tag
			// in the form <tag>...</tag>
			if (hasClosingTag()) {
				if (contentIsCdata()) {
					node.setValue(getCdata());
				} else if (!isTextContentEmpty()) {
					// Usual text content or nested tags -- parse recursively
					LinkedList<XMLNode> child_nodes = 
						parseXmlString(getTextContent());
					
					if (child_nodes != null) {
						// Child nodes exist, add all to the current node
						for (XMLNode child : child_nodes) {
							node.add(child);
						}
					} else {
						// No child nodes returned, so it's text only
						node.setValue(getTextContent());
					}
				}
			}
			
			// Assign comments if any
			if (hasComments()) {
				node.setComment(getComments());
			}
			
			// Process attributes if any
			if (hasAttributes()) {
				parseAttributesAndAssignTo(node);
			}
			
			return node;
		}
		
		/**
		 * Makes use of {@link REGEX_ATTRIB} to parse the whole attribute String
		 * into key-value-pairs which are added to <code>node</code>
		 * 
		 * @param node	<code>XMLNode</code> which receives parsed attributes
		 */
		private void parseAttributesAndAssignTo(XMLNode node) {
			String attributeString = getAttributes().trim();
			
			Matcher m = Pattern.compile(REGEX_ATTRIB).matcher(attributeString);
			
			int pos = 0;
			while (m.find()) {
				node.insert(
						new XMLAttribute(m.group(ATTRIBUTE_NAME), m.group(ATTRIBUTE_VALUE)), 
						pos);
				
				pos++;
			}
		}
	}
	
	/*
	private String replaceCData(String content, ArrayList<String> data) {
		Pattern dataPattern = Pattern.compile("((<!\\[CDATA\\[\\s*)(.*?)(\\s*\\]\\]>))", Pattern.DOTALL);
		Matcher dataMatcher = dataPattern.matcher(content);
		int num = 1;
		while (dataMatcher.find()) {
			// 1: all
			// 2: <![CDATA[
			// 3: text
			// 4: ]]>
			String value = dataMatcher.group(3);
			data.add(value);
			num++;
		}
		
		dataMatcher.reset();
		dataMatcher.replaceAll("!!!CDATA!!!");
		
		return content;
	}
	*/
}