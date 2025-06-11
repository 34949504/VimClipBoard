package org.example.vimclip.Keypressed;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
@Setter
public class JsonTraverser_statusv2 {

    private int status;
    String appShortcut = null;

    public static int STATUS_WRONG = 3; // clear keystack
}
