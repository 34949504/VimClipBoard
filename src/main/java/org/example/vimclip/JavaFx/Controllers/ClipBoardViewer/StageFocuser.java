package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.application.Platform;
import javafx.geometry.Bounds;

import java.awt.*;
import java.awt.event.InputEvent;

public class StageFocuser {

    int previous_x;
    int previous_y;
    SharedInfo sharedInfo;
    Robot robot;

    public StageFocuser(SharedInfo sharedInfo)
    {

        this.sharedInfo = sharedInfo;
        initializeRobot();
    }
    private void initializeRobot()
    {

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void giveFocus()
    {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            System.out.println("Moving mouse and shit");

            Point pointerInfo =  MouseInfo.getPointerInfo().getLocation();
            previous_x = pointerInfo.x;
            previous_y= pointerInfo.y;

            Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(
                    sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());

            int my = (int) bounds.getMaxY() - 10;
            int mx = (int)bounds.getMinX() + 10;

            robot.mouseMove(mx,my);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.mouseMove(previous_x,previous_y);

            }
        });
        }

    public void giveFocusBack()
    {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        });
    }
}

