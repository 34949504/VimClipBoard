package org.example.vimclip.Keypressed;


import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Observar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


public class JsonTraverser implements Observar {

    private JsonTraversalState traversalState = new JsonTraversalState();
    private JSONObject combos;
    private ArrayList<Observar> observadores_list;
    JsonTraverser_statusv2 jsonTraverserStatusv2 = null;


    public JsonTraverser(JSONObject combos,ArrayList<Observar> observadores_list) {
        this.combos = combos;
        this.observadores_list = observadores_list;

    }

    public JsonTraverser_statusv2 traverseV3(ArrayList<String> keystack)
    {
        traversalState.setKeystack(keystack);
        jsonTraverserStatusv2 = new JsonTraverser_statusv2();
        jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_NEUTRAL);
        JSONObject pointer = combos;

        if (!validKeystack())
            return jsonTraverserStatusv2;

        if (if_norbit())
            return jsonTraverserStatusv2;

        if (if_foundActions()) //My realization, it only receives the action after the register has been selected xd
            return jsonTraverserStatusv2;





        System.out.println();
        for (int i = 0; i < keystack.size(); i++) {
            try {
                pointer = pointer.getJSONObject(keystack.get(i));
            } catch (JSONException e) {

                if (find_appshortcut(pointer,keystack.get(i)))
                {
                    return jsonTraverserStatusv2;
                }

                if (!finding_parameters_without_keyword(keystack.get(i),pointer,traversalState)) {
                    status_wrong();
                    return jsonTraverserStatusv2;
                }
            }
        }
        System.out.println();
        get_desc_traversing(pointer);
        get_registers(pointer,jsonTraverserStatusv2);
        find_actions(pointer);
        imprimir_nextKeys();
        find_scriptPath(pointer,jsonTraverserStatusv2);
        find_script_parameters(pointer,jsonTraverserStatusv2);
        find_same_script(pointer);

