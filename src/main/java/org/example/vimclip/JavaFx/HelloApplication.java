package org.example.vimclip.JavaFx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
public class HelloApplication extends Application {

    public int myint = 0;

    @Override
    public void start(Stage stage) throws IOException {

        setUserAgentStylesheet(STYLESHEET_CASPIAN);
stage.setTitle("Hello world");
        Button btn = new Button();
        btn.setText("Hello World");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                btn.setText(Integer.toString(myint++));
                if (myint>3)myint = 0;
                System.out.println("Hello world ");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        ObservableList<Node> chi = root.getChildren();
        chi.addAll(new Rectangle(100,100, Color.BLUE),new Label("Go"));
        stage.setScene(new Scene(root,300,250));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}