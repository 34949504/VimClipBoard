package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs.DialogSimilarFuncs;
import org.example.vimclip.Observar;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;


//What i have to do. Assign to the buttons their corresponding actions and images

public class MainButtons implements Observar {

    private HashMap<String, Leboutton.ButtonInfo> buttons = new HashMap<>();

    private Leboutton.MyImages myImages = new Leboutton.MyImages();
   private  MyButtonFuncs myButtonFuncs = new MyButtonFuncs();

   private SharedInfo sharedInfo;

    int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();
    private ArrayList<Observar> observadores_list = new ArrayList<>();
    private StageFocuser stageFocuser;
    public MainButtons(ArrayList<Button> buttons_array,
                       SharedInfo sharedInfo) {

        this.sharedInfo = sharedInfo;
        stageFocuser = new StageFocuser(sharedInfo);
        addObserver(sharedInfo.getAcciones().getClipBoardListener());
        creatingButtonInfo();
        inicializando_botones(buttons_array);

    }


    private void creatingButtonInfo()
    {

        buttons.put("expand", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("expand.png", "expandPressed.png"),
                () -> myButtonFuncs.expand()
        ));

        buttons.put("trashButton", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("trash.png", "trashPressed.png"),
                () -> myButtonFuncs.trashCan()
        ));

        buttons.put("copyButton", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("copy.png", "copyPressed.png"),
                () -> myButtonFuncs.copyHand()
        ));

        buttons.put("selectAll", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("selectAll.png", "selectAllPressed.png"),
                () -> myButtonFuncs.selectAll()
        ));

        buttons.put("startRecordingButton", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("startRecording.png", "startRecordingPressed.png"),
                () -> myButtonFuncs.startCopying(),false
        ));

        buttons.put("gearButton", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("gear.png", "gearPressed.png"),
                () -> myButtonFuncs.gear()
        ));

        buttons.put("switchEdge", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("switchEdge.png", "switchEdgePressed.png"),
                () -> myButtonFuncs.switchEdge()
        ));
        buttons.put("separator", new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("separator.png", "separatorPressed.png"),
                () -> myButtonFuncs.separator()
        ));

        buttons.put("copy_and_remove",new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("copy_and_remove.png","copy_and_removePressed.png"),
                () -> myButtonFuncs.copy_and_remove()
        ));

        buttons.put("shortcut_button",new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("shortbutton_active.png","shortbutton_not_active.png"),
                () -> myButtonFuncs.shortcut_button(),false
        ));
        buttons.put("hide_app",new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("hideApp.png","hideApp_pressed.png"),
                () -> myButtonFuncs.hideApp()
        ));
        buttons.put("help",new Leboutton.ButtonInfo(
                myImages.imageViewConstructor("help.png","helpPressed.png"),
                () -> myButtonFuncs.help_button()
        ));
    }

    private void inicializando_botones(ArrayList<Button> buttons_array) {
        for (Button button : buttons_array) {
            Leboutton.inicialiar_boton(buttons,button);
        }
    }


    public class MyButtonFuncs {


        boolean allSelected = false;
        boolean shorctutButton_active = true;

        boolean expanded = false;
        int stage_previousX;
        int stage_previousY;



        public MyButtonFuncs() {

        }

        public boolean isShorctutButton_active()
        {
            return shorctutButton_active;
        }

        public void hideApp()
        {

            Stage stage = sharedInfo.getStage();
            stage.setIconified(!stage.isIconified());

            if (!stage.isIconified()) {
                stageFocuser.giveFocus();
            }
            else {
                stageFocuser.giveFocusBack();
            }
        }


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


            }
        }

        public void separator()
        {
            System.out.println("separating");

            for (Observar observar:observadores_list)
            {
                observar.separator_button_was_clicked();
                System.out.println("Class is "+observar.getClass());
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
        private void prohecing_disable_hand(boolean flag,String id)
        {
            Leboutton.ButtonInfo buttonInfo = buttons.get(id);
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
                prohecing_disable_hand(false,"copyButton");
                prohecing_disable_hand(false,"copy_and_remove");

            } else {

                sharedInfo.getAcciones().doCommand(sharedInfo.getCurrent_register(), "clipboard_listener");
                sharedInfo.getCopyingStrings().set(true);
                prohecing_disable_hand(true,"copyButton");
                prohecing_disable_hand(true,"copy_and_remove");
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
                System.out.println("shrinking");
                sharedInfo.getStage().setY(stage_previousY);
                sharedInfo.getStage().setHeight(sharedInfo.getConfigLoader().stage_defaultHeight);

            } else {
                System.out.println("Super expanding");
                stage_previousX = (int) sharedInfo.getStage().getX();
                stage_previousY = (int) sharedInfo.getStage().getY();
                sharedInfo.getStage().setY(0);
                sharedInfo.getStage().setHeight(screenHeight);
            }
            expanded = !expanded;


            PauseTransition pause = new PauseTransition(Duration.millis(50));
            pause.setOnFinished(event -> {

                // Now call observers when bounds are correct
                for (Observar observar : observadores_list) {
                    observar.stage_has_been_resized();
                }
            });
            pause.play();


        }
        public void copy_and_remove()
        {
            //check if current reg not empty. check if selected
            //remove from top to bottom// disable when button active
            System.out.println("hello");

            ArrayList<Integer> selected_labels = get_selected_labels();

            int status = sharedInfo.getAcciones().doCommand4(sharedInfo.getCurrent_register(), selected_labels, "get_values");

            if (status == 0) {

                 sharedInfo.getAcciones().doCommand4(sharedInfo.getCurrent_register(), selected_labels, "remove_values");
                for (Observar observador:observadores_list)
                {
                    observador.blocs_were_deleted(selected_labels);
                }
            }
            else { // It means there was no selection then  reg size

               int reg_size = sharedInfo.getRegistryManager().get_array_size(sharedInfo.getCurrent_register());

               if ( reg_size <= 0){
                   System.out.println("Cannot remove and copy because reg size is 0");
                   return;
               }
               sharedInfo.getAcciones().doCommand(sharedInfo.getCurrent_register(),"get_first_value");
               sharedInfo.getAcciones().doCommand(sharedInfo.getCurrent_register(),"remove_first_value");

               ArrayList<Integer> s = new ArrayList<>();
               s.add(0);
                for (Observar observador:observadores_list)
                {
                    observador.blocs_were_deleted(s);
                }


            }
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

            for (int i = 0; i < array_list.size(); i++) {

                int index = array_list.get(i);

                SharedInfo.WholePackage wholePackage = sharedInfo.getCurrentWholePackageArray().get(index);
                BlocText blocText = wholePackage.getBlocText();

                blocText.set_selected(false);

            }
        }

        public void shortcut_button()
        {
            System.out.println("shortcut");
            shorctutButton_active = !shorctutButton_active;
        }

        public void help_button()
        {
            System.out.println("help");
            for (Observar observar:observadores_list)
            {
                observar.showHelpDialog();
            }
        }

    }



    @Override
    public void appShortcut_beenPressed(String shortcut) {
        System.out.println("was this called");
       Leboutton.ButtonInfo buttonInfo =  buttons.get(shortcut);

       String name = shortcut;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (name.compareTo("shortcut_button")==0)
                {
                    buttonInfo.button.fire();
                    return;
                }

                if (name.compareTo("move_down")==0 || name.compareTo("move_up")==0)
                {
                    for (Observar observar:observadores_list)
                    {
                        observar.up_or_down_keyPressed(name);
                    }
                    return;
                }

                if (name.compareTo("space")==0)
                {
                    for (Observar observar:observadores_list)
                    {
                        observar.spaceBar_keyPressed();

                    }
                    return;
                }


                if (myButtonFuncs.isShorctutButton_active()) {
                    buttonInfo.button.fire();
                }
            }
        });
    }

    public void addObserver(Observar observer) {
        observadores_list.add(observer);
    }

    public void setObservadores_list(ArrayList<Observar> observadores_list) {
        this.observadores_list = observadores_list;
    }
}
