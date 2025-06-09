package org.example.vimclip;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CustomLabelListView extends Application {

    @Override
    public void start(Stage primaryStage) {
        ListView<String> listView = new ListView<>();
        for (int i = 0; i < 50; i++) {
            listView.getItems().add("Important Message #" + i);
        }

        // Custom cell factory using Labels
        listView.setCellFactory(lv -> new ListCell<>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    label.setFont(Font.font("Consolas", 14));
                    label.setTextFill(Color.DARKBLUE);
                    label.setStyle("-fx-background-color: lightgray; -fx-padding: 5;");
                    setGraphic(label);
                }
            }
        });

        VBox root = new VBox(listView);
        Scene scene = new Scene(root, 350, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Custom ListView with Label Cells");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
