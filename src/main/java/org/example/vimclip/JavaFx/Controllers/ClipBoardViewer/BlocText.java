package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BlocText {

    Label label = new Label();
    boolean selected = false;
    boolean selected_tovisualize = false;
    String style = null;
    BlocText blocText = this;

    String selected_style = "-fx-padding: 8; -fx-background-color: #fff8cc; -fx-border-color: #ffd208; -fx-background-radius: 5;";
    String not_selected_style = "-fx-padding: 8; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-background-radius: 5;";


    public BlocText(String text, ConfigLoader configLoader, MyDialog myDialog) {
        label.setText(text);
        label.setWrapText(true);
        label.setMaxWidth(configLoader.mainPane_defaultWidth);
        label.setPrefWidth(configLoader.mainPane_defaultWidth);
        label.setMaxHeight(configLoader.getLabel_maxHeight());
        label.setStyle(not_selected_style);

//            label.setEffect(dropShadow);
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {


                MouseButton mouseButton = mouseEvent.getButton();

                if (mouseButton == MouseButton.PRIMARY) {

                    if (!selected) {
                        label.setStyle(selected_style);
                        selected = !selected;
                    } else {

                        label.setStyle(not_selected_style);
                        selected = !selected;
                    }
                } else if (mouseButton == MouseButton.SECONDARY) {
                    selected_tovisualize = true;
                    myDialog.setCurrentBlocktext(blocText);
                    myDialog.setDialog_showing(true);
                }
            }
        });
    }

    public void set_selected(boolean selected) {
        if (selected) {
            label.setStyle(selected_style);
        } else {
            label.setStyle(not_selected_style);
            System.out.println("Deselecting");
        }
        this.selected = selected;

    }


}

