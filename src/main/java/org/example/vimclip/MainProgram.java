
package org.example.vimclip;

import org.example.vimclip.JavaFx.AppContext;
import org.example.vimclip.JavaFx.MyApp;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Keypressed.KeyPressed;
import org.json.JSONObject;


public class MainProgram {

    public static KeyPressed keyPressed;

    public static void main(String[] args) {


//        String file = Utils.getPath("/Data/combos_v3.json");
//        System.out.println(file);


        JSONObject combos =  Utils.readJsonFromResource("/Data/combos_v3.json");
        JSONObject config =  Utils.readJsonFromResource("/Data/config.json");
        ConfigMaster configMaster = new ConfigMaster(config);
        java.util.List<String> fonts = javafx.scene.text.Font.getFontNames();



        RegistryManager registryManager = new RegistryManager(config);
        Acciones acciones = new Acciones(registryManager,configMaster);
        keyPressed = new KeyPressed(registryManager,combos,acciones);

        AppContext.keyPressed = keyPressed;
        AppContext.registryManager = registryManager;
        AppContext.config = config;
        AppContext.acciones = acciones;
        AppContext.configMaster = configMaster;

        MyApp myApp = new MyApp();
        MyApp.launch(myApp.getClass());


    }


}
