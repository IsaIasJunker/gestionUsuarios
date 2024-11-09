package com.grupo1.demo.Models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;


@Entity
@Getter
@Setter
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSesiones;

    @Column(name = "token" , nullable = false , unique = true)
    private String token;

    @Column(name = "inicio" , nullable = false)
    private Date createdAt;

    @Column(name = "expira" , nullable = false)
    private Date expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;
}

