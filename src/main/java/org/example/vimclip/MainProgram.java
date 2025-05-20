
package org.example.vimclip;

import org.example.vimclip.JavaFx.Command_displayer;
import org.example.vimclip.Keypressed.JsonTraverser;
import org.example.vimclip.Keypressed.KeyPressed;
import org.json.JSONObject;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner; // Import the Scanner class to read text files

public class MainProgram {

    public static KeyPressed keyPressed;


    public static void main(String[] args) {

//        String str = "Easy French@@COOLSCRIPTSEPARATOR@@\"https://www.youtube.com/@EasyFrench/videos\"";
//        String sep = "@@COOLSCRIPTSEPARATOR@@";
//

        JSONObject combos = Utils.readJson("C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\java\\org\\example\\vimclip\\Data\\combos_v3.json");
        RegistryManager registryManager = new RegistryManager();
        keyPressed = new KeyPressed(registryManager,combos);

        Command_displayer commandDisplayer = new Command_displayer();
        Command_displayer.launch(commandDisplayer.getClass());

        //Everything after launch will not be executed after closed



    }


}
