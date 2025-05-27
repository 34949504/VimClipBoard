module org.example.vimclip {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.kwhat.jnativehook;

    requires org.controlsfx.controls;
    requires static lombok;
    requires org.json;

    opens org.example.vimclip to javafx.fxml;
    exports org.example.vimclip;
    exports org.example.vimclip.Keypressed;
    opens org.example.vimclip.Keypressed to javafx.fxml;
    exports org.example.vimclip.Keypressed.Comandos;
    opens org.example.vimclip.Keypressed.Comandos to javafx.fxml;
    exports org.example.vimclip.JavaFx;
    opens org.example.vimclip.JavaFx to javafx.fxml;
    exports org.example.vimclip.JavaFx.Controllers;
    opens org.example.vimclip.JavaFx.Controllers to javafx.fxml;
}