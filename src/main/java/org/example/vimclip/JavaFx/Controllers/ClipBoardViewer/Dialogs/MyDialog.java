package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.BlocText;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.SharedInfo;
import org.example.vimclip.Observar;
import org.example.vimclip.Utils;

import java.util.ArrayList;

public class MyDialog extends Dialog implements Observar {

    ArrayList<Observar> observers_list = new ArrayList<>();

    DialogPane dialogPane = this.getDialogPane();
    BorderPane borderPane = new BorderPane();
    BlocText currentBlocktext;

    DialogSimilarFuncs dialogSimilarFuncs;



    private Button close_button = new Button();
    private TextArea textArea = new TextArea();

    SharedInfo sharedInfo;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;

    public DialogSimilarFuncs getDialogSimilarFuncs() {
        return dialogSimilarFuncs;
    }

    public void init(SharedInfo sharedInfo, ConfigMaster configMaster){


        this.sharedInfo = sharedInfo;
        this.clipboardViewer_config = configMaster.getClipboardViewer_config();
        this.dialogSimilarFuncs = new DialogSimilarFuncs(sharedInfo,this,clipboardViewer_config);
        dialogSimilarFuncs.initializeDims();
        dialogSimilarFuncs.settingDialogContent(borderPane);
        settingUp_dialogLayout();
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);


        Window window = getDialogPane().getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).setAlwaysOnTop(true);
        }
    }



    public void setting_textArea_text() {
        textArea.setText(currentBlocktext.getLabel().getText());
    }



    private void settingUp_dialogLayout() {
        settingUp_closeButton();
        borderPane.setTop(close_button);
        borderPane.setCenter(textArea);
    }

    private void settingUp_closeButton() {
        ImageView imageView =  Utils.getImageView("/assets/images/close.png");
        imageView.setFitWidth(40);
        imageView.setFitHeight(20);
        close_button.setGraphic(imageView);
        close_button.setStyle("-fx-background-color: transparent;");

        close_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Observar observer : observers_list) {
                    observer.text_in_visualize_mode_modified(textArea.getText());
                }
                dialogSimilarFuncs.hideDialog();
            }
        });
    }



    public void setCurrentBlocktext(BlocText currentBlocktext) {
        this.currentBlocktext = currentBlocktext;
    }


    public void addObserver(Observar observer) {
        observers_list.add(observer);
    }



    @Override
    public void blocText_wasRightCicked(BlocText blocText) {
        System.out.println("hell yeah it was clicked");
        setCurrentBlocktext(blocText);
        setting_textArea_text();
        dialogSimilarFuncs.showDialog();
    }
}
