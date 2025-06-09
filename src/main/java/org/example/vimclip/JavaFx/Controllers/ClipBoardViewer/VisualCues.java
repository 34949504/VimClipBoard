package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
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

public class VisualCues extends Dialog implements Observar {

    SharedInfo sharedInfo;
    ConfigMaster CF;
    DialogPane dialogPane = this.getDialogPane();
    Dialog dialog = this;
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));


    ImageView visual_cue_copied;



    public void init(SharedInfo sharedInfo, ConfigMaster configMaster)
    {
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
            if (window != null) window.hide();
        });
    }


    @Override
    public void something_was_copied(Object copiedString) {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("Is iconified "+sharedInfo.getStage().isIconified());

//                if (sharedInfo.getStage().isIconified())
//                {
                System.out.println("Fadetransition status"+fadeTransition.getStatus());
                    if (fadeTransition.getStatus() == Animation.Status.RUNNING)
                    {
                        dialogPane.setOpacity(0);  // set opacity to final value, e.g. 0
                        fadeTransition.stop();
                        System.out.println("Stopping animation");
                    }
                    System.out.println("This fadetransition was called");

                    dialog.show();
                    fadeTransition.play();
                }
//            }
        });

    }
    private void initDialogPosition()
    {

    }


    private void loadingImages()
    {
        visual_cue_copied = Utils.getImageViewV2("/assets/images/visual_cue_copied.png");
    }

    private void settingDialog()
    {
        dialogPane.setContent(visual_cue_copied);
    }


    private void initTransition()
    {
        //Setting the node for Transition
        fadeTransition.setNode(dialogPane);

        //Setting the property fromValue of the transition (opacity)
        fadeTransition.setFromValue(1.0);

        //Setting the property toValue of the transition (opacity)
        fadeTransition.setToValue(0.0);

    }

}
