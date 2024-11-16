package com.grupo1.demo.Models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "sistemas")
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sistemas" , nullable = false)
    private long id_sistemas;

    @Column(name = "nombre" , nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "sistema" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Permisos> permisos;

    // Constructor para crear instancias con solo nombre
    public Sistema(String nombre){
        this.nombre = nombre;
    }
    public Sistema() {
    }

    
}
