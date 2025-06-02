package org.example.vimclip.JavaFx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.ClipBoardViewer;
import org.example.vimclip.JavaFx.Controllers.Command_displayer;
import org.example.vimclip.Utils;

import java.io.File;
import java.io.IOException;

public class MyApp extends Application {

    Command_displayer commandDisplayer;

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setOpacity(0);
        commandDisplayer = new Command_displayer(AppContext.config, primaryStage);
        creating_communication();

        initialize_clipboardViewer();


    }

    private void creating_communication() {
        AppContext.keyPressed.addObserver(commandDisplayer);
    }

    private void initialize_clipboardViewer() throws IOException {


//        File fxmlFile = new File(Utils.getInputStream("/FXML/ClipBoardViewer.fxml"));
//        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL()); // convert to URL
        FXMLLoader loader = new FXMLLoader(MyApp.class.getResource("/FXML/ClipBoardViewer.fxml")); // convert to URL
        Parent root = loader.load();

        ClipBoardViewer controller = loader.getController();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        controller.setStage(stage);
        controller.setRegistryManager(AppContext.registryManager);
        controller.setAcciones(AppContext.acciones);
        controller.setConfig(AppContext.config);


        //comunicacion
        controller.addObserver(AppContext.acciones.getListenToClipboard());
        controller.addObserver(commandDisplayer);

        AppContext.acciones.getClipBoardListener().addObserver(controller);
        controller.initialize_shit();

        stage.setWidth(300);
        stage.setHeight(300);

        int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

        double widthPercent = (300.0 / screenWidth) * 100;
        double heightPercent = (300.0 / screenHeight) * 100;

        System.out.printf("Width: %.2f%% of screen%n", widthPercent);
        System.out.printf("Height: %.2f%% of screen%n", heightPercent);


        stage.show();
    }




}
