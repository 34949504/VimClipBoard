package org.example.vimclip.Keypressed.Comandos;

import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.ClipBoardListener;
import org.example.vimclip.Keypressed.ClipboardUtils;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.example.vimclip.Utils;
import org.json.JSONArray;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Setter
@Getter
public class Acciones {

    private RegistryManager registryManager;
    private HashMap<String, doing_action> hashMap = new HashMap<>();
    private String copied_contents = null;
    private Robot robot;
    private boolean DEBUGG = false;
    private ClipBoardListener clipBoardListener = new ClipBoardListener();
    private Listen_to_clipboard listenToClipboard;
    private listen_for_numbers listenForNumbers;

    public Acciones(RegistryManager registryManager) {
        this.registryManager = registryManager;

//        hashMap.put("copy_to_register_replace", new copy_to_register_replace());
//        hashMap.put("copytext", new copyText());
//        hashMap.put("pass_to_ctrlv", new pass_to_ctrlv());
//        hashMap.put("copy_to_register_append", new copy_to_register_append());
//        hashMap.put("pass_to_ctrlv_all_contents",new pass_to_ctrlv_all_contents());
//        hashMap.put("pass_from_clipboard_to_register",new pasar_del_clipboard_a_registro());
        hashMap.put("script", new do_script());
        hashMap.put("get_last_value",new get_lat_value());
        hashMap.put("get_first_value",new get_first_value());
        hashMap.put("get_all_values",new get_all_values());
        hashMap.put("remove_all_values",new remove_all_values());
        hashMap.put("remove_last_value",new remove_last_value());
        hashMap.put("get_value_by_number",new getValue());
        hashMap.put("remove_value_by_number",new removeValue());

        listenToClipboard = new Listen_to_clipboard();
        hashMap.put("clipboard_listener",listenToClipboard);

        listenForNumbers = new listen_for_numbers();
        hashMap.put("listen_for_numbers",listenForNumbers);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void doCommand(Character reg, String command) {
        System.out.printf("Llamando do command?");
        System.out.println(command);
        hashMap.get(command).do_command(reg);
    }

    public void doCommand2(AtomicBoolean listenkeys, String command) {
        hashMap.get(command).do_command2(listenkeys);
    }

    public  void doCommand3(Character reg,int index,String command)
    {
        System.out.println("Do command was caled ");
        hashMap.get(command).do_command3(reg,index);
    }
    public  void doscript(String script_path, JSONArray arguments){

        hashMap.get("script").do_script(script_path,arguments);

    }


    /// ////////////////////////////////////////
    /// ////////////COMANDOS/////////////////////
    /// ////////////////////////////////////////

//
//    class copy_to_register_replace implements doing_action {
//
//        @Override
//        public void do_command(Character reg) {
//            registryManager.addValue(reg, getCopied_contents());
//            registryManager.loopy();
//        }
//    }
//
//    class copy_to_register_append implements doing_action {
//
//        @Override
//        public void do_command(Character reg) {
//
//
//            StringBuilder contents;
//            if (registryManager.getValue(reg) != null) {
//                contents = new StringBuilder(registryManager.getValue(reg));
//                Utils.imprimir_DEGUGG(String.format("El valor de registro no es nulo\n%s",contents),DEBUGG);
//
//
//            }else{
//                Utils.imprimir_DEGUGG("El valor de registro es nulo",DEBUGG);
//                contents = new StringBuilder("");
//            }
//
//            contents.append("\n\n");
//            contents.append(copied_contents);
//            Utils.imprimir_DEGUGG(String.format("Appending to contents %s",copied_contents),DEBUGG);
//            registryManager.setValue(reg,contents.toString());
//        }
//    }
//
//    class copyText implements doing_action {
//
//        @Override
//        public void do_command2(AtomicBoolean listenKeys) {
//
//            listenKeys.set(false);
//
//            getRobot().keyPress(KeyEvent.VK_CONTROL);
//            getRobot().keyPress(KeyEvent.VK_C);
//            getRobot().keyRelease(KeyEvent.VK_C);
//            getRobot().keyRelease(KeyEvent.VK_CONTROL);
//            getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
//            getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//
//
//            new Thread(() -> {
//                try {
//                    Thread.sleep(100);  // wait for clipboard to update
//                    String contents = ClipboardUtils.getClipboardContents();
//
//                    System.out.println("Clipboard contents: " + contents);
//                    setCopied_contents(contents);
//                    listenKeys.set(true);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//        }
//    }
//
//
//    class pass_to_ctrlv implements doing_action {
//
//        @Override
//        public void do_command(Character reg) {
//
//            String contents = getRegistryManager().getValue(reg);
//            if (contents == null) {
//                System.out.println("No hay nada");
//                return;
//            }
//            ClipboardUtils.setClipboardContents(contents);
//
//        }
//    }
//
//
//    class pass_to_ctrlv_all_contents implements doing_action {
//
//        @Override
//        public void do_command(Character reg) {
//
//            StringBuilder allcontents = new StringBuilder();
//            for (Character register:registryManager.getClipboardRegistries().keySet())
//            {
//                String contents =  registryManager.getValue(register);
//
//                if (contents != null)
//                {
//                    allcontents.append(contents);
//                    allcontents.append("\n\n");
//                }
//
//            }
//
//            ClipboardUtils.setClipboardContents(allcontents.toString());
//        }
//    }
//
//    class pasar_del_clipboard_a_registro implements  doing_action {
//
//        @Override
//        public void do_command(Character reg) {
//            System.out.printf("Pasandole al reg %c contenidos del clipboard",reg);
//        }
//    }

    class do_script implements doing_action{

        @Override
        public void do_script(String script_path,JSONArray arguments) {

            System.out.printf("Doing script");
            ArrayList<String> script = new ArrayList<>();
            script.add(script_path);

           if (arguments != null)
           {
               for (int i = 0; i < arguments.length(); i++) {

                   String arg = arguments.getString(i);
                   script.add(arg);
               }

           }
            String[] command = script.toArray(new String[0]);
            ProcessBuilder pb = new ProcessBuilder(command);
            try{
                pb.start();
            } catch (IOException e) {
                System.out.printf("Soehting went rong");
            }


        }
    }
    public class Listen_to_clipboard implements  doing_action, Observar
    {
        private ArrayList<Observar> observers_list = new ArrayList<>();
        private Character cur_reg = null;

        @Override
        public void do_command(Character reg) {

            cur_reg = reg;

            if (clipBoardListener.isTimer_running() == false) {
                clipBoardListener.start_listener();
                System.out.println("Timer has started");

                for (Observar observer : observers_list)
                {
                    observer.isTimerOn(true);
                }
            }
        }

        @Override
        public void esc_was_pressed() {
            System.out.println("Esc was pressed ");

            System.out.println("The cur reg is "+cur_reg);

            if (clipBoardListener.isTimer_running()) {
                clipBoardListener.stop_timer();
                System.out.println("Timer has stopped ");
                ArrayList<String> contents = clipBoardListener.getCopied_strings();


                for (String copied : contents) {
                    System.out.println(copied);
                    registryManager.addValue(cur_reg,copied);
                }
                for (Observar observer : observers_list)
                {
                    observer.isTimerOn(false);
                }

            }
        }
        public void add_observer(Observar observer)
        {
            observers_list.add(observer);
        }
    }

    public class get_lat_value implements  doing_action
    {
        @Override
        public void do_command(Character reg) {

            String last_value = registryManager.get_last_value(reg);
            if (last_value != null)
            {
                System.out.println("Se ha pasado al clipboard el last value");
                ClipboardUtils.setClipboardContents(last_value);
                return;
            }

            ClipboardUtils.setClipboardContents("");

        }
    }

    public class get_first_value implements  doing_action
    {
        @Override
        public void do_command(Character reg) {

            String firstValue = registryManager.get_first_value(reg);
            if (firstValue != null)
            {
                System.out.println("Se ha pasado al clipboard el first value");
                ClipboardUtils.setClipboardContents(firstValue);
                return;
            }

            ClipboardUtils.setClipboardContents("");

        }
    }

    public class get_all_values implements  doing_action
    {
        @Override
        public void do_command(Character reg) {

            String allValues = registryManager.get_all_values(reg);
            if (allValues != null)
            {
                System.out.println("Se ha pasado al clipboard all values ");
                ClipboardUtils.setClipboardContents(allValues);
                return;

            }

            ClipboardUtils.setClipboardContents("");

        }
    }

    public class remove_all_values implements  doing_action
    {
        @Override
        public void do_command(Character reg) {
            registryManager.clearArray(reg);
        }
    }

    public class remove_last_value implements  doing_action
    {
        @Override
        public void do_command(Character reg) {
            registryManager.remove_last_value(reg);
        }
    }

    public class listen_for_numbers implements doing_action,Observar
    {
        private ArrayList<Observar> observers_list = new ArrayList<>();
        private Character cur_reg = null;

        @Override
        public void do_command(Character reg) {

            for (Observar observer:observers_list)
            {
                observer.listenForNumbers();
            }

        }

        public void add_observer(Observar observer)
        {
            observers_list.add(observer);
        }
    }
    public  class getValue implements doing_action
    {
        @Override
        public void do_command3(Character reg,int index) {

            System.out.println("Getting value here in getVALUE");
            String value = registryManager.getValue(reg,index);

            if (value != null)
             ClipboardUtils.setClipboardContents(value);
        }
    }
    public  class removeValue implements doing_action
    {
        @Override
        public void do_command3(Character reg,int index) {

            System.out.println("Removing value value here in getVALUE");
            registryManager.removeValue(reg,index);
        }
    }
}


