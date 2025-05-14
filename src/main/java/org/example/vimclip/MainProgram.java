
package org.example.vimclip;

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


    public static void main(String[] args) {

        JSONObject combos = Utils.readJson("C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\java\\org\\example\\vimclip\\Data\\combos.json");
        RegistryManager registryManager = new RegistryManager();
        KeyPressed keyPressed = new KeyPressed(registryManager,combos);

//        ProcessBuilder pb = new ProcessBuilder("java", "-version");
//
////        pb.directory(new File("C:\\Users\\gerar\\Downloads"));
//        try {
//            Process p = pb.start();
//        }catch (IOException e)
//        {
//            System.out.println("bruhv");
//        }
//
//        List<String> l = pb.command();
//
//        for (String i :l){
//            System.out.println(i);
//        }
//        System.out.println(pb.directory());
//
//
//        String os = System.getProperty("os.name").toLowerCase();
//        if (os.contains("win")) {
//            System.out.println("its windows");
//        } else if (os.contains("nix") || os.contains("nux")) {
//            // Linux-specific logic
//        }
//




    }


}
