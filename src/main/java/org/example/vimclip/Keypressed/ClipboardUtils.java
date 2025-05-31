package org.example.vimclip.Keypressed;

import org.example.vimclip.ClipboardImageDetector;
import org.example.vimclip.Observar;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipboardUtils {


    public static Object getClipboardContents() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);

             ClipboardImageDetector.ImageInfo info =  ClipboardImageDetector.detectClipboardImageType(contents);
//            try {
//                if (info != null) {
//                    System.out.println("Detected image type: " + info.format);
//                    System.out.println("Source: " + info.source);
//                } else {
//                    System.out.println("No image detected in clipboard.");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            if (info != null) {
                return (Image) contents.getTransferData(DataFlavor.imageFlavor);
            }

            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setClipboardContents(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }
}
