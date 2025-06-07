package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.SharedInfo;
import org.example.vimclip.Observar;
import org.example.vimclip.Utils;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;

public class ConfigurationDialog extends Dialog implements Observar {

    SharedInfo sharedInfo;
    ConfigMaster configMaster;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;
    DialogPane dialogPane = this.getDialogPane();

    DialogSimilarFuncs dialogSimilarFuncs;

    BorderPane borderPane = new BorderPane();
    ScrollPane scrollPane = new ScrollPane(borderPane);



    public ConfigurationDialog(SharedInfo sharedInfo, ConfigMaster configMaster)
    {

        Label label = new Label("checing chang");
        borderPane.setCenter(label);

        this.sharedInfo = sharedInfo;
        this.configMaster = configMaster;
        this.clipboardViewer_config = configMaster.getClipboardViewer_config();
        this.dialogSimilarFuncs = new DialogSimilarFuncs(sharedInfo,this,clipboardViewer_config);

        dialogSimilarFuncs.initializeDims();
        dialogSimilarFuncs.settingDialogContent(scrollPane);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);

        Window window = getDialogPane().getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).setAlwaysOnTop(true);
        }

    }

//    private void initializeDims()
//    {
//        double w = clipboardViewer_config.getStage_width();
//        double h = clipboardViewer_config.getStage_height();
//
//        dialogPane.setPrefWidth(w);
//        dialogPane.setPrefHeight(h);
//    }
//    private void settingDialogContent()
//    {
//        dialogPane.setContent(scrollPane);
//    }


//    @Override
//    public void showHelpDialog() {
//        if (!this.isShowing()) {
////            settingUpPosition();
//            this.show();
//            changingWholePos();
//            return;
//        }
//
//        Window window = getDialogPane().getScene().getWindow();
//        if (window != null) window.hide();
//    }


//     @Override
//    public void stage_was_moved() {
//        changingWholePos();
//    }

//    private void changingWholePos()
//    {
//
//        if (this.isShowing()) {
//            dialogPane.applyCss();
//            dialogPane.layout();
//            DialogSimilarFuncs.Coords coords = dialogSimilarFuncs.calculating_where_dialog_should_appear((int)dialogPane.getHeight(),(int)dialogPane.getWidth());
//            Window window = getDialogPane().getScene().getWindow();
//            if (window != null) {
//                window.setX(coords.getX());
//                window.setY(coords.getY());
//            }
//        }
//    }

//    @Override
//    public void stage_minimizing() {
//        if (this.isShowing()) {
//            Window window = getDialogPane().getScene().getWindow();
//            if (window != null) window.hide();
//        }
//    }

//    @Override
//    public void stage_has_been_resized() {
//        if (this.isShowing()) {
//            // Defer repositioning until after resize/layout pass
//            Platform.runLater(() -> {
//                dialogPane.applyCss();
//                dialogPane.layout();
//                DialogSimilarFuncs.Coords coords = dialogSimilarFuncs.calculating_where_dialog_should_appear((int)dialogPane.getHeight(),(int)dialogPane.getWidth());
//
//                Window window = getDialogPane().getScene().getWindow();
//                if (window != null) {
//                    window.setX(coords.x);
//                    window.setY(coords.y);
//                }
//            });
//        }
//    }

    public DialogSimilarFuncs getDialogSimilarFuncs()
    {
        return  dialogSimilarFuncs;
    }

    @Override
    public void showConfigDialog() {
        System.out.println("Show config_dialog");
        dialogSimilarFuncs.showDialog();
    }
}
