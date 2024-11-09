package com.grupo1.demo.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table (name = "usuarios")
/**
 * Clase User que define las columanas y atributos de la base de datos
 */
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario" , nullable = false)
    private long id;

    @Column(name = "username" , nullable = false , unique = true)
    private String username;

    @Column(name = "password" , nullable = false)
    private String pasword;

    @Column(name = "rol" , nullable = false)
    private String rol;

    @OneToMany(mappedBy = "usuario" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Permisos> permisos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> sesiones;

    public Usuario() {
    }

    public Usuario(int id, String username, String pasword, String rol) {
        this.id = id;
        this.username = username;
        this.pasword = pasword;
        this.rol = rol;
    }
}
