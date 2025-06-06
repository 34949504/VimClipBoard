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
import org.example.vimclip.Observar;
import  javafx.stage.*;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import org.example.vimclip.Utils;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;

public class HelpDialog extends Dialog implements Observar {

    SharedInfo sharedInfo;
    ConfigMaster configMaster;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;
    DialogPane dialogPane = this.getDialogPane();

    Hyperlink link = new Hyperlink("Open Google");
    DialogSimilarFuncs dialogSimilarFuncs;

    HelpManual helpManual;
    ScrollPane scrollPane;



    public HelpDialog(SharedInfo sharedInfo,ConfigMaster configMaster)
    {

        this.sharedInfo = sharedInfo;
        this.configMaster = configMaster;
        this.clipboardViewer_config = configMaster.getClipboardViewer_config();
        this.dialogSimilarFuncs = new DialogSimilarFuncs(sharedInfo,this);

        initializeDims();
        helpManual = new HelpManual();
        scrollPane = new ScrollPane(helpManual);
       //WARNING look at this initializing shit
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
        private String copy_and_remove_string = "Works like ctrl x, it copies the selected block/blocks and remove's it/remove's them";
        private String trashButton_string = "Deletes the selected block/blocks";
        private String copyButton_string = "Copies to your clipboard the selected block/blocks";
        private String selectAll_string = "Toggles between selecting all blocks and deselecting all blocks";
        private String startRecordingButton_string = "It lets the program know to listen for changes in the clipbpoard so that it is able to register them as blocks";
        private String shortcut_button_string = "Toggles if shortcut mode is on or off";
        private String separator_string = "When you get more thatn one text of block, you define what separates the text";
        private String switchEdge_string = "Switches the app between the 4 edges of the screen";
        private String expand_string = "Toggles the app height to be the full height of the screen and the default height";
        private String hide_app_string = "Toggles the app minimizing it or restoring it";
        private String help_string = "Displays useful information about the app";
        private String gearButton_string = "Where you can configure the app";

        LoadingImages loadingImages = new LoadingImages();


        public HelpManual()
        {

            setPrefWidth(dialogPane.getPrefWidth());
            initilize_textFlow();
        }
        private void initilize_textFlow()
        {
            Text text = new Text(copy_and_remove_string);
            this.getChildren().addAll(text);
//            this.getChildren().addAll(text,loadingImages.getImage("copy_and_remove"));
        }

        private void learn_the_buttons_section()
        {
            Text text = new Text("Learn the buttons");
//            text.setFont(new Font());
        }


        private class LoadingImages
        {
            private HashMap<String,ImageView> images = new HashMap<>(); // id, image
            private String resourcePath = "/assets/images/";


            String[] id_list = {"copy_and_remove", "trashButton", "copyButton", "selectAll", "startRecordingButton", "shortcut_button", "separator", "switchEdge", "expand", "hide_app", "help", "gearButton"};
            String[] images_names = {"copy_and_remove.png", "trash.png", "copy.png", "selectAll.png", "startRecording.png", "shortbutton_active.png", "Separator.png", "switchEdge.png", "expand.png", "hideApp.png", "help.png", "gear.png"};
            public LoadingImages()
            {

                if (id_list.length != images_names.length) {
                    throw new IllegalArgumentException("Array lengths do not match!");
                }

                for (int i = 0; i < id_list.length; i++) {
                    String image_name = images_names[i];
                    String id = id_list[i];
                    putImage(id,image_name);
                }
            }
            public void putImage(String id,String image_name)
            {
                String path = String.format("%s%s",resourcePath,image_name);
                ImageView imageView =  Utils.getImageView(path);
                images.put(id,imageView);
            }
            public ImageView getImage(String id)
            {
                try {
                    ImageView imageView = images.get(id);
                    return imageView;
                } catch (Exception e) {

                    throw new IllegalArgumentException("Id "+id+" does not exist");
                }

            }


        }
    }

}
