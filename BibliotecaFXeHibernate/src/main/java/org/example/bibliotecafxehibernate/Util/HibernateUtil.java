package org.example.bibliotecafxehibernate.Util;

import org.example.bibliotecafxehibernate.Autor.Autor;
import org.example.bibliotecafxehibernate.Libro.Libro;
import org.example.bibliotecafxehibernate.Prestamos.Prestamos;
import org.example.bibliotecafxehibernate.Socios.Socio;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Carga la configuración desde hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();

            // 🔹 REGISTRA LAS CLASES MAPEADAS (importante)
            configuration.addAnnotatedClass(Autor.class);
            configuration.addAnnotatedClass(Libro.class);
            configuration.addAnnotatedClass(Socio.class);
            configuration.addAnnotatedClass(Prestamos.class);

            // 🔹 MANEJO CORRECTO DEL SERVICEREGISTRY
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("❌ Error al inicializar Hibernate: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // 🔹 MÉTODO PARA CERRAR SESIÓN CUANDO LA APP TERMINE
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("✅ Hibernate cerrado correctamente.");
        }
    }
}
