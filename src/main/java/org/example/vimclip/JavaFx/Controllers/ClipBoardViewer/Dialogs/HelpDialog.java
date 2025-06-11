package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Window;
import javafx.util.Duration;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.SharedInfo;
import org.example.vimclip.Observar;
import  javafx.stage.*;
import javafx.stage.Stage;
import org.example.vimclip.Utils;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;


import javafx.geometry.Pos;

public class HelpDialog extends Dialog implements Observar {

    SharedInfo sharedInfo;
    ConfigMaster configMaster;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;
    DialogPane dialogPane = this.getDialogPane();

    Hyperlink link = new Hyperlink("https://www.youtube.com/watch?v=v-yI4J9cd7g");
    DialogSimilarFuncs dialogSimilarFuncs;

    HelpManual helpManual;
    ScrollPane scrollPane;



    public void init(SharedInfo sharedInfo,ConfigMaster configMaster)
    {

        this.sharedInfo = sharedInfo;
        this.configMaster = configMaster;
        this.clipboardViewer_config = configMaster.getClipboardViewer_config();
        this.dialogSimilarFuncs = new DialogSimilarFuncs(sharedInfo,this,configMaster.getClipboardViewer_config());

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
        if (this.isShowing()) {
            // Defer repositioning until after resize/layout pass
            Platform.runLater(() -> {
                dialogPane.applyCss();
                dialogPane.layout();
                DialogSimilarFuncs.Coords coords = dialogSimilarFuncs.calculating_where_dialog_should_appear((int)dialogPane.getHeight(),(int)dialogPane.getWidth());

                Window window = getDialogPane().getScene().getWindow();
                if (window != null) {
                    window.setX(coords.x);
                    window.setY(coords.y);
                }
            });
        }
    }



    private class HelpManual extends TextFlow
    {
        private String[] copy_and_remove_string = { "Works like ctrl x, it copies the selected block/blocks and remove's it/remove's them","Alt + x" };
        private String[] trashButton_string = { "Deletes the selected block/blocks","Alt + d" };
        private String[] copyButton_string = { "Copies to your clipboard the selected block/blocks","Alt + c" };
        private String[] selectAll_string = { "Toggles between selecting all blocks and deselecting all blocks","Alt + a" };
        private String[] startRecordingButton_string = { "It lets the program know to listen for changes in the clipboard so that it is able to register them as blocks","alt + s" };
        private String[] shortcut_button_string = { "Toggles if shortcut mode is on or off\n\nWhen this is not active none of the shortcuts would react","Alt + Alt\n + t" };
        private String[] separator_string = { "When you get more than one text block, you define what separates the text","Alt + ," };
        private String[] switchEdge_string = { "Switches the app between the 4 edges of the screen","Alt + e" };
        private String[] expand_string = { "Toggles the app height to be the full height of the screen and the default height","alt + h" };
        private String[] hide_app_string = { "Toggles the app minimizing it or restoring it","alt + v" };
        private String[] help_string = { "Displays useful information about the app","alt + q" };
        private String[] gearButton_string = { "Where you can configure the app","alt + g" };

        LoadingImages loadingImages = new LoadingImages();

        private Font title_font = Font.loadFont(Utils.getInputStream("/assets/RifficFree-Bold.ttf"),24);
        private Font shorcutFont = Font.loadFont(Utils.getInputStream("/assets/BarberChop.otf"),12);
        private Font descFont = Font.loadFont(Utils.getInputStream("/assets/Quicksand_Light.otf"),16);
        private Font warningFont = Font.loadFont(Utils.getInputStream("/assets/MonospaceTypewriter.ttf"),20);

        String[][] descriptions = { copy_and_remove_string, trashButton_string, copyButton_string, selectAll_string, startRecordingButton_string, shortcut_button_string, separator_string, switchEdge_string, expand_string, hide_app_string, help_string, gearButton_string };

        public HelpManual()
        {

//            setPrefWidth(100);
            setPrefWidth(dialogPane.getPrefWidth() - dialogPane.getPrefHeight() * 10 / 100);
            initilize_textFlow();
        }
        private void initilize_textFlow()
        {
            how_to_use_the_commands();
            tips_sections();
            learn_the_buttons_section();
            tutorial_section();
//            this.getChildren().addAll(text,loadingImages.getImage("copy_and_remove"));
        }

        private void what_is_this_section()
        {

            Text title = new Text("\n\n");
            title.setFont(title_font);
        }

