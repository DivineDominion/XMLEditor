/*
 *  Tiled Map Editor, (c) 2004-2006
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Adam Turk <aturk@biggeruniverse.com>
 *  Bjorn Lindeijer <b.lindeijer@xs4all.nl>
 */

package ctietze.xmleditor;

import java.util.ResourceBundle;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class implements static accessors to common editor resources. These
 * currently include icons and internationalized strings.
 *
 * @version 2009-07-15 (Christian Tietze)
 */
public final class Resources {
    // The resource bundle used by this class
    private static final ResourceBundle resourceBundle =
            ResourceBundle.getBundle(
                    Resources.class.getPackage().getName() + ".resources.gui");

    // Prevent instanciation
    private Resources() {
    }
    
    /**
     * Retrieves an absolute file URL from inside the 'resources' folder.
     * 
     * @param fileName	File name, relative to <code>.../resources</code>
     * @return			Absolute path to the file
     */
    public static URL getFileURL(String fileName) {
    	// When no absolute path is provided, the package's name is used with
    	// the standard dot notation being converted to slashes
    	return Resources.class.getResource("resources/" + fileName);
    }

    /**
     * Retrieves a string from the resource bundle in the default locale.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     */
    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    /**
     * Loads an image from the resources directory. This directory is part of
     * the distribution jar.
     *
     * @param filename the filename relative from the resources directory
     * @return A BufferedImage instance of the image
     * @throws IOException if an error occurs during reading
     * @throws IllegalArgumentException when the resource could not be found
     */
    private static Image getImage(String filename) throws IOException,
            IllegalArgumentException {
        return ImageIO.read(Resources.class.getResourceAsStream(
                "resources/" + filename));
    }

    /**
     * Loads the image using {@link #getImage(String)} and uses it to create
     * a new {@link ImageIcon} instance.
     *
     * @param filename the filename of the image relative from the
     *                 <code>resources</code> directory
     * @return the loaded icon, or <code>null</code> when an error occured
     *         while loading the image
     */
    public static Icon getIcon(String filename) {
        try {
            return new ImageIcon(getImage(filename));
        } catch (IOException e) {
            System.out.println("Failed to load as image: " + filename);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to load resource: " + filename);
        }
        return null;
    }
}
