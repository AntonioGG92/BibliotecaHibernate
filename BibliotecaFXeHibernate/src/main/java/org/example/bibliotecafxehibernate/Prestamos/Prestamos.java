package org.example.bibliotecafxehibernate.Prestamos;

import jakarta.persistence.*;
import org.example.bibliotecafxehibernate.Libro.Libro;
import org.example.bibliotecafxehibernate.Socios.Socio;

import java.time.LocalDate;

@Entity
@Table(name = "prestamos") // Asegúrate de que coincide con el nombre en tu BD
public class Prestamos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con Libro (columna libro_id en la BD)
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    // Relación ManyToOne con Socio (columna socio_id en la BD)
    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDate fechaDevolucion;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    // Constantes para el estado
    public static final String ESTADO_ACTIVO = "Activo";
    public static final String ESTADO_FINALIZADO = "Finalizado";

    // Constructor vacío (requerido por Hibernate)
    public Prestamos() {
    }

    // Constructor con objeto Socio
    public Prestamos(Libro libro, Socio socio, LocalDate fechaPrestamo, LocalDate fechaDevolucion, String estado) {
        this.libro = libro;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
