package com.grupo1.demo.Auth;

import com.grupo1.demo.Jwt.JwtService;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    /**
     * Autentica al usuario y genera un token JWT.
     */
    public String authenticateAndGenerateToken(String username, String password, UserDetails userDetails) {
        // Autenticar credenciales del usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Generar el token JWT
        String token = jwtService.getToken(userDetails);

        // Actualizar el token actual del usuario en la base de datos
        Optional<Usuario> optionalUsuario = userRepository.findOptionalByUsername(username);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setCurrentToken(token);
            userRepository.save(usuario);
        }

        return token;
    }

    /**
     * Verifica si el token actual del usuario coincide con el almacenado en la base de datos.
     *
     * @param username El nombre de usuario.
     * @param token    El token a verificar.
     * @return true si el token coincide, false en caso contrario.
     */
    public boolean isTokenValid(String username, String token) {
        Optional<Usuario> optionalUsuario = userRepository.findOptionalByUsername(username);
        if (optionalUsuario.isEmpty()) {
            return false; // Usuario no encontrado
        }

        Usuario usuario = optionalUsuario.get();
        // Comparar el token actual con el almacenado
        return token.equals(usuario.getCurrentToken());
    }
}