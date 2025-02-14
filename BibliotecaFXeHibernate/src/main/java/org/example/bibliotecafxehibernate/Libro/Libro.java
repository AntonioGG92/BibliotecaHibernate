package org.example.bibliotecafxehibernate.Libro;

import jakarta.persistence.*;
import org.example.bibliotecafxehibernate.Autor.Autor;
import org.example.bibliotecafxehibernate.Prestamos.Prestamos;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros") // Asegúrate de que coincide con el nombre real en tu BD
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @Column(name = "editorial", nullable = false)
    private String editorial;

    @Column(name = "anio_publicacion", nullable = false)
    private int anioPublicacion;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prestamos> prestamos = new ArrayList<>();

    // ─────────────────────────────────────────────────────────────
    // Constructor vacío (obligatorio para Hibernate)
    public Libro() {
    }

    // Constructor con parámetros
    public Libro(String titulo, String isbn, Autor autor, String editorial, int anioPublicacion) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
    }

    // ─────────────────────────────────────────────────────────────
    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public List<Prestamos> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamos> prestamos) {
        this.prestamos = prestamos;
    }

    // ─────────────────────────────────────────────────────────────
    // Métodos "publisher" y "year" (opcionalmente, para compatibilidad con tu GUI)
    public void setPublisher(String editorial) {
        this.editorial = editorial;
    }

    public void setYear(int anio) {
        this.anioPublicacion = anio;
    }

    public String getPublisher() {
        return this.editorial;
    }

    // ─────────────────────────────────────────────────────────────
    // toString para depuración
    @Override
    public String toString() {
        return "Título: " + titulo + ", ISBN: " + isbn +
                ", Autor: " + (autor != null ? autor.getNombre() : "Desconocido") +
                ", Editorial: " + editorial + ", Año de Publicación: " + anioPublicacion;
    }
}
