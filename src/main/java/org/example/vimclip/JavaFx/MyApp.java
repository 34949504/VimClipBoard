package org.example.vimclip.JavaFx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.vimclip.AppContext;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.ClipBoardViewer;
import org.example.vimclip.JavaFx.Controllers.Command_displayer;

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


        File fxmlFile = new File("C:/Users/gerar/IdeaProjects/VimClip/src/main/resources/FXML/ClipBoardViewer.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL()); // convert to URL
        Parent root = loader.load();

        ClipBoardViewer controller = loader.getController();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

//        stage.setResizable(true);
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
        stage.show();
    }




}