        System.out.println(traversalState.isFoundScript() + "" +traversalState.isFoundSameScript() );
        if (traversalState.isFoundScript()  || (traversalState.isFoundSameScript() && traversalState.getScriptParameters() != null))
        {
            System.out.println("Found script?");
            jsonTraverserStatusv2.setScript_parameters(traversalState.getScriptParameters());
            jsonTraverserStatusv2.setScript_path(traversalState.getScriptPath());
            jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_CORRECT);
            cleaning_everything_after();

        }


        return  jsonTraverserStatusv2;
    }

    private void get_desc_traversing(JSONObject pointer)
    {
        try {
            JSONObject desc_traversing = pointer.getJSONObject(AK.desc_traversing);
            traversalState.getNextKeys().clear();
            traversalState.getNextKeysDesc().clear();
            Iterator<String> keys = desc_traversing.keys();

            while ( keys.hasNext())
            {
                String key = keys.next();
                String desc = desc_traversing.getString(key);

                String s = find_norbit_structure(desc,key);
                if (s != null)
                    desc = s;


                traversalState.getNextKeys().add(key);
                traversalState.getNextKeysDesc().add(desc);
            }

        } catch (JSONException e) {

        }
    }
    private String find_norbit_structure(String desc,String key)
    {

        String[] split =desc.split(AK.script_separator);
        if (split.length == 2)
        {
            if (traversalState.getNorbit() == null) {
                traversalState.setNorbit(new JSONObject());
                System.out.println("Reduce redundancy no es null");}

            desc = split[0];
            String script_path = split[1];
            traversalState.getNorbit().put(key,script_path);
            System.out.println("Se encontro esa wea");

            return desc;
        }
        return null;
    }

    private void get_registers(JSONObject pointer,JsonTraverser_statusv2 jsonTraverserStatusv2)
    {
        try {
            JSONArray reg = pointer.getJSONArray("registers");
            traversalState.setRegisters(reg);
        } catch (JSONException e) {

        }
    }
    private void find_actions(JSONObject pointer)
    {
        try{
            JSONArray act =pointer.getJSONArray("actions");
            traversalState.setActions(act);
            traversalState.setFoundActions(true);
            find_actions_param(pointer);

            traversalState.getNextKeys().clear();
            traversalState.getNextKeysDesc().clear();

            for (int i = 0; i < traversalState.getRegisters().length(); i++) {

                String key = traversalState.getRegisters().getString(i);
                String desc = String.format("Registro %s",key);
                traversalState.getNextKeys().add(key);
                traversalState.getNextKeysDesc().add(desc);

            }
        } catch (JSONException e) {


        }

    }

    private void cleaning_everything_after()
    {
        traversalState = new JsonTraversalState();
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
        if (traversalState.getNextKeys().size() <= 0)return;

        for (int i = 0; i < traversalState.getNextKeys().size(); i++) {

            String key = traversalState.getNextKeys().get(i);
            String desc = traversalState.getNextKeysDesc().get(i);
        }
                for (Observar observador: observadores_list)
                {
                    observador.recibir_next_keys(traversalState.getNextKeys(),traversalState.getNextKeysDesc());
                }
    }



    private void find_scriptPath(JSONObject pointer,JsonTraverser_statusv2 jsonTraverserStatusv2)
    {
       try {
           traversalState.setScriptPath(pointer.getString("script_path"));
           traversalState.setFoundScript(true);
           System.out.printf("Founds script");

       } catch (JSONException e) {

       }
    }
    private void find_script_parameters(JSONObject pointer,JsonTraverser_statusv2 jsonTraverserStatusv2)
    {
        try {
            traversalState.setScriptParameters(pointer.getJSONArray("parameters"));
        } catch (JSONException e) {

        }
    }

    private void find_same_script(JSONObject pointer)
    {

        try {
            String same_script = pointer.getString("same_script");
            traversalState.setScriptPath(same_script);
            traversalState.setFoundSameScript( true);

        } catch (JSONException e) {

        }
    }
    private void find_actions_param(JSONObject pointer)
    {
       try {
           String ap = pointer.getString("actions_param");
           traversalState.setActionParam( ap);
           System.out.println("Actions params was found "+traversalState.getActionParam());

       } catch (JSONException e) {

           System.out.println("Actions params was not found ");

       }
    }

    private boolean validKeystack() {

        if (traversalState.getKeystack().size() <= 0) {
            System.out.println("Keystack bien pequeno");
            cleaning_everything_after();
            jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
            return false;
        }
        return  true;
    }

    private void status_wrong()
    {
        jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
        traversalState.getNextKeys().clear();
        traversalState.getNextKeysDesc().clear();
    }

    /**
     * Available keywords
     */
    public static class AK
    {
        public static String desc_traversing = "desc_traversing";
        public static String registers = "registers";
        public static String actions = "actions";
        public static String actions_param = "actions_param";
        public static String script_separator = "@@COOLSCRIPTSEPARATOR@@";
        public static String  same_script = "same_script";

    }


    private boolean if_norbit()
    {

        if (traversalState.getNorbit() != null)
        {
            String last_key = traversalState.getKeystack().getLast();

            Iterator<String> reduce_redunndacy_keys = traversalState.getNorbit().keys();
            ArrayList<String> reduce_redundancy_array = new ArrayList<>();

            while (reduce_redunndacy_keys.hasNext())
            {
                String key = reduce_redunndacy_keys.next();
                reduce_redundancy_array.add(key);
            }




            //Neither one contains it hence must be wrong
            if (!traversalState.getNextKeys().contains(last_key) && !reduce_redundancy_array.contains(last_key))
            {
                status_wrong();
                cleaning_everything_after();
                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
                return true;
            }
            else if (traversalState.nextKeys.contains(last_key) && reduce_redundancy_array.contains(last_key)) {

                String script_param = traversalState.getNorbit().getString(last_key);
                JSONArray params = new JSONArray();
                params.put(script_param);

                System.out.println("para m herei nthis shit is " + script_param);

                jsonTraverserStatusv2.setScript_parameters(params);
                jsonTraverserStatusv2.setScript_path(traversalState.getScriptPath());
                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_CORRECT);
                cleaning_everything_after();
                return true;
            }

        }
        return  false;
    }
    private boolean if_foundActions()
    {
        if (traversalState.isFoundActions())
        {
            String registro_seleccionado = traversalState.getKeystack().getLast();
            if (contains_it(traversalState.getRegisters(),registro_seleccionado))
            {
                jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_CORRECT);
                jsonTraverserStatusv2.setAccion_arrays(traversalState.getActions());
                jsonTraverserStatusv2.setRegistro_seleccionado(registro_seleccionado);
                jsonTraverserStatusv2.setComando_terminado(true);
                jsonTraverserStatusv2.setActions_params(traversalState.getActionParam());

                cleaning_everything_after();
                return  true;
            }
        }
        return false;
    }


    @Getter
    @Setter
    public class JsonTraversalState {

        public ArrayList<String> nextKeys = new ArrayList<>();
        public ArrayList<String> nextKeysDesc = new ArrayList<>();
        public JSONArray registers = null;
        public JSONArray actions = null;
        public boolean foundActions = false;
        public boolean foundScript = false;
        public boolean foundSameScript = false;

        public String scriptPath = null;
        public JSONArray scriptParameters = null;
        public ArrayList<Observar> observadoresList = new ArrayList<>();
        public String actionParam = null;

        public JSONObject norbit = null;
        public ArrayList<String> keystack = null;

        public JsonTraverser_statusv2 jsonTraverserStatus = null;
    }
    private boolean finding_parameters_without_keyword(String key,JSONObject pointer,JsonTraversalState jsonTraversalState)
    {
        try {
            JSONArray jsonArray = pointer.getJSONArray(key); // getting parameter
            jsonTraversalState.setScriptParameters(jsonArray);
            pointer  = new JSONObject();

            return true;


        } catch (JSONException e) {

            return false;
        }

    }

    private boolean find_appshortcut(JSONObject pointer,String key)
    {

        try {
            String text = pointer.getString(key);

            String[] split = text.split("@");
            if (split.length == 2)
            {

                String f = split[0];
                String s = split[1];

                if (f.compareTo("appshortcut") == 0)
                {
                    jsonTraverserStatusv2.setAppShortcut(s);
                    return true;
                }
            }

            return false;

        } catch (JSONException e) {
           return false;
        }
    }
}

//Explaining

//Norbit : Its a JsonObject that stores key pair, and is used when it finds something like this "Easy French@@COOLSCRIPTSEPARATOR@@https://www.youtube.com/@EasyFrench/videos"