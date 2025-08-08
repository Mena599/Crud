package org.example.crud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class HomeController {

    @FXML
    private TableView<Usuario> usuariosTable;
    @FXML
    private TableColumn<Usuario, String> idColumn;
    @FXML
    private TableColumn<Usuario, String> nombreColumn;
    @FXML
    private TableColumn<Usuario, String> emailColumn;
    @FXML
    private TableColumn<Usuario, String> passwordColumn;

    private static final String DB_URL = "jdbc:sqlite:wallet.db";
    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Enlaza las columnas de la tabla a las propiedades de la clase Usuario
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Carga los datos de la base de datos
        loadUsuarios();
    }

    private void loadUsuarios() {
        listaUsuarios.clear();
        String sql = "SELECT id, nombre, email, password FROM usuarios";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaUsuarios.add(new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
            usuariosTable.setItems(listaUsuarios);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void handleAgregarUsuario() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Registro.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Añadir Nuevo Usuario");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadUsuarios(); // Recarga los datos al cerrar la ventana de registro
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error de Carga", "No se pudo abrir la ventana de registro.");
        }
    }

    @FXML
    private void handleModificarUsuario() {
        Usuario selectedUser = usuariosTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Lógica para abrir una ventana de modificación (similar a 'handleAgregarUsuario')
            // y pasar los datos del usuario seleccionado
            showAlert(Alert.AlertType.INFORMATION, "Modificar", "Modificar usuario con ID: " + selectedUser.getId());
        } else {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Seleccione un usuario para modificar.");
        }
    }

    @FXML
    private void handleEliminarUsuario() {
        Usuario selectedUser = usuariosTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "DELETE FROM usuarios WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, selectedUser.getId());
                    pstmt.executeUpdate();
                    loadUsuarios(); // Recarga los datos después de la eliminación
                    showAlert(Alert.AlertType.INFORMATION, "Eliminación Exitosa", "El usuario ha sido eliminado.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Error al eliminar el usuario: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Seleccione un usuario para eliminar.");
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
