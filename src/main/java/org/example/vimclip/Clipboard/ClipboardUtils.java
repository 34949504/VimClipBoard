package org.example.vimclip.Clipboard;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;

import java.awt.Graphics2D;

public class ClipboardUtils {

    public static Object getClipboardContentsWithRetry(int maxRetries, long delayMillis) {
        int tries = 0;

        while (tries < maxRetries) {
            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = clipboard.getContents(null);

                if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    return contents.getTransferData(DataFlavor.stringFlavor);
                } else {
                    return null; // Clipboard empty or unsupported data
                }
            } catch (IllegalStateException e) {
                // Clipboard is busy, wait and retry
                tries++;
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break; // If interrupted, stop retrying
                }
            } catch (Exception ex) {
                // Other exceptions - print and break to avoid infinite loop
                ex.printStackTrace();
                break;
            }
        }
        return null; // Return null if failed all retries
    }



    public static void setClipboardContentsWithRetry(String text, int maxRetries, long delayMillis) {
        int tries = 0;
        while (tries < maxRetries) {
            try {
                StringSelection selection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                return; // success
            } catch (IllegalStateException e) {
                tries++;
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break; // stop retrying if thread was interrupted
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                break; // other exceptions should not retry
            }
        }
        System.err.println("Failed to set clipboard contents after retries.");
    }


}
