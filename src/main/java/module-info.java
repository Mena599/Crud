module org.example.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Asegúrate de incluir este si usas SQL

    // Exporta el paquete para que FXML pueda acceder a tus controladores
    exports org.example.crud;
    opens org.example.crud to javafx.fxml;
}