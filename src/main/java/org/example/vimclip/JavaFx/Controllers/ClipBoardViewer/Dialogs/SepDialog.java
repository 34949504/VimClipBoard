package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Dialogs;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.Leboutton;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.SharedInfo;
import org.example.vimclip.Observar;

import java.util.ArrayList;
import java.util.HashMap;

public class SepDialog extends Dialog implements Observar {

    private ArrayList<Observar> observers_list = new ArrayList<>();

    private DialogPane dialogPane = this.getDialogPane();
    private BorderPane borderPane = new BorderPane();
   private Button newLine_button = new Button();
   private Button pattern_button = new Button();
   private PatternCreator_contextMenu patternCreatorContextMenu = new PatternCreator_contextMenu();
   private LoadingButtons loadingButtons = new LoadingButtons();

   private Label label = new Label();
   private DialogSimilarFuncs DSF;


    boolean dialog_showing = false;

    int currentHeight;
    int currentWidth;

    int x;
    int y;

    private Button close_button = new Button();
    private TextField textfield = new TextField();

    SharedInfo sharedInfo;
    private ConfigMaster.ClipboardViewer_config clipboardViewer_config;
    private ConfigMaster configMaster;

    public SepDialog(SharedInfo sharedInfo, ConfigMaster configMaster) {
        this.sharedInfo = sharedInfo;
        this.configMaster = configMaster;
        clipboardViewer_config = configMaster.getClipboardViewer_config();
        settingDims();
        settingUp_dialogLayout();
        dialogPane.setContent(borderPane);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);

        separator_textfieldListener();

        DSF = new DialogSimilarFuncs(sharedInfo,this);

        // Let window positioning happen after dialog is shown
        this.setOnShown(e -> {
            dialogPane.applyCss();
            dialogPane.layout();

            DialogSimilarFuncs.Coords coords =  DSF.calculating_where_dialog_should_appear(currentHeight,currentWidth);
            x = coords.x;
            y = coords.y;

            Window window = getDialogPane().getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).setAlwaysOnTop(true);
            }
            window.setX(x);
            window.setY(y);
        });

