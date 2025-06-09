package org.example.vimclip;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Random;
import java.util.stream.IntStream;

public class ListViewScrollDemo extends Application {

    private final ListView<String> listView = new ListView<>();
    private final Random random = new Random();

    @Override
    public void start(Stage primaryStage) {
        // Populate the list with 100 items
        IntStream.range(0, 100).forEach(i -> listView.getItems().add("Item #" + i));

        Button scrollToLast = new Button("Scroll to Last Item");
        scrollToLast.setOnAction(e -> scrollToIndex(listView.getItems().size() - 1));

        Button scrollToRandom = new Button("Scroll to Random Item");
        scrollToRandom.setOnAction(e -> scrollToIndex(random.nextInt(listView.getItems().size())));

        HBox controls = new HBox(10, scrollToLast, scrollToRandom);
        VBox root = new VBox(10, listView, controls);

        Scene scene = new Scene(root, 300, 400);
        primaryStage.setTitle("ListView Scroll Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void scrollToIndex(int index) {
        // Scroll and select after layout is complete
        Platform.runLater(() -> {
            listView.scrollTo(index);
            listView.getSelectionModel().clearAndSelect(index);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
