package org.example.vimclip.JavaFx.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Observar;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

public class Command_displayer implements Observar {

    Stage main_stage;
    VBox vBox = new VBox();
    Timeline timeline = new Timeline();
    Scene scene;

    private String current_color = "-fx-background-color: white;";
    private AtomicBoolean copying_strings = new AtomicBoolean(false);
    private Command_displayer_properties CDP = new Command_displayer_properties();

    private JSONObject inner_config;
    private Parent root = null;
    private String colorAfter = null;


    public Command_displayer(JSONObject config, Stage main_stage)
    {

        this.inner_config = config.getJSONObject("command_displayer_config");
        this.main_stage = main_stage;
        this.scene = new Scene(vBox);

        getting_values_config();
        position_stage();
        setting_up_timeline();

        main_stage.setScene(scene);
//        main_stage.show();
//        main_stage.hide();

    }
    private void setting_up_timeline()
    {

        timeline.setCycleCount(1);

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), actionEvent -> {
            main_stage.setOpacity(0);
            if (colorAfter != null)
                vBox.setStyle(colorAfter);
        }));
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

        Platform.runLater(() -> {

            vBox.setStyle("-fx-background-color: white;");
            System.out.println("recibiendo?");
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
            main_stage.show();
        });

        vBox.requestLayout();
    }

    @Override
    public void command_restarted() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (!copying_strings.get()){
                vBox.getChildren().clear();
                main_stage.setOpacity(0);}
                vBox.setStyle("-fx-background-color: white;");
                main_stage.hide();

            }
        });

    }

    @Override
    public void something_was_copied(Object object) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (object instanceof String){
                if (CDP.show_background_cues) {
                    vBox.setStyle("-fx-background-color: green;");
                    main_stage.setOpacity(1);
                    colorAfter = null;
                    timeline.play();
                    copying_strings.set(true);
                }}
            }
        });
    }

    @Override
    public void esc_was_pressed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (copying_strings.get())
                {
                    if (CDP.show_background_cues) {
                            main_stage.setOpacity(1);
                            vBox.setStyle("-fx-background-color: blue;");
                        System.out.println("suppose to change to blue");
                        colorAfter ="-fx-background-color: white;";
                            timeline.play();
                            copying_strings.set(false);
                        System.out.println("boolean is " + copying_strings.get());
                    }
                }
                else {

                    System.out.println("cleared?");
                    vBox.getChildren().clear();
                    main_stage.setOpacity(0);
                    vBox.setStyle("-fx-background-color: white;");
                    main_stage.hide();
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
