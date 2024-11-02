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

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "lastname" , nullable = false)
    private String lastName;

    @Column(name = "username" , nullable = false , unique = true)
    private String username;

    @Column(name = "password" , nullable = false)
    private String password;

    @Column(name = "email_address" , nullable = false , unique = true)
    private String email;

}
