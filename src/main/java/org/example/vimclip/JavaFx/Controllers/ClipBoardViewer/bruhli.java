package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class bruhli extends Application implements NativeKeyListener {

    public ListView<MyText> listView = new ListView<>();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        GlobalScreen.setEventDispatcher(new SwingDispatchService());

        primaryStage.setAlwaysOnTop(true);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);

        listView.setEditable(true); // ✨ Allow editing

        // Add some sample items
        String tex = "HHello is it me your are looking for, i can see it in your eyes Hello is it me your are looking for, i can see it in your eyes ello is it me your are looking for, i can see it in your eyes ";
        listView.getItems().addAll(new MyText(tex), new MyText("HELLO"), new MyText("BONJOUR"));

        listView.setCellFactory(param -> new MyCell());



        VBox root = new VBox(listView);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Editable ListView");
        primaryStage.show();
    }

    // Editable cell
    public static class MyCell extends ListCell<MyText> {
        private final Label label = new Label();

        public MyCell() {
            label.setWrapText(true);
            label.setTextFill(Color.BLACK);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.TOP_LEFT);
            label.setPadding(new Insets(5,0,0,0));

//            label.setFocusTraversable(true);
            setPrefWidth(0); // Allow wrapping
            setMaxWidth(Double.MAX_VALUE);

            this.setOnMouseClicked(event -> {
                MyText myText = getItem();
                if (myText == null) return;

                myText.isSelected = !myText.isSelected;
                setStyle(myText.isSelected ? "-fx-background-color: orange;" : "-fx-background-color: white;");
                label.requestFocus();
            });

        }

        @Override
        protected void updateItem(MyText item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                setStyle("");
            } else {
                label.setText(item.text);
                setGraphic(label);  // Use label instead of setText
                setText(null);      // Avoid conflict with default text
                setStyle(item.isSelected ? MyText.selectedStyle : MyText.not_selectedStyle );

                if (item.blueSelected)
                {
                    setStyle(item.isSelected ? MyText.selected_and_inposition:MyText.in_position_selectedStyle);
                }
            }
        }
    }

    public static class MyText {
        boolean isSelected = false;
        boolean blueSelected = false;
        String text;

        static String selectedStyle = "-fx-background-color: orange;";
        static String not_selectedStyle = "-fx-background-color: white;";
        static String in_position_selectedStyle = "-fx-background-color: blue;";
        static String selected_and_inposition = "-fx-background-color: #FFD580;";




        public MyText(String text) {
            this.text = text;
        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {

        int keycode = e.getKeyCode();

        String key = NativeKeyEvent.getKeyText(keycode).toLowerCase();
        if (key.compareTo("space") == 0)
        {
            System.out.println("space was pressed");
            MyText myText =  listView.getSelectionModel().getSelectedItem();

            if (myText != null)
            {
                System.out.println("not null");
                myText.isSelected = !myText.isSelected;
                listView.refresh();
            }
        }

        if  (key.compareTo("down") == 0 || key.compareTo("up") == 0)
        {
            int dir = key.compareTo("down") == 0 ? 1:-1;
            SelectionModel<MyText> selectionModel = listView.getSelectionModel();

            MyText myText =  selectionModel.getSelectedItem();

            if (myText == null)
            {
                listView.getItems().getFirst().blueSelected = true;
                listView.refresh();
                return;
            }



            myText.blueSelected = true;
            listView.refresh();






        }
    }

}
