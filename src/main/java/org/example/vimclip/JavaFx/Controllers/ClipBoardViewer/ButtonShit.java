package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.example.vimclip.Observar;

import java.util.ArrayList;
import java.util.HashMap;


//What i have to do. Assign to the buttons their corresponding actions and images

public class ButtonShit implements Observar {

    private HashMap<String, ButtonInfo> buttons = new HashMap<>();

    private MyImages myImages = new MyImages();
   private  MyButtonFuncs myButtonFuncs = new MyButtonFuncs();

   private SharedInfo sharedInfo;

    int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();
    private ArrayList<Observar> observadores_list = new ArrayList<>();

    public ButtonShit(ArrayList<Button> buttons_array,
                      SharedInfo sharedInfo) {

        this.sharedInfo = sharedInfo;
        addObserver(sharedInfo.getAcciones().getClipBoardListener());
        creatingButtonInfo();
        inicializando_botones(buttons_array);

    }


    private void creatingButtonInfo()
    {


        buttons.put("expand", new ButtonInfo(
                myImages.imageViewConstructor("expand.png", "expandPressed.png"),
                () -> myButtonFuncs.expand()
        ));

        buttons.put("trashButton", new ButtonInfo(
                myImages.imageViewConstructor("trash.png", "trashPressed.png"),
                () -> myButtonFuncs.trashCan()
        ));

        buttons.put("copyButton", new ButtonInfo(
                myImages.imageViewConstructor("copy.png", "copyPressed.png"),
                () -> myButtonFuncs.copyHand()
        ));

        buttons.put("selectAll", new ButtonInfo(
                myImages.imageViewConstructor("selectAll.png", "selectAllPressed.png"),
                () -> myButtonFuncs.selectAll()
        ));

        buttons.put("startRecordingButton", new ButtonInfo(
                myImages.imageViewConstructor("startRecording.png", "startRecordingPressed.png"),
                () -> myButtonFuncs.startCopying(),false
        ));

        buttons.put("gearButton", new ButtonInfo(
                myImages.imageViewConstructor("gear.png", "gearPressed.png"),
                () -> myButtonFuncs.gear()
        ));

        buttons.put("switchEdge", new ButtonInfo(
                myImages.imageViewConstructor("switchEdge.png", "switchEdgePressed.png"),
                () -> myButtonFuncs.switchEdge()
        ));

    }

    private void inicializando_botones(ArrayList<Button> buttons) {
        for (Button button : buttons) {
            inicialiar_boton(button);
        }
    }


