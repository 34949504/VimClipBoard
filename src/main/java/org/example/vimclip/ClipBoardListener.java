package org.example.vimclip;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Keypressed.ClipboardUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
public class ClipBoardListener implements Observar {

    private String previous_clipboard_content = null;

    @Getter(AccessLevel.NONE)
    private String contents = "";
    private Timer timer = new Timer();
    private boolean timer_running = false;
    private boolean skipped_first_time = false;

    private ArrayList<Observar> observers_list = null;
    private RegistryManager registryManager;

    private Character reg_selected = null;




    public void start_listener()
    {
        timer_running = true;
        skipped_first_time = false;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!skipped_first_time)
                {
                    previous_clipboard_content = ClipboardUtils.getClipboardContents();
                    ClipboardUtils.setClipboardContents("");
                    skipped_first_time = true;
                    System.out.println("Here skipped");
                    return;
                }

                String c = ClipboardUtils.getClipboardContents();

                if (c != null && c.compareTo(contents) != 0)
                {
                    System.out.printf("C is %s\n",c);
                    System.out.printf("Content  is %s\n",contents);
                    contents = c;
                    registryManager.addValue(reg_selected,contents);

                    for (Observar observador:observers_list)
                    {
                        observador.something_was_copied(contents);
                    }

                }
            }
        },0,100);
    }
    public void stop_timer()
    {
        timer.cancel();
        timer_running = false;
        skipped_first_time = false;
        timer = new Timer();
        ClipboardUtils.setClipboardContents(previous_clipboard_content);
        contents = "";

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

    //WARNING THIs code might be deep to fail in the future for using setobserverlist and addobserver omicuouslysne

}
