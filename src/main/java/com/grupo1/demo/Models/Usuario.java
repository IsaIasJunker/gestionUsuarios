package com.grupo1.demo.Models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.grupo1.demo.config.Views;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    @JsonView(Views.NoCrudView.class) // Incluido en ambas vistas
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    @JsonView(Views.NoCrudView.class) // Incluido en ambas vistas
    private String username;

    @Column(name = "password", nullable = false)
    @JsonView(Views.NoCrudView.class) // Incluido en ambas vistas
    private String password;

    @Column(name = "firstName", nullable = false)
    @JsonView(Views.NoCrudView.class) // Incluido en ambas vistas
    private String firstName;

    @Column(name = "lastName", nullable = false)
    @JsonView(Views.NoCrudView.class) // Incluido en ambas vistas
    private String lastName;

    // Permisos solo presentes en la vista CRUD
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.CrudView.class)
    private List<Permisos> permisos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Siempre ignorar sesiones en todas las vistas
    private Set<Token> sesiones;

    // Constructor que recibe un UsuarioDTO
    public Usuario(UsuarioDTO usuarioDTO) {
        this.firstName = usuarioDTO.getFirstName();
        this.lastName = usuarioDTO.getLastName();
        this.username = usuarioDTO.getUsername();
        this.password = usuarioDTO.getPassword();
    }
    public Usuario(){        
    }
}