package com.grupo1.demo.Services;

import com.grupo1.demo.Auth.AuthService;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.UserRepository;
import com.grupo1.demo.Auth.LoginRequest;
import com.grupo1.demo.Auth.RegisterRequest;
import com.grupo1.demo.Auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    /**
     * Obtener todos los usuarios.
     */
    public ResponseEntity<?> getAllUsers() {
        List<Usuario> usuarios = userRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener un usuario por su ID.
     */
    public ResponseEntity<?> getUserById(long id) {
        Optional<Usuario> usuario = userRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(usuario);
    }

    /**
     * Eliminar un usuario por su ID.
     */
    public ResponseEntity<?> deleteUserById(long id) {
        Optional<Usuario> usuario = userRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("El usuario ha sido eliminado");
    }

    /**
     * Registrar un nuevo usuario.
     */
    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        if (verifyEmptyFields(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("Verifique los datos ingresados"));
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("El usuario ya existe"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getUsername());
        usuario.setContrasenia(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getUsername());
        usuario.setNombre(request.getFirstname());
        usuario.setApellido(request.getLastname());

        userRepository.save(usuario);

        UserDetails userDetails = User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasenia())
                .roles(usuario.getRole())
                .build();

        String token = authService.authenticateAndGenerateToken(request.getUsername(), request.getPassword(), userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Loguear un usuario existente.
     */
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        Optional<Usuario> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Usuario no encontrado"));
        }

        Usuario usuario = optionalUser.get();

        UserDetails userDetails = User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasenia())
                .roles(usuario.getRole())
                .build();

        String token = authService.authenticateAndGenerateToken(request.getUsername(), request.getPassword(), userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Verificar si los campos del registro están vacíos.
     */
    private boolean verifyEmptyFields(RegisterRequest request) {
        return request.getUsername().isEmpty() ||
                request.getPassword().isEmpty() ||
                request.getFirstname().isEmpty() ||
                request.getLastname().isEmpty();
    }
}