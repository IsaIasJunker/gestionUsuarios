package com.grupo1.demo.repository;


import com.grupo1.demo.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Interface UserRepository que extiende de JpaRepository
 */
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional <User> findByNombre(String nombre);
    Optional <User> findByApellido(String apellido);
}
