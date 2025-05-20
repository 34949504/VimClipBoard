package org.example.vimclip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Utils {


    public static void imprimir_DEGUGG(String message,boolean DEBUGG)
    {
        if (DEBUGG)
        System.out.println(message);
    }

    public static boolean check_that_instruction_exist(String instruccion)
    {
        JSONObject RDI = readJson("C:\\Users\\gerar\\IdeaProjects\\VimClip\\src\\main\\java\\org\\example\\vimclip\\Data\\registro_de_instrucciones.json");
        JSONArray array = RDI.getJSONArray("instrucciones_disponibles");

        boolean found_match = false;
        for (int i = 0; i < array.length(); i++) {

            if (array.getString(i) == instruccion)
            {
                found_match = true;
                break;
            }

        }
        return found_match;

    }

   public static JSONObject readJson(String filePath) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            File myObj = new File(filePath);
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

    /**
     * Only ran one time to check if json doesnt instructions that dont match in registro_instrucciones
     * @param RDI es el registro de instrucciones
     */
    public static boolean check_json_health(JSONObject combos,JSONObject RDI)
    {
        ArrayList<String> stack_keys = new ArrayList<>();
        ArrayList<JSONObject> array_pointer = new ArrayList<>();
        ArrayList<Integer> keys_removed = new ArrayList<>();

        int current_keys_a_quitar = 0;
        int number_of_iterations = 0;
        boolean resetted = false;

        array_pointer.add(combos);
        Iterator<String > keys = array_pointer.getLast().keys();


        boolean initialize = false;
        //Objetivo, encotrar todas las keys que tengas acciones
        //Traversar JSONOBJECTS y checar si esta la key acciones

          while (!stack_keys.isEmpty() || !initialize)
        {
            initialize = true;

            while (keys.hasNext())
            {
                current_keys_a_quitar += 1;
                String key = keys.next();
                System.out.printf("Key in while loop is %s\n",key);

                if (key.compareTo("actions") == 0)
                {
                    System.out.printf("Found key actions ");
                }
                stack_keys.add(key);
                resetted =false;
            }



        }

        return false;
    }


    private static void imprimir_stackkeys(ArrayList<String> stackKeys)
    {
        System.out.println("Stackeys:");
        System.out.printf("[");
        for (int i = 0; i < stackKeys.size(); i++) {

            System.out.printf("%s,",stackKeys.get(i));
        }
        System.out.printf("]\n");
    }

}
