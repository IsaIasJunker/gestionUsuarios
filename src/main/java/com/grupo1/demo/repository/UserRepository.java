package com.grupo1.demo.repository;


import com.grupo1.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Interface BookRepository que extiende de JpaRepository
 */
public interface UserRepository  extends JpaRepository<User, Long> {

}
