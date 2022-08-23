module combgo {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    
    opens combgo to javafx.fxml;
    exports combgo;
}
