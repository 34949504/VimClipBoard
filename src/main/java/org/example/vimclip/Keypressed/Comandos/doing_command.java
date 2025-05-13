package org.example.vimclip.Keypressed.Comandos;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public interface doing_command{

    default void do_command(Character reg){};
    default void do_command2(AtomicBoolean listenKeys){};

}
