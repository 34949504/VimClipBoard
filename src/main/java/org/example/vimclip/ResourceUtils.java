package org.example.vimclip;

import java.io.InputStream;
import java.net.URL;

public class ResourceUtils {

    /**
     * Returns InputStream for any resource in your classpath.
     * Usage: for JSON, audio, text files, etc.
     */
    public static InputStream getResourceAsStream(String resourcePath) {
        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1); // remove leading slash for ClassLoader
        }
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return stream;
    }

    /**
     * Returns URL for any resource in your classpath.
     * Usage: for images, FXML, etc.
     */
    public static URL getResource(String resourcePath) {
        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return url;
    }
}
