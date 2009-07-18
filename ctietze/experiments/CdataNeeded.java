package ctietze.experiments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ctietze.experiments.XMLParsingTest.TagData;

public class CdataNeeded {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pattern = ".*?(<|>|\\n).*";
		
		String value = "asd asd asd\n > a asda sd";
		Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(value);

		System.out.println(matcher.matches());
		System.out.println(value.matches(pattern));

	}

}
