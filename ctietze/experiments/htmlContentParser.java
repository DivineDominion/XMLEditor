package ctietze.experiments;

import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class htmlContentParser {
	public static void main (String[] args) throws IOException {
 
		String sourceLine;
		String content = "";
 
		// The URL address of the page to open.
		URL address = new URL("http://www.heise.de/");
 
		// Open the address and create a BufferedReader with the source code.
		InputStreamReader pageInput = new InputStreamReader(address.openStream());
		BufferedReader source = new BufferedReader(pageInput);
 
		// Append each new HTML line into one string. Add a tab character.
		while ((sourceLine = source.readLine()) != null)
			content += sourceLine + "\t";
 
		// Remove style tags & inclusive content
		Pattern style = Pattern.compile("<style.*?>.*?</style>");
		Matcher mstyle = style.matcher(content);
		while (mstyle.find()) content = mstyle.replaceAll("");
 
		// Remove script tags & inclusive content
		Pattern script = Pattern.compile("<script.*?>.*?</script>");
		Matcher mscript = script.matcher(content);
		while (mscript.find()) content = mscript.replaceAll("");
 
		// Remove primary HTML tags
		Pattern tag = Pattern.compile("<.*?>");
		Matcher mtag = tag.matcher(content);
		while (mtag.find()) content = mtag.replaceAll("");
 
		// Remove comment tags & inclusive content
		Pattern comment = Pattern.compile("<!--.*?-->");
		Matcher mcomment = comment.matcher(content);
		while (mcomment.find()) content = mcomment.replaceAll("");
 
		// Remove special characters, such as &nbsp;
		Pattern sChar = Pattern.compile("&.*?;");
		Matcher msChar = sChar.matcher(content);
		while (msChar.find()) content = msChar.replaceAll("");
 
		// Remove the tab characters. Replace with new line characters.
		Pattern nLineChar = Pattern.compile("\t+");
		Matcher mnLine = nLineChar.matcher(content);
		while (mnLine.find()) content = mnLine.replaceAll("\n");
 
		// Print the clean content & close the Readers
		System.out.println(content);
		pageInput.close();
		source.close();
	}
}