package org.example.vimclip;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class KeyboardSimulator {
    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();

        // Simulate Ctrl+C
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        System.out.println("Sent Ctrl+C");
    }
}
