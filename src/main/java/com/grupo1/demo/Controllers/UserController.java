package com.grupo1.demo.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Services.UserService;

@RestController
@RequestMapping("/sistema")
/**
 * Clase UserController
 */
public class UserController {

    @Autowired
    private UserService userService;

    
    //Obtener todos los usuarios de la base de datos
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }

    //Obtener un usuario en concreto utilizando su id
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable ("userId") long userId){
        return userService.getUserById(userId);
    }

    //Eliminar un usuario utilizando su id 
    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<?> deleteStudent(@PathVariable("userId") long userId){
        return userService.deleteUserById(userId);
    }

    //Loguearse utilizando un token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario){
        return null; //Falta terminar por falta de token
    }

    //Registrar un usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario){
        return userService.addUser(usuario);
    }
}
