package com.grupo1.demo.Models;


import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "sistemas")
public class Sistemas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sistemas" , nullable = false)
    private long id_sistemas;

    @Column(name = "nombre" , nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "sistema" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Permisos> permisos;

}
