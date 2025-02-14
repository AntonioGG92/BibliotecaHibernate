package org.example.bibliotecafxehibernate.Prestamos;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.example.bibliotecafxehibernate.HelloController;
import org.example.bibliotecafxehibernate.Libro.Libro;
import org.example.bibliotecafxehibernate.Socios.Socio;
import org.example.bibliotecafxehibernate.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class RegistrarPrestamoController {

    @FXML
    private ComboBox<Libro> libroComboBox;

    @FXML
    private ComboBox<Socio> socioComboBox;

    @FXML
    private DatePicker fechaPrestamoPicker;

    @FXML
    private DatePicker fechaDevolucionPicker;

    private HelloController mainController;

    @FXML
    public void initialize() {
        cargarLibros();
        cargarSocios();
    }

    private void cargarLibros() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            libroComboBox.getItems().addAll(session.createQuery("FROM Libro", Libro.class).list());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarSocios() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            socioComboBox.getItems().addAll(session.createQuery("FROM Socio", Socio.class).list());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registrarPrestamo() {
        Libro libroSeleccionado = libroComboBox.getValue();
        Socio socioSeleccionado = socioComboBox.getValue();
        LocalDate fechaPrestamo = fechaPrestamoPicker.getValue();
        LocalDate fechaDevolucion = fechaDevolucionPicker.getValue();

        // Validaciones básicas
        if (libroSeleccionado == null || socioSeleccionado == null || fechaPrestamo == null || fechaDevolucion == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        if (fechaDevolucion.isBefore(fechaPrestamo)) {
            mostrarAlerta("La fecha de devolución no puede ser anterior a la fecha de préstamo.");
            return;
        }

        Prestamos prestamo = new Prestamos(
                libroSeleccionado,
                socioSeleccionado,
                fechaPrestamo,
                fechaDevolucion,
                Prestamos.ESTADO_ACTIVO
        );

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(prestamo);
            transaction.commit();
            System.out.println("Préstamo registrado en la base de datos: " + prestamo);

            // Recargar la tabla de préstamos en la ventana principal
            if (mainController != null) {
                mainController.cargarDatosPrestamos();
            }

            // Cerrar la ventana
            ((Stage) libroComboBox.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al registrar el préstamo en la base de datos.");
        }
    }

    /**
     * Método agregado para registrar la devolución del préstamo.
     * Este método debe completarse con la lógica necesaria.
     */
    @FXML
    public void registrarDevolucion() {
        System.out.println("Registrar devolución no implementado aún.");
        mostrarAlerta("Función de registrar devolución pendiente de implementación.");
    }

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
