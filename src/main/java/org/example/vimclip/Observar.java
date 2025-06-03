package org.example.vimclip;

import org.json.JSONObject;

import java.util.ArrayList;

public interface Observar {

    default public void recibir_next_keys(ArrayList<String> nextKeys,ArrayList<String> desc){}
    default public void command_restarted(){}
    default public void esc_was_pressed(){}
    default public void isTimerOn(boolean flag){}
    default public void listenForNumbers(){}
    default public void something_was_copied(Object copiedString){}

    default public void stage_was_moved(){}
    default public void text_in_visualize_mode_modified(String newText){}
    default public void tab_changed(Character reg){}
    default public void blocs_were_deleted(ArrayList<Integer> selectedLabels){};
    default public void stage_closing(){};
    default public void stage_has_been_resized(){}
    default public void separator_button_was_clicked(){}


}
