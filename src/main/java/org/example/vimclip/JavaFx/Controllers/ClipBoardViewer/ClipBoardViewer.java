package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.*;
import lombok.Setter;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.AppContext;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs.ConfigurationDialog;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs.HelpDialog;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs.MyDialog;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs.SepDialog;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.example.vimclip.Utils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Setter
public class ClipBoardViewer implements Observar {

    ClipBoardViewer clipBoardViewer = this;


    Stage stage;
    Acciones acciones;
    private SharedInfo sharedInfo = new SharedInfo();
    private RegistryManager registryManager;

    private MainButtons buttonShit;
    private ConfigLoader configLoader;
    private MyDialog myDialog;
    private Instance_manager instanceManager;
    private SepDialog sepDialog;
    private Scroller scroller;
    private HelpDialog helpDialog;
    private ConfigurationDialog configurationDialog;
    private VisualCues visualCues;

    private ArrayList<Observar> observadores_list = new ArrayList<>();
    private JSONObject config;
    private ConfigMaster configMaster;
    private ConfigMaster.ClipboardViewer_config clipboardViewer_config;

    private Observer_initializer observerInitializer = new Observer_initializer();

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
    @FXML private Button shortcut_button;
    @FXML private Button hide_app;
    @FXML private Button help;

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
                copy_and_remove,
                shortcut_button,
                hide_app,
                help
        ));


        disablingTraversingTabs();
        tabsPane_listener();
        registryTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        sharedInfo.setCurrent_register(registryTabPane.getTabs().get(0).getId().charAt(0));


    }

    private void stage_hidden_listener()
    {
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.out.println("was hidden");
            }
        });
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

    @Override
    public void appShortcut_beenPressed(String shortcut) {

        System.out.println("Shortcut suckers here is "+shortcut);
    }

    public void initialize_shit() {

        initialize_classes(); // Starts constructors
        settingUp_sharedInfo(); // The order matter
        initialize_classes_init(); // calls init

        changeListener_stagePosition();
        stage_hidden_listener();
        stage_window_listener();

        observerInitializer.initialize_observers();
        stage.setWidth(clipboardViewer_config.getStage_width());
        stage.setHeight(clipboardViewer_config.getStage_height());


    }


    public void initialize_classes()
    {
        clipboardViewer_config = configMaster.getClipboardViewer_config();
        configLoader = new ConfigLoader();
        myDialog = new MyDialog();
        buttonShit = new MainButtons();
        instanceManager = new Instance_manager();
        sepDialog = new SepDialog();
        scroller = new Scroller();
        helpDialog= new HelpDialog();
        configurationDialog = new ConfigurationDialog();
        visualCues = new VisualCues();

    }
    public void initialize_classes_init()
    {

        clipboardViewer_config.init();
        configLoader.init(config.getJSONObject("clipboardViewer_config"),sharedInfo);
        myDialog.init(sharedInfo,configMaster);
        buttonShit.init(buttons,sharedInfo);
        instanceManager.init(sharedInfo);
        sepDialog.init(sharedInfo,configMaster);
        scroller.init(sharedInfo);
        helpDialog.init(sharedInfo,configMaster);
        configurationDialog.init(sharedInfo,configMaster);
        visualCues.init(sharedInfo,configMaster);
    }

    public void settingUp_sharedInfo() {
        sharedInfo.setContentPane(contentPane);
        sharedInfo.setConfigLoader(configLoader);
        sharedInfo.setMainPane(mainPane);
        sharedInfo.setStage(stage);
        sharedInfo.setAcciones(acciones);
        sharedInfo.setRegistryManager(registryManager);
        sharedInfo.setScrollPane_blocs(scroll);
        sharedInfo.setConfigLoader(configLoader);
        sharedInfo.setMyDialog(myDialog);
        sharedInfo.initialize_available_instances(configLoader, myDialog);
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

        if (vBox.getChildren().isEmpty()) {
            vBox.getChildren().addAll(blocText.getLabel(), separator);
//            blocText.addObserver(scroller);
        }



        blocText.getLabel().setText(copiedString);
        separator.setOrientation(Orientation.HORIZONTAL);

        sharedInfo.getCurrentWholePackageArray().add(wholePackage);

        contentPane.getChildren().add(vBox);

        for (Observar observar:observadores_list)
        {
            observar.bloc_was_created();
        }
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
        expand.setTooltip(new MyTooltip("Alt + h"));
        gearButton.setTooltip(new MyTooltip("Alt + g"));
        trashButton.setTooltip(new MyTooltip(" Alt + d"));
        copyButton.setTooltip(new MyTooltip("Alt + c"));
        startRecordingButton.setTooltip(new MyTooltip("Alt + s"));
        selectAll.setTooltip(new MyTooltip("Alt + a"));
        switchEdge.setTooltip(new MyTooltip("Alt + e"));
        separator.setTooltip(new MyTooltip("Alt + ,"));
        copy_and_remove.setTooltip(new MyTooltip("Alt + x"));
        shortcut_button.setTooltip(new MyTooltip("Alt + Alt + t"));
        hide_app.setTooltip(new MyTooltip("Alt + v"));
        help.setTooltip(new MyTooltip("Alt + q"));
    }

    public class MyTooltip extends Tooltip {


        public static InputStream inputStream = Utils.getInputStream("/assets/BarberChop.otf");
        public static Font font = Font.loadFont(inputStream, 14);

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


    public class ShortcutManager
    {
        private HashMap<String,Runnable> shortcuts_hash = new HashMap<>();

        public ShortcutManager()
        {

        }

        private void inicializar_shortcuts()
        {

        }



    }


    private  void disablingTraversingTabs()
    {

        registryTabPane.setFocusTraversable(false);
//        a.setDisable(true);
//        s.setDisable(true);
//        d.setDisable(true);
//        f.setDisable(true);
    }


    private class Observer_initializer
    {


        public void initialize_observers()
        {
           initialize_ClipBoardViewerObservers();
           initialize_MyDialog();
            initialize_MainButtons();
            initialize_appContextClasses();
            initialize_blockText();

        }

        private void initialize_ClipBoardViewerObservers()
        {

            addObserver(scroller);
            addObserver(myDialog);
            addObserver(configLoader);
            addObserver(instanceManager);
            addObserver(acciones.getClipBoardListener());
            addObserver(AppContext.acciones.getListenToClipboard());
            addObserver(AppContext.configMaster);
            addObserver(helpDialog);
            addObserver(configMaster);


            //Dialogs observers
            addObserver(configurationDialog.getDialogSimilarFuncs().getObservont());
            addObserver(sepDialog.getDialogSimilarFuncs().getObservont());
            addObserver(myDialog.getDialogSimilarFuncs().getObservont());
        }
        private void initialize_MyDialog()
        {
            myDialog.addObserver(clipBoardViewer);
        }

        private void initialize_MainButtons()
        {

            buttonShit.setObservadores_list(new ArrayList<>(observadores_list));
            buttonShit.addObserver(sepDialog);
            buttonShit.addObserver(configurationDialog);
        }

        private void initialize_appContextClasses()
        {

            AppContext.keyPressed.addObserver(buttonShit);
            AppContext.acciones.getClipBoardListener().addObserver(clipBoardViewer);
            AppContext.acciones.getClipBoardListener().addObserver(visualCues);
        }

        private void initialize_blockText()
        {
            BlocText.addObserver(myDialog);
            BlocText.addObserver(scroller);
        }

    }


}