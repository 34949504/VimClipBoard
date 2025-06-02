package org.example.vimclip.Clipboard;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;

import java.awt.Graphics2D;

public class ClipboardUtils {

    public static Object getClipboardContents() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);

//            ClipboardImageDetector.ImageInfo info = ClipboardImageDetector.detectClipboardImageType(contents);
//
//            if (info != null) {
//                java.awt.Image awtImage = (java.awt.Image) contents.getTransferData(DataFlavor.imageFlavor);
//
//                BufferedImage bufferedImage = toBufferedImage(awtImage);
//
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                ImageIO.write(bufferedImage, "png", os);
//
//                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
//
//                return new Image(is);
//            }
//            else {
//                System.out.println("info es snull");
//            }

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

    public static BufferedImage toBufferedImage(java.awt.Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
}
