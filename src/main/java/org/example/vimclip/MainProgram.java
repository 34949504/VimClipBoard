
package org.example.vimclip;

import org.example.vimclip.JavaFx.MyApp;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Keypressed.KeyPressed;
import org.json.JSONObject;

import java.awt.*;
import java.io.InputStream;


public class MainProgram {

    public static KeyPressed keyPressed;

    public static void main(String[] args) {


        JSONObject combos =  Utils.readJsonFromResource("/Data/combos_v3.json");
        JSONObject config =  Utils.readJsonFromResource("/Data/config.json");

        java.util.List<String> fonts = javafx.scene.text.Font.getFontNames();

        for (String f:fonts)
        {
            System.out.println(f);
        }

        RegistryManager registryManager = new RegistryManager();
        Acciones acciones = new Acciones(registryManager);
        keyPressed = new KeyPressed(registryManager,combos,acciones);

        AppContext.keyPressed = keyPressed;
        AppContext.registryManager = registryManager;
        AppContext.config = config;
        AppContext.acciones = acciones;

        MyApp myApp = new MyApp();
        MyApp.launch(myApp.getClass());


    }


}
