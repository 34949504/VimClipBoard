package org.example.vimclip.Keypressed;


import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Constants;
import org.example.vimclip.Keypressed.Comandos.JsonTraverser_statusv2;
import org.example.vimclip.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JsonTraverser {




    private JSONObject combos;

    private ArrayList<String> next_keys = new ArrayList<>();
    private JSONArray registers = null;
    private JSONArray actions = null;

    private boolean found_actions = false;


    //    private boolean wrong_command;
//    private boolean found_string_keys; // key "keys"
//    private boolean found_key_inside_keylist;
//    private boolean DEBUGG = false;
//    String name_of_group_found = null;
//
//
    public JsonTraverser(JSONObject combos) {
        this.combos = combos;

    }
//
//    public JsonTraverser_status traverse(ArrayList<String> keyStacks) {
//        if (DEBUGG) {
//            System.out.println("Current key stack is :");
//            for (int i = 0; i < keyStacks.size(); i++) {
//
//                System.out.printf("%s,", keyStacks.get(i));
//            }
//            System.out.println("\n\n");
//        }
//
//
//        JsonTraverser_status jsonTraverserStatus = new JsonTraverser_status();
//
//        wrong_command = false;
//        found_string_keys = false;
//        found_key_inside_keylist = false;
//        found_string_keys = false;
//
//
//        PointerInfo pointer_info = json_traverser(combos, keyStacks);
//
//        //Didnt found any key, so intead try to check if there is a key called "keys"
//        if (pointer_info.isFound_key() == false) {
//
//            int group_size = find_groupSize(keyStacks,pointer_info.pointer);
//            if (group_size > -1)
//            {
//                for (int i = 0; i< group_size;++i)
//                {
//                    String groupKey = String.format("%s%d","group",i+1);
//                    JSONObject jason = pointer_info.pointer.getJSONObject(groupKey);
//
//                    found_key_inside_keylist = check_list_keys(jason, keyStacks);
//
//                    Utils.imprimir_DEGUGG(
//                            "Estamos loopeando en los grupos y luego en el array" +
//                            "de keys para ver si encontramos un match ",DEBUGG);
//                    if (found_key_inside_keylist == true)
//                    {
//                        name_of_group_found = groupKey;
//                        break;
//                    }
//
//                }
//            }
//
////            found_key_inside_keylist = check_list_keys(pointer_info.pointer, keyStacks);
//            Utils.imprimir_DEGUGG(
//                    "No se encontró la key typeada, lo que indica " +
//                            "que talvez estamos dentro donde esta la llave \"keys\"*Tal vez*", DEBUGG);
//
//            //Utils.imprimir_DEGUGG
//            //It didnt found the string keys, which means that the command was wrong
//            if (found_string_keys == false) {
//                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_INCORRECT_COMMAND);
//                jsonTraverserStatus.setAccion_arrays(null);
//
//                Utils.imprimir_DEGUGG("El comando es incorrecto porque no encontró el string \"keys\"", DEBUGG);
//            }
//
//            //It found the the string keys, but if wasnt able to locate de command entered,
//            //either because it was wrong, or because the command is a long text
//            else if (found_string_keys == true && found_key_inside_keylist == false) {
//                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_INCORRECT_COMMAND);
//                jsonTraverserStatus.setAccion_arrays(null);
//
//
//                Utils.imprimir_DEGUGG("Encontró el string \"keys\" pero no logro localizar" +
//                        "la key dentra del array de keylist", DEBUGG);
//            }
//
//            //It was able to localize the key that was pressed
//            else if (found_string_keys == true && found_key_inside_keylist == true) {
//
//                JSONObject jason  = pointer_info.getPointer().getJSONObject(name_of_group_found);
//                JSONArray array_actions = jason.getJSONArray("actions");
//                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_CORRECT_COMMAND);
//                jsonTraverserStatus.setAccion_arrays(array_actions);
//
//                Utils.imprimir_DEGUGG("Logró encontrar la key presionada, entonces se va a ejucar" +
//                        "la acción", DEBUGG);
//            }
//
//        }
//
//        //Foun regular key, so itrs traversing the json as usal, like for example ctrl ctrl i would trigger this
//        else if (pointer_info.isFound_key() == true) {
//            jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_FOUND_SINGLE_KEY);
//            jsonTraverserStatus.setAccion_arrays(null);
//            Utils.imprimir_DEGUGG("Esta traversando el json", DEBUGG);
//
//            System.out.println("Imprimiendo keys disponibles");

    /// /            imprimiendo_keys(pointer_info.pointer);
//            JSONObject names = pointer_info.pointer.getJSONObject("names");
//            Iterator<String> keys = names.keys();
//            while (keys.hasNext())
//            {
//                String key = keys.next();
//                String value = names.getString(key);
//                System.out.printf("%s:%s\n",key,value);
//
//            }
//
//
//            //If the "special" keyword is around then do its commands
//            if (find_specialKey(pointer_info.pointer)) {
//                JSONArray array_special = pointer_info.getPointer().getJSONArray("special");
//
//                if (DEBUGG) {
//                    for (int i = 0; i < array_special.length(); i++) {
//                        Utils.imprimir_DEGUGG(String.format("Special command %s\n", array_special.get(i)), DEBUGG);
//                    }
//                }
//
//                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_FOUND_SPECIAL_KEY);
//                jsonTraverserStatus.setAccion_arrays(array_special);
//            }
//
//        }
//
//        return jsonTraverserStatus;
//
//
//    }

//
//    private PointerInfo json_traverser(JSONObject original, ArrayList<String> keystack) {
//        PointerInfo pointerInfo = new PointerInfo();
//        //ctrl ctrl i keys
//        JSONObject pointer = original;
//
//
//        for (String key : keystack) {
//            try {
//                Utils.imprimir_DEGUGG(String.format("Key traversad es : %s\n", key), DEBUGG);
//                pointer = pointer.getJSONObject(key);
//                pointerInfo.setPointer(pointer);
//                pointerInfo.setFound_key(true);
//            } catch (JSONException e) {
//                pointerInfo.setPointer(pointer);
//                pointerInfo.setFound_key(false);
//            }
//        }
//        return pointerInfo;
//    }
//
//    private boolean check_list_keys(JSONObject pointer, ArrayList<String> keystack) {
//        try {
//            JSONArray array = pointer.getJSONArray("keys");
//            found_string_keys = true;
//            for (int i = 0; i < array.length(); i++) {
//                String key = array.getString(i);
//                String last_key = keystack.getLast();
//
//                if (key.compareTo(last_key) == 0) {
//                    return true;
//                }
//
//
//            }
//        } catch (JSONException e) {
//        }
//
//        wrong_command = true;
//        return false;
//    }
//
//
//    private boolean find_specialKey(JSONObject pointer) {
//        Iterator<String> keys = pointer.keys();
//
//        Utils.imprimir_DEGUGG("\n\nIterando el json para encontrar la special key", DEBUGG);
//        while (keys.hasNext()) {
//            String key = keys.next();
//            Utils.imprimir_DEGUGG(String.format("key es %s\n", key), DEBUGG);
//            if (key.compareTo("special") == 0) {
//                Utils.imprimir_DEGUGG("Se encontró la llave special", DEBUGG);
//                return true;
//            }
//        }
//        Utils.imprimir_DEGUGG("No se encontró la llave especial", DEBUGG);
//        return false;
//
//    }
//
//    private void imprimiendo_keys(JSONObject pointer) {
//        Iterator<String> keys = pointer.keys();
//
//        while (keys.hasNext()) {
//            String key = keys.next();
//            System.out.printf("key : %s\n", key);
//        }
//    }
//
//    private int find_groupSize(ArrayList<String> keystack, JSONObject pointer) {
//        Iterator<String> keys = pointer.keys();
//
//        while (keys.hasNext()) {
//            String key = keys.next();
//
//            if (key.compareTo("group_size") == 0) {
//                int group_size = pointer.getInt(key);
//                return group_size;
//            }
//
//
//        }
//
//        return -1;
//    }


    @Getter
    @Setter
    class PointerInfo {

        private JSONObject pointer;
        private boolean found_key;
    }


    public JsonTraverser_statusv2 traverseV3(ArrayList<String> keystack)
    {
        JsonTraverser_statusv2 jsonTraverserStatusv2 = new JsonTraverser_statusv2();
        jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_NEUTRAL);

        JSONObject pointer = combos;

        if (found_actions)
        {
            System.out.println("yo aqui debo preguntar sobre los registros");
            String registro_seleccionado = keystack.getLast();
            if (contains_it(registers,registro_seleccionado))
            {
                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_CORRECT);
                jsonTraverserStatusv2.setAccion_arrays(actions);
                jsonTraverserStatusv2.setRegistro_seleccionado(registro_seleccionado);

                cleaning_everything_after();
                return  jsonTraverserStatusv2;
            }



        }



        for (int i = 0; i < keystack.size(); i++) {
            try {
                pointer = pointer.getJSONObject(keystack.get(i));
            } catch (JSONException e) {

                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
                next_keys.clear();
                return jsonTraverserStatusv2;
            }
        }
        get_desc_traversing(pointer);
        get_one_time_action(pointer,jsonTraverserStatusv2);
        get_registers(pointer,jsonTraverserStatusv2);
        find_actions(pointer);
        imprimir_registros();


        return  jsonTraverserStatusv2;
    }


    /**
     * -Stores in a list the available keys to traverse
     * -Prints the available keys to traverse
      */
    public void get_descTraversing()
    {

    }


    private void get_desc_traversing(JSONObject pointer)
    {
        try {
            JSONObject desc_traversing = pointer.getJSONObject("desc_traversing");


            Iterator<String> keys = desc_traversing.keys();

            while ( keys.hasNext())
            {
                String key = keys.next();
                System.out.printf("%s:%s\n",key,desc_traversing.get(key));

                next_keys.add(key);
            }

        } catch (JSONException e) {

        }
    }

    private void get_one_time_action(JSONObject pointer,JsonTraverser_statusv2 jsonTraverserStatusv2)
    {
        try{
            JSONArray one_time_actions = pointer.getJSONArray("one_time_actions");

            jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_INMMEDIATE_ACTION);
            jsonTraverserStatusv2.setAccion_arrays(one_time_actions);

        } catch (Exception e) {

        }
    }
    private void get_registers(JSONObject pointer,JsonTraverser_statusv2 jsonTraverserStatusv2)
    {
        try {
            JSONArray reg = pointer.getJSONArray("registers");
            registers = reg;
        } catch (JSONException e) {

        }
    }
    private void find_actions(JSONObject pointer)
    {
        try{
            JSONArray act =pointer.getJSONArray("actions");
            actions = act;
            found_actions = true;
        } catch (JSONException e) {

//            System.out.println("Didnt find actions my bad");

        }

    }

    private void cleaning_everything_after()
    {
        registers = null;
        next_keys.clear();
        found_actions = false;
        actions = null;
    }

    private void imprimir_registros()
    {
        if (found_actions && registers.length() > 0)
        {

            for (int i = 0; i < registers.length(); i++) {

                String reg = registers.getString(i);
                System.out.printf("Registro %s\n",reg);
            }
        }
    }

    private boolean contains_it(JSONArray jsonArray,String suspect)
    {


        for (int i = 0; i < jsonArray.length(); i++) {
           if (jsonArray.getString(i).compareTo(suspect) == 0)
           {
               return true;
           }
        }
        return false;
    }

}