package org.example.bibliotecafxehibernate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.bibliotecafxehibernate.Autor.Autor;
import org.example.bibliotecafxehibernate.Libro.AgregarLibroController;
import org.example.bibliotecafxehibernate.Libro.Libro;
import org.example.bibliotecafxehibernate.Prestamos.Prestamos;
import org.example.bibliotecafxehibernate.Socios.Socio;
import org.example.bibliotecafxehibernate.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HelloController {

    @FXML
    private TableColumn<Autor, String> nombreColumn;
    @FXML
    private TableColumn<Autor, String> nacionalidadColumn;
    @FXML
    private TextField searchField2;
    @FXML
    private TextField searchField3;
    @FXML
    private TableColumn<Socio, String> nombreColumn2;
    @FXML
    private TableColumn<Socio, String> direccionColumn;
    @FXML
    private TableColumn<Socio, String> telefonoColumn;
    @FXML
    private TextField buscarTextField;
    @FXML
    private TableColumn<Prestamos, String> libroColumn;
    @FXML
    private TableColumn<Prestamos, String> socioColumn;
    @FXML
    private TableColumn<Prestamos, String> fechaPrestamColumn;
    @FXML
    private TableColumn<Prestamos, String> fechaDevolucionColumn;
    @FXML
    private TableColumn<Prestamos, String> estadoColumn;
    @FXML
    private TabPane tabPane;


    // ────────────────────────── LIBROS ──────────────────────────
    @FXML
    private TableView<Libro> booksTable;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Libro, String> titleColumn;
    @FXML
    private TableColumn<Libro, String> isbnColumn;
    @FXML
    private TableColumn<Libro, String> authorColumn;
    @FXML
    private TableColumn<Libro, String> publisherColumn;
    @FXML
    private TableColumn<Libro, Integer> yearColumn;

    private final ObservableList<Libro> bookList = FXCollections.observableArrayList();

    // ────────────────────────── AUTORES ─────────────────────────
    @FXML
    private TableView<Autor> authorsTable;
    private final ObservableList<Autor> authorList = FXCollections.observableArrayList();

    // ────────────────────────── SOCIOS ──────────────────────────
    @FXML
    private TableView<Socio> sociosTable;
    private final ObservableList<Socio> socioList = FXCollections.observableArrayList();

    // ────────────────────────── PRÉSTAMOS ───────────────────────
    @FXML
    private TableView<Prestamos> loansTable;
    private final ObservableList<Prestamos> prestamosList = FXCollections.observableArrayList();

    // ────────────────────────── INITIALIZE ──────────────────────
    @FXML
    public void initialize() {
        System.out.println("📌 Inicializando controlador...");

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));

        nombreColumn2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        libroColumn.setCellValueFactory(new PropertyValueFactory<>("libro"));
        socioColumn.setCellValueFactory(new PropertyValueFactory<>("socio"));
        fechaPrestamColumn.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        fechaDevolucionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        System.out.println("Inicializando controlador...");
        System.out.println("🔍 searchField2: " + searchField2);
        System.out.println("🔍 searchField3: " + searchField3);
        System.out.println("🔍 buscarTextField: " + buscarTextField);
        System.out.println("🔍 tabPane: " + tabPane);

        cargarDatosLibro();
        cargarDatosAutor();
        cargarDatosSocio();
        cargarDatosPrestamos();
    }


    // ────────────────────────── CARGAR DATOS ─────────────────────
    public void cargarDatosLibro() {
        System.out.println("📚 Cargando libros desde la base de datos...");

        if (booksTable == null) {
            System.err.println("⚠️ ERROR: booksTable es null. Verifica que el ID en el FXML coincida.");
            return;
        }

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));

        bookList.clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Libro> libros = session.createQuery("FROM Libro", Libro.class).list();
            if (libros != null) {
                bookList.setAll(libros);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Error al cargar los libros desde la base de datos.");
        }

        if (!bookList.isEmpty()) {
            booksTable.setItems(bookList);
            System.out.println("✅ Libros cargados: " + bookList.size());
        } else {
            System.err.println("⚠️ No hay libros en la base de datos.");
        }
    }

    @FXML
    public void cargarDatosAutor() {
        System.out.println("✍️ Cargando autores...");

        if (authorsTable == null) {
            System.err.println("⚠️ ERROR: authorsTable es null. Verifica que el ID en el FXML coincida.");
            return;
        }

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));

        authorList.clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Autor> autores = session.createQuery("FROM Autor", Autor.class).list();
            if (autores != null) {
                authorList.setAll(autores);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Error al cargar los autores desde la base de datos.");
        }

        if (!authorList.isEmpty()) {
            authorsTable.setItems(authorList);
            System.out.println("✅ Autores cargados: " + authorList.size());
        } else {
            System.err.println("⚠️ No hay autores en la base de datos.");
        }
    }

    @FXML
    public void cargarDatosSocio() {
        System.out.println("👥 Cargando socios...");

        if (sociosTable == null) {
            System.err.println("⚠️ ERROR: sociosTable es null. Verifica que el ID en el FXML coincida.");
            return;
        }

        nombreColumn2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        socioList.clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Socio> socios = session.createQuery("FROM Socio", Socio.class).list();
            if (socios != null) {
                socioList.setAll(socios);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Error al cargar los socios desde la base de datos.");
        }

        if (!socioList.isEmpty()) {
            sociosTable.setItems(socioList);
            System.out.println("✅ Socios cargados: " + socioList.size());
        } else {
            System.err.println("⚠️ No hay socios en la base de datos.");
        }
    }

    @FXML
    public void cargarDatosPrestamos() {
        System.out.println("📖 Cargando préstamos...");

        if (loansTable == null) {
            System.err.println("⚠️ ERROR: loansTable es null. Verifica que el ID en el FXML coincida.");
            return;
        }

        libroColumn.setCellValueFactory(new PropertyValueFactory<>("libro"));
        socioColumn.setCellValueFactory(new PropertyValueFactory<>("socio"));
        fechaPrestamColumn.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        fechaDevolucionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        prestamosList.clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Prestamos> prestamos = session.createQuery("FROM Prestamos", Prestamos.class).list();
            if (prestamos != null) {
                prestamosList.setAll(prestamos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Error al cargar los préstamos desde la base de datos.");
        }

        if (!prestamosList.isEmpty()) {
            loansTable.setItems(prestamosList);
            System.out.println("✅ Préstamos cargados: " + prestamosList.size());
        } else {
            System.err.println("⚠️ No hay préstamos en la base de datos.");
        }
    }


    // ────────────────────────── LIBROS ───────────────────────────

    @FXML
    public void abrirVentanaAgregarLibro(ActionEvent event) {
        System.out.println("📝 Abriendo ventana para agregar libro...");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/bibliotecafxehibernate/AgregarLibro.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);
            Stage stage = new Stage();
            stage.setTitle("Agregar Libro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            AgregarLibroController controller = fxmlLoader.getController();
            controller.setMainController(this);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de agregar libro.");
        }
    }

    @FXML
    public void abrirVentanaModificarLibro(ActionEvent event) {
        System.out.println("📝 Abriendo ventana para modificar libro...");

        Libro libroSeleccionado = booksTable.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            mostrarAlerta("Primero selecciona un libro para modificar.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/bibliotecafxehibernate/ModificarLibro.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);
            Stage stage = new Stage();
            stage.setTitle("Modificar Libro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Aquí asumes que ModificarLibro.fxml también usa AgregarLibroController
            // OJO: Si tienes un controlador distinto, cambia la clase
            AgregarLibroController controller = fxmlLoader.getController();
            controller.cargarDatosLibro(libroSeleccionado);
            controller.setMainController(this);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de modificar libro.");
        }
    }

    @FXML
    public void eliminarLibro(ActionEvent event) {
        System.out.println("🗑️ Eliminando libro...");
        Libro libroSeleccionado = booksTable.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            mostrarAlerta("Primero selecciona un libro para eliminar.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(libroSeleccionado);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar el libro.");
            return;
        }

        cargarDatosLibro();
    }

    @FXML
    public void buscarLibros(ActionEvent event) {
        System.out.println("🔍 Buscando libros...");
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            mostrarAlerta("Ingrese un texto de búsqueda.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Libro> resultados = session.createQuery(
                            "FROM Libro l " +
                                    "WHERE l.titulo LIKE :param " +
                                    "   OR l.isbn LIKE :param " +
                                    "   OR l.autor.nombre LIKE :param",
                            Libro.class)
                    .setParameter("param", "%" + query + "%")
                    .list();

            bookList.setAll(resultados);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al buscar libros.");
        }
    }

    @FXML
    public void mostrarTodo(ActionEvent event) {
        System.out.println("📋 Mostrando todos los libros...");
        cargarDatosLibro();
    }

    // ────────────────────────── AUTORES ──────────────────────────

    @FXML
    public void abrirVentanaAgregarAutor(ActionEvent event) {
        System.out.println("📝 Abriendo ventana para agregar autor...");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/bibliotecafxehibernate/AgregarAutor.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load(), 400, 200);
            Stage stage = new Stage();
            stage.setTitle("Agregar Autor");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // AgregarAutorController controller = fxmlLoader.getController();
            // controller.setMainController(this);

            stage.showAndWait();
            cargarDatosAutor();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de agregar autor.");
        }
    }

    @FXML
    public void abrirVentanaModificarAutor(ActionEvent event) {
        System.out.println("📝 Abriendo ventana para modificar autor...");
        Autor autorSeleccionado = authorsTable.getSelectionModel().getSelectedItem();
        if (autorSeleccionado == null) {
            mostrarAlerta("Primero selecciona un autor para modificar.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/bibliotecafxehibernate/ModificarAutor.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load(), 400, 200);
            Stage stage = new Stage();
            stage.setTitle("Modificar Autor");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // ModificarAutorController controller = fxmlLoader.getController();
            // controller.cargarDatosAutor(autorSeleccionado);
            // controller.setMainController(this);

            stage.showAndWait();
            cargarDatosAutor();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de modificar autor.");
        }
    }

    @FXML
    public void eliminarAutor(ActionEvent event) {
        System.out.println("🗑️ Eliminando autor...");
        Autor autorSeleccionado = authorsTable.getSelectionModel().getSelectedItem();
        if (autorSeleccionado == null) {
            mostrarAlerta("Primero selecciona un autor para eliminar.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(autorSeleccionado);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar el autor.");
            return;
        }

        cargarDatosAutor();
    }

    @FXML
    public void buscarAutor(ActionEvent event) {
        System.out.println("🔍 Buscando autor...");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Autor");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el texto de búsqueda (nombre o nacionalidad):");

        dialog.showAndWait().ifPresent(query -> {
            if (query.trim().isEmpty()) {
                mostrarAlerta("No se ingresó texto de búsqueda.");
                return;
            }
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                List<Autor> resultados = session.createQuery(
                                "FROM Autor a WHERE a.nombre LIKE :param OR a.nacionalidad LIKE :param",
                                Autor.class)
                        .setParameter("param", "%" + query + "%")
                        .list();
                authorList.setAll(resultados);
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al buscar autores.");
            }
        });
    }

    @FXML
    public void mostrarTodoAutor(ActionEvent event) {
        System.out.println("📋 Mostrando todos los autores...");
        cargarDatosAutor();
    }

    // ────────────────────────── SOCIOS ───────────────────────────

    @FXML
    public void abrirVentanaAgregarSocio(ActionEvent event) {
        System.out.println("📝 Abriendo ventana para agregar socio...");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/bibliotecafxehibernate/AgregarSocio.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load(), 400, 200);
            Stage stage = new Stage();
            stage.setTitle("Agregar Socio");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // AgregarSocioController controller = fxmlLoader.getController();
            // controller.setMainController(this);

            stage.showAndWait();
            cargarDatosSocio();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de agregar socio.");
        }
    }

    @FXML
    public void abrirVentanaModificarSocio(ActionEvent event) {
        System.out.println("📝 Abriendo ventana para modificar socio...");
        Socio socioSeleccionado = sociosTable.getSelectionModel().getSelectedItem();
        if (socioSeleccionado == null) {
            mostrarAlerta("Primero selecciona un socio para modificar.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/bibliotecafxehibernate/ModificarSocio.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load(), 400, 200);
            Stage stage = new Stage();
            stage.setTitle("Modificar Socio");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // ModificarSocioController controller = fxmlLoader.getController();
            // controller.cargarDatosSocio(socioSeleccionado);
            // controller.setMainController(this);

            stage.showAndWait();
            cargarDatosSocio();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de modificar socio.");
        }
    }

    @FXML
    public void eliminarSocio(ActionEvent event) {
        System.out.println("🗑️ Eliminando socio...");
        Socio socioSeleccionado = sociosTable.getSelectionModel().getSelectedItem();
        if (socioSeleccionado == null) {
            mostrarAlerta("Primero selecciona un socio para eliminar.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(socioSeleccionado);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar el socio.");
            return;
        }

        cargarDatosSocio();
    }

    @FXML
    public void buscarSocios(ActionEvent event) {
        System.out.println("🔍 Buscando socios...");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Socios");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el texto de búsqueda (nombre, dirección, etc.):");

        dialog.showAndWait().ifPresent(query -> {
            if (query.trim().isEmpty()) {
                mostrarAlerta("No se ingresó texto de búsqueda.");
                return;
            }
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                List<Socio> resultados = session.createQuery(
                                "FROM Socio s " +
                                        "WHERE s.nombre LIKE :param OR s.direccion LIKE :param OR s.telefono LIKE :param",
                                Socio.class)
                        .setParameter("param", "%" + query + "%")
                        .list();
                socioList.setAll(resultados);
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al buscar socios.");
            }
        });
    }

    @FXML
    public void mostrarTodoSocio(ActionEvent event) {
        System.out.println("📋 Mostrando todos los socios...");
        cargarDatosSocio();
    }

    // ────────────────────────── PRÉSTAMOS ────────────────────────

    /**
     * Abre la ventana "RegistrarPrestamos.fxml" en la misma carpeta,
     * para registrar un préstamo nuevo.
     */
    @FXML
    public void abrirVentanaRegistrarPrestamo(ActionEvent event) {
        System.out.println("📝 Abriendo ventana para registrar préstamo...");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/bibliotecafxehibernate/RegistrarPrestamos.fxml")
                    // Renombrado a "RegistrarPrestamos.fxml" según tu preferencia
            );
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Registrar Préstamos");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // RegistrarPrestamoController controller = fxmlLoader.getController();
            // controller.setMainController(this);

            stage.showAndWait();
            cargarDatosPrestamos();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de registrar préstamos.");
        }
    }

    @FXML
    public void registrarDevolucion(ActionEvent event) {
        System.out.println("🔄 Registrando devolución...");
        Prestamos prestamoSeleccionado = loansTable.getSelectionModel().getSelectedItem();
        if (prestamoSeleccionado == null) {
            mostrarAlerta("Primero selecciona un préstamo para registrar la devolución.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Prestamos prestamo = session.get(Prestamos.class, prestamoSeleccionado.getId());
            prestamo.setEstado(Prestamos.ESTADO_FINALIZADO);
            prestamo.setFechaDevolucion(LocalDate.now());

            session.merge(prestamo);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al registrar la devolución.");
            return;
        }

        cargarDatosPrestamos();
    }

    @FXML
    public void buscarPrestamos(ActionEvent event) {
        System.out.println("🔍 Buscando préstamos...");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Préstamos");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese un texto para buscar (por ejemplo, estado, socio, etc.):");

        dialog.showAndWait().ifPresent(query -> {
            if (query.trim().isEmpty()) {
                mostrarAlerta("No se ingresó texto de búsqueda.");
                return;
            }
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                List<Prestamos> resultados = session.createQuery(
                                "FROM Prestamos p " +
                                        "WHERE p.estado LIKE :param OR p.socio.nombre LIKE :param",
                                Prestamos.class)
                        .setParameter("param", "%" + query + "%")
                        .list();
                prestamosList.setAll(resultados);
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error al buscar préstamos.");
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}