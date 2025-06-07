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
