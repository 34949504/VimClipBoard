package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Keypressed.Comandos.Acciones;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.control.Separator;
import org.example.vimclip.RegistryManager;

import javax.swing.border.LineBorder;

@Getter
@Setter
public class SharedInfo {

    private Character current_register;
    private VBox contentPane;
    private ArrayList<WholePackage> currentWholePackageArray = new ArrayList<>(); // the one being displayed on the labels
    private AtomicBoolean copyingStrings = new AtomicBoolean(false);
    private ConfigLoader configLoader;
    private Stage stage;
    private BorderPane mainPane;
    private Acciones acciones;
    private MyDialog myDialog;
    private RegistryManager registryManager;

    private ArrayList<WholePackage> available_instances = new ArrayList<>(); // the one that stores the instances when not needed or some shit


    public SharedInfo()
    {

    }

    public void initialize_available_instances(ConfigLoader configLoader, MyDialog myDialog)
    {
        for (int i = 0; i < 10; i++) {
            WholePackage wholePackage = WholePackage.cleanInstance(configLoader,myDialog);
            available_instances.add(wholePackage);
        }
    }

    public WholePackage getInstance_from_available_wholePackage()
    {


        if (available_instances.size() <= 0)
        {
            WholePackage wholePackage =  WholePackage.cleanInstance(configLoader,myDialog);
            return wholePackage;
        }

        WholePackage wholePackage = available_instances.getLast();
        available_instances.removeLast();
        return wholePackage;
    }

    @Getter
    @Setter
    public static class WholePackage
    {
        BlocText blocText;
        Separator separator;
        VBox vBox;

        public static WholePackage cleanInstance(ConfigLoader configLoader, MyDialog myDialog) {
            WholePackage wp = new WholePackage();
            wp.setBlocText(new BlocText("", configLoader, myDialog));
            wp.setSeparator(new Separator());
            Label label = wp.getBlocText().getLabel();
            VBox vBox1 = new VBox(label, wp.getSeparator());
            vBox1.setFillWidth(true); // Optional, usually VBox does this by default
            wp.setVBox(vBox1);
            return wp;
        }
    }




}
