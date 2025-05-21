package org.example.vimclip.JavaFx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.vimclip.Observar;

import java.awt.*;
import java.util.ArrayList;

import org.example.vimclip.MainProgram;

public class Command_displayer extends Application implements Observar {

    Stage main_stage;
    VBox vBox = new VBox();

    @Override
    public void start(Stage stage) throws Exception {

        this.main_stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        position_stage();
        stage.setAlwaysOnTop(true);
        stage.setOpacity(0);

        if (MainProgram.keyPressed != null) {
            MainProgram.keyPressed.addObserver(this);
        }


        Scene scene = new Scene(vBox);

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("was clickied");
                vBox.getChildren().add(new Label("Whatupsis"));
            }
        });

        stage.setScene(scene);
        stage.show();


    }
    private void position_stage()
    {

        Screen primary = Screen.getPrimary();
        Rectangle2D rectangle2D =  primary.getBounds();
        int height = (int)rectangle2D.getHeight();
        int width = (int)rectangle2D.getWidth();

        System.out.printf("%d %d",(int)height,(int)width);
        Dimension dims = calculating_desired_dims(height,width);
        int x_pos = width - dims.width;
        int y_pos = height - dims.height;

        main_stage.setHeight(dims.height);
        main_stage.setWidth(dims.width);

        main_stage.setX(x_pos);
        main_stage.setY(y_pos);


    }
    private Dimension calculating_desired_dims(int h, int w)
    {
       int width = (int) w/4;
       int height = (int) h/4;

       return new Dimension(width,height);

    }






    @Override
    public void recibir_next_keys(ArrayList<String> nextKeys, ArrayList<String> desc) {

//        System.out.println("This getting called?");
        Platform.runLater(() -> {
            vBox.getChildren().clear();
//            System.out.println("Key -> Description:");
            for (int i = 0; i < nextKeys.size(); i++) {
                String key = nextKeys.get(i);
                String description = desc.get(i);
                String completo = String.format("%s -> %s", key, description);
                System.out.println("completo " + completo);

                Label label = new Label(completo);
                vBox.getChildren().add(label);
            }
//            System.out.println("Allegedly showings");
            main_stage.setOpacity(1);
        });

        vBox.requestLayout();
    }

    @Override
    public void command_restarted() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Se ha reiniciado");
                vBox.getChildren().clear();
                main_stage.setOpacity(0);
            }
        });

    }
}
