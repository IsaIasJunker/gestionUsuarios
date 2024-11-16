package com.grupo1.demo.Services;

import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario por el correo (username)
        Usuario usuario = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Devolver un objeto UserDetails usando User.builder() con los datos del Usuario
        return User.builder()
                .username(usuario.getEmail())       // Se usa email como username
                .password(usuario.getContrasenia()) // Se usa la contraseña del usuario
                .roles(usuario.getRole())           // Asumiendo que 'role' es un String que indica el rol del usuario
                .build(); // Construye el objeto UserDetails
    }
}