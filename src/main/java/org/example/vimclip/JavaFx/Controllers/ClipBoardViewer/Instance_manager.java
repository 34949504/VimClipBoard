package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.*;
import lombok.Setter;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import org.example.vimclip.Observar;

public class Instance_manager implements Observar {

    SharedInfo sh;

    public void  init(SharedInfo sharedInfo)
    {
        this.sh = sharedInfo;
    }

    @Override
    public void tab_changed(Character reg) {

        ArrayList<Object> array_of_reg = sh.getRegistryManager().getArray(reg);
        int array_of_reg_size = array_of_reg.size();


        ArrayList<SharedInfo.WholePackage> WPC = sh.getCurrentWholePackageArray();
        ArrayList<SharedInfo.WholePackage> WPAI = sh.getAvailable_instances();

        int WPC_size = WPC.size();
        int WPAI_size = WPAI.size();

        if (array_of_reg_size <= 0) {

            for (int i = WPC.size() - 1; i >= 0; i--) {
                SharedInfo.WholePackage wp = WPC.remove(i);
                wp.getBlocText().purify_instance();
                WPAI.add(wp);
            }
            sh.getContentPane().getChildren().clear();
        } else {

            //cases request more wholepackages
            //uses less wholepackages
            //exact quantity
            //if array_reg_size surpasses current array, current array,
            //will have to request for more, if there a no more, then there will need
            // to more created

            int i = 0;
            for (; i < array_of_reg_size; i++) {

                if (i >= WPC_size) //Means requesting more than available on current
                {
                    SharedInfo.WholePackage wholePackage = sh.getInstance_from_available_wholePackage();
                    WPC.add(wholePackage);
                    sh.getContentPane().getChildren().add(wholePackage.getVBox());
                }

                Object contents = array_of_reg.get(i);
                if (contents instanceof String cunt) {
                    SharedInfo.WholePackage wp = WPC.get(i);
                    BlocText blocText = wp.getBlocText();
                    blocText.purify_instance();
                    blocText.getLabel().setText(cunt);
                }
                else if (contents instanceof Image image)
                {

                }
            }

            if (i < WPC_size) //Si solamente se ocuparon unos y otros no, hay pasar esos al WPAI y quitarlos de las vista
            {
                int mark = i;
                int originalWPCSize = WPC_size; // already available
                for (; mark < originalWPCSize; mark++) {
                    SharedInfo.WholePackage wp = WPC.remove(i); // always remove at `i` because list shrinks
                    wp.getBlocText().purify_instance();
                    sh.getContentPane().getChildren().remove(i);
                    WPAI.add(wp);
                }
            }


        }
        //change in tab means, maybe instead of cleaning first, iterate over
        // the values of the list of the new reg, if its size 0 then just
        //pass all of them to the storage
        //size of the ones in used, the not in used
    }

    @Override
    public void blocs_were_deleted(ArrayList<Integer> selectedLabels) {
        //selected labels are sorted 6 5 4 ..
        int size = selectedLabels.size();
        ArrayList<SharedInfo.WholePackage> WPC = sh.getCurrentWholePackageArray();
        ArrayList<SharedInfo.WholePackage> WPAI = sh.getAvailable_instances();

        //remove from vbox and pass from wpc to wpai

        for (int i = 0; i < size; i++) {

            int index = selectedLabels.get(i);
            SharedInfo.WholePackage wp = WPC.remove(index);
            BlocText blocText = wp.getBlocText();
            blocText.purify_instance();
            WPAI.add(wp);

            sh.getContentPane().getChildren().remove(index);

        }

        check_register_size();



    }

    public void check_register_size()
    {
        int biggest_array_reg = -1;
        Character big_reg = null;
        List<Character> reg_names = Arrays.asList('a','s','d','f');

        for (Character c:reg_names)
        {
            int size = sh.getRegistryManager().get_array_size(c);
            System.out.printf("Array register %c has %d strings\n",c,size);

            if (size >biggest_array_reg)
            {
                biggest_array_reg = size;
                big_reg = c;
            }
        }
        System.out.printf("Current instances are %d\n",sh.getCurrentWholePackageArray().size());
        System.out.printf("Available instances are %d\n",sh.getAvailable_instances().size());
        System.out.printf("Total of instances is %d\n",sh.getCurrentWholePackageArray().size()+ sh.getAvailable_instances().size());
        int total_instances = sh.getCurrentWholePackageArray().size()+ sh.getAvailable_instances().size();
        int trehshold = biggest_array_reg *30 /100;

        System.out.println("Treshold is");
        if (total_instances > trehshold)
        {
            ArrayList<SharedInfo.WholePackage> WPAI = sh.getAvailable_instances();

            int quantity_to_remove = total_instances- trehshold;

            for (int i = 0; i < quantity_to_remove && WPAI.size() > 0;++i)
            {
                WPAI.removeLast();
            }

        }
    }
}
