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
import org.example.vimclip.Utils;
import org.json.JSONArray;
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

    private boolean DEBUGG = true;


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

        if (esc_pressed(e)) return; //CLEARING KEYS




        String key = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        keyStack.add(key);

        JsonTraverser_status jsonTraverserStatus = jsonTraverser.traverse(keyStack);
        JSONArray acciones_array = null;


        if (jsonTraverserStatus.getStatus()== JsonTraverser_status.STATUS_FOUND_SINGLE_KEY)
        {
            return; // dont do nothing
        }
        else if (jsonTraverserStatus.getStatus() == JsonTraverser_status.STATUS_INCORRECT_COMMAND)
        {
            System.out.println("Cleaning stack because command was incorrect");
            keyStack.clear();
            return;
        }
        else if (jsonTraverserStatus.getStatus() == JsonTraverser_status.STATUS_FOUND_SPECIAL_KEY)
        {
            acciones_array = jsonTraverserStatus.getAccion_arrays();

        }
        else if (jsonTraverserStatus.getStatus() == JsonTraverser_status.STATUS_CORRECT_COMMAND)
        {
            acciones_array = jsonTraverserStatus.getAccion_arrays();
        }

        if (acciones == null)
        {
            System.out.println("Acciones es null");
            return;
        }
        if (acciones_array instanceof JSONArray){}else
        {
            System.out.println("No es json array y se neceista checar porque ");
            return;
        }


            System.out.println("ES un jsonarray");

            for (int i =0; i < acciones_array.length();++i)
            {
                System.out.println(acciones_array.get(i));

                String texto = (String )acciones_array.get(i);
                String[] split = texto.split("@");

                String type = split[0];
                String instruction = split[1];
                boolean skip_cleaning = false;

                if (Utils.check_that_instruction_exist(instruction) == false)
                {

                }
                Utils.imprimir_DEGUGG(String.format("%s\n%s\n",type,instruction),DEBUGG);



                if (type.compareTo("command") == 0) {
                    Character reg = keyStack.getLast().charAt(0);
                    acciones.doCommand(reg, instruction);
                } else if (type.compareTo("script") == 0) {

                } else if (type.compareTo("necessary") == 0) {

                    System.out.printf("Instruccion es %s\n",instruction);
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


















