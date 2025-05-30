package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;

import java.io.ObjectInputFilter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class SharedInfo {

    private Character current_register;
    private VBox contentPane;
    private ArrayList<ClipBoardViewer.BlocText> blocTextArrayList = new ArrayList<>();
    private AtomicBoolean copyingStrings = new AtomicBoolean(false);
    private ConfigLoader configLoader;
    private Stage stage;
    private BorderPane mainPane;
    private Acciones acciones;

}
