package org.example.vimclip;

import javafx.application.Platform;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.nio.file.Path;

@Getter
@Setter
public class ConfigMaster implements Observar {

    private String separator_when_getting_all_text;
    private String unprocessed_separator_when_getting_all_text;
    public JSONObject config;
    public Path configPath;

    double screen_width;
    double screen_height;



    private ClipboardViewer_config clipboardViewer_config;
    private Command_displayer_config command_displayer_config;

    public ConfigMaster(JSONObject config){
        this.config = config;
    }
    public void init()
    {
       separator_when_getting_all_text = config.getString("separator_when_getting_all_text");
        clipboardViewer_config = new ClipboardViewer_config().init();
        command_displayer_config = new Command_displayer_config().init();

    }




    @Getter
    @Setter
    public class ClipboardViewer_config {
        private double stage_height_percent;
        private double stage_width_percent;
        private int label_max_height;
        private String stage_edge_postition;

        private double stage_width;
        private double stage_height;

        public ClipboardViewer_config init() {



            JSONObject c = config.getJSONObject("clipboardViewer_config");
            stage_height_percent = c.getDouble("stage_height_percent");
            stage_width_percent = c.getDouble("stage_width_percent");
            label_max_height = c.getInt("label_max_height");
            stage_edge_postition =  c.getString("stage_edge_postition");

            calculate_stageDims();

            return this;
        }

        private void calculate_stageDims()
        {
            stage_width = screen_width* stage_width_percent / 100;
            stage_height = screen_height * stage_height_percent / 100;
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

    public void writingConfig()
    {
        System.out.println("writing to config");
        config.put("separator_when_getting_all_text",unprocessed_separator_when_getting_all_text);
        Utils.writeFile(configPath,config.toString(4));

    }

    @Override
    public void stage_closing() {
      writingConfig();
    }
}
