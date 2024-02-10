module cht.gzihx2_projekt_cht {
    requires javafx.controls;
    requires javafx.fxml;


    opens cht.projekt_cht to javafx.fxml;
    exports cht.projekt_cht;
}