        private void tips_sections()
        {

            Text title = new Text("Tips\n\n");
            title.setFont(title_font);

            Text tip1 = new Text("Use your up and down arrow to navigate the copied blocks\n\n");
            tip1.setFont(descFont);
            Text tip2 = new Text("Use spacebar or left click to select blocks\n\n");
            Text tip3 = new Text("Right clicking a block allows you to edit the contents of the block\n\n");
            tip2.setFont(descFont);


            this.getChildren().addAll(title,tip1,tip2,tip3);
        }
        private void tutorial_section()
        {
            Text title = new Text("Tutorial");
            title.setFont(title_font);
            title.setFill(Color.BLACK);
            ImageView imageView = Utils.getImageView("/assets/images/youtube_btn.png",100,100);

            Image firstImage = new Image(Utils.getInputStream("/assets/images/youtube_btn.png")); // prefix "file:" for absolute paths
            Image secondImage = new Image(Utils.getInputStream("/assets/images/youtube_btnPressed.png")); // prefix "file:" for absolute paths
            Timeline timeline = new Timeline();

            timeline.setCycleCount(1);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), actionEvent -> {

                imageView.setImage(firstImage);
                try {
                    Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=v-yI4J9cd7g"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }));

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    imageView.setImage(secondImage);
                    timeline.play();
                }
            });
//
//
//            Rectangle background = new Rectangle();
//            background.setWidth(100); // adjust as needed
//            background.setHeight(40);
//            background.setFill(Color.RED);
//            background.setArcWidth(10);
//            background.setArcHeight(10);
//
//            StackPane stack = new StackPane(background, title);
//            stack.setPadding(new Insets(5)); // optional, adjust spacing
//            stack.setAlignment(Pos.CENTER_LEFT); // optional alignment

            this.getChildren().addAll(title,new Text("\n"),imageView); // stack contains both text & rectangle
        }

        private void learn_the_buttons_section()
        {
            Text title = new Text("Learn the buttons\n\n");
            title.setFont(title_font);

            this.getChildren().add(title);

            for (int i = 0; i < descriptions.length; i++) {

                String[] desc_obj = descriptions[i];

                String desc = desc_obj[0];
                String shortcut = desc_obj[1];
                String id = loadingImages.id_list[i];
                createBlock(desc,shortcut,id);

            }

        }
        private void how_to_use_the_commands()
        {
            Text title = createTitle("How to do the commands\n\n");

            Text text = createDesc("The commands work by pressing a sequence of keys," +
                    "for example if  i want to do the copy command i would press alt   " +
                    " press the key c\n\n");

            Text warning =  createWarning("WARNING\n");
            Text t = createDesc("Do not do the commands by holding the modifier alt because that would trigger " +
                    "actions of the apps that use those commands, so press alt without holding it, then the other key to do the command\n\n");

            this.getChildren().addAll(title,text,warning,t);



        }

        private Text createTitle(String text)
        {

            Text title = new Text(text);
            title.setFont(title_font);
            return  title;
        }
        private Text createDesc(String text)
        {
            Text desc = new Text(text);
            desc.setFont(descFont);
            return desc;
        }
        private Text createWarning(String text)
        {
            Text warning = new Text(text);
            warning.setFont(warningFont);
            warning.setFill(Color.RED);
            return warning;
        }




        private void createBlock(String desc_str,String shortcut_str,String image_id)
        {

            Text shortcut = new Text(shortcut_str);
            shortcut.setFont(shorcutFont);

            ImageView imageView = loadingImages.getImage(image_id);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(40);  // adjust size as needed

            VBox vBoxLeft = new VBox(shortcut, imageView);
            vBoxLeft.setSpacing(5);
//            vBoxLeft.setStyle("-fx-background-color: yellow;");
            vBoxLeft.setAlignment(Pos.TOP_LEFT);
            vBoxLeft.setMinWidth(50); // fixed width to allow room for wrapping
            vBoxLeft.setMaxWidth(50);

            // Right side text that wraps
            Text descText = new Text(desc_str);
            descText.setFont(descFont);

            TextFlow textFlowRight = new TextFlow(descText);
            textFlowRight.setLineSpacing(3);

            // Horizontal box to combine both
            HBox hBox = new HBox(vBoxLeft, textFlowRight);
            hBox.setAlignment(Pos.TOP_LEFT);
            hBox.setPrefWidth(dialogPane.getPrefWidth() - dialogPane.getPrefHeight() * 0.1);

            Separator separator = new Separator();
            separator.setMaxWidth(Double.MAX_VALUE);
            separator.setMinHeight(3);
            separator.setMinWidth(dialogPane.getPrefWidth());
            separator.setOrientation(Orientation.HORIZONTAL);
            separator.setStyle("-fx-background-color: black;");
            this.getChildren().addAll(hBox,new Text("\n\n"),separator,new Text("\n\n"));
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
