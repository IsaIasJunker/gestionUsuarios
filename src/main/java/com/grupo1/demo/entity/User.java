package com.grupo1.demo.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table (name = "tbl_user")
/**
 * Clase User que define las columanas y atributos de la base de datos
 */
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre" , nullable = false)
    private String nombre;

    @Column(name = "apellido" , nullable = false)
    private String apellido;

    @Column(name = "email" , nullable = false , unique = true)
    private String email;

    @Column(name = "usuario" , nullable = false , unique = true)
    private String usuario;

    @Column(name = "contrasenia" , nullable = false)
    private String contrasenia;
}
