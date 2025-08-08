package org.example.crud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField passwordField;

    private static final String USUARIO_CORRECTO = "root";
    private static final String PASSWORD_CORRECTO = "123admin";

    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText();
        String password = passwordField.getText();

        if (USUARIO_CORRECTO.equals(usuario) && PASSWORD_CORRECTO.equals(password)) {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Inicio de sesión exitoso!");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Home.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) usuarioField.getScene().getWindow();
                stage.setTitle("Gestión de Usuarios");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error de Carga", "No se pudo cargar la vista Home.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error de Credenciales", "Usuario o contraseña incorrectos.");
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