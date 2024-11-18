package com.grupo1.demo.Services;


import com.grupo1.demo.Models.Permisos;
import com.grupo1.demo.Models.Sistema;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Models.UsuarioDTO;
import com.grupo1.demo.Repositories.PermisoRepository;
import com.grupo1.demo.Repositories.SistemaRepository;
import com.grupo1.demo.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service

public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SistemaRepository sistemaRepository;
    PermisoRepository permisosRepository;

    /**
     * Metodo para obtener todos los usuarios de la base de datos
     * @return una lista con todos los usuarios de la base de datos 
    */
    public ResponseEntity<?> getAllUsers(){
        List<Usuario> usuarios = userRepository.findAll();
        return ResponseEntity.ok(usuarios);
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


    // SIN TESTEAR. Metodo para editar un usuario
    public ResponseEntity<?> editUser(long id, UsuarioDTO updatedUser) {
        // Buscar el usuario por ID
        Optional<Usuario> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario existingUser = existingUserOpt.get();

        // Actualizar los datos del usuario con los valores del DTO
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());


        // Verificar si el nombre de usuario ya existe (para evitar duplicados)
        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
            userRepository.findByUsername(updatedUser.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya está en uso");
        }

        // Actualizar los permisos (si es necesario)
        if (updatedUser.getSistemaIds() != null && !updatedUser.getSistemaIds().isEmpty()) {
            List<Permisos> permisos = new ArrayList<>();
            
            for (Long sistemaId : updatedUser.getSistemaIds()) {
                Optional<Sistema> sistemaOpt = sistemaRepository.findById(sistemaId);
                if (sistemaOpt.isPresent()) {
                    Sistema sistema = sistemaOpt.get();
                    Permisos permiso = new Permisos();
                    permiso.setUsuario(existingUser);
                    permiso.setSistema(sistema);
                    permisos.add(permiso);
            }
        }

        // Actualizar permisos del usuario (esto podría implicar borrar los permisos anteriores) TESTEAR
        existingUser.setPermisos(permisos);
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
}
