

package com.grupo1.demo.Controllers;

import com.grupo1.demo.Auth.LoginRequest;
import com.grupo1.demo.Auth.RegisterRequest;
import com.grupo1.demo.Auth.AuthResponse;
import com.grupo1.demo.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sistema")
@RequiredArgsConstructor
/**
 * Clase UserController
 */
public class UserController {

    private final UserService userService;

    /**
     * Obtener todos los usuarios de la base de datos.
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Obtener un usuario en concreto utilizando su id.
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") long userId) {
        return userService.getUserById(userId);
    }

    /**
     * Eliminar un usuario utilizando su id.
     */
    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") long userId) {
        return userService.deleteUserById(userId);
    }

    /**
     * Loguearse utilizando un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    /**
     * Registrar un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }
}