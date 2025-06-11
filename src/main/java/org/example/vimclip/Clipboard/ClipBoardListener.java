package org.example.vimclip.Clipboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Observar;
import org.example.vimclip.RegistryManager;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
public class ClipBoardListener implements Observar {

    private String previous_clipboard_content = null;
    private Image previous_image = null;

    @Getter(AccessLevel.NONE)
    private String contents = "";
    private Timer timer = new Timer();
    private boolean timer_running = false;
    private boolean skipped_first_time = false;

    private ArrayList<Observar> observers_list = null;
    private RegistryManager registryManager;

    private Character reg_selected = null;

    private boolean hand_was_pressed_flag = false;


    String real_previous = "";


    public void start_listener()
    {
        timer_running = true;
        skipped_first_time = false;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                Object object_contetnt = ClipboardUtils.getClipboardContentsWithRetry(5,20);
                if (object_contetnt == null)
                {
                    System.out.println("Object contentd is null");
                    return;
                }

                if (!skipped_first_time)
                {
                    if (object_contetnt instanceof String string)
                    {
                        previous_clipboard_content = string;
                    }
                    else if (object_contetnt instanceof Image image)
                    {
                        previous_image = image;
                    }



                    ClipboardUtils.setClipboardContentsWithRetry("",5,100);
                    skipped_first_time = true;
                    System.out.println("Here skipped");
                    return;
                }

                String c = null;
                if (object_contetnt instanceof String string)
                {
                    c = string;
                    if (c != null && !c.isEmpty())
                    {

                        contents = c;

                        registryManager.addValue(reg_selected,contents);

                        System.out.println("calling here in clipboardlistener");
                        for (Observar observador:observers_list)
                        {
                            observador.something_was_copied(contents);
                        }
                        ClipboardUtils.setClipboardContentsWithRetry("",5,100);

                    }
                }
                else if (object_contetnt instanceof  Image image)
                {
                    System.out.println("image was detetected");
                    registryManager.addValue(reg_selected,image);

                    for (Observar observador:observers_list)
                    {
                        observador.something_was_copied(image);
                    }
                    ClipboardUtils.setClipboardContentsWithRetry("",5,100);

                }

            }
        },0,10);
    }
    public void stop_timer()
    {
        timer.cancel();
        timer_running = false;
        skipped_first_time = false;
        timer = new Timer();
        ClipboardUtils.setClipboardContentsWithRetry(previous_clipboard_content,5,100);
        contents = "";

    }

    private boolean comparing(String oldClip,String newClip)
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedNow = now.format(formatter);
        System.out.println("Current Timestamp:" + formattedNow);
        return  false;
    }
    public void setObservers_list(ArrayList<Observar> observers_list)
    {
       this.observers_list = observers_list;
    }

    public boolean isTimer_running() {
        return timer_running;
    }

    public void addObserver(Observar observer)
    {
        observers_list.add(observer);
    }

    @Override
    public void tab_changed(Character reg) {

        reg_selected = reg;
    }




    //WARNING THIs code might be deep to fail in the future for using setobserverlist and addobserver omicuouslysne

}
