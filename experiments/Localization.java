package experiments;

import java.util.Locale;

import xmleditor.Resources;

public class Localization {
	public static void main(String[] args) {		
		// English
		Locale.setDefault(Locale.ENGLISH);
		System.out.print(Locale.getDefault() + ":\t");
		System.out.println(Resources.getString("gui.root") + "\n\r");
		
		// German
		Locale.setDefault(Locale.GERMAN);
		System.out.print(Locale.getDefault() + ":\t");
		System.out.println(Resources.getString("gui.root") + "\n\r");
		
		// Foreign falls back to last working
		Locale.setDefault(Locale.CHINA);
		System.out.print(Locale.getDefault() + ":\t");
		System.out.println(Resources.getString("gui.root") + "\n\r");
	}
}
