package org.example.vimclip.Keypressed;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import org.example.vimclip.Keypressed.Comandos.Acciones;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.example.vimclip.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;


public class KeyPressed implements NativeKeyListener, Observar {



    public ArrayList<String> keyStack = new ArrayList<>();
    RegistryManager registryManager;
    private AtomicBoolean listenKeys = new AtomicBoolean(true);

    private boolean DEBUGG = true;

    Acciones acciones;
    JsonTraverser jsonTraverser;

    private JSONObject combos;

    private ArrayList<String> strings_detonantes = new ArrayList<>();

    private ArrayList<Observar> observadores_list = new ArrayList<>();

    boolean isTimerOn = false;
    boolean listen_for_numbers = false;


    private StringBuilder number = new StringBuilder();
    private Character previousChar = null;
    private String previousCommand = null;
    private String current_action_param = null;





    public KeyPressed(RegistryManager registryManager, JSONObject combos,Acciones acciones) {

        this.registryManager = registryManager;
        this.jsonTraverser = new JsonTraverser(combos,observadores_list);
        this.acciones = acciones;
        this.combos = combos;

        addObserver(acciones.getListenToClipboard());
        acciones.getListenToClipboard().add_observer(this);

        addObserver(acciones.getListenForNumbers());
        acciones.getListenForNumbers().add_observer(this);

        acciones.getClipBoardListener().setObservers_list(observadores_list);

        getTriggerKeys();

        GlobalScreen.setEventDispatcher(new SwingDispatchService());

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

        int keycode = e.getKeyCode();
        String key = NativeKeyEvent.getKeyText(keycode).toLowerCase();

        System.out.println("Key pressed here is "+key);

        if (!listenKeys.get()) // To prevent funcs getting called whenever robot executes copy
            return;


        if (if_listen_for_number(keycode,key))
            return;


        if (keycode == NativeKeyEvent.VC_ESCAPE) {
            for (Observar observer:observadores_list)
            {
                observer.esc_was_pressed();
            }
            System.out.println("Clearing keys ");
            keyStack.clear();
            return;
        }
        if (isTimerOn)
        {
            System.out.println("Timer is on thats why returned");
            return;
        }



        if (e.getKeyCode() != NativeKeyEvent.VC_ESCAPE) {
            keyStack.add(key);
        }

        JsonTraverser_statusv2 js = jsonTraverser.traverseV3(keyStack);
        JSONArray acciones_array = js.getAccion_arrays();


        if (js.getRegistro_seleccionado() != null)
        {
            previousChar = js.getRegistro_seleccionado().charAt(0);
        }
        if (js.actions_params != null) {
            current_action_param = js.getActions_params();
        }
        System.out.println("Actions params here is "+current_action_param);

        int status = js.getStatus();


        if (status == JsonTraverser_statusv2.STATUS_WRONG)
        {
            for (Observar observador: observadores_list){
                observador.command_restarted();
            }
            keyStack.clear();
            return;
        }

        if (js.script_path != null)
        {
            System.out.println("Found script lol xd");;
            keyStack.clear();
            acciones.doscript(js.getScript_path(),js.getScript_parameters());

            for (Observar observador: observadores_list){
                observador.command_restarted();
            }
            return;
        }

            for (int i =0; i < acciones_array.length();++i)
            {
                System.out.println(acciones_array.get(i));

                String texto = (String )acciones_array.get(i);
                String[] split = texto.split("@");

                String type = split[0];
                String instruction = split[1];
                boolean skip_cleaning = false;

                Utils.imprimir_DEGUGG(String.format("Type:%s\nInstruction:%s\n",type,instruction),DEBUGG);



                Character reg = js.getRegistro_seleccionado() != null ? js.getRegistro_seleccionado().charAt(0):'p';
                System.out.printf("Reg is %c\n",reg);

                if (type.compareTo("command") == 0) {
                    System.out.println("Esto se esta llamando ");
                    acciones.doCommand(reg, instruction);

                }
                 else if (type.compareTo("necessary") == 0) {

                    acciones.doCommand2(listenKeys, instruction);
                    skip_cleaning = true;
                }
                else {
                }
                if (!skip_cleaning)
                    keyStack.clear();
                skip_cleaning = false;
            }


            if (js.isComando_terminado())
            {

                for (Observar observador: observadores_list){
                    observador.command_restarted();
                }
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

            System.out.println("Clearing keys ");
            keyStack.clear();
            return true;
        }
        return false;
    }

    public void getTriggerKeys()
    {
        Iterator<String> keys = combos.keys();

        while (keys.hasNext())
        {
            String detonante = keys.next();
            strings_detonantes.add(detonante);
        }
    }

    public void addObserver(Observar observador)
    {
        observadores_list.add(observador);


    }

    @Override
    public void isTimerOn(boolean flag) {
        System.out.println("This was called "+flag);
       isTimerOn = flag;
    }
    @Override
    public void listenForNumbers()
    {
        System.out.println("Listen for numbers is now true");
        listen_for_numbers = true;
    }

    private boolean if_listen_for_number(int keycode,String key)
    {


        if (listen_for_numbers == true)
        {
            System.out.println("Inside if statemtn listen for numbers ");
            boolean errors = false;
            int num = -1;

            if (keycode == NativeKeyEvent.VC_ESCAPE) //Didnt want to continue
            {
                errors = true;
            }
            if (keycode == NativeKeyEvent.VC_ENTER) { //Sumbits number
                if (number.length() <= 0)
                {
                    errors = true;
                }
                else {
                    int n = Integer.parseInt(number.toString()) - 1;
                    String command;
                    if (current_action_param.compareTo("get") == 0)
                    {
                        command ="get_value_by_number";
                    }
                    else if (current_action_param.compareTo("remove") == 0)
                    {
                        command = "remove_value_by_number";
                    }
                    else command = "fuck sake ";

                    acciones.doCommand3(previousChar,n,command);

                    listen_for_numbers = false;
                    previousChar = null;
                    current_action_param = null;
                    number.setLength(0);
                    return true;
                }
            }


            try {
                num = Integer.parseInt(key);
            } catch (NumberFormatException ex) {
                errors = true;
            }

            if (errors)
            {
                listen_for_numbers = false;
                previousChar = null;
                number.setLength(0);
                return true;
            }

            number.append(key);
            //If its esc, then its cancel, if it enter then done, everytime check if number is correct, only allow real numbers

        }



        return false;

    }


}

