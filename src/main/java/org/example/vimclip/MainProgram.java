
package org.example.vimclip;

import org.example.vimclip.JavaFx.MyApp;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Keypressed.KeyPressed;
import org.json.JSONObject;


public class MainProgram {

    public static KeyPressed keyPressed;

    public static void main(String[] args) {

//        String str = "Easy French@@COOLSCRIPTSEPARATOR@@\"https://www.youtube.com/@EasyFrench/videos\"";
//        String sep = "@@COOLSCRIPTSEPARATOR@@";
//

        JSONObject combos = Utils.readJson("C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\java\\org\\example\\vimclip\\Data\\combos_v3.json");
        JSONObject config = Utils.readJson("C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\java\\org\\example\\vimclip\\Data\\config.json");

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
