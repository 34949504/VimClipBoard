package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

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
import org.example.vimclip.Observar;

import java.util.ArrayList;

public class MyDialog extends Dialog implements Observar {
    ArrayList<Observar> observers_list = new ArrayList<>();

    DialogPane dialogPane = this.getDialogPane();
    BorderPane borderPane = new BorderPane();
    Window window = this.getDialogPane().getScene().getWindow();
    BlocText currentBlocktext;

    boolean dialog_showing = false;

    int currentHeight;
    int currentWidth;

    int x;
    int y;

    private Button close_button = new Button();
    private TextArea textArea = new TextArea();

    SharedInfo sharedInfo;

    public MyDialog(SharedInfo sharedInfo) {
        this.sharedInfo = sharedInfo;
        settingDims();
        settingUp_dialogLayout();
        dialogPane.setContent(borderPane);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);


        if (window instanceof Stage) {
            ((Stage) window).setAlwaysOnTop(true);
        }

    }


    public void show_dialog() {

        calculating_where_dialog_should_appear();
        move_dialog();
        if (dialog_showing) {
            setting_textArea_text();
            this.show();
        }
        else {

        }
    }
    public void setting_textArea_text()
    {
        textArea.setText(currentBlocktext.getLabel().getText());
    }

    public void move_dialog()
    {

        this.setOnShown(e -> {
            Window window = this.getDialogPane().getScene().getWindow();
            window.setX(x);
            window.setY(y);

        });
    }

    private void settingDims() {
        dialogPane.setPrefWidth(sharedInfo.getConfigLoader().stage_defaultWidth);
        dialogPane.setPrefHeight(sharedInfo.getConfigLoader().stage_defaultHeight);
        setResizable(true);

        currentHeight = sharedInfo.getConfigLoader().stage_defaultHeight;
        currentWidth = sharedInfo.getConfigLoader().stage_currentWidth;
    }

    private void calculating_where_dialog_should_appear() {
        //priorotize appearing on bottom
        int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

        int available_space_below = screenHeight - (int) (sharedInfo.getStage().getY() + sharedInfo.getStage().getHeight());
        int available_space_above = (int)sharedInfo.getStage().getY();
        int available_space_left = (int)sharedInfo.getStage().getX();



        System.out.println("Available space " + available_space_below);
        System.out.println("dialog height is " + getHeight());
        if (available_space_below > currentHeight) {
            Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());


            x =(int) bounds.getMinX();
            y = (int)bounds.getMaxY(); // directly below content
        }
        else if (available_space_above >currentHeight)
        {
            Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());

            x = (int) bounds.getMinX();
            y = (int) (bounds.getMinY() - currentHeight);  // directly above the stage content

        }
        else if (available_space_left > currentWidth)
        {

//            System.out.println("Positing left ");
//            Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());
//
//            x = (int) bounds.getMinX() - currentWidth;
//            y = (int) (bounds.getMaxY() - currentHeight);  // directly above the stage content
            Stage stage = sharedInfo.getStage();
            x = (int) (stage.getX() - currentWidth);
            y = (int) (stage.getY() + stage.getHeight() - currentHeight);
            x=0;
            y=0;
        }


    }

    private void settingUp_dialogLayout()
    {

        settingUp_closeButton();
        borderPane.setTop(close_button);
        borderPane.setCenter(textArea);
    }
    private void settingUp_closeButton()
    {

        Image image = new Image("file:" + "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\close.png" ); // prefix "file:" for absolute paths
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40); // Optional: scale the image
        imageView.setFitHeight(20);
        close_button.setGraphic(imageView);
        close_button.setStyle("-fx-background-color: transparent;"); // Optional styling

        close_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Observar observer:observers_list)
                {
                    observer.text_in_visualize_mode_modified(textArea.getText());
                }
                setDialog_showing(false);
            }
        });
    }
    private void settingUp_textArea()
    {

    }

    public  void setDialog_showing(boolean showing)
    {
        dialog_showing = showing;
        if (dialog_showing) {
            show_dialog();
        }else {
            System.out.println("closing dialog");

            window.hide();
        }
    }

    @Override
    public void stage_was_moved() {
        move_dialog();
        if (dialog_showing)
            show_dialog();
    }

    public void setCurrentBlocktext(BlocText currentBlocktext) {
        this.currentBlocktext = currentBlocktext;

    }

    @Override
    public void stage_closing() {

        if (dialog_showing)
        {
            window.hide();
        }
    }

    public void addObserver(Observar observer)
    {
        observers_list.add(observer);
    }

    @Override
    public void tab_changed(Character reg) {

        setDialog_showing(false);
    }
}
