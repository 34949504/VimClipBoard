package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import org.example.vimclip.Observar;

public class SeparateDialog extends Dialog implements Observar {
    private SharedInfo sharedInfo;

    DialogPane dialogPane = this.getDialogPane();
    Window window = this.getDialogPane().getScene().getWindow();
    TextArea textArea = new TextArea();


    public SeparateDialog(SharedInfo sharedInfo)
    {
        this.sharedInfo = sharedInfo;
    }


}
