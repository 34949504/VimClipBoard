package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.SharedInfo;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.StageFocuser;
import org.example.vimclip.Observar;
import  javafx.stage.*;


public class HelpDialog extends Dialog implements Observar {

    SharedInfo sharedInfo;
    ConfigMaster configMaster;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;
    DialogPane dialogPane = this.getDialogPane();

    DialogSimilarFuncs dialogSimilarFuncs;

    public HelpDialog(SharedInfo sharedInfo,ConfigMaster configMaster)
    {

        this.sharedInfo = sharedInfo;
        this.configMaster = configMaster;
        this.clipboardViewer_config = configMaster.getClipboardViewer_config();
        this.dialogSimilarFuncs = new DialogSimilarFuncs(sharedInfo,this);

        initializeDims();
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
}
