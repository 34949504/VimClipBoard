package org.example.vimclip;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RegistryManager implements Observar {

    private HashMap<Character,String> clipboardRegistries = new HashMap<>();

    public RegistryManager(){


        for (char c = 'a'; c <= 'z'; c++) {
            clipboardRegistries.put(c, null);
        }

    }


    public void setValue(Character registry, String value)
    {
        clipboardRegistries.put(registry,value);
    }
    public String getValue(Character registry)
    {
        return clipboardRegistries.get(registry);
    }

    public HashMap<Character, String> getClipboardRegistries() {
        return clipboardRegistries;
    }

    public void loopy()
    {
        for (Character i : clipboardRegistries.keySet()) {
            System.out.println("key: " + i + " value: " + clipboardRegistries.get(i));
        }
    }

    @Override
    public void recibir_next_keys(ArrayList<String> nexkeys,ArrayList<String> desc) {
        System.out.println("---------------------------------------------");
        System.out.printf("Recibiendo keys:\n");
        for (int i = 0; i < nexkeys.size(); i++) {

            String key = nexkeys.get(i);
            String d = desc.get(i);

            System.out.printf("%s: %s\n",key,d);
        }

    }
}
