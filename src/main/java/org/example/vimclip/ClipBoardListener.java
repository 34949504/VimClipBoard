package org.example.vimclip;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.vimclip.Keypressed.ClipboardUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public class ClipBoardListener implements Observar {

    private  ArrayList<String> copied_strings = new ArrayList<>();
    private String previous_clipboard_content = null;

    @Getter(AccessLevel.NONE)
    private String contents = "";
    private Timer timer = new Timer();
    private boolean timer_running = false;
    private boolean skipped_first_time = false;

    private ArrayList<Observar> observers_list = null;



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
                    return;
                }

                String c = ClipboardUtils.getClipboardContents();

                if (c != null && c.compareTo(contents) != 0)
                {
                    contents = c;
                    copied_strings.add(contents);

                    for (Observar observador:observers_list)
                    {
                        observador.something_was_copied(contents);
                    }

                }
            }
        },0,100);
    }
    public void clear_copied_strings()
    {
        copied_strings.clear();
    }
    public void stop_timer()
    {
        timer.cancel();
        timer_running = false;
        skipped_first_time = false;
        timer = new Timer();
        ClipboardUtils.setClipboardContents(previous_clipboard_content);

    }
    public void setObservers_list(ArrayList<Observar> observers_list)
    {
       this.observers_list = observers_list;
    }

    public boolean isTimer_running() {
        return timer_running;
    }
}
