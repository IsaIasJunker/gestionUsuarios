
package com.grupo1.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.grupo1.demo.Models.Permisos;

@Repository
public interface PermisoRepository extends JpaRepository<Permisos, Long>{
}