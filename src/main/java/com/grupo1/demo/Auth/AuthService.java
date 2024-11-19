package com.grupo1.demo.Auth;

import com.grupo1.demo.Jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Autentica al usuario y genera un token JWT.
     */
    public String authenticateAndGenerateToken(String username, String password, UserDetails userDetails) {
        // Autenticar credenciales del usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Generar el token JWT
        return jwtService.getToken(userDetails);
    }
}