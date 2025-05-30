package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Setter
public class ClipBoardViewer implements Observar {


    private SharedInfo sharedInfo = new SharedInfo();
    private Scene scene;
    private RegistryManager registryManager;

    private ButtonShit buttonShit;
    private ConfigLoader configLoader;
    private MyDialog myDialog;
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
    private TextArea tunca;

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
                String values = registryManager.get_all_values(reg);
                sharedInfo.setCurrent_register(reg);

            }
        });
    }


    private void create_and_add_blocText_to_vbox(String text) {

        BlocText blocText = new BlocText(text);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        sharedInfo.getBlocTextArrayList().add(blocText);

        VBox hBox = new VBox();
        hBox.getChildren().addAll(blocText.getLabel(), separator);

        contentPane.getChildren().add(hBox);
    }

    public void initialize_shit() {
        settingUp_sharedInfo();
        sharedInfo.getStage().setResizable(false);
        configLoader = new ConfigLoader(config.getJSONObject("clipboardViewer_config"),sharedInfo);
        sharedInfo.setConfigLoader(configLoader);

        myDialog = new MyDialog(sharedInfo);
        buttonShit = new ButtonShit(buttons,sharedInfo);
        changeListener_stagePosition();



        addObserver(myDialog);
        addObserver(configLoader);
        myDialog.addObserver(this);
    }

    public void settingUp_sharedInfo()
    {
        sharedInfo.setContentPane(contentPane);
        sharedInfo.setConfigLoader(configLoader);
        sharedInfo.setMainPane(mainPane);
    }

    @Override
    public void text_in_visualize_mode_modified(String newTex) {

        int i = 0;
        for (; i < sharedInfo.getBlocTextArrayList().size(); i++) {
            BlocText blocText = sharedInfo.getBlocTextArrayList().get(i);

            if (blocText.isSelected_tovisualize())
            {
                blocText.getLabel().setText(newTex);
                blocText.setSelected_tovisualize(false);
                break;
            }
        }
        registryManager.changeValue(sharedInfo.getCurrent_register(),i,newTex);

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
                for (Observar observador:observadores_list)
                {
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
                for (Observar observador:observadores_list)
                {
                    observador.stage_was_moved();
                }
            }
        });
    }


    @Override
    public void something_was_copied(String copiedString) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                System.out.println("Copied thing is here in thang " + copiedString);
                create_and_add_blocText_to_vbox(copiedString);
                sharedInfo.getCopyingStrings().set(true);
            }
        });
    }

    @Override
    public void esc_was_pressed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (sharedInfo.getCopyingStrings().get()) {
//                    startRecordingButton.setGraphic(myImages.getImageView(3, 0));
                    sharedInfo.getCopyingStrings().set(false);

                }
            }
        });
    }


    @Setter
    @Getter
    public class BlocText {

        Label label = new Label();
        boolean selected = false;
        boolean selected_tovisualize = false;
        String style = null;
        BlocText blocText = this;

        String selected_style = "-fx-padding: 8; -fx-background-color: #fff8cc; -fx-border-color: #ffd208; -fx-background-radius: 5;";
        String not_selected_style = "-fx-padding: 8; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-background-radius: 5;";


        public BlocText(String text) {
            label.setText(text);
            label.setWrapText(true);
            label.setMaxWidth(configLoader.mainPane_defaultWidth);
            label.setPrefWidth(configLoader.mainPane_defaultWidth);
            label.setMaxHeight(configLoader.getLabel_maxHeight());
            label.setStyle(not_selected_style);

//            label.setEffect(dropShadow);
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {


                    MouseButton mouseButton = mouseEvent.getButton();

                    if (mouseButton == MouseButton.PRIMARY) {

                        if (!selected) {
                            label.setStyle(selected_style);
                            selected = !selected;
                        } else {

                            label.setStyle(not_selected_style);
                            selected = !selected;
                        }
                    } else if (mouseButton == MouseButton.SECONDARY) {
                        selected_tovisualize = true;
                        myDialog.setCurrentBlocktext(blocText);
                        myDialog.setDialog_showing(true);
                    }
                }
            });
        }

        public void set_selected(boolean selected) {
            if (selected) {
                label.setStyle(selected_style);
            } else {
                label.setStyle(not_selected_style);
                System.out.println("Deselecting");
            }
            this.selected = selected;

        }


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
    public void addObserver(Observar observador)
    {
        observadores_list.add(observador);
    }
    public void setStage(Stage stage)
    {
        sharedInfo.setStage(stage);
    }
    public void setAcciones(Acciones acciones)
    {
        sharedInfo.setAcciones(acciones);
    }





}