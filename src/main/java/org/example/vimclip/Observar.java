package org.example.vimclip;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.vimclip.JavaFx.Controllers.ClipBoardViewer.BlocText;

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
    default public void stage_minimizing(){};
    default public void stage_has_been_resized(){}
    default public void separator_button_was_clicked(){}
    default public void stage_closing(){}
    default public void appShortcut_beenPressed(String shortcut){}

    default public void appShortcut_beenPressed(ArrayList<String> shortCut){}

    default public void move_scrollbar(String direction){}
    default public void bloc_was_created(){}
    default public void up_or_down_keyPressed(String direction){}
    default public void spaceBar_keyPressed(){}
    default public void block_was_clicked(){}
    default public void showHelpDialog(){}

    default public void showConfigDialog(){}

    default public void blocText_wasRightCicked(BlocText blocText){}


    default public void copyListener_is_on(){}
    default public void show_visual_cues(Image imageView){}





}
