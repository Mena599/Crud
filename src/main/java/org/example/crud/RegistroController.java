package org.example.crud;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    // Conexión a la base de datos SQLite
    private static final String DB_URL = "jdbc:sqlite:wallet.db";

    @FXML
    private void handleRegistro() {
        String id = idField.getText();
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Se asegura de que la tabla 'usuarios' exista
            createTable(conn);

            // Prepara y ejecuta la consulta SQL para insertar un nuevo usuario
            String sql = "INSERT INTO usuarios (id, nombre, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, nombre);
                pstmt.setString(3, email);
                pstmt.setString(4, password);
                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "El usuario se ha registrado correctamente.");
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Ocurrió un error al registrar el usuario: " + e.getMessage());
        }
    }

    private void createTable(Connection conn) throws SQLException {
        // Crea la tabla 'usuarios' si no existe
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id TEXT PRIMARY KEY,"
                + "nombre TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE," // 'UNIQUE' para evitar correos duplicados
                + "password TEXT NOT NULL"
                + ");";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        idField.clear();
        nombreField.clear();
        emailField.clear();
        passwordField.clear();
    }
}