package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.Observar;
import org.example.vimclip.Utils;

import java.sql.SQLOutput;

public class VisualCues extends Dialog implements Observar {

    SharedInfo sharedInfo;
    ConfigMaster CF;
    DialogPane dialogPane = this.getDialogPane();
    Dialog dialog = this;
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));

    ImageView visual_cue_copied;
    ImageView visual_cue_itson;

    Double x;
    Double y;

    // Flag to prevent overlapping animations
    private boolean isShowing = false;

    public void init(SharedInfo sharedInfo, ConfigMaster configMaster) {
        this.sharedInfo = sharedInfo;
        this.CF = configMaster;
        loadingImages();
        settingDialog();
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.TRANSPARENT);
        initTransition();

        Window window = getDialogPane().getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).setAlwaysOnTop(true);
        }

        dialogPane.setStyle("-fx-background-color: transparent;");

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);

        fadeTransition.setOnFinished(event -> {
            if (window != null) {
                window.hide();
                isShowing = false; // Reset flag when animation completes
            }
        });

        // Calculate position once when window is first shown
        calculateInitialPosition();
    }

    private void calculateInitialPosition() {
        Window window = dialogPane.getScene().getWindow();
        window.setOnShown(e -> {
            if (x == null || y == null) { // Only calculate once
                double width = window.getWidth();
                double height = window.getHeight();
                double screenWidth = CF.getScreen_width();
                double screenHeight = CF.getScreen_height();

                x = screenWidth - width;
                y = screenHeight - height;

                System.out.printf("Initial position calculated: x = %f, y = %f\n", x, y);
            }
            window.setX(x);
            window.setY(y);
        });
    }

    private void ensureCorrectPosition() {
        Platform.runLater(() -> {
            Window window = dialogPane.getScene().getWindow();

            // If position hasn't been calculated yet, do it now
            if (x == null || y == null) {
                if (!window.isShowing()) {
                    dialog.show();
                    window.setOpacity(0);
                }

                Platform.runLater(() -> {
                    double width = window.getWidth();
                    double height = window.getHeight();
                    double screenWidth = CF.getScreen_width();
                    double screenHeight = CF.getScreen_height();

                    x = screenWidth - width;
                    y = screenHeight - height;

                    window.setX(x);
                    window.setY(y);
                    System.out.printf("Position recalculated: x = %f, y = %f\n", x, y);
                });
            } else {
                // Use already calculated position
                window.setX(x);
                window.setY(y);
            }
        });
    }

    @Override
    public void copyListener_is_on() {
        dialogPane.setContent(visual_cue_itson);
        showing_visualCue();
    }

    @Override
    public void something_was_copied(Object copiedString) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                dialogPane.setContent(visual_cue_copied);
                showing_visualCue();
            }
        });
    }

    private void showing_visualCue() {
        // Prevent overlapping animations
        if (isShowing) {
            return; // Exit early if already showing
        }

        isShowing = true;

        Platform.runLater(() -> {
            System.out.println("Is iconified " + sharedInfo.getStage().isIconified());

            // Stop any running animation first
            if (fadeTransition.getStatus() == Animation.Status.RUNNING) {
                fadeTransition.stop();
                System.out.println("Stopping animation");
            }

            // Reset opacity and show dialog
            dialogPane.setOpacity(1.0);
            dialog.show();

            // Ensure correct positioning
            ensureCorrectPosition();

            // Small delay to ensure positioning is complete before starting animation
            Platform.runLater(() -> {
                Window window = dialogPane.getScene().getWindow();
                window.setOpacity(1);
                fadeTransition.play();
            });
        });
    }

    private void loadingImages() {
        visual_cue_copied = Utils.getImageViewV2("/assets/images/visual_cue_copied.png");
        visual_cue_itson = Utils.getImageViewV2("/assets/images/itson.png");
    }

    private void settingDialog() {
        dialogPane.setContent(visual_cue_copied);
    }

    private void initTransition() {
        fadeTransition.setNode(dialogPane);

        fadeTransition.setFromValue(1.0);

        fadeTransition.setToValue(0.0);
    }

    @Override
    public void show_visual_cues(Image image) {
        ImageView imageView = new ImageView(image);
        dialogPane.setContent(imageView);
        showing_visualCue();
    }
}
