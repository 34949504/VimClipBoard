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
        double screenWidth =  Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight =  Screen.getPrimary().getVisualBounds().getHeight();

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
        controller.setConfigMaster(AppContext.configMaster);

        AppContext.configMaster.setScreen_width(screenWidth);
        AppContext.configMaster.setScreen_height(screenHeight);
        AppContext.configMaster.init();


        //comunicacion
        controller.addObserver(AppContext.acciones.getListenToClipboard());
        controller.addObserver(commandDisplayer);
        controller.addObserver(AppContext.configMaster);

        AppContext.acciones.getClipBoardListener().addObserver(controller);
        controller.initialize_shit();


        stage.show();
    }




}
