package org.example.vimclip.Keypressed;


import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Observar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class JsonTraverser implements Observar {


    private JSONObject combos;

    private ArrayList<String> next_keys = new ArrayList<>();
    private ArrayList<String> next_keys_desc = new ArrayList<>();
    private JSONArray registers = null;
    private JSONArray actions = null;


    private boolean found_actions = false;
    private boolean found_script = false;
    private boolean found_same_script = false; // to avoid redundacy in json


    private String script_path = null;
    private JSONArray script_parameters = null;
    private ArrayList<Observar> observadores_list;


    JSONObject reduce_reduncdancy = null;

   String script_separator = "@@COOLSCRIPTSEPARATOR@@";

    public JsonTraverser(JSONObject combos,ArrayList<Observar> observadores_list) {
        this.combos = combos;
        this.observadores_list = observadores_list;

    }

    public JsonTraverser_statusv2 traverseV3(ArrayList<String> keystack)
    {


        JsonTraverser_statusv2 jsonTraverserStatusv2 = new JsonTraverser_statusv2();
        jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_NEUTRAL);

        JSONObject pointer = combos;

        if (reduce_reduncdancy != null)
        {
            System.out.println("Se llego a este punto?");
            String last_key = keystack.getLast();

            Iterator<String> reduce_redunndacy_keys = reduce_reduncdancy.keys();
            ArrayList<String> reduce_redundancy_array = new ArrayList<>();

            while (reduce_redunndacy_keys.hasNext())
            {
               String key = reduce_redunndacy_keys.next();
               reduce_redundancy_array.add(key);
            }




            //Neither one contains it hence must be wrong
            if (!next_keys.contains(last_key) && !reduce_redundancy_array.contains(last_key))
            {
                cleaning_everything_after();
                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
                return jsonTraverserStatusv2;
            }
            else if (next_keys.contains(last_key) && reduce_redundancy_array.contains(last_key)) {

                String script_param = reduce_reduncdancy.getString(last_key);
                JSONArray params = new JSONArray();
                params.put(script_param);

                System.out.println("para m herei nthis shit is " + script_param);

                jsonTraverserStatusv2.setScript_parameters(params);
                jsonTraverserStatusv2.setScript_path(script_path);
                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_CORRECT);
                cleaning_everything_after();

                return jsonTraverserStatusv2;
            }

        }


        if (keystack.size() <= 0)
        {
            System.out.println("Keystack bien pequeno");
            cleaning_everything_after();
            jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
            return jsonTraverserStatusv2;
        }
        if (found_actions)
        {
            String registro_seleccionado = keystack.getLast();
            if (contains_it(registers,registro_seleccionado))
            {
                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_CORRECT);
                jsonTraverserStatusv2.setAccion_arrays(actions);
                jsonTraverserStatusv2.setRegistro_seleccionado(registro_seleccionado);
                jsonTraverserStatusv2.setComando_terminado(true);

                cleaning_everything_after();
                return  jsonTraverserStatusv2;
            }
        }


        System.out.println();
        for (int i = 0; i < keystack.size(); i++) {
            try {
                pointer = pointer.getJSONObject(keystack.get(i));
            } catch (JSONException e) {



                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
                next_keys.clear();
                next_keys_desc.clear();
                return jsonTraverserStatusv2;
            }
        }
        System.out.println();
        get_desc_traversing(pointer);
        get_one_time_action(pointer,jsonTraverserStatusv2);
        get_registers(pointer,jsonTraverserStatusv2);
        find_actions(pointer);
        imprimir_nextKeys();
        find_scriptPath(pointer,jsonTraverserStatusv2);
        find_script_parameters(pointer,jsonTraverserStatusv2);
        find_same_script(pointer);

        if (found_script || (found_same_script && script_parameters != null))
        {
            jsonTraverserStatusv2.setScript_parameters(script_parameters);
            jsonTraverserStatusv2.setScript_path(script_path);
            jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_CORRECT);
            cleaning_everything_after();

        }


        return  jsonTraverserStatusv2;
    }

    private void get_desc_traversing(JSONObject pointer)
    {
        try {
            JSONObject desc_traversing = pointer.getJSONObject("desc_traversing");
            next_keys.clear();
            next_keys_desc.clear();
            Iterator<String> keys = desc_traversing.keys();

            while ( keys.hasNext())
            {
                String key = keys.next();
                String desc = desc_traversing.getString(key);
                System.out.println("Key aqui es "+key);

                String[] split =desc.split(script_separator);
                if (split.length == 2)
                {
                    if (reduce_reduncdancy == null) {reduce_reduncdancy = new JSONObject();
                        System.out.println("Reduce redundancy no es null");}

                    desc = split[0];
                    String script_path = split[1];
                    reduce_reduncdancy.put(key,script_path);
                    System.out.println("Se encontro esa wea");

                }

                next_keys.add(key);
                next_keys_desc.add(desc);
            }

        } catch (JSONException e) {

            System.out.println("Wasnt able to find edsc_traversing");



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

            next_keys.clear();
            next_keys_desc.clear();

            for (int i = 0; i < registers.length(); i++) {

                String key = registers.getString(i);
                String desc = String.format("Registro %s",key);
                next_keys.add(key);
                next_keys_desc.add(desc);



            }
        } catch (JSONException e) {

//            System.out.println("Didnt find actions my bad");

        }

    }

    private void cleaning_everything_after()
    {
        registers = null;
        next_keys.clear();
        next_keys_desc.clear();
        found_actions = false;
        found_script = false;
        actions = null;
        script_path = null;
        script_parameters = null;
        found_same_script = false;
        reduce_reduncdancy = null;

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

    public void imprimir_nextKeys()
    {
        if (next_keys.size() <= 0)return;

        for (int i = 0; i < next_keys.size(); i++) {

            String key = next_keys.get(i);
            String desc = next_keys_desc.get(i);
//            System.out.printf("%s:%s\n",key,desc);
        }


                for (Observar observador: observadores_list)
                {
                    System.out.println("Supposed to be called?");
                    observador.recibir_next_keys(next_keys,next_keys_desc);
                }
    }



    private void find_scriptPath(JSONObject pointer,JsonTraverser_statusv2 jsonTraverserStatusv2)
    {
       try {
           script_path = pointer.getString("script_path");
           found_script = true;
           System.out.printf("Founds script");

       } catch (JSONException e) {

       }
    }
    private void find_script_parameters(JSONObject pointer,JsonTraverser_statusv2 jsonTraverserStatusv2)
    {
        try {
            script_parameters= pointer.getJSONArray("parameters");
        } catch (JSONException e) {

        }
    }

    private void find_same_script(JSONObject pointer)
    {

        try {
            String same_script = pointer.getString("same_script");
            script_path = same_script;
            found_same_script = true;

        } catch (JSONException e) {

        }
    }

}