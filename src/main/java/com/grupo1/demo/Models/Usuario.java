package com.grupo1.demo.Models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Data
@Entity
@Table (name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario" , nullable = false)
    private long id;

    @Column(name = "nombreUsuario" , nullable = false , unique = true)
    private String nombreUsuario;

    @Column(name = "contrasenia" , nullable = false)
    private String contrasenia;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "nombre" , nullable = false)
    private String nombre;

    @Column(name = "apellido" , nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String role;

    @JsonIgnore //Notacion para que a la hora de mandar la request en el json no se tenga en cuenta
    @OneToMany(mappedBy = "usuario" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Permisos> permisos;

    @JsonIgnore //Notacion para que a la hora de mandar la request en el json no se tenga en cuenta
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> sesiones;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Set<Permisos> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<Permisos> permisos) {
        this.permisos = permisos;
    }

    public Set<Token> getSesiones() {
        return sesiones;
    }

    public void setSesiones(Set<Token> sesiones) {
        this.sesiones = sesiones;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}


