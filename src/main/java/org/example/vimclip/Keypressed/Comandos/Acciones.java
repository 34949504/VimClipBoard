package org.example.vimclip.Keypressed.Comandos;

import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Clipboard.ClipBoardListener;
import org.example.vimclip.Clipboard.ClipboardUtils;
import org.example.vimclip.ConfigMaster;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    private ConfigMaster configMaster;


    public Acciones(RegistryManager registryManager, ConfigMaster configMaster) {
        this.registryManager = registryManager;
        this.configMaster = configMaster;

        clipBoardListener.setRegistryManager(registryManager);

        hashMap.put("script", new do_script());
        hashMap.put("get_last_value",new get_lat_value());
        hashMap.put("get_first_value",new get_first_value());
        hashMap.put("get_all_values",new get_all_values());
        hashMap.put("remove_all_values",new remove_all_values());
        hashMap.put("remove_last_value",new remove_last_value());
        hashMap.put("remove_first_value",new remove_last_value());
        hashMap.put("get_value_by_number",new getValue());
        hashMap.put("remove_value_by_number",new removeValue());
        hashMap.put("get_values", new get_values());
        hashMap.put("remove_values",new remove_values());

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
    public int doCommand4(Character reg, ArrayList<Integer> index_list,String command)
    {
        return hashMap.get(command).do_command4(reg,index_list);
    }
    public  void doscript(String script_path, JSONArray arguments){

        hashMap.get("script").do_script(script_path,arguments);

    }


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
                System.out.println("Reg here is "+cur_reg);
                clipBoardListener.setReg_selected(cur_reg);
                System.out.println("Timer has started");

                for (Observar observer : observers_list)
                {
                    observer.isTimerOn(true);
                }
            }
        }

        @Override
        public void esc_was_pressed() {


            if (clipBoardListener.isTimer_running()) {
                clipBoardListener.stop_timer();

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

            Object last_value = registryManager.get_last_value(reg);
            if (last_value != null && last_value instanceof String last_val)
            {
                System.out.println("Se ha pasado al clipboard el last value");
                ClipboardUtils.setClipboardContents(last_val);
                return;
            }

            ClipboardUtils.setClipboardContents("");

        }
    }

    public class get_first_value implements  doing_action
    {
        @Override
        public void do_command(Character reg) {

            Object firstValue = registryManager.get_first_value(reg);
            if (firstValue != null && firstValue instanceof String first_val)
            {
                System.out.println("Se ha pasado al clipboard el first value");
                ClipboardUtils.setClipboardContents(first_val);
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
            Object value = registryManager.getValue(reg,index);

            if (value != null && value instanceof String val)
             ClipboardUtils.setClipboardContents(val);
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

    public class get_values implements  doing_action
    {
        @Override
        public int do_command4(Character reg, ArrayList<Integer> index_array) {

            if (index_array.size() > 0)
            {
                StringBuilder contents = new StringBuilder();
                for (int i = 0; i < index_array.size(); i++) {

                    int index = index_array.get(i);
                    Object reg_cont = registryManager.getValue(reg,index);
                    if (reg_cont instanceof String str) {
                        if (index_array.size() == 1)
                        {
                            contents.append( str);
                            continue;
                        }
                        else if (index_array.size()-1 == i)
                        {

                            contents.append( str);
                            continue;
                        }
                        contents.append(String.format("%s%s", str,configMaster.getSeparator_when_getting_all_text()));
                    }
                }
                ClipboardUtils.setClipboardContents(contents.toString());
                return 0;
            }

            System.out.println("No se pudo pasar a clipboard porque no hay index_array es  es menor o igual a 0");
            return -1;
        }
    }

    public class remove_values implements  doing_action
    {
        @Override
        public int do_command4(Character reg, ArrayList<Integer> index_array) {

            if (index_array.size() <=0)
                return -1;

            Collections.sort(index_array, Collections.reverseOrder());

            for (int i = 0; i < index_array.size(); i++) {

                int index = index_array.get(i);
                System.out.println("Index here removing is "+index);
                registryManager.removeValue(reg,index);
            }
            return 0;

        }
    }

    public class remove_first_value implements doing_action
    {

        @Override
        public void do_command(Character reg) {

            registryManager.removeValue(reg,0);
        }
    }
}


