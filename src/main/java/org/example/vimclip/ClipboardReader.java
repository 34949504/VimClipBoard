package org.example.vimclip;

import java.awt.datatransfer.*;
import java.awt.Toolkit;

public class ClipboardReader {
    public static String getClipboardContents() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);

            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
