module opamp.combgo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    
    opens opamp.combgo to javafx.fxml;
    exports opamp.combgo;
}
