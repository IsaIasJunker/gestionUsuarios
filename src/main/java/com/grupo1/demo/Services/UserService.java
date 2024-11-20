package com.grupo1.demo.Services;

import com.grupo1.demo.Auth.AuthService;

import com.grupo1.demo.Models.Permisos;
import com.grupo1.demo.Models.Sistema;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.SistemaRepository;
import com.grupo1.demo.Repositories.UserRepository;
import com.grupo1.demo.Auth.LoginRequest;
import com.grupo1.demo.Auth.AuthResponse;
import com.grupo1.demo.dto.PermisosDTO;
import com.grupo1.demo.dto.UsuarioDTO;
import com.grupo1.demo.dto.UsuarioResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SistemaRepository sistemaRepository;

    @Autowired
    AuthService authService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    /**
     * Metodo para obtener todos los usuarios de la base de datos
     * @return una lista con todos los usuarios de la base de datos 
    */
    public ResponseEntity<?> getAllUsers(){
        List<Usuario> usuarios = userRepository.findAll();

        // Mapear Usuario a UsuarioResponseDTO
        List<UsuarioResponseDTO> responseDTOs = usuarios.stream().map(usuario -> {
            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(usuario.getId());
            dto.setUsername(usuario.getUsername());
            dto.setFirstName(usuario.getFirstName());
            dto.setLastName(usuario.getLastName());
            
            // Mapear permisos a PermisosDTO
            List<PermisosDTO> permisosDTOs = usuario.getPermisos().stream().map(permiso -> {
                PermisosDTO permisosDTO = new PermisosDTO();
                permisosDTO.setSystemId(permiso.getSistemaId());
                permisosDTO.setName(permiso.getSistemaNombre());
                return permisosDTO;
            }).collect(Collectors.toList());
            
            dto.setPermisos(permisosDTOs);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Metodo para registrar un usuario en la base de datos
     * @param usuario, usuario que le pasamos por parametro
     * @return un mensaje indicando que se registro correctamente
    */
    public ResponseEntity<?> addUser(UsuarioDTO usuarioDTO) {
        // Crear el objeto Usuario a partir del DTO
        Usuario usuario = new Usuario(usuarioDTO);

        // Verificar campos vacíos
        if (verifyEmptyFields(usuario)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Verifique los datos ingresados");
        }

        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(usuario.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario ya existe");
        }

        // Hashear la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Crear permisos y asignarlos al usuario
        if (usuarioDTO.getSistemaIds() != null && !usuarioDTO.getSistemaIds().isEmpty()) {
            // Lista que utilizamos para iterar y agregar los permisos antes de setearlos.
            List<Permisos> permisos = new ArrayList<>();  
            
            for (Long sistemaId : usuarioDTO.getSistemaIds()) {
                Optional<Sistema> sistemaOpt = sistemaRepository.findById(sistemaId);
                if (sistemaOpt.isPresent()) {
                    Sistema sistema = sistemaOpt.get();
                    Permisos permiso = new Permisos();

                    // Asignamos las entidades correspondientes a permiso.
                    permiso.setUsuario(usuario);  
                    permiso.setSistema(sistema);
                    permisos.add(permiso); 
                }
            }
            // Asignar permisos al usuario
            usuario.setPermisos(permisos);
        }

        // Guardar el usuario, JPA manejará la inserción de los permisos asociados debido al cascade
        userRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    // Editar usuarios
    public ResponseEntity<?> editUser(long id, UsuarioDTO updatedUser) {
        // Buscar el usuario por ID
        Usuario existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    
        // Verificar si el email (username) ya existe para otro usuario
        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
            userRepository.findByUsername(updatedUser.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya está en uso");
        }
    
        // Actualizar los atributos del usuario con los datos de updatedUser
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        
        // Actualizar la contraseña solo si se proporcionó una nueva
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(hashedPassword);
        }
    
        // Actualizar los permisos
        if (updatedUser.getSistemaIds() != null) {
            // Obtener los sistemas correspondientes a los IDs
            List<Sistema> sistemas = sistemaRepository.findAllById(updatedUser.getSistemaIds());
    
            // Crear una nueva lista de permisos basada en los sistemas proporcionados
            List<Permisos> nuevosPermisos = sistemas.stream().map(sistema -> {
                Permisos permiso = new Permisos();
                permiso.setUsuario(existingUser);
                permiso.setSistema(sistema);
                return permiso;
            }).toList();
    
            // Eliminar permisos actuales del usuario
            existingUser.getPermisos().clear();
    
            // Agregar los nuevos permisos
            existingUser.getPermisos().addAll(nuevosPermisos);
        }
    
        // Guardar los cambios del usuario
        userRepository.save(existingUser);
    
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }
    



     /**
     * Metodo que elimina un usuario utilizando su id
     * @param id , del usuario que vamos a eliminar
     * @return , un mensaje indicando que se borró correctamente el usuario
    */
    public ResponseEntity<?> deleteUserById(long id){
        Optional<Usuario> usuario = userRepository.findById(id);
        //Verifico si el usuario existe en la base de datos 
        if(usuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }else{
            userRepository.deleteById(id);
            return ResponseEntity.ok("El usuario ha sido eliminado");
        }
    }

    /**
     * Metodo que busca un usuario por su id
     * @param id , del usuario que queremos buscar
     * @return , el usuario que buscamos por id
    */
    public ResponseEntity<?> getUserById(long id){
        Optional<Usuario> usuario = userRepository.findById(id);
        //Verifico que el usuario exista en la base de datos
        if(usuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(usuario);
    }
    
    /**
     * Metodo que verifica si alguno de los campos del objeto usuario está vacio
     * @param usuario , usuario que vamos a verificar que sus campos no estén vacios
     * @return, "false" si ninguno de sus campos está vacio, si alguno de los campos está vacio retorna "True".
    */
    private boolean verifyEmptyFields (Usuario usuario){
        if(usuario.getUsername().isEmpty() || 
        usuario.getPassword().isEmpty() || usuario.getFirstName().isEmpty() 
        || usuario.getLastName().isEmpty()){
            return true;
        }
        return false;
    }


    /**
     * Loguear un usuario existente.
     */
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        Optional<Usuario> optionalUser = userRepository.findOptionalByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("Usuario no encontrado"));
        }

        Usuario usuario = optionalUser.get();

        UserDetails userDetails = User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .build();

        String token = authService.authenticateAndGenerateToken(request.getUsername(), request.getPassword(), userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}