package org.example.crud;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModificarUsuarioController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private static final String DB_URL = "jdbc:sqlite:wallet.db";
    private Usuario usuario;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        idField.setText(usuario.getId());
        nombreField.setText(usuario.getNombre());
        emailField.setText(usuario.getEmail());
        passwordField.setText(usuario.getPassword());
    }

    @FXML
    private void handleSave() {
        String id = idField.getText();
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, email);
                pstmt.setString(3, password);
                pstmt.setString(4, id);
                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Actualización Exitosa", "El usuario se ha modificado correctamente.");
                dialogStage.close(); // Cierra la ventana de edición
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Error al modificar el usuario: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
