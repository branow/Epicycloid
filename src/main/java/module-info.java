module com.kpi.epicycloid {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.kpi.epicycloid to javafx.fxml;
    exports com.kpi.epicycloid;
}