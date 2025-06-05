package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

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
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs.MyDialog;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs.SepDialog;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

@Setter
public class ClipBoardViewer implements Observar {

    Stage stage;
    Acciones acciones;
    private SharedInfo sharedInfo = new SharedInfo();
    private RegistryManager registryManager;

    private MainButtons buttonShit;
    private ConfigLoader configLoader;
    private MyDialog myDialog;
    private Instance_manager instanceManager;
    private SepDialog sepDialog;

    private ArrayList<Observar> observadores_list = new ArrayList<>();
    private JSONObject config;
    private ConfigMaster configMaster;
    private ConfigMaster.ClipboardViewer_config clipboardViewer_config;


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
    private Button separator;

    @FXML
    private Button expand;
    @FXML
    private Button switchEdge;

    @FXML private Button copy_and_remove;

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
        setting_tooltips();
        scroll.setFitToWidth(true);
        contentPane.setFillWidth(true); // Ensures children can use full width


        buttons = new ArrayList<>(Arrays.asList(
                gearButton,
                trashButton,
                copyButton,
                startRecordingButton,
                selectAll,
                expand,
                switchEdge,
                separator,
                copy_and_remove
        ));


        tabsPane_listener();
        registryTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        sharedInfo.setCurrent_register(registryTabPane.getTabs().get(0).getId().charAt(0));


    }


    @Override
    public void appShortcut_beenPressed(ArrayList<String> shortCut) {

        for(String str: shortCut)
        {
            System.out.println(str);
        }
        System.out.println("Found it sucka");

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

        clipboardViewer_config = configMaster.getClipboardViewer_config();


        settingUp_sharedInfo();
        configLoader = new ConfigLoader(config.getJSONObject("clipboardViewer_config"), sharedInfo);
        sharedInfo.setConfigLoader(configLoader);
        myDialog = new MyDialog(sharedInfo,configMaster);
        sharedInfo.setMyDialog(myDialog);
        buttonShit = new MainButtons(buttons, sharedInfo);
        buttonShit.setObservadores_list(observadores_list);
        changeListener_stagePosition();

        sharedInfo.initialize_available_instances(configLoader, myDialog);

        instanceManager = new Instance_manager(sharedInfo);
        sepDialog = new SepDialog(sharedInfo,configMaster);
        buttonShit.addObserver(sepDialog); //WARNING i am using setObservers in buttonshit wit hthe observers of clipboardviewer

        addObserver(myDialog);
        addObserver(configLoader);
        addObserver(instanceManager);
        addObserver(acciones.getClipBoardListener());
        myDialog.addObserver(this);
//        buttonShit.addObserver(myDialog);

        stage_window_listener();


        stage.setWidth(clipboardViewer_config.getStage_width());
        stage.setHeight(clipboardViewer_config.getStage_height());

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
            }
        });
//        System.out.println("wtfaoee" + myDialog.getHeight() + "lefromgaae height is ");


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
                    settingUp_blocText(copiedString);
                    sharedInfo.getCopyingStrings().set(true);
                }
                else if (object instanceof Image image)
                {
                    settingUp_imageBloc(image);
                    System.out.println("Image was copied");

                }
            }
        });
    }

    public void settingUp_blocText(String copiedString) {
        SharedInfo.WholePackage wholePackage = sharedInfo.getInstance_from_available_wholePackage();

        BlocText blocText = wholePackage.getBlocText();
        Separator separator = wholePackage.getSeparator();
        VBox vBox = wholePackage.getVBox();

        if (vBox.getChildren().isEmpty())
            vBox.getChildren().addAll(blocText.getLabel(),separator);


        blocText.getLabel().setText(copiedString);
        separator.setOrientation(Orientation.HORIZONTAL);

        sharedInfo.getCurrentWholePackageArray().add(wholePackage);

        contentPane.getChildren().add(vBox);
    }
    public void settingUp_imageBloc(Image image)
    {

        SharedInfo.WholePackage wholePackage = sharedInfo.getInstance_from_available_wholePackage();

        BlocImage blocImage = wholePackage.getBlocImage();
        Separator separator = wholePackage.getSeparator();
        VBox vBox = wholePackage.getVBox();

        blocImage.setImage(image);


        vBox.getChildren().addAll(blocImage.getImageView(),separator);
        separator.setOrientation(Orientation.HORIZONTAL);
        sharedInfo.getCurrentWholePackageArray().add(wholePackage);

        contentPane.getChildren().add(vBox);
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

    private void setting_tooltips() {
        expand.setTooltip(new MyTooltip("Expands application vertically"));
        gearButton.setTooltip(new MyTooltip("Configuration"));
        trashButton.setTooltip(new MyTooltip("Deletes selected blocks"));
        copyButton.setTooltip(new MyTooltip("Copies to your normal clipboard the selected blocks"));
        startRecordingButton.setTooltip(new MyTooltip("Saves everything that you copy when its on in the current tab"));
        selectAll.setTooltip(new MyTooltip("Selects all blocks/Deselects all blocks"));
        switchEdge.setTooltip(new MyTooltip("Switches edge"));
        separator.setTooltip(new MyTooltip(""));
    }

    public class MyTooltip extends Tooltip {
        public static Font font = Font.loadFont("file:C:/Users/gerar/IdeaProjects/VimClip/src/main/resources/assets/BarberChop.otf", 14);

        public MyTooltip(String message) {
            this.setText(message);
            this.setFont(font);
        }

    }

    private void stage_window_listener()
    {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {

                if (WindowEvent.WINDOW_CLOSE_REQUEST == windowEvent.getEventType())
                {
                    for (Observar observer:observadores_list)
                    {
                        observer.stage_closing();
                    }
                    System.out.println("X symbol to close was clicked ");
                    System.exit(0);
                }
                if (WindowEvent.WINDOW_HIDDEN == windowEvent.getEventType())
                {

                }
            }
        });

        stage.iconifiedProperty().addListener((obs, wasMinimized, isNowMinimized) -> {
            if (isNowMinimized) {
                for (Observar observer:observadores_list)
                {
                    observer.stage_minimizing();
                }
            } else {
                System.out.println("Window was restored");
            }
        });
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


    public class ShortCutManager
    {

        public ShortCutManager()
        {
            z
        }
    }





}