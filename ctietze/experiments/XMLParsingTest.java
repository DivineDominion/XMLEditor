package ctietze.experiments;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.*;

import ctietze.xmleditor.Resources;
import ctietze.xmleditor.xml.*;


public class XMLParsingTest {
	public static void main(String[] args) {
		XMLParsingTest test = new XMLParsingTest();
	}
	
	/**
	 * Matches whitespace 
	 *  1) outside of tags which contain text
	 *  2) before or after tags which have no text in between (+ comments)
	 *  3) outside of <![CDATA[ ... ]]> multiline text
	 *  
	 * @see <a href="http://regexadvice.com/forums/thread/39708.aspx">clear whitespace in xml</a>
	 */
	private static String REGEX_WHITESPACE = "\\s*(<([^>]*)>[^<]*</\\2>|<[^>]*>|<!\\[CDATA\\[.*\\]\\]>)\\s*";
	
	private static final String REGEXP_ATTRIB = "\\b(\\w+?)\\b\\s*?=\\s*?\"([^\"]*?)\"";
	
	public XMLParsingTest() {
		URL url = XMLParsingTest.class.getResource("/ctietze/xmleditor/resources/example3.xml");
		
		String content = "";
		
		try {
			content = getFileString(url).toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		content = cleanDocument(content);
		//content = removeComments(content);
		
		//putContent(content);
		
		LinkedList<XMLNode> ll = parseXmlString(content);
		System.out.print(
				printNode(ll.getFirst(), 0)
				);
	}
	
	private String printNode(XMLNode node, int d) {
		StringBuffer buf = new StringBuffer();
		
		if (node.getComment() != null) 
			buf.append("/* " + indent(d) + node.getComment() + " */");
		
		buf.append(indent(d)
				+ node.getName()
				+ "=" + node.getValue() + "\n");
		
		for (int i = 0; i < node.getChildCount(); i++) {
			buf.append(printNode((XMLNode)node.getChildAt(i), d + 1));
		}
		
		return buf.toString();
	}
	
	private String indent(int d) {
		StringBuffer buf = new StringBuffer();
		while ((--d) >= 0) {
			buf.append("   ");
		}
		return buf.toString();
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
	private LinkedList<XMLNode> parseXmlString(String content) {
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
	
	private XMLNode createNodeFromCurrentMatch(Matcher matcher) {
		String[] groups = new String[matcher.groupCount() + 1];
		for (int i = 0; i <= matcher.groupCount(); i++) {
			groups[i] = matcher.group(i); 
		}
		
		return (new TagData(groups)).createNode();
	}
	
	class TagData {
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
		
		private String[] groups = null; 
		
		public TagData(String[] groups) {
			for (int i = 0; i < groups.length; i++) {
				// W3C says: Do not trim! 
				// But assign 'null' if string is empty
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
		
		public String getName() {
			if (hasClosingTag()) {
				return groups[OPEN_TAG];
			}
			
			return groups[SINGLE_TAG];
		}
		
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
		
		public XMLNode createNode() {
			XMLNode node = new XMLNode(getName());;
			
			// Text content is only possible if it's an ordinary tag
			// in the form <tag>...</tag>
			if (hasClosingTag()) {
				if (contentIsCdata()) {
					node.setValue("CDATA4TW"+getCdata());
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
				String attribs = getAttributes().trim();
				Matcher m = Pattern.compile(REGEXP_ATTRIB).matcher(attribs);
				int pos = 0;
				while (m.find()) {
					node.insert(new XMLAttribute(m.group(1), m.group(2)), 
							pos);
					pos++;
				}
			}
			
			return node;
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
	
	private String removeComments(String content) {
		Matcher comments = Pattern.compile("<!--.*?-->\\s*?", Pattern.DOTALL).matcher(content);
		return comments.replaceAll("");
	}
	
	private String cleanDocument(String content) {
		return removeWhitespace(removeHeader(content));
	}
	
	private String removeHeader(String content) {
		// Remove header
		Matcher header = Pattern.compile("<\\?.*?\\?>\\s*?").matcher(content);
		return header.replaceFirst("");
	}
	
	private String removeWhitespace(String content) {
		// (?<=(\\]\\]>|</[\\w\\s]+?>|<[\\w\\s]+?/>)) 
		Matcher whitespace = Pattern.compile(REGEX_WHITESPACE, Pattern.DOTALL).matcher(content);
		return whitespace.replaceAll("$1");
	}
	
	private StringBuffer getFileString(URL url) throws IOException {
		if (url == null) throw new IllegalArgumentException("url is null");
		
		StringBuffer string = new StringBuffer();
		InputStream stream = url.openStream();
		InputStreamReader streamReader = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(streamReader);
		
		String line = "";
		
		while ((line = reader.readLine()) != null) {
			string.append(line + "\n");
		}
		
		reader.close();
		streamReader.close();
		stream.close();
		
		return string;
	}
	
	private static void putContent(String content) {
		System.out.print(content + "\n* * * * * * * * *\n");
	}
}
