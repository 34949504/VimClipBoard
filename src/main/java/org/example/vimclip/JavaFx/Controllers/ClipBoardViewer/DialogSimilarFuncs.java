package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.geometry.Bounds;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;


public class DialogSimilarFuncs {

    SharedInfo sharedInfo;
    Dialog dialog;
    DialogPane dialogPane;

    public DialogSimilarFuncs(SharedInfo sharedInfo, Dialog dialog)
    {

        this.sharedInfo = sharedInfo;
        this.dialog = dialog;
        this.dialogPane = dialog.getDialogPane();
    }



    public Coords calculating_where_dialog_should_appear(int currentHeight,int currentWidth) {
        int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

        Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(
                sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());

        int available_space_below = (int) (screenHeight - bounds.getMaxY());
        int available_space_above = (int) (bounds.getMinY());
        int available_space_left = (int) (bounds.getMinX());
        int available_space_right = (int) (screenWidth - bounds.getMaxX());

        int x = 0;
        int y = 0;

        if (available_space_below > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) bounds.getMaxY();
        } else if (available_space_above > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) (bounds.getMinY() - currentHeight);
        } else if (available_space_left > currentWidth) {
            x = (int) (bounds.getMinX() - dialogPane.getWidth());
            y = (int) bounds.getMaxY() - currentHeight;
        }else if (available_space_right > currentWidth)
        {
            x = (int) (bounds.getMaxX()) ;
            y = (int) bounds.getMaxY() - currentHeight;
        }

        return new Coords(x,y);

    }


    @Getter
    @Setter
    public static class Coords
    {
        int x;
        int y;

        public Coords(int x, int y )
        {
            this.x = x; this.y = y;
        }
    }
}
