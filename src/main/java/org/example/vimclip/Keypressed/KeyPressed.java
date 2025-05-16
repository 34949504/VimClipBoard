package org.example.vimclip.Keypressed;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Constants;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Keypressed.Comandos.JsonTraverser_statusv2;
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

    Character extraSymbolTyped = null;
    boolean modifier_was_relased = false;


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

        JsonTraverser_statusv2 js = jsonTraverser.traverseV3(keyStack);
        JSONArray acciones_array = js.getAccion_arrays();


        int status = js.getStatus();


        if (status == JsonTraverser_statusv2.STATUS_INMMEDIATE_ACTION)
        {
            System.out.println("Doing inmmediate actions ");
        }
        else if (status == JsonTraverser_statusv2.STATUS_WRONG)
        {
            System.out.println("Was wrong");
            keyStack.clear();
            return;
        }
        else if (status == JsonTraverser_statusv2.STATUS_CORRECT)
        {

        }




//
//        if (jsonTraverserStatus.getStatus() ==  JsonTraverser_status.STATUS_NO_ACCION)
//            return;
//
//
//         JSONArray acciones_array = jsonTraverserStatus.getAccion_arrays();
//
//
//
////        String key = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
////        keyStack.add(key);
////
////        JsonTraverser_status jsonTraverserStatus = jsonTraverser.traverse(keyStack);
////        JSONArray acciones_array = null;
////
////
////        if (jsonTraverserStatus.getStatus()== JsonTraverser_status.STATUS_FOUND_SINGLE_KEY)
////        {
////            return; // dont do nothing
////        }
////        else if (jsonTraverserStatus.getStatus() == JsonTraverser_status.STATUS_INCORRECT_COMMAND)
////        {
////            System.out.println("Cleaning stack because command was incorrect");
////            keyStack.clear();
////            return;
////        }
////        else if (jsonTraverserStatus.getStatus() == JsonTraverser_status.STATUS_FOUND_SPECIAL_KEY)
////        {
////            acciones_array = jsonTraverserStatus.getAccion_arrays();
////
////        }
////        else if (jsonTraverserStatus.getStatus() == JsonTraverser_status.STATUS_CORRECT_COMMAND)
////        {
////            acciones_array = jsonTraverserStatus.getAccion_arrays();
////        }
////
////        if (acciones == null)
////        {
////            System.out.println("Acciones es null");
////            return;
////        }
////        if (acciones_array instanceof JSONArray){}else
////        {
////            System.out.println("No es json array y se neceista checar porque ");
////            return;
////        }



//
//            System.out.println("ES un jsonarray");
//
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
                Utils.imprimir_DEGUGG(String.format("Type:%s\nInstruction:%s\n",type,instruction),DEBUGG);



                Character reg = js.getRegistro_seleccionado() != null ? js.getRegistro_seleccionado().charAt(0):'p';
                System.out.printf("Reg is %c\n",reg);

                if (type.compareTo("command") == 0) {
                    System.out.println("Esto se esta llamando ");
                    acciones.doCommand(reg, instruction);

                } else if (type.compareTo("script") == 0) {

                } else if (type.compareTo("necessary") == 0) {

                    acciones.doCommand2(listenKeys, instruction);
                    skip_cleaning = true;
                } else {
                }
                if (!skip_cleaning)
                    keyStack.clear();
                skip_cleaning = false;
            }
//

    }

    public void nativeKeyReleased(NativeKeyEvent e) {

//        System.out.println("Released "+ e.getKeyChar());
//        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public boolean esc_pressed(NativeKeyEvent e) {


        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            keyStack.clear();
            return true;
        }
        return false;
    }


}


















