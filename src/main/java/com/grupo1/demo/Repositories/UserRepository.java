package com.grupo1.demo.Repositories;


import com.grupo1.demo.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Interface UserRepository que extiende de JpaRepository
 */
public interface UserRepository  extends JpaRepository<Usuario, Long> {

    Usuario findByNombre (String nombre);
    Usuario findByNombreUsuario (String nombreUsuario);
}
