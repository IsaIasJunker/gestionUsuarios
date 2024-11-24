

package com.grupo1.demo.Controllers;


import com.grupo1.demo.Jwt.JwtService;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.grupo1.demo.Auth.AuthResponse;
import com.grupo1.demo.Auth.LoginRequest;
import com.grupo1.demo.Services.UserService;
import com.grupo1.demo.config.Views;
import com.grupo1.demo.dto.UsuarioDTO;

@RestController
@RequestMapping("/cuentas/API")
/**
 * Clase UserController
 */
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    /** ENDPOINTS NO CRUD (NO INCLUYE Permisos) **/

    /**
     * Loguearse utilizando un token JWT.
     */
    @PostMapping("/login")
    @JsonView(Views.NoCrudView.class)
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    /**
     * Registrar un nuevo usuario.
     */
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
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authHeader){

        // Asegurarse de que la cabecera de autorización esté presente
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid");
        }

        //Extraer el token del encabezado

        String token = authHeader.substring(7); // Elimina el "Bearer " al inicio del token
        // Obtener el nombre de usuario del token (esto dependerá de cómo esté estructurado el JWT)
        String username = jwtService.getUsernameFromToken(token);

        // Obtener el usuario desde la base de datos
        Usuario usuario = userRepository.findByUsername(username); // Asegúrate de tener el repositorio adecuado

        // Verificar que el token sea válido
        if ((usuario == null || !jwtService.isTokenValid(token, usuario))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }

        //Si todo es valido, retorna la lista de usuarios
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