package org.example.vimclip;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class ConfigMaster {

    private String separator_when_getting_all_text;
    public JSONObject config;

    private ClipboardViewer_config clipboardViewer_config;
    private Command_displayer_config command_displayer_config;

    public ConfigMaster(JSONObject config){
        this.config = config;

        init();
        clipboardViewer_config = new ClipboardViewer_config().init();
        command_displayer_config = new Command_displayer_config().init();

    }
    private void init()
    {
       separator_when_getting_all_text = config.getString("separator_when_getting_all_text");
    }




    @Getter
    @Setter
    public class ClipboardViewer_config {
        private double mainPane_height_percent;
        private double mainPane_width_percent;
        private int label_max_height;
        private String stage_edge_postition;


        public ClipboardViewer_config init() {



            JSONObject c = config.getJSONObject("clipboardViewer_config");
            mainPane_height_percent = c.getDouble("mainPane_height_percent");
            mainPane_width_percent = c.getDouble("mainPane_width_percent");
            label_max_height = c.getInt("label_max_height");
            stage_edge_postition =  c.getString("stage_edge_postition");

            return this;
        }
        }

    @Getter
    @Setter
    public class Command_displayer_config
    {

    private int font_size;
    private String font_color;
    private String font_name;
    private String default_background;
    private boolean show_background_cues;

        public Command_displayer_config()
        {

        }
        private Command_displayer_config init()
        {

            JSONObject c = config.getJSONObject("command_displayer_config");


            font_size = c.getInt("font_size");
            font_color = c.getString("font_color");
            font_name = c.getString("font_name");
            default_background = c.getString("default_background");
            show_background_cues = c.getBoolean("show_background_cues");

            return this;
        }
    }
}
