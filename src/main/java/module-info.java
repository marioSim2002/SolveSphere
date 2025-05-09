module com.example.solvesphere {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires spring.security.crypto;
    requires java.sql;
    requires org.testng;
    requires annotations;
    requires fontawesomefx;
    requires okhttp3;
    requires java.json;
    requires com.google.gson;

    opens com.example.solvesphere to javafx.fxml;
    exports com.example.solvesphere;
    exports com.example.solvesphere.SecurityUnit;
    opens com.example.solvesphere.SecurityUnit to javafx.fxml;
    exports com.example.solvesphere.UserData;
    opens com.example.solvesphere.UserData to javafx.fxml;
    exports com.example.solvesphere.TestUnit;
    opens com.example.solvesphere.TestUnit to javafx.fxml;
    exports com.example.solvesphere.ServerUnit;
    opens com.example.solvesphere.ServerUnit to javafx.fxml;


}