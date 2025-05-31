package org.example.vimclip;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class Prueba extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a label and a separator
        Label label = new Label("Hello, this is a label inside VBox.");
        label.setStyle("-fx-background-color: orange; -fx-padding: 10px;");
        label.setMaxWidth(Double.MAX_VALUE); // 💡 Make the label fill available width

        Separator separator = new Separator();

        VBox innerVbox = new VBox();
        innerVbox.getChildren().addAll(label, separator);
        innerVbox.setFillWidth(true); // Optional, usually VBox does this by default

// Outer VBox
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(innerVbox);
        vbox.setFillWidth(true); // Ensures children can use full width

// ScrollPane
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true); // Ensures VBox fills the scrollpane width

// BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scrollPane);

// Scene
        Scene scene = new Scene(borderPane, 400, 300);

// Stage
        primaryStage.setTitle("JavaFX Layout Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
