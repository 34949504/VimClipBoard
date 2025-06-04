package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.Observar;

import java.util.ArrayList;

public class SepDialog extends Dialog implements Observar {

    private ArrayList<Observar> observers_list = new ArrayList<>();

    private DialogPane dialogPane = this.getDialogPane();
    private BorderPane borderPane = new BorderPane();
   private Button newLine_button = new Button("New line button");
   private Button pattern_button = new Button("Pattern");
   private PatternCreator_contextMenu patternCreatorContextMenu = new PatternCreator_contextMenu();



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
        action_patternButton();
        dialogPane.setContent(borderPane);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);

        // Let window positioning happen after dialog is shown
        this.setOnShown(e -> {
            dialogPane.applyCss();
            dialogPane.layout();

            calculating_where_dialog_should_appear();

            Window window = getDialogPane().getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).setAlwaysOnTop(true);
            }
            window.setX(x);
            window.setY(y);
        });

        new_line_button_onAction();

        textfield.setText(configMaster.getSeparator_when_getting_all_text());
        configMaster.setSeparator_when_getting_all_text(processing_string());
    }

    private void new_line_button_onAction()
    {
       newLine_button.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {

               StringBuilder cur_text =new StringBuilder(textfield.getText());

               cur_text.append("\\n");
               textfield.setText(cur_text.toString());
           }
       });
    }


    public void show_dialog() {
        if (dialog_showing) {
            this.show();
        }
    }


    private void settingDims() {
        dialogPane.setPrefWidth(sharedInfo.getConfigLoader().stage_defaultWidth);
        dialogPane.setPrefHeight(sharedInfo.getConfigLoader().stage_defaultHeight / 2);
        setResizable(true);

//        currentHeight = sharedInfo.getConfigLoader().stage_defaultHeight / 2;
//        currentWidth = sharedInfo.getConfigLoader().stage_currentWidth;
        currentHeight = (int)(clipboardViewer_config.getStage_height()/2);
        currentWidth = (int)(clipboardViewer_config.getStage_width());
    }

    private void calculating_where_dialog_should_appear() {
        int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();

        Bounds bounds = sharedInfo.getStage().getScene().getRoot().localToScreen(
                sharedInfo.getStage().getScene().getRoot().getBoundsInLocal());

        int available_space_below = (int) (screenHeight - bounds.getMaxY());
        int available_space_above = (int) (bounds.getMinY());
        int available_space_left = (int) (bounds.getMinX());
        int available_space_right = (int) (screenWidth - bounds.getMaxX());


        System.out.printf(".......................\n" +
                "Imprimiendo bounds del stage\n" +
                "min x es %f\n" +
                "min y es %f\n" +
                "max x es %f\n" +
                "max y es %f\n" +
                "..................................\n",bounds.getMinX(),bounds.getMinY(),
                bounds.getMaxX(),bounds.getMaxY());


        System.out.printf("-------------------\nPrinting available space:\n" +
                "below %s\n" +
                "above %s \n" +
                "left %s\n" +
                "right %s\n", available_space_below, available_space_above, available_space_left,available_space_right);
        System.out.println("---------------------------");

        System.out.printf("Current height is %d and current width %d\n-----------------",currentHeight,currentWidth);


        if (available_space_below > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) bounds.getMaxY();
        } else if (available_space_above > currentHeight) {
            x = (int) bounds.getMinX();
            y = (int) (bounds.getMinY() - currentHeight);
        } else if (available_space_left > currentWidth) {
            System.out.printf("Dialog width %f\nDialogPane width %f\n", getWidth(), dialogPane.getWidth());
            x = (int) (bounds.getMinX() - dialogPane.getWidth());
            y = (int) bounds.getMaxY() - currentHeight;
        }else if (available_space_right > currentWidth)
        {

            x = (int) (bounds.getMaxX()) ;
            y = (int) bounds.getMaxY() - currentHeight;
        }
    }

    private void settingUp_dialogLayout() {
        settingUp_closeButton();
        borderPane.setTop(close_button);
        borderPane.setCenter(textfield);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(newLine_button,pattern_button);
        borderPane.setBottom(hBox);

    }

    private void settingUpLayout_contextMenu()
    {

    }

    private void action_patternButton()
    {
        pattern_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                patternCreatorContextMenu.show(pattern_button, Side.BOTTOM,0,0);
            }
        });

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
            calculating_where_dialog_should_appear();
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
    System.out.println("yeha motherfuckers ");

    if (dialog_showing) {
        // Defer repositioning until after resize/layout pass
        Platform.runLater(() -> {
            dialogPane.applyCss();
            dialogPane.layout();
            calculating_where_dialog_should_appear();

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
}
