package org.example.vimclip;

import org.example.vimclip.Keypressed.JsonTraverser;
import org.example.vimclip.Keypressed.KeyPressed;
import org.json.JSONObject;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class MainProgram {


    public static void main(String[] args) {

        JSONObject combos = readJson();
        RegistryManager registryManager = new RegistryManager();
        KeyPressed keyPressed = new KeyPressed(registryManager,combos);



    }

    public static JSONObject readJson() {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            File myObj = new File("C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\java\\org\\example\\vimclip\\Data\\combos.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                stringBuilder.append(data);
//                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
        return new JSONObject(stringBuilder.toString());

    }
}
