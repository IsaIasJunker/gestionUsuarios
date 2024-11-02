package com.grupo1.demo.controller;


import com.grupo1.demo.entity.User;
import com.grupo1.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "api/users")
/**
 * Clase UserController
 */
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping()
    public List<User> getAll(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public Optional<User> getBookById(@PathVariable("userId") Long userId){
        return userService.getUser(userId);
    }

    @PostMapping
    public String saveUpdate(@RequestBody User user) {
        if (user.getPassword() == null || user.getUsername() == null || user.getEmail() == null) {
            return "Todos los campos (username, email y password) son obligatorios.";
        } else {
            userService.saveOrUpdate(user);
            return "Los cambios se han guardado correctamente.";
        }
    }

    @DeleteMapping("/{userId}")
    public String delete(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return "El usuario ha silo eliminado correctamente";
    }

}
