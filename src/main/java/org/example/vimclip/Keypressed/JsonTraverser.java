package org.example.vimclip.Keypressed;


import org.example.vimclip.Observar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonTraverser implements Observar {

    private JSONObject combos;
    private ArrayList<Observar> observadores_list;
    JsonTraverser_statusv2 jsonTraverserStatusv2 = null;


    public JsonTraverser(JSONObject combos, ArrayList<Observar> observadores_list) {
        this.combos = combos;
        this.observadores_list = observadores_list;

    }

    public JsonTraverser_statusv2 traverseV3(ArrayList<String> keystack) {
        jsonTraverserStatusv2 = new JsonTraverser_statusv2();
        JSONObject pointer = combos;

        if (!validKeystack(keystack))
            return jsonTraverserStatusv2;

        for (int i = 0; i < keystack.size(); i++) {
            try {
                pointer = pointer.getJSONObject(keystack.get(i));
            } catch (JSONException e) {

                if (find_appshortcut(pointer, keystack.get(i))) {
                    return jsonTraverserStatusv2;
                }

            }
        }


        return jsonTraverserStatusv2;
    }

    private boolean find_appshortcut(JSONObject pointer, String key) {

        try {
            String text = pointer.getString(key);

            String[] split = text.split("@");
            if (split.length == 2) {

                String f = split[0];
                String s = split[1];

                if (f.compareTo("appshortcut") == 0) {
                    jsonTraverserStatusv2.setAppShortcut(s);
                    return true;
                }
            }

            return false;

        } catch (JSONException e) {
            return false;
        }
    }

    private boolean validKeystack(ArrayList<String> keystack) {

        if (keystack.size() <= 0) {
            System.out.println("Keystack bien pequeno");
            jsonTraverserStatusv2.setStatus(JsonTraverser_statusv2.STATUS_WRONG);
            return false;
        }
        return true;
    }

}