    private void inicialiar_boton(Button button) {



        ButtonInfo buttonInfo =buttons.get(button.getId());
        ImageView[] imageViews = buttonInfo.imageViews;
        ImageView first_image = imageViews[0];
        ImageView second_image = imageViews[1];
        Timeline timeline = new Timeline();
        buttonInfo.button = button;

        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), actionEvent -> {

            button.setGraphic(first_image);


        }));
        button.setGraphic(first_image);
        button.setStyle("-fx-background-color: transparent;"); // Optional styling
        button.setOnAction(e -> {
            System.out.println("Button clicked!");
        });

        if (buttonInfo.quickanimation) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    button.setGraphic(second_image);
                    timeline.play();
                    buttonInfo.func.run();
                    System.out.println("was pressed");
                }
            });
        } else {


            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    buttonInfo.toogle_image = buttonInfo.toogle_image == 0 ? 1 : 0;
                    button.setGraphic(imageViews[buttonInfo.toogle_image]);
                    buttonInfo.func.run();
                }
            });

        }
    }


    public class MyImages {

        private String path = "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\";

        public String[] string_creator(String... names) {
            String[] strings = new String[names.length];
            int i = 0;
            for (String str : names) {
                String new_string = String.format("%s%s", path, str);
                strings[i++] = new_string;
            }
            return strings;
        }
        public ImageView[] imageViewConstructor(String... names)
        {
            String[] paths = string_creator(names);
            ImageView[] imageViews = new ImageView[names.length];

            for (int i = 0; i < paths.length; i++) {
                Image image = new Image("file:" + paths[i]); // prefix "file:" for absolute paths
                System.out.println("path is" + String.format("file:%s",paths[i]));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(40); // Optional: scale the image
                imageView.setFitHeight(40);
                imageViews[i] = imageView;

            }
            return imageViews;

        }

        }

    public class MyButtonFuncs {


        boolean allSelected = false;

        public MyButtonFuncs() {

        }

        boolean expanded = false;
        int stage_previousX;
        int stage_previousY;


        public void trashCan() {
            ArrayList<Integer> selected_labels = get_selected_labels();
            int status = sharedInfo.getAcciones().doCommand4(sharedInfo.getCurrent_register(), selected_labels, "remove_values");

            //SIDE EFFECTA, it sorts the selected labels to 6 5 4 3 2 etc
            System.out.println("Trashcan");
            if (status == 0) //upadte, ite means that values have been deleted from the register
            {

                for (Observar observador:observadores_list)
                {
                    observador.blocs_were_deleted(selected_labels);
                }

//                for (int i = 0; i < selected_labels.size(); i++) {
//                    int index = selected_labels.get(i);
//                    sharedInfo.getContentPane().getChildren().remove(index);
//                    sharedInfo.getCurrentWholePackageArray().remove(index);
//                }
            }
        }

        public void gear() {

            System.out.println("Gear");
        }

        public void copyHand() {


            ArrayList<Integer> selected_labels = get_selected_labels();

            int status = sharedInfo.getAcciones().doCommand4(sharedInfo.getCurrent_register(), selected_labels, "get_values");
            System.out.println("Copyhand ");



            if (status == 0) {
                deselect_labels(selected_labels);
                allSelected = false;
            }
        }

        /**
         * Disable le button quand l'autre boutton est active, the one
         * that actividates copying
         * @param flag
         */
        private void prohecing_disable_hand(boolean flag)
        {
            ButtonInfo buttonInfo = buttons.get("copyButton");
            Button button = buttonInfo.button;
            button.setDisable(flag);
        }

        public void startCopying() {
            //Button was pressed when copyingStrings was active
            if (sharedInfo.getCopyingStrings().get()) {

                for (Observar observador : observadores_list) {
                    observador.esc_was_pressed();
                }
                sharedInfo.getCopyingStrings().set(false);
                prohecing_disable_hand(false);
            } else {

                sharedInfo.getAcciones().doCommand(sharedInfo.getCurrent_register(), "clipboard_listener");
                sharedInfo.getCopyingStrings().set(true);
                prohecing_disable_hand(true);
            }
        }

        public void selectAll() {
            if (sharedInfo.getCurrentWholePackageArray().size() <= 0) {
                System.out.println("No se puede seleccionar porque no hay nada");
                return;
            }
            boolean value = !allSelected;
            for (int i = 0; i < sharedInfo.getCurrentWholePackageArray().size(); i++) {

                System.out.println("value es "+value);
                SharedInfo.WholePackage wholePackage = sharedInfo.getCurrentWholePackageArray().get(i);
                BlocText blocText = wholePackage.getBlocText();
                if (blocText.selected == allSelected) {
                    blocText.set_selected(value);
                }
            }
            allSelected = !allSelected;
            System.out.println("Selecting all");
        }

        public void switchEdge() {
            System.out.println("switching edge");
            String[] edges = {"topRight_edge", "topLeft_edge", "bottomLeft_edge", "bottomRight_edge",};
            String currentEdge = sharedInfo.getConfigLoader().currentEdge;

            int i = 0;
            for (; i < 4; i++) {

                String e = edges[i];
                if (currentEdge.compareTo(e) == 0) {
                    break;
                }
            }
            i += 1;
            if (i >= 4)
                i = 0;

            System.out.println("choosen edge is " + edges[i]);
            sharedInfo.getConfigLoader().setCurrentEdge(edges[i]);
            sharedInfo.getConfigLoader().position_stage_atEgde(edges[i]);

            sharedInfo.getStage().setX(sharedInfo.getConfigLoader().stage_defaultX);
            sharedInfo.getStage().setY(sharedInfo.getConfigLoader().stage_defaultY);


        }

        public void expand() {

            if (expanded) // its big so now shrink
            {
                sharedInfo.getStage().setY(stage_previousY);
                sharedInfo.getStage().setHeight(sharedInfo.getConfigLoader().mainPane_defaultHeight);

            } else {
                stage_previousX = (int) sharedInfo.getStage().getX();
                stage_previousY = (int) sharedInfo.getStage().getY();
                sharedInfo.getStage().setY(0);
                sharedInfo.getStage().setHeight(screenHeight);
            }
            expanded = !expanded;


            System.out.println("Expanding");
        }

        public ArrayList<Integer> get_selected_labels() {
            ArrayList<Integer> index_arrray = new ArrayList<>();
            for (int i = 0; i < sharedInfo.getCurrentWholePackageArray().size(); i++) {

                SharedInfo.WholePackage wholePackage = sharedInfo.getCurrentWholePackageArray().get(i);
                BlocText blocText = wholePackage.getBlocText();

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

                SharedInfo.WholePackage wholePackage = sharedInfo.getCurrentWholePackageArray().get(index);
                BlocText blocText = wholePackage.getBlocText();

                System.out.println("index is " + i);
                blocText.set_selected(false);

            }
        }
    }

    public class ButtonInfo {

        public ImageView[] imageViews ;
        public Runnable func;
        public boolean quickanimation = true;
        public int toogle_image = 0;
        public Button button;


        public  ButtonInfo(ImageView[] imageViews,Runnable func,boolean quickanimation)
        {
            if (quickanimation) {
                System.out.println("[Info] quickanimation is already true by default — you can omit it.");
            }

            this.imageViews = imageViews;
            this.func = func;
            this.quickanimation = quickanimation;
        }

        public  ButtonInfo(ImageView[] imageViews,Runnable func)
        {
            this.imageViews = imageViews;
            this.func = func;
        }

    }
    public void addObserver(Observar observer) {
        observadores_list.add(observer);
    }

    public void setObservadores_list(ArrayList<Observar> observadores_list) {
        this.observadores_list = observadores_list;
    }
}
