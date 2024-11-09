package com.grupo1.demo.Models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "permisos")
public class Permisos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permisos" , nullable = false)
    private long id_permiso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario" , nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sistema", nullable = false)
    private Sistemas sistema;
}
