package org.example.bibliotecafxehibernate.Autor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bibliotecafxehibernate.HelloController;
import org.example.bibliotecafxehibernate.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ModificarAutorController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField nacionalidadField;

    private Autor selectedAutor;
    private HelloController mainController;

    // Método para recibir los datos del autor seleccionado
    public void setAutorData(Autor autor) {
        this.selectedAutor = autor;
        nombreField.setText(autor.getNombre());
        nacionalidadField.setText(autor.getNacionalidad());
    }

    // Método para manejar la acción de actualizar el autor
    @FXML
    private void actualizarAutor() {
        if (selectedAutor != null) {
            String updatedNombre = nombreField.getText();
            String updatedNacionalidad = nacionalidadField.getText();

            // Validación básica de campos
            if (updatedNombre.isEmpty() || updatedNacionalidad.isEmpty()) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            // Lógica para actualizar el autor en la base de datos
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                // Actualiza los atributos del autor seleccionado
                selectedAutor.setNombre(updatedNombre);
                selectedAutor.setNacionalidad(updatedNacionalidad);

                session.update(selectedAutor); // Actualiza el autor en la base de datos
                transaction.commit();
                System.out.println("Autor actualizado en la base de datos: " + selectedAutor);

                // Actualizar la lista en el controlador principal
                if (mainController != null) {
                    mainController.cargarDatosAutor(); // Llama al método para recargar los datos
                }

                // Cerrar la ventana después de actualizar
                ((Stage) nombreField.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al actualizar el autor en la base de datos.");
            }
        }
    }

    // Método para mostrar un Alert
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
}