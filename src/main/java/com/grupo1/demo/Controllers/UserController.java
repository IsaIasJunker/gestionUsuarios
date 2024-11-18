package com.grupo1.demo.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Models.UsuarioDTO;
import com.grupo1.demo.Services.UserService;
import com.grupo1.demo.config.Views;

@RestController
@RequestMapping("/cuentas/API")
/**
 * Clase UserController
 */
public class UserController {

    @Autowired
    private UserService userService;

    /** ENDPOINTS NO CRUD (NO INCLUYE Permisos) **/

    //Loguearse utilizando un token
    @PostMapping("/login")
    @JsonView(Views.NoCrudView.class)
    public ResponseEntity<?> login(@RequestBody Usuario usuario){
        return null; //Falta terminar por falta de token
    }

    //Registrar un usuario
    @PostMapping("/register")
    @JsonView(Views.NoCrudView.class)
    public ResponseEntity<?> register(@RequestBody UsuarioDTO usuarioDTO){
        return userService.addUser(usuarioDTO);
    }

    //Obtener un usuario en concreto utilizando su id
    @GetMapping("/users/{userId}")
    @JsonView(Views.NoCrudView.class)
    public ResponseEntity<?> getUserById(@PathVariable ("userId") long userId){
        return userService.getUserById(userId);
    }

    /** ENDPOINTS CRUD (INCLUYE Permisos) **/

    // Obtener todos los usuarios de la base de datos
    @GetMapping("/users")
    @JsonView(Views.CrudView.class)
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }   

    // Añadir un usuario 
    @PostMapping("/users")
    @JsonView(Views.CrudView.class)
    public ResponseEntity<?> createUser(@RequestBody UsuarioDTO usuarioDTO) {
        return userService.addUser(usuarioDTO);
    }
    

    // Editar un usuario utilizando su id
    @PutMapping("/users/{userId}")
    @JsonView(Views.CrudView.class)
    public ResponseEntity<?> editUser(
        @PathVariable("userId") long userId,
        @RequestBody UsuarioDTO updatedUser
    ) {
        // Llamar al servicio para editar el usuario
        return userService.editUser(userId, updatedUser);
    }

    // Eliminar un usuario utilizando su id 
    @DeleteMapping("/users/{userId}")
    @JsonView(Views.CrudView.class)
    public ResponseEntity<?> deleteStudent(@PathVariable("userId") long userId){
        return userService.deleteUserById(userId);
    }
}
