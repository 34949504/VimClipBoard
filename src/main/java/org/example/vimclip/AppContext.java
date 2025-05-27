package org.example.vimclip;

import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Keypressed.KeyPressed;
import org.json.JSONObject;

public class AppContext {

    public static KeyPressed keyPressed;
    public static RegistryManager registryManager;
    public static JSONObject config;
    public static Acciones acciones;

}
