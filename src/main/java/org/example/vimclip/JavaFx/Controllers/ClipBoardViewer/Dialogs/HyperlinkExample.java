package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

public class HyperlinkExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        Hyperlink link = new Hyperlink("Open Google");

        try {
            link.setOnAction(e -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=1FXjK717Aes"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        StackPane root = new StackPane(link);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
