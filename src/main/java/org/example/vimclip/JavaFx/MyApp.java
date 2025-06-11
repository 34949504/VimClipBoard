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
import org.example.vimclip.Utils;

import java.io.File;
import java.io.IOException;

public class MyApp extends Application {

    ClipBoardViewer clipBoardViewer;
    @Override
    public void start(Stage primaryStage) throws IOException {

        double screenWidth =  Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight =  Screen.getPrimary().getVisualBounds().getHeight();

        FXMLLoader loader = new FXMLLoader(MyApp.class.getResource("/FXML/ClipBoardViewer.fxml")); // convert to URL
        Parent root = loader.load();

        clipBoardViewer= loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        clipBoardViewer.setStage(primaryStage);
        clipBoardViewer.setRegistryManager(AppContext.registryManager);
        clipBoardViewer.setAcciones(AppContext.acciones);
        clipBoardViewer.setConfig(AppContext.config);
        clipBoardViewer.setConfigMaster(AppContext.configMaster);

        AppContext.configMaster.setScreen_width(screenWidth);
        AppContext.configMaster.setScreen_height(screenHeight);
        AppContext.configMaster.init();


        clipBoardViewer.initialize_shit();
        primaryStage.show();
    }




}
