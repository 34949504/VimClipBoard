package org.example.vimclip.Keypressed.Comandos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.vimclip.Keypressed.ClipboardUtils;
import org.example.vimclip.RegistryManager;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Setter
@Getter
public class Acciones {

    private RegistryManager registryManager;
    private HashMap<String, doing_command> hashMap = new HashMap<>();
    private String copied_contents = null;
    private Robot robot;

    public Acciones(RegistryManager registryManager) {
        this.registryManager = registryManager;

        hashMap.put("copy_to_register", new guardar_registro());
        hashMap.put("copyText", new exec_copy());
        hashMap.put("pass_to_ctrlv", new pass_to_ctrlv());
        hashMap.put("copy_to_register_append", new guardar_registro_append());
        hashMap.put("pass_to_ctrlv_all_contents",new pass_all_to_ctrlv());

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void doCommand(Character reg, String command) {
        hashMap.get(command).do_command(reg);
    }

    public void doCommand2(AtomicBoolean listenkeys, String command) {
        hashMap.get(command).do_command2(listenkeys);
    }


    /// ////////////////////////////////////////
    /// ////////////COMANDOS/////////////////////
    /// ////////////////////////////////////////


    class guardar_registro implements doing_command {

        @Override
        public void do_command(Character reg) {
            registryManager.setValue(reg, getCopied_contents());
            registryManager.loopy();
        }
    }

    class guardar_registro_append implements doing_command {

        @Override
        public void do_command(Character reg) {

            StringBuilder contents;
            if (registryManager.getValue(reg) != null) {
                contents = new StringBuilder(registryManager.getValue(reg));
            }else{
                contents = new StringBuilder("");
            }

            contents.append("\n\n");
            contents.append(copied_contents);
            registryManager.setValue(reg,contents.toString());
            System.out.println("whatu p");
        }
    }

    class exec_copy implements doing_command {

        @Override
        public void do_command2(AtomicBoolean listenKeys) {

            listenKeys.set(false);

            getRobot().keyPress(KeyEvent.VK_CONTROL);
            getRobot().keyPress(KeyEvent.VK_C);
            getRobot().keyRelease(KeyEvent.VK_C);
            getRobot().keyRelease(KeyEvent.VK_CONTROL);
            getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
            getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);


            new Thread(() -> {
                try {
                    Thread.sleep(100);  // wait for clipboard to update
                    String contents = ClipboardUtils.getClipboardContents();

                    System.out.println("Clipboard contents: " + contents);
                    setCopied_contents(contents);
                    listenKeys.set(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }


    class pass_to_ctrlv implements doing_command {

        @Override
        public void do_command(Character reg) {

            String contents = getRegistryManager().getValue(reg);
            if (contents == null) {
                System.out.println("No hay nada");
                return;
            }
            ClipboardUtils.setClipboardContents(contents);

        }
    }


    class pass_all_to_ctrlv implements doing_command {

        @Override
        public void do_command(Character reg) {

            StringBuilder allcontents = new StringBuilder();
            for (Character register:registryManager.getClipboardRegistries().keySet())
            {
                String contents =  registryManager.getValue(register);

                if (contents != null)
                {
                    allcontents.append(contents);
                    allcontents.append("\n\n");
                }

            }

            ClipboardUtils.setClipboardContents(allcontents.toString());
        }
    }
}


