package org.example.vimclip;


import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.ConfigLoader;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RegistryManager implements Observar {

    private HashMap<Character,ArrayList<Object>> clipboardRegistries = new HashMap<>();
    JSONObject configLoader;
    String getting_all_separator;


    public RegistryManager(JSONObject  configLoader){
        this.configLoader = configLoader;
        getting_all_separator = configLoader.getString("separator_when_getting_all_text");


        for (char c = 'a'; c <= 'z'; c++) {
            clipboardRegistries.put(c, new ArrayList<>());
        }
    }
    public ArrayList<Object> getArray(Character reg)
    {
        return clipboardRegistries.get(reg);
    }

    public void changeValue(Character registry,int index,String newText)
    {

        ArrayList<Object> arrray = clipboardRegistries.get(registry);
        arrray.set(index,newText);
    }
    public Object getValue(Character registry,int index)
    {
        ArrayList<Object> arrray = clipboardRegistries.get(registry);
        int length = arrray.size();

        boolean flag = checkArray_size(length,index,
                "Cannot get because array length is 0",
                String.format("Cannot get because index greater than array length ",index,length),
                String.format("Cannot get becaue index is below zero ",index));

        if (!flag)
            return null;

        return  arrray.get(index);
    }

    public void addValue(Character registry,Object value)
    {
        ArrayList<Object> arrray = clipboardRegistries.get(registry);
        arrray.add(value);
    }

    public void removeValue(Character registry,int index)
    {
        ArrayList<Object> arrray = clipboardRegistries.get(registry);
        int length = arrray.size();

        boolean flag = checkArray_size(length,index,
                "Cannot removed because array length is 0",
                String.format("Cannot removed because index greater than array length ",index,length),
                String.format("Cannot remove becaue index is below zero ",index));

        if (!flag)
            return;


        System.out.println("Removing from registry");
        arrray.remove(index);
    }
    public void removeLastValue(Character registry,int index)
    {

        ArrayList<Object> arrray = clipboardRegistries.get(registry);
        int length = arrray.size();
        if (length <=0)
        {
            System.out.println("Size <= 0 ");
            return;
        }

        arrray.removeLast();
    }
    public void removeRange(Character registry,int start_index,int end_index)
    {

        ArrayList<Object> arrray = clipboardRegistries.get(registry);
        int length = arrray.size();
    }
    public void clearArray(Character registry)
    {

        ArrayList<Object> arrray = clipboardRegistries.get(registry);
        arrray.clear();
    }

    public HashMap<Character, ArrayList<Object>> getClipboardRegistries() {
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
    private boolean checkArray_size(int length,int index,String... texts)
    {

        if (length ==0)
        {
            System.out.println(texts[0]);
            return false;
        }
        if (index >= length)
        {
            System.out.println(texts[1]);
            return false; }
        if (index < 0)
        {
            System.out.println(texts[2]);
            return false;
        }

        return true;
    }

    public int get_array_size(Character reg)
    {
        return clipboardRegistries.get(reg).size();
    }
    public Object get_last_value(Character reg)
    {
        ArrayList<Object> array = clipboardRegistries.get(reg);
        int length = array.size();

        if (length <=0)
        {
            System.out.printf("Cannot return last because array size is %d\n",length);
            return null;
        }

        return  array.getLast();

    }

    public Object get_first_value(Character reg)
    {
        ArrayList<Object> array = clipboardRegistries.get(reg);
        int length = array.size();

        if (length <=0)
        {
            System.out.printf("Cannot return first because array size is %d\n",length);
            return null;
        }

        return  array.getFirst();

    }
    //Get all values only allows strings
    public String get_all_values(Character reg)
    {
        StringBuilder contents = new StringBuilder();
        ArrayList<Object> array = clipboardRegistries.get(reg);
        int length = array.size();

        if (length <=0)
        {
            System.out.printf("Cannot return because array size is %d\n",length);
            return null;
        }

        for (Object str: array)
        {
            if (str instanceof String string) {
                contents.append(String.format("%s%s", string, getting_all_separator));
                System.out.println("separator is "+getting_all_separator);
            }
        }

        return  contents.toString();

    }
    public void remove_last_value(Character reg)
    {

        ArrayList<Object> array = clipboardRegistries.get(reg);
        int length = array.size();

        if (length <=0)
        {
            System.out.printf("Cannot remove last because array size is %d\n",length);
        }

        array.removeLast();
    }
}
