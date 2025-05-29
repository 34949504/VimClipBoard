package org.example.vimclip.Keypressed.Comandos;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public interface doing_action {

    default void do_command(Character reg){};
    default void do_command2(AtomicBoolean listenKeys){};
    default void do_command3(Character reg,int index){};
    default int do_command4(Character reg, ArrayList<Integer> index_array){
        return 0;
    };
    default void do_script(String script_path, JSONArray arguments){};

}
