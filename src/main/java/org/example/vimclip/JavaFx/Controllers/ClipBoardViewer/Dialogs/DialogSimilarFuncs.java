package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Screen;
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.SharedInfo;
import org.example.vimclip.Observar;


public class DialogSimilarFuncs{

    SharedInfo sharedInfo;
    Dialog dialog;
    DialogPane dialogPane;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;
    Observont observont = new Observont();

    public DialogSimilarFuncs(SharedInfo sharedInfo, Dialog dialog, ConfigMaster.ClipboardViewer_config clipboardViewer_config) {

        this.sharedInfo = sharedInfo;
        this.dialog = dialog;
        this.dialogPane = dialog.getDialogPane();
        this.clipboardViewer_config = clipboardViewer_config;
    }

    public Observont getObservont()
    {
        return  observont;
    }


    public Coords calculating_where_dialog_should_appear(int currentHeight, int currentWidth) {
        int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

        Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(
                sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());

        int available_space_below = (int) (screenHeight - bounds.getMaxY());
        int available_space_above = (int) (bounds.getMinY());
        int available_space_left = (int) (bounds.getMinX());
        int available_space_right = (int) (screenWidth - bounds.getMaxX());

        int x = 0;
        int y = 0;
        String side = null;

        if (available_space_below > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) bounds.getMaxY();
            side = "below";
        } else if (available_space_above > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) (bounds.getMinY() - currentHeight);
            side = "above";
        } else if (available_space_left > currentWidth) {
            x = (int) (bounds.getMinX() - dialogPane.getWidth());
            y = (int) bounds.getMaxY() - currentHeight;
            side = "left";

        } else if (available_space_right > currentWidth) {
            x = (int) (bounds.getMaxX());
            y = (int) bounds.getMaxY() - currentHeight;
            side = "right";
        }

        Coords coords = new Coords(x, y);
        coords.setSide(side);

        return coords;

    }


    @Getter
    @Setter
    public static class Coords {
        public int x;
        public int y;
        public String side = null;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public  void initializeDims() {
        double w = clipboardViewer_config.getStage_width();
        double h = clipboardViewer_config.getStage_height();

        dialogPane.setPrefWidth(w);
        dialogPane.setPrefHeight(h);
    }

    public void settingDialogContent(Node node) {
        dialogPane.setContent(node);
    }

    public void showDialog()
    {

        if (!dialog.isShowing()) {
//            settingUpPosition();
            dialog.show();
            changingWholePos();
            return;
        }

        Window window = dialogPane.getScene().getWindow();
        if (window != null) window.hide();
    }




    private void changingWholePos()
    {

        if (dialog.isShowing()) {
            dialogPane.applyCss();
            dialogPane.layout();
            DialogSimilarFuncs.Coords coords = calculating_where_dialog_should_appear((int)dialogPane.getHeight(),(int)dialogPane.getWidth());
            Window window = dialogPane.getScene().getWindow();
            if (window != null) {
                window.setX(coords.getX());
                window.setY(coords.getY());
            }
        }
    }

    private class Observont implements  Observar
    {
        @Override
        public void stage_minimizing() {
            if (dialog.isShowing()) {
                Window window = dialogPane.getScene().getWindow();
                if (window != null) window.hide();
            }
        }
        @Override
        public void stage_has_been_resized() {
            if (dialog.isShowing()) {
                // Defer repositioning until after resize/layout pass
                Platform.runLater(() -> {
                    dialogPane.applyCss();
                    dialogPane.layout();
                    DialogSimilarFuncs.Coords coords = calculating_where_dialog_should_appear((int)dialogPane.getHeight(),(int)dialogPane.getWidth());

                    Window window =dialogPane.getScene().getWindow();
                    if (window != null) {
                        window.setX(coords.x);
                        window.setY(coords.y);
                    }
                });
            }
        }
        @Override
        public void stage_was_moved() {
            changingWholePos();
        }
    }

}