//        new_line_button_onAction();

        textfield.setText(configMaster.getSeparator_when_getting_all_text());
        configMaster.setUnprocessed_separator_when_getting_all_text(textfield.getText());
        configMaster.setSeparator_when_getting_all_text(processing_string());

    }

    private void separator_textfieldListener()
    {
//       textfield.textProperty().addListener(new ChangeListener<String>() {
//           @Override
//           public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
//
//               System.out.println(t1);
//           }
//       });
//
        label.textProperty().bind(textfield.textProperty()); // ✅ RIGHT DIRECTION


    }


    public void show_dialog() {
        if (dialog_showing) {
            this.show();
        }
    }


    private void settingDims() {

        int w =sharedInfo.getConfigLoader().getStage_defaultWidth();
        int h = sharedInfo.getConfigLoader().getStage_defaultHeight() / 2;
        dialogPane.setPrefWidth(w);
        dialogPane.setPrefHeight(h);
        setResizable(true);

        currentHeight = (int)(h);
        currentWidth = (int)(w);
    }


    private void settingUp_dialogLayout() {
        settingUp_closeButton();
        borderPane.setTop(close_button);
        borderPane.setCenter(textfield);
        borderPane.setRight(label);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(newLine_button,pattern_button);
        borderPane.setBottom(hBox);

    }

    private void settingUpLayout_contextMenu()
    {

    }



    private void settingUp_closeButton() {
        Image image = new Image("file:" + "C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\resources\\assets\\images\\close.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(20);
        close_button.setGraphic(imageView);
        close_button.setStyle("-fx-background-color: transparent;");

        close_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                configMaster.setSeparator_when_getting_all_text(processing_string());
                configMaster.setUnprocessed_separator_when_getting_all_text(textfield.getText());
                setDialog_showing(false);
            }
        });
    }
    private String processing_string()
    {
        String unprocessed =  textfield.getText();
        StringBuilder processed = new StringBuilder();


        System.out.println("Unprocessed string es "+unprocessed);
        boolean found_slash = false;
        // first ( then num then string then parenthesis

        for (int i = 0; i < unprocessed.length(); i++) {

            boolean continue_first_loop = false;
            System.out.println("i is "+i);
            Character c = unprocessed.charAt(i);


            if (c == 'n' && found_slash)
            {
                processed.append("\n");
                found_slash = false;
                continue;
            }

            if (found_slash)
                found_slash = false;


            if (c == '\\') {
                found_slash = true;
                continue;
            }

            if (c == '('){
                System.out.println("se encontro left par");
               for (int x = i; x < unprocessed.length();++x)
               {
                   Character e = unprocessed.charAt(x);

                   if (e == ')')
                   {
                       System.out.println("se encontro right par");
                       String pattern = unprocessed.substring(i,x+1);
                       PatternInfo patternInfo =find_pattern(pattern);
                       if ( patternInfo == null)
                       {
                           break;
                       }
                       else
                       {
                          int times = patternInfo.number_of_times;
                          String pattern_text = patternInfo.pattern_text;

                           System.out.printf("Times %d and text %s\n",times,pattern_text);
                           for(int y = 0; y < times;++y)
                           {
                               processed.append(pattern_text);
                           }

                          i = x;
                           continue_first_loop = true;
                           System.out.println("next char is "+unprocessed.charAt(i));
                           break;
                       }
                   }
               }
            }
            if (continue_first_loop)
            {
                continue;
            }


            System.out.println("apending to c ");
            processed.append(c);

        }
        System.out.printf("Separator is %s hello myname is jeff \n",processed.toString());
        return  processed.toString();

    }
    private PatternInfo find_pattern(String pattern)
    {
        //we already know that 0 is ( and pattern.length -1 es )
        //(130,texto)

        boolean stage_number = true;
        boolean stage_text = false;
        boolean lexic_error = false;

        boolean found_slash = false;

        int number = 0;
        StringBuilder text = new StringBuilder();

        for (int i = 1; i < pattern.length()-1; i++) {

            Character c = pattern.charAt(i);
            if (stage_number)
            {
               if (Character.isDigit(c))
               { //125
                   number = number * 10 + (c - '0');
                   continue;
               }

               if (c == ',') {

                   System.out.println("Se encontro la coma");
                   stage_number = false;
                   stage_text = true;
                   continue;
               }

               lexic_error = true;
               break;

            }
            else if (stage_text)
            {
                if (c == '\\')
                {
                    found_slash = true;
                    continue;
                }
                if (c == 'n')
                {
                    text.append('\n');
                }

                if (found_slash)
                {
                    found_slash = false;
                    continue;
                }


                text.append(c);
            }

        }

        if (lexic_error)
        {
            System.out.println("There is lexic error");
            return null;
        }

        System.out.println("number es "+number+" and text is "+text.toString());

        PatternInfo patternInfo = new PatternInfo();
        patternInfo.number_of_times = number;
        patternInfo.pattern_text = text.toString();


        return patternInfo;
    }




    public void setDialog_showing(boolean showing) {
        dialog_showing = showing;
        if (dialog_showing) {
            show_dialog();
        } else {
            System.out.println("closing dialog");
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) window.hide();
        }
    }

    @Override
    public void stage_was_moved() {
        if (dialog_showing) {
            dialogPane.applyCss();
            dialogPane.layout();
            DialogSimilarFuncs.Coords coords = DSF.calculating_where_dialog_should_appear(currentHeight,currentWidth);
            x = coords.x;
            y = coords.y;
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) {
                window.setX(x);
                window.setY(y);
            }
        }
    }


    @Override
    public void stage_minimizing() {
        if (dialog_showing) {
            Window window = getDialogPane().getScene().getWindow();
            if (window != null) window.hide();
        }
    }

    public void addObserver(Observar observer) {
        observers_list.add(observer);
    }

    @Override
    public void tab_changed(Character reg) {
        setDialog_showing(false);
    }

   @Override
