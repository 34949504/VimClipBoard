package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlocImage {

    String type = null;
    Image image = null;
    ImageView imageView = null;


    public BlocImage()
    {
    }
    public void setImage(Image image)
    {
        this.image = image;
        imageView = new ImageView(image);
    }
}
