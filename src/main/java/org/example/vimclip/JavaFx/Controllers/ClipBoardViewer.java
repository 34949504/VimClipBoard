package org.example.vimclip.JavaFx.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.jar.JarEntry;

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
    public void initialize() throws IOException {
        setting_tooltips();
        setting_up_timeline();
        inicializar_botones();
        tabsPane_listener();
        registryTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        current_register = registryTabPane.getTabs().get(0).getId().charAt(0);

    }

    private void setting_tooltips()
    {
        expand.setTooltip(new MyTooltip("Expands application vertically"));
        gearButton.setTooltip(new MyTooltip("Configuration"));
        trashButton.setTooltip(new MyTooltip("Deletes selected blocks"));
        copyButton.setTooltip(new MyTooltip("Copies to your normal clipboard the selected blocks"));
        startRecordingButton.setTooltip(new MyTooltip("Saves everything that you copy when its on in the current tab"));
        selectAll.setTooltip(new MyTooltip("Selects all blocks/Deselects all blocks"));
    }

    private void tabsPane_listener()
    {

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

    private void setting_up_timeline()
    {

        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), actionEvent -> {

            clickedButton.setGraphic(myImages.getImageView(index_clickedButton,0));


        }));
    }

    private void create_and_add_blocText_to_vbox(String text) {

        BlocText blocText = new BlocText(text);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        blocTextArrayList.add(blocText);

        VBox hBox = new VBox();
        hBox.getChildren().addAll(blocText.getLabel(),separator);

        contentPane.getChildren().add(hBox);
//        contentPane.getChildren().add(blocText.getLabel());
//        contentPane.getChildren().add(separator);
    }

    public void initialize_shit() {

        stage.setResizable(false);
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

                if (copyingStrings.get())
                {
                    startRecordingButton.setGraphic(myImages.getImageView(3,0));
                    copyingStrings.set(false);

                }
            }
        });
    }

    private void inicializar_botones()
    {
        inicialiar_boton(gearButton,0,true);
        inicialiar_boton(trashButton,1,true);
        inicialiar_boton(copyButton,2,true);
        inicialiar_boton(startRecordingButton,3,false);
        inicialiar_boton(selectAll,4,true);
        inicialiar_boton(expand,5,true);

    }

    private void inicialiar_boton(Button button,int array_index, boolean quickanimation) {


        ImageView imageView = myImages.getImageView(array_index,0);
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
        }
        else {
            ButtonInfo buttonInfo = new ButtonInfo();
            buttonInfo.image_int = 0;


            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    buttonInfo.image_int =  buttonInfo.image_int == 0 ? 1 : 0;
                    button.setGraphic(myImages.getImageView(array_index,buttonInfo.image_int));
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
        String style = null;

        String selected_style  = "-fx-padding: 8; -fx-background-color: #fff8cc; -fx-border-color: #ffd208; -fx-background-radius: 5;";
        String not_selected_style  = "-fx-padding: 8; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-background-radius: 5;";


        public BlocText(String text) {
            label.setText(text);
            label.setWrapText(true);
            label.setMaxWidth(300);
            label.setPrefWidth(300);
            System.out.println(contentPane.getWidth());
            System.out.println(mainPane.getWidth());
            label.setStyle(not_selected_style);

//            label.setEffect(dropShadow);
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (!selected) {
                        label.setStyle(selected_style);
                        selected = !selected;
                    } else {

                        label.setStyle(not_selected_style);
                        selected = !selected;
                    }
                }
            });
        }
        public void set_selected(boolean selected)
        {
            if (selected)
            {
                label.setStyle(selected_style);
            }
            else {
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



        // 2D array holding ImageViews
        ImageView[][] iconImages = new ImageView[][] {
                gearImages,
                trashImages,
                copyImages,
                startCopyingImages,
                selectAllImages,
                expandImages

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
                   "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\selectAllPressed.png" );

            loadImages(5,
                    "C:\\\\Users\\\\gerar\\\\IdeaProjects\\\\VimClip\\\\src\\\\main\\\\resources\\\\assets\\\\images\\expand.png",
                    "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\expandPressed.png" );
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
                () -> expand()
        );


        public void trashCan()
        {
            ArrayList<Integer> selected_labels = get_selected_labels();
            int status =acciones.doCommand4(current_register,selected_labels,"remove_values");
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
        public void gear()
        {

            System.out.println("Gear");
        }
        public void copyHand()
        {
            ArrayList<Integer> selected_labels = get_selected_labels();
            int status =acciones.doCommand4(current_register,selected_labels,"get_values");
            System.out.println("Copyhand ");

            if (status == 0)
            {
                deselect_labels(selected_labels);
            }
        }

        public void startCopying()
        {
            //Button was pressed when copyingStrings was active
            if (copyingStrings.get())
            {

                for (Observar observador:observadores_list){
                    observador.esc_was_pressed();
                }
                copyingStrings.set(false);
            }
            else {

                acciones.doCommand(current_register,"clipboard_listener");
                copyingStrings.set(true);
            }
        }
        public void selectAll()
        {
            if (blocTextArrayList.size() <=0)
            {
                System.out.println("No se puede seleccionar porque no hay nada");
                return;
            }
            boolean value = allSelected ? false:true;
            for (int i = 0; i < blocTextArrayList.size(); i++) {
                BlocText blocText = blocTextArrayList.get(i);
                if (blocText.selected == !value)
                {
                    blocText.set_selected(value);
                }
            }
            allSelected = !allSelected;
            System.out.println("Selecting all");
        }
        public void expand()
        {
            System.out.println("Expanding");
        }
    }

    public class ButtonInfo {

        public Button button;
        public int image_int;
    }

    public void addObserver(Observar observer){
        observadores_list.add(observer);
    }

    public ArrayList<Integer> get_selected_labels()
    {
        ArrayList<Integer> index_arrray = new ArrayList<>();
        for (int i = 0; i < blocTextArrayList.size(); i++) {

            BlocText blocText = blocTextArrayList.get(i);

            if (blocText.isSelected())
            {
                index_arrray.add(i);
            }
        }
        return index_arrray;
    }

    public void deselect_labels(ArrayList<Integer> array_list)
    {
        System.out.println("Deselection");

        for (int i = 0; i < array_list.size(); i++) {

            int index = array_list.get(i);
            BlocText blocText = blocTextArrayList.get(index);

            System.out.println("index is "+i);
            blocText.set_selected(false);

        }
    }

    public class MyTooltip extends Tooltip
    {
        public static Font font = Font.loadFont("file:C:/Users/gerar/IdeaProjects/VimClip/src/main/resources/assets/BarberChop.otf", 14);
        public MyTooltip(String message)
        {
           this.setText(message);
           this.setFont(font);
        }
    }


}