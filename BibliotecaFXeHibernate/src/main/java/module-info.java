module org.example.bibliotecafxehibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires jakarta.transaction;
    requires jakarta.annotation;
    requires jakarta.inject;
    requires jakarta.cdi;

    requires java.naming;
    requires java.desktop;

    opens org.example.bibliotecafxehibernate to javafx.fxml;
    opens org.example.bibliotecafxehibernate.Autor to org.hibernate.orm.core, javafx.fxml;
    opens org.example.bibliotecafxehibernate.Libro to org.hibernate.orm.core, javafx.fxml;
    opens org.example.bibliotecafxehibernate.Socios to org.hibernate.orm.core, javafx.fxml;
    opens org.example.bibliotecafxehibernate.Prestamos to org.hibernate.orm.core, javafx.fxml;

    exports org.example.bibliotecafxehibernate;
    exports org.example.bibliotecafxehibernate.Libro;
    exports org.example.bibliotecafxehibernate.Autor;
    exports org.example.bibliotecafxehibernate.Socios;
    exports org.example.bibliotecafxehibernate.Prestamos;
}
