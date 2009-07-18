package ctietze.experiments;

import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegExTesting{

	public static final String THE_REGEX = "^[a-zA-Z]+[a-zA-Z_-]*[a-zA-Z]*$";
	
	public static final Pattern THE_PATTERN = Pattern.compile(THE_REGEX);
	
    public static void main(String[] args){
        PrintStream console = System.out;
        
        String[] names  = { 
        		"html", "p", "long_tag", "other-tag",
        		
        		"1qwert", "qw3rt", "qwer7", 
        		"", " ", "\t", "\n", "\r", 
        		"!asd", "a$d", "as¶",
        		" def", "def ", "de fg"
        		};
        
        for (String name : names) {
        	if (!nameIsValid(name)) {
        		console.println("illegal: " + name);
        	} else {
        		console.println("OKAY! " + name);
        	}
        }
    }
    
    public static boolean nameIsValid(String name) {
        Matcher matcher = THE_PATTERN.matcher(name);

        return matcher.find();
    }
}