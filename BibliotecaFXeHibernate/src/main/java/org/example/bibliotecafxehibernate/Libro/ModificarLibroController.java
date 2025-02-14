package org.example.bibliotecafxehibernate.Libro;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.bibliotecafxehibernate.HelloController;
import org.example.bibliotecafxehibernate.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ModificarLibroController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField publisherField;

    @FXML
    private TextField yearField;

    private Libro selectedBook; // Almacenar el libro seleccionado
    private HelloController mainController;

    // Método para recibir los datos del libro seleccionado
    public void setLibroData(Libro libro) {
        this.selectedBook = libro; // Guardar el libro seleccionado
        titleField.setText(libro.getTitulo());
        isbnField.setText(libro.getIsbn());
        authorField.setText(libro.getAutor().getNombre()); // Obtener nombre del autor
        publisherField.setText(libro.getEditorial());
        yearField.setText(String.valueOf(libro.getAnioPublicacion()));
    }

    // Método para manejar la acción de actualizar el libro
    @FXML
    private void actualizarLibro() {
        if (selectedBook != null) {
            String updatedTitle = titleField.getText();
            String updatedIsbn = isbnField.getText();
            String updatedAuthor = authorField.getText();
            String updatedPublisher = publisherField.getText();
            String updatedYear = yearField.getText();

            // Validación básica de campos
            if (updatedTitle.isEmpty() || updatedIsbn.isEmpty() || updatedAuthor.isEmpty() || updatedPublisher.isEmpty() || updatedYear.isEmpty()) {
                mostrarAlerta("Todos los campos son obligatorios.");
                return;
            }

            // Lógica para actualizar el libro en la base de datos
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                // Actualiza los atributos del libro seleccionado
                selectedBook.setTitulo(updatedTitle);
                selectedBook.setIsbn(updatedIsbn);
                selectedBook.getAutor().setNombre(updatedAuthor); // Ajustar autor
                selectedBook.setEditorial(updatedPublisher);
                selectedBook.setAnioPublicacion(Integer.parseInt(updatedYear));

                session.update(selectedBook); // Actualiza el libro en la base de datos
                transaction.commit();
                System.out.println("Libro actualizado en la base de datos: " + selectedBook);

                // Actualizar la lista en el controlador principal
                if (mainController != null) {
                    mainController.cargarDatosLibro(); // Recargar la lista de libros
                }

                // Cerrar la ventana después de actualizar
                ((Stage) titleField.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al actualizar el libro en la base de datos.");
            }
        }
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    // Método para mostrar un Alert
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
