package org.example.vimclip;

import org.json.JSONObject;

import java.util.ArrayList;

public interface Observar {

    default public void recibir_next_keys(ArrayList<String> nextKeys,ArrayList<String> desc){}
    default public void command_restarted(){}
    default public void esc_was_pressed(){}
    default public void isTimerOn(boolean flag){}
    default public void listenForNumbers(){}
}
