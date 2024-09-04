module org.codex.codex {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.rmi;


    opens org.codex.codex to javafx.fxml;
    exports org.codex.codex;
    exports org.codex.codex.Controller;
    exports org.codex.codex.RMI to java.rmi;
}