package org.example.vimclip;


import java.util.HashMap;

public class RegistryManager {

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

    public void loopy()
    {
        for (Character i : clipboardRegistries.keySet()) {
            System.out.println("key: " + i + " value: " + clipboardRegistries.get(i));
        }
    }


}
