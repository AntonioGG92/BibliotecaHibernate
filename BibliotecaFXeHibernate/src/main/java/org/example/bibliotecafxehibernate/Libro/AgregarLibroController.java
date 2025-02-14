package org.example.bibliotecafxehibernate.Libro;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.example.bibliotecafxehibernate.Autor.Autor;
import org.example.bibliotecafxehibernate.HelloController;
import org.example.bibliotecafxehibernate.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AgregarLibroController {

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

    private HelloController mainController;

    /**
     * Libro en edición.
     * - null => estamos creando un libro nuevo.
     * - no null => estamos modificando un libro existente.
     */
    private Libro libroEnEdicion;

    /**
     * Método que se llama al pulsar el botón "Agregar" o "Guardar".
     * Si libroEnEdicion es null, crea un libro nuevo.
     * Si libroEnEdicion no es null, modifica el libro existente.
     */
    @FXML
    private void AgregarLibro(ActionEvent event) {
        String titulo = titleField.getText();
        String isbn = isbnField.getText();
        String nombreAutor = authorField.getText();
        String editorial = publisherField.getText();
        String anio_publicacion = yearField.getText();

        if (titulo.isEmpty() || isbn.isEmpty() || nombreAutor.isEmpty()
                || editorial.isEmpty() || anio_publicacion.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(anio_publicacion);
        } catch (NumberFormatException e) {
            System.out.println("El año de publicación debe ser un número válido.");
            return;
        }

        // ────────────────────────── Buscar o crear Autor ──────────────────────────
        Autor autor;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            autor = session.createQuery("FROM Autor WHERE nombre = :nombreAutor", Autor.class)
                    .setParameter("nombreAutor", nombreAutor)
                    .uniqueResult();

            if (autor == null) {
                // Crear nuevo autor si no existe
                autor = new Autor();
                autor.setNombre(nombreAutor);
                autor.setNacionalidad("Desconocida");

                Transaction transaction = session.beginTransaction();
                session.persist(autor);
                transaction.commit();

                System.out.println("Nuevo autor creado: " + autor.getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // ────────────────────────── Crear o editar Libro ──────────────────────────
        // Si libroEnEdicion es null => estamos creando un libro nuevo
        // Si no es null => estamos modificando
        if (libroEnEdicion == null) {
            // ─── Crear libro nuevo ───────────────────────────────────────────────
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setIsbn(isbn);
            libro.setAutor(autor);
            libro.setPublisher(editorial);
            libro.setYear(anio);

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.persist(libro);
                transaction.commit();
                System.out.println("Libro guardado en la base de datos: " + libro);

                if (mainController != null) {
                    mainController.cargarDatosLibro();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // ─── Modificar libro existente ──────────────────────────────────────
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                // Recargar el libro de la BD para asegurarnos de que esté sincronizado
                Libro libroExistente = session.get(Libro.class, libroEnEdicion.getId());
                if (libroExistente == null) {
                    System.out.println("No se encontró el libro en la BD para editar.");
                    transaction.rollback();
                    return;
                }

                // Actualizar campos
                libroExistente.setTitulo(titulo);
                libroExistente.setIsbn(isbn);
                libroExistente.setAutor(autor);
                libroExistente.setPublisher(editorial);
                libroExistente.setYear(anio);

                session.merge(libroExistente);
                transaction.commit();
                System.out.println("Libro actualizado en la base de datos: " + libroExistente);

                if (mainController != null) {
                    mainController.cargarDatosLibro();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga en el formulario los datos de un libro existente para editarlo.
     * @param libroSeleccionado el libro a editar.
     */
    public void cargarDatosLibro(Libro libroSeleccionado) {
        this.libroEnEdicion = libroSeleccionado;

        if (libroSeleccionado != null) {
            titleField.setText(libroSeleccionado.getTitulo());
            isbnField.setText(libroSeleccionado.getIsbn());
            authorField.setText(libroSeleccionado.getAutor().getNombre());
            publisherField.setText(libroSeleccionado.getPublisher());
            yearField.setText(String.valueOf(libroSeleccionado.getAnioPublicacion()));
        }
    }

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
}
