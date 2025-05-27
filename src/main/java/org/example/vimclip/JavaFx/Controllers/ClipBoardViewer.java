package org.example.vimclip.JavaFx.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;

import java.io.IOException;
import java.util.ArrayList;

@Setter
public class ClipBoardViewer implements Observar {

    private Stage stage;
    private Scene scene;
    private RegistryManager registryManager;
    ArrayList<Label> arrayListLabels = new ArrayList<>();

    @FXML private BorderPane mainPane;
    @FXML private TabPane registryTabPane;
    @FXML private ScrollPane scroll;
    @FXML private VBox contentPane;



    @FXML
    public void initialize() throws IOException {

        registryTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        registryTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {

                System.out.println("has changed");
                String text1 = tab.getText();
                String text2 = t1.getText();

                Character reg = t1.getId().charAt(0);

                String values = registryManager.get_all_values(reg);

                System.out.println(values);
            }
        });
    }

    public void initialize_shit()
    {

        stage.setResizable(false);
    }

    @Override
    public void something_was_copied(String copiedString) {

        System.out.println("Copied thing is here in thang "+copiedString);
    }
}