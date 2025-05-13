package org.example.vimclip.Keypressed;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.util.ArrayList;
import java.util.List;

public class JsonTraverser {

    private JSONObject combos;


    public JsonTraverser(JSONObject combos)
    {
        this.combos = combos;
    }

    public Object traverse(ArrayList<String>keyStacks)
    {
        int ks_len = keyStacks.size();
        JSONPointer jsonPointer = new JSONPointer(keyStacks);
        Object object = null;

        try {
            object = jsonPointer.queryFrom(combos);
        }catch (Exception e)
        {
            System.out.println("Wrong path");
            return null;
        }

        if (object instanceof  JSONObject i)
        {
            try{
            String special = i.getString("special");
            return special;
            }catch (Exception e)
            {

            }
        }




        if (object instanceof String comando )
        {
            return comando;
        }


        return object;

    }







}
