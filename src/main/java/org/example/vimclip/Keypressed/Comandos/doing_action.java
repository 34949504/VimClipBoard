package org.example.vimclip.Keypressed.Comandos;

import java.util.concurrent.atomic.AtomicBoolean;

public interface doing_action {

    default void do_command(Character reg){};
    default void do_command2(AtomicBoolean listenKeys){};
    default void do_script(String script_path){};

}
