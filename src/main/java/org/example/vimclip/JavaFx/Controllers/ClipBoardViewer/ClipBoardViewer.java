package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.*;
import lombok.Setter;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.*;

@Setter
public class ClipBoardViewer implements Observar {

    Stage stage;
    Acciones acciones;
    private SharedInfo sharedInfo = new SharedInfo();
    private RegistryManager registryManager;

    private ButtonShit buttonShit;
    private ConfigLoader configLoader;
    private MyDialog myDialog;
    private Instance_manager instanceManager;

    private ArrayList<Observar> observadores_list = new ArrayList<>();
    private JSONObject config;


    @FXML
    private BorderPane mainPane;
    @FXML
    private TabPane registryTabPane;
    @FXML
    private ScrollPane scroll;
    @FXML
    private VBox contentPane;

    @FXML
    private Button gearButton;
    @FXML
    private Button trashButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button startRecordingButton;
    @FXML
    private Button selectAll;

    @FXML
    private Button expand;
    @FXML
    private Button switchEdge;

    ArrayList<Button> buttons;

    @FXML
    private Tab a;
    @FXML
    private Tab s;
    @FXML
    private Tab d;
    @FXML
    private Tab f;


    @FXML
    public void initialize() throws IOException {
        scroll.setFitToWidth(true);
        contentPane.setFillWidth(true); // Ensures children can use full width

//        contentPane.setMaxWidth(Double.MAX_VALUE);
//        contentPane.setMaxHeight(Double.MAX_VALUE);

        buttons = new ArrayList<>(Arrays.asList(
                gearButton,
                trashButton,
                copyButton,
                startRecordingButton,
                selectAll,
                expand,
                switchEdge
        ));


        tabsPane_listener();
        registryTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        sharedInfo.setCurrent_register(registryTabPane.getTabs().get(0).getId().charAt(0));


    }


    private void tabsPane_listener() {

        registryTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {

                System.out.println("has changed");
                Character reg = t1.getId().charAt(0);
                sharedInfo.setCurrent_register(reg);

                for(Observar observer:observadores_list)
                {
                    observer.tab_changed(reg);
                }




            }
        });
    }


    public void initialize_shit() {
        settingUp_sharedInfo();
//        sharedInfo.getStage().setResizable(false);
        configLoader = new ConfigLoader(config.getJSONObject("clipboardViewer_config"), sharedInfo);
        sharedInfo.setConfigLoader(configLoader);
        myDialog = new MyDialog(sharedInfo);
        sharedInfo.setMyDialog(myDialog);
        buttonShit = new ButtonShit(buttons, sharedInfo);
        buttonShit.setObservadores_list(observadores_list);
        changeListener_stagePosition();

        sharedInfo.initialize_available_instances(configLoader, myDialog);

        instanceManager = new Instance_manager(sharedInfo);

        addObserver(myDialog);
        addObserver(configLoader);
        addObserver(instanceManager);
        addObserver(acciones.getClipBoardListener());
        myDialog.addObserver(this);
    }

    public void settingUp_sharedInfo() {
        sharedInfo.setContentPane(contentPane);
        sharedInfo.setConfigLoader(configLoader);
        sharedInfo.setMainPane(mainPane);
        sharedInfo.setStage(stage);
        sharedInfo.setAcciones(acciones);
        sharedInfo.setRegistryManager(registryManager);
    }

    @Override
    public void text_in_visualize_mode_modified(String newTex) {

        int i = 0;
        for (; i < sharedInfo.getCurrentWholePackageArray().size(); i++) {

            SharedInfo.WholePackage wholePackage = sharedInfo.getCurrentWholePackageArray().get(i);
            BlocText blocText = wholePackage.getBlocText();


            if (blocText.isSelected_tovisualize()) {
                blocText.getLabel().setText(newTex);
                blocText.setSelected_tovisualize(false);
                break;
            }
        }
        registryManager.changeValue(sharedInfo.getCurrent_register(), i, newTex);

    }

    private void changeListener_stagePosition() {

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        sharedInfo.getStage().xProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double maxX = screenWidth - sharedInfo.getStage().getWidth();
                if (t1.doubleValue() > maxX) {
                    sharedInfo.getStage().setX(maxX);
                } else if (t1.doubleValue() < 0) {
                    sharedInfo.getStage().setX(0);
                }
                for (Observar observador : observadores_list) {
                    observador.stage_was_moved();
                }
                System.out.println("Numbers " + number + " " + t1);
            }
        });
        System.out.println("wtfaoee" + myDialog.getHeight() + "lefromgaae height is ");


        sharedInfo.getStage().yProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                double maxY = screenHeight - sharedInfo.getStage().getHeight();
                if (t1.doubleValue() > maxY) {
                    sharedInfo.getStage().setY(maxY);
                } else if (t1.doubleValue() < 0) {
                    sharedInfo.getStage().setY(0);
                }
                for (Observar observador : observadores_list) {
                    observador.stage_was_moved();
                }
            }
        });
    }


    @Override
    public void something_was_copied(Object object) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (object instanceof String copiedString) {
                    System.out.println("Copied thing is here in thang " + copiedString);
                    doing_array_surgery(copiedString);
                    sharedInfo.getCopyingStrings().set(true);
                }
                else if (object instanceof Image image)
                {

                }
            }
        });
    }

    public void doing_array_surgery(String copiedString) {
        SharedInfo.WholePackage wholePackage = sharedInfo.getInstance_from_available_wholePackage();

        BlocText blocText = wholePackage.getBlocText();
        Separator separator = wholePackage.getSeparator();
        VBox hBox = wholePackage.getVBox();

        blocText.getLabel().setText(copiedString);
        separator.setOrientation(Orientation.HORIZONTAL);
//        hBox.getChildren().addAll(blocText.getLabel(), separator);

        sharedInfo.getCurrentWholePackageArray().add(wholePackage);

        contentPane.getChildren().add(hBox);

    }


    @Override
    public void esc_was_pressed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (sharedInfo.getCopyingStrings().get()) {
                    sharedInfo.getCopyingStrings().set(false);

                }
            }
        });
    }


    public class MyTooltip extends Tooltip {
        public static Font font = Font.loadFont("file:C:/Users/gerar/IdeaProjects/VimClip/src/main/resources/assets/BarberChop.otf", 14);

        public MyTooltip(String message) {
            this.setText(message);
            this.setFont(font);
            setting_tooltips();
        }

        private void setting_tooltips() {
            expand.setTooltip(new MyTooltip("Expands application vertically"));
            gearButton.setTooltip(new MyTooltip("Configuration"));
            trashButton.setTooltip(new MyTooltip("Deletes selected blocks"));
            copyButton.setTooltip(new MyTooltip("Copies to your normal clipboard the selected blocks"));
            startRecordingButton.setTooltip(new MyTooltip("Saves everything that you copy when its on in the current tab"));
            selectAll.setTooltip(new MyTooltip("Selects all blocks/Deselects all blocks"));
            switchEdge.setTooltip(new MyTooltip("Switches edge"));
        }
    }

    public void addObserver(Observar observador) {
        observadores_list.add(observador);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAcciones(Acciones acciones) {
        this.acciones = acciones;
    }


}