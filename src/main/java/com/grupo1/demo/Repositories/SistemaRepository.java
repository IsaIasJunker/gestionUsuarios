package com.grupo1.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo1.demo.Models.Sistema;

public interface SistemaRepository  extends JpaRepository<Sistema, Long>{
    Sistema findByNombre(String nombre); //Buscar un sistema por su nombre
}
