
package org.example.vimclip;

import com.sun.tools.javac.Main;
import org.example.vimclip.JavaFx.AppContext;
import org.example.vimclip.JavaFx.MyApp;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Keypressed.KeyPressed;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainProgram {

    public static KeyPressed keyPressed;

    public static void main(String[] args) throws IOException {
        Path appDataPath = Paths.get(System.getenv("LOCALAPPDATA"), "Text Vault");
        Files.createDirectories(appDataPath); // make sure folder exists

        Path configPath = Utils.getPathfromAppData(appDataPath,"config.json","/Data/config.json");






        JSONObject combos =  Utils.readJsonFromResource("/Data/combos_v3.json");
        JSONObject config = Utils.readJsonFromPath(configPath);
        ConfigMaster configMaster = new ConfigMaster(config);
        java.util.List<String> fonts = javafx.scene.text.Font.getFontNames();

        configMaster.setConfigPath(configPath);


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
