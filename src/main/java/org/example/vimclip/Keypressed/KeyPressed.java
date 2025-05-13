package org.example.vimclip.Keypressed;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Constants;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.RegistryManager;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class KeyPressed implements NativeKeyListener {

    private static String CTRL = "ctrl";
    private static String QUOTE = "quote";
    private static String SHIFT = "shift";

    public ArrayList<String> keyStack = new ArrayList<>();
    RegistryManager registryManager;
    private AtomicBoolean listenKeys = new AtomicBoolean(true);


    Acciones acciones;


    JsonTraverser jsonTraverser;


    public KeyPressed(RegistryManager registryManager, JSONObject combos) {

        this.registryManager = registryManager;
        this.jsonTraverser = new JsonTraverser(combos);
        this.acciones = new Acciones(registryManager);


        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);

    }

    public void nativeKeyPressed(NativeKeyEvent e) {

        if (!listenKeys.get())
            return;

        if (esc_pressed(e)) return;




        String key = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        keyStack.add(key);

        Object selection = jsonTraverser.traverse(keyStack);


        if (selection == null) {
            keyStack.clear();
            return;
        }

        if (selection instanceof String texto) {
            String[] split = texto.split("@");

            String type = split[0];
            String instruction = split[1];
            boolean skip_cleaning = false;


            if (type.compareTo("command") == 0) {
                Character reg = keyStack.getLast().charAt(0);
                acciones.doCommand(reg, instruction);
            } else if (type.compareTo("script") == 0) {

            } else if (type.compareTo("necessary") == 0) {
                acciones.doCommand2(listenKeys, instruction);
                skip_cleaning = true;
            } else {
                System.out.println("Ha habido un error");
            }

            if (!skip_cleaning)
                keyStack.clear();

            skip_cleaning = false;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
//        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
//        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    public boolean esc_pressed(NativeKeyEvent e) {


        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            keyStack.clear();
            return true;
        }
        return false;
    }


}


















