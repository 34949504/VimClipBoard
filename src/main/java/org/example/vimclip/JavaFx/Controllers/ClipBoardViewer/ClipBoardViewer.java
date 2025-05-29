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

    private Stage stage;
    private Scene scene;
    private RegistryManager registryManager;
    private Acciones acciones;

    private Timeline timeline = new Timeline();
    private MyImages myImages = new MyImages();
    private MyButtonFuncs myButtonFuncs = new MyButtonFuncs();
    private Button clickedButton;
    private int index_clickedButton;

    private Character current_register;
    AtomicBoolean copyingStrings = new AtomicBoolean(false);

    private ArrayList<Observar> observadores_list = new ArrayList<>();

    private ArrayList<BlocText> blocTextArrayList = new ArrayList<>();

    private ArrayList<HBox> label_array = new ArrayList<>();

    boolean allSelected = false;


    int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

    private JSONObject config;
    private ConfigLoader configLoader;
    private MyDialog myDialog;

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
        setting_up_timeline();
        inicializar_botones();
        tabsPane_listener();
        registryTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        current_register = registryTabPane.getTabs().get(0).getId().charAt(0);


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

    private void tabsPane_listener() {

        registryTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {

                System.out.println("has changed");
                Character reg = t1.getId().charAt(0);
                String values = registryManager.get_all_values(reg);
                current_register = reg;

            }
        });
    }

    private void setting_up_timeline() {

        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), actionEvent -> {

            clickedButton.setGraphic(myImages.getImageView(index_clickedButton, 0));


        }));
    }

    private void create_and_add_blocText_to_vbox(String text) {

        BlocText blocText = new BlocText(text);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        blocTextArrayList.add(blocText);

        VBox hBox = new VBox();
        hBox.getChildren().addAll(blocText.getLabel(), separator);

        contentPane.getChildren().add(hBox);
//        contentPane.getChildren().add(blocText.getLabel());
//        contentPane.getChildren().add(separator);
    }

    public void initialize_shit() {

        stage.setResizable(false);
        configLoader = new ConfigLoader(config.getJSONObject("clipboardViewer_config"));
        myDialog = new MyDialog();
        changeListener_stagePosition();

//        create_and_add_blocText_to_vbox("prueba");

        addObserver(myDialog);
        addObserver(configLoader);
        myDialog.addObserver(this);


    }

    @Override
    public void text_in_visualize_mode_modified(String newTex) {

        int i = 0;
        for (; i < blocTextArrayList.size(); i++) {
            BlocText blocText = blocTextArrayList.get(i);

            if (blocText.isSelected_tovisualize())
            {
                blocText.getLabel().setText(newTex);
                blocText.setSelected_tovisualize(false);
                break;
            }
        }
        registryManager.changeValue(current_register,i,newTex);

    }

    private void changeListener_stagePosition() {

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        stage.xProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double maxX = screenWidth - stage.getWidth();
                if (t1.doubleValue() > maxX) {
                    stage.setX(maxX);
                } else if (t1.doubleValue() < 0) {
                    stage.setX(0);
                }
                for (Observar observador:observadores_list)
                {
                    observador.stage_was_moved();
                }
                System.out.println("Numbers " + number + " " + t1);
            }
        });
        System.out.println("wtfaoee" + myDialog.getHeight() + "lefromgaae height is ");


        stage.yProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                double maxY = screenHeight - stage.getHeight();
                if (t1.doubleValue() > maxY) {
                    stage.setY(maxY);
                } else if (t1.doubleValue() < 0) {
                    stage.setY(0);
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
                copyingStrings.set(true);
            }
        });
    }

    @Override
    public void esc_was_pressed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (copyingStrings.get()) {
                    startRecordingButton.setGraphic(myImages.getImageView(3, 0));
                    copyingStrings.set(false);

                }
            }
        });
    }

    private void inicializar_botones() {
        inicialiar_boton(gearButton, 0, true);
        inicialiar_boton(trashButton, 1, true);
        inicialiar_boton(copyButton, 2, true);
        inicialiar_boton(startRecordingButton, 3, false);
        inicialiar_boton(selectAll, 4, true);
        inicialiar_boton(expand, 5, true);
        inicialiar_boton(switchEdge, 6, true);

    }

    private void inicialiar_boton(Button button, int array_index, boolean quickanimation) {


        ImageView imageView = myImages.getImageView(array_index, 0);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;"); // Optional styling
        button.setOnAction(e -> {
            System.out.println("Button clicked!");
        });

        if (quickanimation) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    ImageView secondImage = myImages.getImageView(array_index, 1);
                    index_clickedButton = array_index;
                    clickedButton = button;
                    button.setGraphic(secondImage);
                    timeline.play();
                    myButtonFuncs.funcs.get(array_index).run();
                    System.out.println("was pressed");
                }
            });
        } else {
            ButtonInfo buttonInfo = new ButtonInfo();
            buttonInfo.image_int = 0;


            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    buttonInfo.image_int = buttonInfo.image_int == 0 ? 1 : 0;
                    button.setGraphic(myImages.getImageView(array_index, buttonInfo.image_int));
                    myButtonFuncs.funcs.get(array_index).run();
                }
            });

        }
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

    public class MyImages {

        ImageView[] gearImages = new ImageView[2];
        ImageView[] trashImages = new ImageView[2];
        ImageView[] copyImages = new ImageView[2];
        ImageView[] startCopyingImages = new ImageView[2];
        ImageView[] selectAllImages = new ImageView[2];
        ImageView[] expandImages = new ImageView[2];
        ImageView[] switchEdge = new ImageView[2];


        // 2D array holding ImageViews
        ImageView[][] iconImages = new ImageView[][]{
                gearImages,
                trashImages,
                copyImages,
                startCopyingImages,
                selectAllImages,
                expandImages,
                switchEdge

        };

        public MyImages() {
            loadImages(
                    0,
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\gear.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\gearPressed.png"
            );
            loadImages(1,
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\trash.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\trashPressed.png"
            );

            loadImages(2,
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\copy.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\copyPressed.png"
            );

            loadImages(3,
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\startRecording.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\startRecordingPressed.png");

            loadImages(4,
                    "C:\\\\Users\\\\gerar\\\\IdeaProjects\\\\VimClip\\\\src\\\\main\\\\resources\\\\assets\\\\images\\\\selectAll.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\selectAllPressed.png");

            loadImages(5,
                    "C:\\\\Users\\\\gerar\\\\IdeaProjects\\\\VimClip\\\\src\\\\main\\\\resources\\\\assets\\\\images\\expand.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\expandPressed.png");
            loadImages(6,
                    "C:\\\\Users\\\\gerar\\\\IdeaProjects\\\\VimClip\\\\src\\\\main\\\\resources\\\\assets\\\\images\\switchEdge.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\switchEdgePressed.png");
        }

        private void loadImages(int index, String... paths) {
            for (int i = 0; i < paths.length; i++) {
                Image image = new Image("file:" + paths[i]); // prefix "file:" for absolute paths
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); // Optional: scale the image
                imageView.setFitHeight(40);
                iconImages[index][i] = imageView;

            }
        }

        public ImageView getImageView(int arrayIndex, int imagePos) {
            return iconImages[arrayIndex][imagePos];
        }
    }

    public class MyButtonFuncs {

        List<Runnable> funcs = Arrays.asList(
                () -> gear(),
                () -> trashCan(),
                () -> copyHand(),
                () -> startCopying(),
                () -> selectAll(),
                () -> expand(),
                () -> switchEdge()
        );

        boolean expanded = false;
        int stage_previousX;
        int stage_previousY;


        public void trashCan() {
            ArrayList<Integer> selected_labels = get_selected_labels();
            int status = acciones.doCommand4(current_register, selected_labels, "remove_values");
            System.out.println("Trashcan");
            if (status == 0) //upadte
            {
                for (int i = 0; i < selected_labels.size(); i++) {
                    int index = selected_labels.get(i);
                    contentPane.getChildren().remove(index);
                    blocTextArrayList.remove(index);
                }
            }
        }

        public void gear() {

            System.out.println("Gear");
        }

        public void copyHand() {
            ArrayList<Integer> selected_labels = get_selected_labels();
            int status = acciones.doCommand4(current_register, selected_labels, "get_values");
            System.out.println("Copyhand ");

            if (status == 0) {
                deselect_labels(selected_labels);
            }
        }

        public void startCopying() {
            //Button was pressed when copyingStrings was active
            if (copyingStrings.get()) {

                for (Observar observador : observadores_list) {
                    observador.esc_was_pressed();
                }
                copyingStrings.set(false);
            } else {

                acciones.doCommand(current_register, "clipboard_listener");
                copyingStrings.set(true);
            }
        }

        public void selectAll() {
            if (blocTextArrayList.size() <= 0) {
                System.out.println("No se puede seleccionar porque no hay nada");
                return;
            }
            boolean value = allSelected ? false : true;
            for (int i = 0; i < blocTextArrayList.size(); i++) {
                BlocText blocText = blocTextArrayList.get(i);
                if (blocText.selected == !value) {
                    blocText.set_selected(value);
                }
            }
            allSelected = !allSelected;
            System.out.println("Selecting all");
        }

        public void switchEdge()
        {
            System.out.println("switching edge");
            String[] edges = {"topRight_edge","topLeft_edge","bottomLeft_edge","bottomRight_edge",};
            String currentEdge = configLoader.currentEdge;

            int i  =0;
            for (; i < 4 ; i++) {

                String e =  edges[i];
                if (currentEdge.compareTo(e) == 0)
                {
                    break;
                }
            }
            i +=1;
            if (i >= 4)
                i = 0;

            System.out.println("choosen edge is "+edges[i]);
            configLoader.setCurrentEdge(edges[i]);
            configLoader.position_stage_atEgde(edges[i]);

            stage.setX(configLoader.stage_defaultX);
            stage.setY(configLoader.stage_defaultY);




        }

        public void expand() {

            if (expanded) // its big so now shrink
            {
                stage.setY(stage_previousY);
                stage.setHeight(configLoader.mainPane_defaultHeight);

            }else
            {
                stage_previousX = (int)stage.getX();
                stage_previousY = (int)stage.getY();
                stage.setY(0);
                stage.setHeight(screenHeight);
            }
            expanded = !expanded;


            System.out.println("Expanding");
        }
    }

    public class ButtonInfo {

        public Button button;
        public int image_int;
    }

    public void addObserver(Observar observer) {
        observadores_list.add(observer);
    }

    public ArrayList<Integer> get_selected_labels() {
        ArrayList<Integer> index_arrray = new ArrayList<>();
        for (int i = 0; i < blocTextArrayList.size(); i++) {

            BlocText blocText = blocTextArrayList.get(i);

            if (blocText.isSelected()) {
                index_arrray.add(i);
            }
        }
        return index_arrray;
    }

    public void deselect_labels(ArrayList<Integer> array_list) {
        System.out.println("Deselection");

        for (int i = 0; i < array_list.size(); i++) {

            int index = array_list.get(i);
            BlocText blocText = blocTextArrayList.get(index);

            System.out.println("index is " + i);
            blocText.set_selected(false);

        }
    }

    public class MyTooltip extends Tooltip {
        public static Font font = Font.loadFont("file:C:/Users/gerar/IdeaProjects/VimClip/src/main/resources/assets/BarberChop.otf", 14);

        public MyTooltip(String message) {
            this.setText(message);
            this.setFont(font);
        }
    }

    @Getter
    public class ConfigLoader implements Observar {
        JSONObject config;
        int mainPane_defaultHeight;
        int mainPane_defaultWidth;
        int label_maxHeight;

        int stage_currentWidth;
        int stage_currentHeight;

        int stage_defaultX; // if user wants to go back to default
        int stage_defaultY;

        int stage_currentX;
        int stage_currentY;

        String currentEdge;


        public ConfigLoader(JSONObject config) {
            this.config = config;
            initialize_mainPaine_dims();
            initialize_stage_position();

            label_maxHeight = config.getInt("label_max_height");

        }

        private void initialize_mainPaine_dims() {
            Rectangle2D rectangle2D = Screen.getPrimary().getBounds();
            int width = (int) rectangle2D.getWidth();
            int height = (int) rectangle2D.getHeight();
            int height_percent = config.getInt("mainPane_height_percent");
            int width_percent = config.getInt("mainPane_width_percent");

            mainPane_defaultHeight = height * height_percent / 100;
            mainPane_defaultWidth = width * width_percent / 100;

            mainPane.setPrefHeight(mainPane_defaultHeight);
            mainPane.setPrefWidth(mainPane_defaultWidth);

        }
        private void initialize_stage_position()
        {
            calculating_default_postition();
            stage.setX(stage_defaultX);
            stage.setY(stage_defaultY);

            stage_currentX = stage_defaultX;
            stage_currentY = stage_defaultY;
        }

        private void calculating_default_postition()
        {
            /**
             * topRight_edge
             * topLeft_edge
             * bottomRight_edge
             * bottomLeft_edge
             */
            currentEdge = config.getString("stage_edge_postition");
            position_stage_atEgde(currentEdge);



        }
        public void position_stage_atEgde(String edge)
        {

            if (edge.equals("topRight_edge")) {
                stage_defaultX = screenWidth - mainPane_defaultWidth;
                stage_defaultY = 0;
            } else if (edge.equals("topLeft_edge")) {
                stage_defaultX = 0;
                stage_defaultY = 0;
            } else if (edge.equals("bottomRight_edge")) {
                stage_defaultX = screenWidth - mainPane_defaultWidth;
                stage_defaultY = screenHeight - mainPane_defaultHeight;
            } else if (edge.equals("bottomLeft_edge")) {
                stage_defaultX = 0;
                stage_defaultY = screenHeight - mainPane_defaultHeight;
            } else {
                // Fallback (optional)
                stage_defaultX = (screenWidth - mainPane_defaultWidth) / 2;
                stage_defaultY = (screenHeight - mainPane_defaultHeight) / 2;
            }
        }

        @Override
        public void stage_was_moved() {

            stage_currentX = (int)stage.getWidth();
            stage_currentY = (int)stage.getHeight();
        }

        public void setCurrentEdge(String currentEdge) {
            this.currentEdge = currentEdge;
        }
    }


    public class MyDialog extends Dialog implements Observar {
        ArrayList<Observar> observers_list = new ArrayList<>();

        DialogPane dialogPane = this.getDialogPane();
        BorderPane borderPane = new BorderPane();
        Window window = this.getDialogPane().getScene().getWindow();
        BlocText currentBlocktext;

        boolean dialog_showing = false;

        int currentHeight;
        int currentWidth;

        int x;
        int y;

        private Button close_button = new Button();
        private TextArea textArea = new TextArea();

        public MyDialog() {
            settingDims();
            settingUp_dialogLayout();
            dialogPane.setContent(borderPane);
            initModality(Modality.WINDOW_MODAL);
            initStyle(StageStyle.UNDECORATED);


            if (window instanceof Stage) {
                ((Stage) window).setAlwaysOnTop(true);
            }

        }


        public void show_dialog() {

            calculating_where_dialog_should_appear();
            move_dialog();
            if (dialog_showing) {
                setting_textArea_text();
                this.show();
            }
            else {

            }
        }
        public void setting_textArea_text()
        {
           textArea.setText(currentBlocktext.getLabel().getText());
        }

        public void move_dialog()
        {

            this.setOnShown(e -> {
                Window window = this.getDialogPane().getScene().getWindow();
                window.setX(x);
                window.setY(y);

            });
        }

        private void settingDims() {
            dialogPane.setPrefWidth(configLoader.mainPane_defaultWidth);
            dialogPane.setPrefHeight(configLoader.mainPane_defaultHeight);
            setResizable(true);

            currentHeight = configLoader.mainPane_defaultHeight;
            currentWidth = configLoader.stage_currentWidth;
        }

        private void calculating_where_dialog_should_appear() {
            //priorotize appearing on bottom
            int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
            int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

            int available_space_below = screenHeight - (int) (stage.getY() + stage.getHeight());
            int available_space_above = (int)stage.getY();

            System.out.println("Available space " + available_space_below);
            System.out.println("dialog height is " + getHeight());
            if (available_space_below > currentHeight) {
                Bounds bounds = stage.getScene().getRoot().localToScreen(stage.getScene().getRoot().getBoundsInLocal());


                x =(int) bounds.getMinX();
                y = (int)bounds.getMaxY(); // directly below content
            }
            else if (available_space_above >currentHeight)
            {
                Bounds bounds = stage.getScene().getRoot().localToScreen(stage.getScene().getRoot().getBoundsInLocal());

                x = (int) bounds.getMinX();
                y = (int) (bounds.getMinY() - currentHeight);  // directly above the stage content

            }


        }

        private void settingUp_dialogLayout()
        {

            settingUp_closeButton();
            borderPane.setTop(close_button);
            borderPane.setCenter(textArea);
        }
        private void settingUp_closeButton()
        {

            Image image = new Image("file:" + "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\close.png" ); // prefix "file:" for absolute paths
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(40); // Optional: scale the image
            imageView.setFitHeight(20);
            close_button.setGraphic(imageView);
            close_button.setStyle("-fx-background-color: transparent;"); // Optional styling

            close_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    for (Observar observer:observers_list)
                    {
                        observer.text_in_visualize_mode_modified(textArea.getText());
                    }
                    setDialog_showing(false);
                }
            });
        }
        private void settingUp_textArea()
        {

        }

        public  void setDialog_showing(boolean showing)
        {
           dialog_showing = showing;
           if (dialog_showing) {
               show_dialog();
           }else {
               System.out.println("closing dialog");

              window.hide();
           }
        }

        @Override
        public void stage_was_moved() {
            move_dialog();
            if (dialog_showing)
                show_dialog();
        }

        public void setCurrentBlocktext(BlocText currentBlocktext) {
            this.currentBlocktext = currentBlocktext;

        }

        public void addObserver(Observar observer)
        {
            observers_list.add(observer);
        }
    }


}