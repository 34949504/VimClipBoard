package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Window;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.SharedInfo;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.StageFocuser;
import org.example.vimclip.Observar;
import  javafx.stage.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.vimclip.Utils;

import java.awt.Desktop;
import java.net.URI;
import javafx.scene.*;
import javafx.scene.control.*;

public class HelpDialog extends Dialog implements Observar {

    SharedInfo sharedInfo;
    ConfigMaster configMaster;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;
    DialogPane dialogPane = this.getDialogPane();

    Hyperlink link = new Hyperlink("Open Google");
    DialogSimilarFuncs dialogSimilarFuncs;

    TextFlow textFlow = new TextFlow();
    ScrollPane scrollPane = new ScrollPane(textFlow);
    Text text1 = new Text();
    Text text2 = new Text();


    public HelpDialog(SharedInfo sharedInfo,ConfigMaster configMaster)
    {

        this.sharedInfo = sharedInfo;
        this.configMaster = configMaster;
        this.clipboardViewer_config = configMaster.getClipboardViewer_config();
        this.dialogSimilarFuncs = new DialogSimilarFuncs(sharedInfo,this);

        initializeDims();
        settingDialogContent();
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);

        Window window = getDialogPane().getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).setAlwaysOnTop(true);
        }
    }

    private void initializeDims()
    {
        double w = clipboardViewer_config.getStage_width();
        double h = clipboardViewer_config.getStage_height();

        dialogPane.setPrefWidth(w);
        dialogPane.setPrefHeight(h);
    }
    private void settingDialogContent()
    {
        text1.setFill(Color.RED);
        text1.setFont(Font.font("Helvetica", FontPosture.ITALIC, 40));
        text2.setFill(Color.BLUE);
        text2.setFont(Font.font("Helvetica", FontWeight.BOLD, 10));

        text1.setText("What up g\n");
        text2.setText("What up g\n");

        ImageView imageView = Utils.getImageView("/assets/images/copy.png");
        textFlow.getChildren().addAll(text1,text2,imageView);



        dialogPane.setContent(scrollPane);
    }


    @Override
    public void showHelpDialog() {
        if (!this.isShowing()) {
//            settingUpPosition();
            this.show();
            changingWholePos();
            return;
        }

        Window window = getDialogPane().getScene().getWindow();
        if (window != null) window.hide();
    }


    private void settingUpPosition()
    {

        DialogSimilarFuncs.Coords coords = dialogSimilarFuncs.calculating_where_dialog_should_appear((int)dialogPane.getHeight(),(int)dialogPane.getWidth());



        double x = coords.getX();
        double y = coords.getY();

        if (coords.side != null)
        {
            System.out.printf("X is %f\nY is %f\nAnd side is %s\n",x,y,coords.side);
        }

        this.setX(x);
        this.setY(y);

    }

     @Override
    public void stage_was_moved() {
        changingWholePos();
    }

    private void changingWholePos()
    {

        if (this.isShowing()) {
            dialogPane.applyCss();
            dialogPane.layout();
            DialogSimilarFuncs.Coords coords = dialogSimilarFuncs.calculating_where_dialog_should_appear((int)dialogPane.getHeight(),(int)dialogPane.getWidth());
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) {
                window.setX(coords.getX());
                window.setY(coords.getY());
            }
        }
    }

    @Override
    public void stage_minimizing() {
        if (this.isShowing()) {
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) window.hide();
        }
    }

    @Override
    public void stage_has_been_resized() {
        Observar.super.stage_has_been_resized();
    }

    private void hyperLinkAction()
    {
        link.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=1FXjK717Aes"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private class HelpManual extends TextFlow
    {




        public HelpManual()
        {

        }
        private void initilize_textFlow()
        {

        }
    }

}
