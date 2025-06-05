package org.example.vimclip.Keypressed;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Getter
@Setter
public class JsonTraverser_statusv2 {

    private JSONArray accion_arrays = new JSONArray();
    private int status;
    String registro_seleccionado = null;
    String script_path = null;
    JSONArray script_parameters = null;
    boolean comando_terminado = false;
    String actions_params = null;

    ArrayList<String> keystack = null;
    boolean foundAppShortcut = false;

    public static int STATUS_NEUTRAL = 0; //dont do nothing
    public static int STATUS_CORRECT = 1; // clear keystack and execute action
    public static int STATUS_INMMEDIATE_ACTION = 2; //Do inmmediate action but dont clear stack
    public static int STATUS_WRONG = 3; // clear keystack
}
