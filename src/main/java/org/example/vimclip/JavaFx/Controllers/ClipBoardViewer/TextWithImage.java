package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TextWithImage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Text before image
        Text text1 = new Text("Here is an image: ");

        // Load image
        Image image = new Image("C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\startRecording.png"); // or "https://..." for URL
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);

        // Text after image
        Text text2 = new Text(" and more text after the image.");

        TextFlow textFlow = new TextFlow(text1, imageView, text2);

        Scene scene = new Scene(textFlow, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text + Image in JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
