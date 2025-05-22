package org.example.vimclip.JavaFx;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Observar;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.example.vimclip.MainProgram;
import org.json.JSONObject;

public class Command_displayer extends Application implements Observar {

    Stage main_stage;
    VBox vBox = new VBox();
    Timeline timeline = new Timeline();

    private String current_color = "-fx-background-color: white;";
    private AtomicBoolean copying_strings = new AtomicBoolean(false);
    Command_displayer_properties CDP = new Command_displayer_properties();

    private static JSONObject inner_config;


    Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        getting_values_config();
        this.main_stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        position_stage();
        stage.setAlwaysOnTop(true);
        stage.setOpacity(0);
        scene = new Scene(vBox);
        if (MainProgram.keyPressed != null) {
            MainProgram.keyPressed.addObserver(this);
        }

        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), actionEvent -> {
            main_stage.setOpacity(0);
        }));


        stage.setScene(scene);
        stage.show();


    }

    private void position_stage() {

        Screen primary = Screen.getPrimary();
        Rectangle2D rectangle2D = primary.getBounds();
        int height = (int) rectangle2D.getHeight();
        int width = (int) rectangle2D.getWidth();

        System.out.printf("%d %d", (int) height, (int) width);
        Dimension dims = calculating_desired_dims(height, width);
        int x_pos = width - dims.width;
        int y_pos = height - dims.height;

        main_stage.setHeight(dims.height);
        main_stage.setWidth(dims.width);

        main_stage.setX(x_pos);
        main_stage.setY(y_pos);


    }

    private Dimension calculating_desired_dims(int h, int w) {
        int width = (int) w / 4;
        int height = (int) h / 4;

        return new Dimension(width, height);

    }


    @Override
    public void recibir_next_keys(ArrayList<String> nextKeys, ArrayList<String> desc) {

//        System.out.println("This getting called?");
        Platform.runLater(() -> {

            if (current_color.compareTo("-fx-background-color: white;") != 0) {
                vBox.setStyle("-fx-background-color: white;");
                current_color = "-fx-background-color: white;";
            }

            vBox.getChildren().clear();
//            System.out.println("Key -> Description:");
            for (int i = 0; i < nextKeys.size(); i++) {
                String key = nextKeys.get(i);
                String description = desc.get(i);
                String completo = String.format("%s -> %s", key, description);
                System.out.println("completo " + completo);

                Label label = new Label(completo);
                label.setFont(new Font("arial", 16f));
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

                if (CDP.show_background_cues) {
                    if (copying_strings.get()) {
                        System.out.println("wamabanana");
                        main_stage.setOpacity(1);
                        vBox.setStyle("-fx-background-color: blue;");
                        timeline.play();
                        copying_strings.set(false);
                        current_color = "-fx-background-color: blue;";
                    }
                    System.out.println("boolean is " + copying_strings.get());
                }
            }
        });

    }

    @Override
    public void something_was_copied(String copiedString) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (CDP.show_background_cues) {
                    if (current_color.compareTo("-fx-background-color: green;") != 0) {
                        vBox.setStyle("-fx-background-color: green;");
                        current_color = "-fx-background-color: green;";
                    }
                    main_stage.setOpacity(1);

                    timeline.play();
                    copying_strings.set(true);
                }
            }
        });
    }

    @Override
    public void esc_was_pressed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {


                if (current_color.compareTo("-fx-background-color: white;") != 0) {
                    vBox.setStyle("-fx-background-color: white;");
                    current_color = "-fx-background-color: white;";
                }
            }
        });
    }


    private void getting_values_config() {
        CDP.setFont_size(inner_config.getInt("font_size"));
        CDP.setFont_color(inner_config.getString("font_color"));
        CDP.setFont_name(inner_config.getString("font_name"));
        CDP.setDefault_background(inner_config.getString("default_background"));
        CDP.setShow_background_cues(inner_config.getBoolean("show_background_cues"));
    }

    public static void setConfig(JSONObject cfg) {
        inner_config = cfg.getJSONObject("command_displayer_config");
    }

    @Getter
    @Setter
    public class Command_displayer_properties {

        private Integer font_size;
        private String font_color;
        private String font_name;
        private String default_background;
        private boolean show_background_cues;

    }


}
