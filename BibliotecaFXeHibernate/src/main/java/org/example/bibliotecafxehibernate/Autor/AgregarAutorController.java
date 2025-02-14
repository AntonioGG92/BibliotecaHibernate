package org.example.bibliotecafxehibernate.Autor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.bibliotecafxehibernate.HelloController;
import org.example.bibliotecafxehibernate.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AgregarAutorController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField nacionalidadField;

    private HelloController mainController;

    @FXML
    private void agregarAutor(ActionEvent event) {
        String nombre = nombreField.getText();
        String nacionalidad = nacionalidadField.getText();

        if (nombre.isEmpty() || nacionalidad.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        Autor autor = new Autor(nombre, nacionalidad);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(autor);
            transaction.commit();
            System.out.println("Autor guardado en la base de datos: " + autor);

            if (mainController != null) {
                mainController.cargarDatosAutor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
}