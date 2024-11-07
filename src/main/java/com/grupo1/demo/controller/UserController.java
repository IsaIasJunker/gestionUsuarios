package com.grupo1.demo.controller;


import com.grupo1.demo.entity.User;
import com.grupo1.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping()
/**
 * Clase UserController
 */
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Metodo para obtener todos los usuarios de la base de datos 
     * @return una lista con todos los atributos de los usuarios de la base de datos 
     */
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Metodo que busca usuario utilizando el id
     * @param id
     * @return el usuario con la id que le pasamos por parametro
     */
    @GetMapping("/user/{id}")
    public Optional getUSerById(@PathVariable("id") Long id){
        return userService.findUserById(id);
    }

    /**
     * Metodo para eliminar un usuario pasando su id como parametro
     * @param userId
     * @return
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("Usuario eliminado con exito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("usuario no encontrado");
        }
    }

    /**
     * Metodo para registrar un usuario
     * @param user
     */
    @PostMapping("/register")
    public void registerUser(@RequestBody User user){
        userService.registerUser(user);
    }


    /**
     * Metodo para buscar un usuario usando su nombre
     * @param nombre
     * @return
     */
    @GetMapping("/search/{nombre}")
    public Optional getUserByName(@PathVariable ("nombre") String nombre){
        return userService.findUserByName(nombre);
    }

    @GetMapping("/search/user/{apellido}")
    public Optional getUserByApellido(@PathVariable("apellido") String apellido){
        return userService.findUserByApellido(apellido);
    }
}
