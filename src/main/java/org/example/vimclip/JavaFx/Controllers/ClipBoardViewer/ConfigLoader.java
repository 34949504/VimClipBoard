package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.Getter;
import org.example.vimclip.Observar;
import org.json.JSONObject;

@Getter
public class ConfigLoader implements Observar {
    JSONObject config;
    int mainPane_defaultHeight;
    int mainPane_defaultWidth;
    int label_maxHeight;

    int stage_currentWidth;
    int stage_currentHeight;

    int stage_defaultX; // if user wants to go back to default
    int stage_defaultY;

    int stage_currentX;
    int stage_currentY;

    String currentEdge;
    SharedInfo sharedInfo = new SharedInfo();

    int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();


    public ConfigLoader(JSONObject config,SharedInfo sharedInfo) {
        this.config = config;
        this.sharedInfo = sharedInfo;
        initialize_mainPaine_dims();
        initialize_stage_position();

        label_maxHeight = config.getInt("label_max_height");

    }

    private void initialize_mainPaine_dims() {
        Rectangle2D rectangle2D = Screen.getPrimary().getBounds();
        int width = (int) rectangle2D.getWidth();
        int height = (int) rectangle2D.getHeight();
        int height_percent = config.getInt("mainPane_height_percent");
        int width_percent = config.getInt("mainPane_width_percent");

        mainPane_defaultHeight = height * height_percent / 100;
        mainPane_defaultWidth = width * width_percent / 100;

        sharedInfo.getMainPane().setPrefHeight(mainPane_defaultHeight);
        sharedInfo.getMainPane().setPrefWidth(mainPane_defaultWidth);

    }
    private void initialize_stage_position()
    {
        calculating_default_postition();
        sharedInfo.getStage().setX(stage_defaultX);
        sharedInfo.getStage().setY(stage_defaultY);

        stage_currentX = stage_defaultX;
        stage_currentY = stage_defaultY;
    }

    private void calculating_default_postition()
    {
        /**
         * topRight_edge
         * topLeft_edge
         * bottomRight_edge
         * bottomLeft_edge
         */
        currentEdge = config.getString("stage_edge_postition");
        position_stage_atEgde(currentEdge);



    }
    public void position_stage_atEgde(String edge)
    {

        if (edge.equals("topRight_edge")) {
            stage_defaultX = screenWidth - mainPane_defaultWidth;
            stage_defaultY = 0;
        } else if (edge.equals("topLeft_edge")) {
            stage_defaultX = 0;
            stage_defaultY = 0;
        } else if (edge.equals("bottomRight_edge")) {
            stage_defaultX = screenWidth - mainPane_defaultWidth;
            stage_defaultY = screenHeight - mainPane_defaultHeight;
        } else if (edge.equals("bottomLeft_edge")) {
            stage_defaultX = 0;
            stage_defaultY = screenHeight - mainPane_defaultHeight;
        } else {
            // Fallback (optional)
            stage_defaultX = (screenWidth - mainPane_defaultWidth) / 2;
            stage_defaultY = (screenHeight - mainPane_defaultHeight) / 2;
        }
    }

    @Override
    public void stage_was_moved() {

        stage_currentX = (int)sharedInfo.getStage().getWidth();
        stage_currentY = (int)sharedInfo.getStage().getHeight();
    }

    public void setCurrentEdge(String currentEdge) {
        this.currentEdge = currentEdge;
    }
}
