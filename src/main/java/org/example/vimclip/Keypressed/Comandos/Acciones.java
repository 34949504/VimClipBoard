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
    private ClipBoardListener clipBoardListener = new ClipBoardListener();
    private Listen_to_clipboard listenToClipboard;

    private ConfigMaster configMaster;


    public Acciones(RegistryManager registryManager, ConfigMaster configMaster) {
        this.registryManager = registryManager;
        this.configMaster = configMaster;

        clipBoardListener.setRegistryManager(registryManager);

        hashMap.put("get_first_value",new get_first_value());
        hashMap.put("remove_first_value",new remove_first_value());
        hashMap.put("get_values", new get_values());
        hashMap.put("remove_values",new remove_values());

        listenToClipboard = new Listen_to_clipboard();
        hashMap.put("clipboard_listener",listenToClipboard);


    }

    public void doCommand(Character reg, String command) {
        System.out.printf("Llamando do command?");
        System.out.println(command);
        hashMap.get(command).do_command(reg);
    }


    public int doCommand4(Character reg, ArrayList<Integer> index_list,String command)
    {
        return hashMap.get(command).do_command4(reg,index_list);
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

            //WARNING he comentado esto, eso antes era utilizado cuando mi
            //WARNING proyecto era mas rudimentario
            //WARNING Maybe this might  the clipboarddisplayer i dont shit the acciones but who cares they sucked

            if (clipBoardListener.isTimer_running()) {
                clipBoardListener.stop_timer();

            }
        }
        public void add_observer(Observar observer)
        {
            observers_list.add(observer);
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
                System.out.println("First val is "+firstValue);
                ClipboardUtils.setClipboardContentsWithRetry(first_val,5,100);
                return;
            }

            ClipboardUtils.setClipboardContentsWithRetry("",5,100);

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
                ClipboardUtils.setClipboardContentsWithRetry(contents.toString(),5,100);
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


