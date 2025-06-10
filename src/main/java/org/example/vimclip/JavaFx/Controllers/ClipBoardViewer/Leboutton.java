package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.vimclip.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Leboutton {



    public static class ButtonInfo {

        public ImageView[] imageViews ;
        public Runnable func;
        public boolean quickanimation = true;
        public int toogle_image = 0;
        public Button button;
        public ArrayList<Image> currentButtonImage = null;

        SharedInfo sharedInfo;


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

    public static class MyImages {

        private String path = "/assets/images/";

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

                Image image = new Image(Utils.getInputStream(paths[i])); // prefix "file:" for absolute paths
                ImageView imageView = new ImageView(image);

                Rectangle2D screenBounds = Screen.getPrimary().getBounds();
                double screenHeight = screenBounds.getHeight();
                double screenWidth = screenBounds.getWidth();

                imageView.setFitWidth(40);
                imageView.setFitHeight(40);


//                final double REFERENCE_STAGEWIDTH = 299.9808;
//                final double REFERENCE_WIDTH = 1536.0;
//                final double REFERENCE_IMAGE_WIDTH = 40.0;
//
//                double widthRatio = REFERENCE_IMAGE_WIDTH / REFERENCE_STAGEWIDTH;  // ~0.0208
//
//                double stageWidth = screenWidth * 0.1953;
//                System.out.println("stage width "+stageWidth);
//
//                //0.1953
//
//                double imageWidth = stageWidth * widthRatio;
//
//                imageView.setFitWidth(imageWidth);
//                imageView.setFitHeight(imageWidth); // Square

;

                imageViews[i] = imageView;

            }
            return imageViews;

        }
        public ImageView[] imageViewConstructor(SharedInfo sharedInfo,String... names)
        {
            String[] paths = string_creator(names);
            ImageView[] imageViews = new ImageView[names.length];

            for (int i = 0; i < paths.length; i++) {

                Image image = new Image(Utils.getInputStream(paths[i])); // prefix "file:" for absolute paths
                ImageView imageView = new ImageView(image);

                Rectangle2D screenBounds = Screen.getPrimary().getBounds();
                double screenHeight = screenBounds.getHeight();
                double screenWidth = screenBounds.getWidth();

//                double imageSize = screenHeight * 0.045; // 5% of screen height


                double stageWidth = screenWidth * 0.1953;
                double stageHeight = screenHeight * 0.3676;

                double widthPercent = 40 / stageWidth;

                imageView.setFitWidth(stageWidth * widthPercent);
                imageView.setFitHeight(stageWidth * widthPercent);


                imageViews[i] = imageView;

            }
            return imageViews;

        }

    }

    public static void inicialiar_boton(HashMap<String, ButtonInfo> buttons, Button button) {


        ButtonInfo buttonInfo =buttons.get(button.getId());
        ImageView[] imageViews = buttonInfo.imageViews;
        ImageView first_image = imageViews[0];
        ImageView second_image = imageViews[1];
        ArrayList<Image> currentImage = new ArrayList<>();

        Timeline timeline = new Timeline();
        buttonInfo.button = button;
        buttonInfo.currentButtonImage = currentImage;


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
            currentImage.add(first_image.getImage());
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    button.setGraphic(second_image);
                    timeline.play();
                    buttonInfo.func.run();

                }
            });
        } else {


            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    System.out.println("was called");

                    buttonInfo.toogle_image = buttonInfo.toogle_image == 0 ? 1 : 0;
                    button.setGraphic(imageViews[buttonInfo.toogle_image]);

                    if (currentImage.size() <=0)
                    {

                        currentImage.add(imageViews[buttonInfo.toogle_image].getImage());
                    }else {

                        currentImage.set(0,imageViews[buttonInfo.toogle_image].getImage());
                    }

                    buttonInfo.func.run();
                }
            });

        }
    }

}
