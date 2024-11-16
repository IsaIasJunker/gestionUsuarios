package com.grupo1.demo.Auth;

import com.grupo1.demo.Jwt.JwtService;
import com.grupo1.demo.Models.Role;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse register(RegisterRequest request) {
        // Crear un objeto Usuario en lugar de User de Spring Security
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getUsername());
        usuario.setContrasenia(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getUsername());
        usuario.setNombre(request.getFirstname());
        usuario.setApellido(request.getLastname());
        usuario.setRole(Role.USER.name());  // Usar Role como String o Enum, dependiendo de tu diseño

        // Guardar el usuario en el repositorio
        userRepository.save(usuario);

        // Crear un objeto User de Spring Security para la autenticación posterior
        UserDetails userDetails = User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasenia())
                .roles(usuario.getRole())
                .build();

        // Generar el token JWT y devolver la respuesta
        return AuthResponse.builder()
                .token(jwtService.getToken(userDetails))
                .build();
    }

}
