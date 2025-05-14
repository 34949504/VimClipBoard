package org.example.vimclip.Keypressed;


import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Constants;
import org.example.vimclip.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JsonTraverser {

    private JSONObject combos;
    private boolean wrong_command;
    private boolean found_string_keys; // key "keys"
    private boolean found_key_inside_keylist;
    private boolean DEBUGG = true;
    String name_of_group_found = null;


    public JsonTraverser(JSONObject combos) {
        this.combos = combos;
    }

    public JsonTraverser_status traverse(ArrayList<String> keyStacks) {
        if (DEBUGG) {
            System.out.println("Current key stack is :");
            for (int i = 0; i < keyStacks.size(); i++) {

                System.out.printf("%s,", keyStacks.get(i));
            }
            System.out.println("\n\n");
        }


        JsonTraverser_status jsonTraverserStatus = new JsonTraverser_status();

        wrong_command = false;
        found_string_keys = false;
        found_key_inside_keylist = false;
        found_string_keys = false;


        PointerInfo pointer_info = json_traverser(combos, keyStacks);

        //Didnt found any key, so intead try to check if there is a key called "keys"
        if (pointer_info.isFound_key() == false) {

            int group_size = find_groupSize(keyStacks,pointer_info.pointer);
            if (group_size > -1)
            {
                for (int i = 0; i< group_size;++i)
                {
                    String groupKey = String.format("%s%d","group",i+1);
                    JSONObject jason = pointer_info.pointer.getJSONObject(groupKey);

                    found_key_inside_keylist = check_list_keys(jason, keyStacks);

                    Utils.imprimir_DEGUGG(
                            "Estamos loopeando en los grupos y luego en el array" +
                            "de keys para ver si encontramos un match ",DEBUGG);
                    if (found_key_inside_keylist == true)
                    {
                        name_of_group_found = groupKey;
                        break;
                    }

                }
            }

//            found_key_inside_keylist = check_list_keys(pointer_info.pointer, keyStacks);
            Utils.imprimir_DEGUGG(
                    "No se encontró la key typeada, lo que indica " +
                            "que talvez estamos dentro donde esta la llave \"keys\"*Tal vez*", DEBUGG);

            //Utils.imprimir_DEGUGG
            //It didnt found the string keys, which means that the command was wrong
            if (found_string_keys == false) {
                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_INCORRECT_COMMAND);
                jsonTraverserStatus.setAccion_arrays(null);

                Utils.imprimir_DEGUGG("El comando es incorrecto porque no encontró el string \"keys\"", DEBUGG);
            }

            //It found the the string keys, but if wasnt able to locate de command entered,
            //either because it was wrong, or because the command is a long text
            else if (found_string_keys == true && found_key_inside_keylist == false) {
                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_INCORRECT_COMMAND);
                jsonTraverserStatus.setAccion_arrays(null);


                Utils.imprimir_DEGUGG("Encontró el string \"keys\" pero no logro localizar" +
                        "la key dentra del array de keylist", DEBUGG);
            }

            //It was able to localize the key that was pressed
            else if (found_string_keys == true && found_key_inside_keylist == true) {

                JSONObject jason  = pointer_info.getPointer().getJSONObject(name_of_group_found);
                JSONArray array_actions = jason.getJSONArray("actions");
                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_CORRECT_COMMAND);
                jsonTraverserStatus.setAccion_arrays(array_actions);

                Utils.imprimir_DEGUGG("Logró encontrar la key presionada, entonces se va a ejucar" +
                        "la acción", DEBUGG);
            }

        }

        //Foun regular key, so itrs traversing the json as usal, like for example ctrl ctrl i would trigger this
        else if (pointer_info.isFound_key() == true) {
            jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_FOUND_SINGLE_KEY);
            jsonTraverserStatus.setAccion_arrays(null);
            Utils.imprimir_DEGUGG("Esta traversando el json", DEBUGG);

            System.out.println("Imprimiendo keys disponibles");
            imprimiendo_keys(pointer_info.pointer);


            //If the "special" keyword is around then do its commands
            if (find_specialKey(pointer_info.pointer)) {
                JSONArray array_special = pointer_info.getPointer().getJSONArray("special");

                if (DEBUGG) {
                    for (int i = 0; i < array_special.length(); i++) {
                        Utils.imprimir_DEGUGG(String.format("Special command %s\n", array_special.get(i)), DEBUGG);
                    }
                }

                jsonTraverserStatus.setStatus(JsonTraverser_status.STATUS_FOUND_SPECIAL_KEY);
                jsonTraverserStatus.setAccion_arrays(array_special);
            }

        }

        return jsonTraverserStatus;


    }


    private PointerInfo json_traverser(JSONObject original, ArrayList<String> keystack) {
        PointerInfo pointerInfo = new PointerInfo();
        //ctrl ctrl i keys
        JSONObject pointer = original;


        for (String key : keystack) {
            try {
                Utils.imprimir_DEGUGG(String.format("Key traversad es : %s\n", key), DEBUGG);
                pointer = pointer.getJSONObject(key);
                pointerInfo.setPointer(pointer);
                pointerInfo.setFound_key(true);
            } catch (JSONException e) {
                pointerInfo.setPointer(pointer);
                pointerInfo.setFound_key(false);
            }
        }
        return pointerInfo;
    }

    private boolean check_list_keys(JSONObject pointer, ArrayList<String> keystack) {
        try {
            JSONArray array = pointer.getJSONArray("keys");
            found_string_keys = true;
            for (int i = 0; i < array.length(); i++) {
                String key = array.getString(i);
                String last_key = keystack.getLast();

                if (key.compareTo(last_key) == 0) {
                    return true;
                }


            }
        } catch (JSONException e) {
        }

        wrong_command = true;
        return false;
    }


    private boolean find_specialKey(JSONObject pointer) {
        Iterator<String> keys = pointer.keys();

        Utils.imprimir_DEGUGG("\n\nIterando el json para encontrar la special key", DEBUGG);
        while (keys.hasNext()) {
            String key = keys.next();
            Utils.imprimir_DEGUGG(String.format("key es %s\n", key), DEBUGG);
            if (key.compareTo("special") == 0) {
                Utils.imprimir_DEGUGG("Se encontró la llave special", DEBUGG);
                return true;
            }
        }
        Utils.imprimir_DEGUGG("No se encontró la llave especial", DEBUGG);
        return false;

    }

    private void imprimiendo_keys(JSONObject pointer) {
        Iterator<String> keys = pointer.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            System.out.printf("key : %s\n", key);
        }
    }

    private int find_groupSize(ArrayList<String> keystack, JSONObject pointer) {
        Iterator<String> keys = pointer.keys();

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.compareTo("group_size") == 0) {
                int group_size = pointer.getInt(key);
                return group_size;
            }


        }

        return -1;
    }


    @Getter
    @Setter
    class PointerInfo {

        private JSONObject pointer;
        private boolean found_key;
    }


}
