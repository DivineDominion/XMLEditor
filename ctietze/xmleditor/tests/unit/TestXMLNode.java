package ctietze.xmleditor.tests.unit;

import org.junit.Test;

import ctietze.xmleditor.xml.XMLNode;
import junit.framework.TestCase;

public class TestXMLNode extends TestCase {
	private static final String[] VALID_NAMES = {
		"html", "h1", "p", "dc:creator"
	};
	
	private static final String[] INVALID_NAMES  = { 
		"1qwert", 
		"", " ", "\t", "\n", "\r", 
		"!asd", "a$d", "as¶",
		" def", "def ", "de fg",
		"dc:creator:stuff"
		};
	
	protected void setUp() {
	}
	
	protected void tearDown() {
	}
	
	@Test
	public void testCreationWithNullNameShouldFail() {
		try {
			XMLNode node = new XMLNode(null);
			fail("name=null should throw exception");
		} catch (IllegalArgumentException ex) {
			// Should happen
		}
	}
	
	@Test
	public void testCreationWithValidStringShouldPass() {
		XMLNode node = new XMLNode(VALID_NAMES[0]);
		assertNotNull(node);
		assertEquals(VALID_NAMES[0], node.getName());
	}
	
	@Test
	public void testCreationWithInvalidStringsShouldAllFail() {
		XMLNode node;

		for (String invalidName : INVALID_NAMES) {
			try {
				node = new XMLNode(invalidName);
				fail("name=" + invalidName + " should throw exception");
			} catch (IllegalArgumentException ex) {
				// Should happen
			}
		}		
	}
	
	@Test
	public void testNameChangeToAnyValidStringShouldPass() {
		XMLNode node;
		
		for (String name : VALID_NAMES) {
			node = new XMLNode(name);
			assertNotNull(node);
			assertEquals(name, node.getName());
		}
	}
	
	@Test
	public void testNameChangeToInvalidStringShouldAllFail() {
		XMLNode node;
		
		for (String invalidName : INVALID_NAMES) {
			try {
				node = new XMLNode(invalidName);
				assertNull(node);
				fail("name=" + invalidName + " should throw exception");
			} catch (IllegalArgumentException ex) {
				// Should happen
			}
		}
	}
	
}
