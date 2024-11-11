package com.grupo1.demo.Models;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table (name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario" , nullable = false)
    private long id;

    @Column(name = "nombreUsuario" , nullable = false , unique = true)
    private String nombreUsuario;

    @Column(name = "contrasenia" , nullable = false)
    private String contrasenia;

    @Column(name = "nombre" , nullable = false)
    private String nombre;

    @Column(name = "apellido" , nullable = false)
    private String apellido;

    @JsonIgnore //Notacion para que a la hora de mandar la request en el json no se tenga en cuenta
    @OneToMany(mappedBy = "usuario" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Permisos> permisos;

    @JsonIgnore //Notacion para que a la hora de mandar la request en el json no se tenga en cuenta
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> sesiones;
}
