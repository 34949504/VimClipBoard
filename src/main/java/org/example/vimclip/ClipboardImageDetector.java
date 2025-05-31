package org.example.vimclip;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;

public class ClipboardImageDetector {


    public static ImageInfo detectClipboardImageType(Transferable contents) throws Exception {

        if (contents == null) return null;



        // 2. Check for HTML with base64 or <img src=...>
        DataFlavor htmlFlavor = DataFlavor.selectionHtmlFlavor;

        if (contents.isDataFlavorSupported(htmlFlavor)) {
            String html = (String) contents.getTransferData(htmlFlavor);

//            System.out.println("-------------------------------------------------------------------------------------");
//            for (DataFlavor flavor : contents.getTransferDataFlavors()) {
//                System.out.println("Available flavor: " + flavor.getMimeType());
//            }

            // Look for base64 embedded image
            Pattern base64Pattern = Pattern.compile("data:image/([a-zA-Z0-9]+);base64,");
            Matcher base64Matcher = base64Pattern.matcher(html);
            if (base64Matcher.find()) {
                return new ImageInfo(base64Matcher.group(1), "base64-embedded HTML");
            }

            // Look for linked image URL
            Pattern urlPattern = Pattern.compile("<img[^>]*src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
            Matcher urlMatcher = urlPattern.matcher(html);
            if (urlMatcher.find()) {
                String src = urlMatcher.group(1);
                String ext = src.replaceAll("^.*\\.", "").split("\\?")[0]; // crude extension guess
                return new ImageInfo(ext.toLowerCase(), "HTML <img> src: " + src);
            }
        }

        // 3. Check for file list (like image copied from file explorer)
        if (contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            List<?> fileList = (List<?>) contents.getTransferData(DataFlavor.javaFileListFlavor);
            if (!fileList.isEmpty()) {
                File file = (File) fileList.get(0);
                String name = file.getName().toLowerCase();
                if (name.matches(".*\\.(png|jpg|jpeg|gif|bmp|webp|svg)")) {
                    return new ImageInfo(name.replaceAll("^.*\\.", ""), "File: " + file.getAbsolutePath());
                }
            }
        }

        return null;
    }

    // Simple class to hold results
    public static class ImageInfo {
        public final String format;
        public final String source;

        public ImageInfo(String format, String source) {
            this.format = format;
            this.source = source;
        }
    }

}
