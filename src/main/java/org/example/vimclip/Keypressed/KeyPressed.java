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
    JsonTraverser jsonTraverser;

    private JSONObject combos;
    private ArrayList<Observar> observadores_list = new ArrayList<>();


    public KeyPressed(RegistryManager registryManager, JSONObject combos, Acciones acciones) {

        this.registryManager = registryManager;
        this.jsonTraverser = new JsonTraverser(combos, observadores_list);
        this.combos = combos;

        addObserver(acciones.getListenToClipboard());
        acciones.getListenToClipboard().add_observer(this);


        acciones.getClipBoardListener().setObservers_list(new ArrayList<>(observadores_list));

        GlobalScreen.setEventDispatcher(new SwingDispatchService());

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);


    }

    public void nativeKeyPressed(NativeKeyEvent e) {


        int keycode = e.getKeyCode();
        String key = NativeKeyEvent.getKeyText(keycode).toLowerCase();


        if (keycode == NativeKeyEvent.VC_ESCAPE) {
            keyStack.clear();
        }


        if (e.getKeyCode() != NativeKeyEvent.VC_ESCAPE) {
            keyStack.add(key);
        }


        JsonTraverser_statusv2 js = jsonTraverser.traverseV3(keyStack);


        if (js.getAppShortcut() != null) {

            for (Observar observar : observadores_list) {
                observar.appShortcut_beenPressed(js.getAppShortcut());
            }

            keyStack.clear();
        }
        if (js.getStatus() == JsonTraverser_statusv2.STATUS_WRONG )
        {
            keyStack.clear();
        }

    }

    public void addObserver(Observar observador) {
        observadores_list.add(observador);


    }


}

