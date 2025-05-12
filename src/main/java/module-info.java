module org.example.vimclip {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.kwhat.jnativehook;

    requires org.controlsfx.controls;
    requires static lombok;

    opens org.example.vimclip to javafx.fxml;
    exports org.example.vimclip;
}