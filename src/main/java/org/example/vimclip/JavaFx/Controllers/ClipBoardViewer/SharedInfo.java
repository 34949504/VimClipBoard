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
    private ArrayList<BlocText> blocTextArrayList = new ArrayList<>();
    private AtomicBoolean copyingStrings = new AtomicBoolean(false);
    private ConfigLoader configLoader;
    private Stage stage;
    private BorderPane mainPane;
    private Acciones acciones;
    private MyDialog myDialog;

    private ArrayList<BlocText> available_blocText = new ArrayList<>();



    public SharedInfo()
    {

    }

    public void initialize_available_blockText(ConfigLoader configLoader,MyDialog myDialog)
    {
        for (int i = 0; i < 10; i++) {
            available_blocText.add(new BlocText("",configLoader,myDialog));
        }
    }

    public BlocText getInstance_from_available_blocText()
    {

        if (available_blocText.size() <= 0)
        {
            return new BlocText("",configLoader,myDialog);
        }

        BlocText blocText = available_blocText.getLast();
        available_blocText.removeLast();
        return available_blocText.getLast();
    }




}
