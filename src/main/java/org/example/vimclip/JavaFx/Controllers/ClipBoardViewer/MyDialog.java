package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

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
import org.example.vimclip.Observar;

import java.util.ArrayList;

public class MyDialog extends Dialog implements Observar {

    ArrayList<Observar> observers_list = new ArrayList<>();

    DialogPane dialogPane = this.getDialogPane();
    BorderPane borderPane = new BorderPane();
    BlocText currentBlocktext;

    boolean dialog_showing = false;

    int currentHeight;
    int currentWidth;

    int x;
    int y;

    private Button close_button = new Button();
    private TextArea textArea = new TextArea();

    SharedInfo sharedInfo;
    ConfigMaster.ClipboardViewer_config clipboardViewer_config;

    public MyDialog(SharedInfo sharedInfo, ConfigMaster configMaster) {
        this.sharedInfo = sharedInfo;
        clipboardViewer_config = configMaster.getClipboardViewer_config();
        settingDims();
        settingUp_dialogLayout();
        dialogPane.setContent(borderPane);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);

        // Let window positioning happen after dialog is shown
        this.setOnShown(e -> {
            dialogPane.applyCss();
            dialogPane.layout();

            calculating_where_dialog_should_appear();

            Window window = getDialogPane().getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).setAlwaysOnTop(true);
            }
            window.setX(x);
            window.setY(y);
        });
    }

    public void show_dialog() {
        if (dialog_showing) {
            setting_textArea_text();
            this.show();
        }
    }

    public void setting_textArea_text() {
        textArea.setText(currentBlocktext.getLabel().getText());
    }

    private void settingDims() {
        dialogPane.setPrefWidth(sharedInfo.getConfigLoader().stage_defaultWidth);
        dialogPane.setPrefHeight(sharedInfo.getConfigLoader().stage_defaultHeight / 2);
        setResizable(true);

        currentHeight = (int)(clipboardViewer_config.getStage_height()/2);
        currentWidth = (int)(clipboardViewer_config.getStage_width());
    }

    private void calculating_where_dialog_should_appear() {
        int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

        Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(
                sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());

        int available_space_below = (int) (screenHeight - bounds.getMaxY());
        int available_space_above = (int) (bounds.getMinY());
        int available_space_left = (int) (bounds.getMinX());
        int available_space_right = (int) (screenWidth - bounds.getMaxX());


        if (available_space_below > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) bounds.getMaxY();
        } else if (available_space_above > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) (bounds.getMinY() - currentHeight);
        } else if (available_space_left > currentWidth) {
            System.out.printf("Dialog width %f\nDialogPane width %f\n", getWidth(), dialogPane.getWidth());
            x = (int) (bounds.getMinX() - dialogPane.getWidth());
            y = (int) bounds.getMaxY() - currentHeight;
        }else if (available_space_right > currentWidth)
        {

            x = (int) (bounds.getMaxX()) ;
            y = (int) bounds.getMaxY() - currentHeight;
        }
    }

    private void settingUp_dialogLayout() {
        settingUp_closeButton();
        borderPane.setTop(close_button);
        borderPane.setCenter(textArea);
    }

    private void settingUp_closeButton() {
        Image image = new Image("file:" + "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\close.png");
        ImageView imageView = new ImageView(image);
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
                setDialog_showing(false);
            }
        });
    }

    public void setDialog_showing(boolean showing) {
        dialog_showing = showing;
        if (dialog_showing) {
            show_dialog();
        } else {
            System.out.println("closing dialog");
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) window.hide();
        }
    }

    @Override
    public void stage_was_moved() {
        if (dialog_showing) {
            dialogPane.applyCss();
            dialogPane.layout();
            calculating_where_dialog_should_appear();
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) {
                window.setX(x);
                window.setY(y);
            }
        }
    }

    public void setCurrentBlocktext(BlocText currentBlocktext) {
        this.currentBlocktext = currentBlocktext;
    }

    @Override
    public void stage_minimizing() {
        if (dialog_showing) {
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) window.hide();
        }
    }

    public void addObserver(Observar observer) {
        observers_list.add(observer);
    }

    @Override
    public void tab_changed(Character reg) {
        setDialog_showing(false);
    }

   @Override
public void stage_has_been_resized() {
    System.out.println("yeha motherfuckers ");

    if (dialog_showing) {
        // Defer repositioning until after resize/layout pass
        Platform.runLater(() -> {
            dialogPane.applyCss();
            dialogPane.layout();
            calculating_where_dialog_should_appear();

            Window window = getDialogPane().getScene().getWindow();
            if (window != null) {
                window.setX(x);
                window.setY(y);
            }
        });
    }
}
}