public void stage_has_been_resized() {

    if (dialog_showing) {
        // Defer repositioning until after resize/layout pass
        Platform.runLater(() -> {
            dialogPane.applyCss();
            dialogPane.layout();
            DialogSimilarFuncs.Coords coords = DSF.calculating_where_dialog_should_appear(currentHeight,currentWidth);
            x = coords.x;
            y = coords.y;

            Window window = getDialogPane().getScene().getWindow();
            if (window != null) {
                window.setX(x);
                window.setY(y);
            }
        });
    }

}

    @Override
    public void separator_button_was_clicked() {
       setDialog_showing(true);
    }


    public class PatternInfo{

        public int number_of_times;
        public String pattern_text;
    }

    public class PatternCreator_contextMenu extends ContextMenu
    {
        CustomMenuItem customMenuItem = new CustomMenuItem();

        private BorderPane borderPane = new BorderPane();
        private VBox vBox = new VBox();
        private HBox hBox_up = new HBox();
        private HBox hBox_down = new HBox();

        private Label times_label = new Label("times:");
        private Label pattern_label = new Label("pattern:");

        private TextField times_textfield = new TextField();
        private TextField pattern_textfield = new TextField();

        private Button add = new Button("add");


        PatternCreator_contextMenu()
        {

            settingUp();
            addAction_listener();
        }
        private void settingUp()
        {
            times_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches("\\d*")) return;
                times_textfield.setText(newValue.replaceAll("[^\\d]", ""));
            });


           hBox_up.getChildren().addAll(times_label,times_textfield);
           hBox_down.getChildren().addAll(pattern_label,pattern_textfield);


           vBox.getChildren().addAll(hBox_up,hBox_down,add);

           borderPane.setCenter(vBox);

           customMenuItem.setContent(borderPane);



//            customMenuItem.setStyle("-fx-background-color: transparent;");
           this.getItems().add(customMenuItem);



        }


        private void addAction_listener()
        {
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                        String times = times_textfield.getText();
                        String pattern_text = pattern_textfield.getText();
                        String pattern = String.format("(%s,%s)",times,pattern_text);

                        if (times.isEmpty() || pattern_text.isEmpty())return;


                        StringBuilder cur_text = new StringBuilder(textfield.getText());
                        cur_text.append(pattern);

                        textfield.setText(cur_text.toString());


                }
            });
        }
    }

    public class LoadingButtons
    {

        private HashMap<String, Leboutton.ButtonInfo> buttons_hash = new HashMap<>();
        private Leboutton.MyImages myImages = new Leboutton.MyImages();
        private ArrayList<Button> button_array = new ArrayList<>();
        public LoadingButtons()
        {
            settingUpId();
            creatingButtonInfo();
            inicializando_botones(button_array);
        }

        private void settingUpId()
        {
            newLine_button.setId("newLine");
            button_array.add(newLine_button);

            pattern_button.setId("pattern");
            button_array.add(pattern_button);

//            pattern_button.setId("patternButton");
        }
        private void creatingButtonInfo()
        {
            buttons_hash.put("newLine",new Leboutton.ButtonInfo(
                    myImages.imageViewConstructor("new_line.png","new_linePressed.png"),
                    () -> newLine_buttonFunc()));

            buttons_hash.put("pattern",new Leboutton.ButtonInfo(
                    myImages.imageViewConstructor("pattern.png","patternPressed.png"),
                    () -> pattern_buttonFunc()));
        }

        private void newLine_buttonFunc()
        {
            StringBuilder cur_text =new StringBuilder(textfield.getText());
            cur_text.append("\\n");
                    textfield.setText(cur_text.toString());

        }
        private void pattern_buttonFunc()
        {

            patternCreatorContextMenu.show(pattern_button, Side.BOTTOM,0,0);
        }

        private void inicializando_botones(ArrayList<Button> buttons_array) {
            for (Button button : buttons_array) {
                Leboutton.inicialiar_boton(buttons_hash,button);
            }
        }
    }
}
