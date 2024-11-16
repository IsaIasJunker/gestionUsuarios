package com.grupo1.demo.Services;


import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.UserRepository;
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
    /**
     * Metodo para obtener todos los usuarios de la base de datos
     * @return una lista con todos los usuarios de la base de datos 
     */
    public ResponseEntity<?> getAllUsers(){
        List<Usuario> usuarios = userRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Metodo que busca un usuario por su id
     * @param id , del usuario que queremos buscar
     * @return , el usuario que buscamos por id
     */
    public ResponseEntity getUserById(long id){
        Optional<Usuario> usuario = userRepository.findById(id);
        //Verifico que el usuario exista en la base de datos
        if(usuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(usuario);
    }
    /**
     * Metodo que elimina un usuario utilizando su id
     * @param id , del usuario que vamos a eliminar
     * @return , un mensaje indicando que se borró correctamente el usuario
     */
    public ResponseEntity deleteUserById(long id){
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
     * Metodo para registrar un usuario en la base de datos
     * @param usuario, usuario que le pasamos por parametro
     * @return un mensaje indicando que se registro correctamente
     */
    public ResponseEntity addUser(Usuario usuario){
        //Compruebo que no hayan campos vacios
        if (verifyEmptyFields(usuario) == true) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Verifique los datos ingresados");
        }
        //Compruebo que el nombre ingresado no exista en la base de datos
        if(userRepository.findByUsername(usuario.getNombreUsuario())!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario ya existe");
        }
        //Si el nombre no existe entonces guardo el nuevo usuario en la base de datos.
        userRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    
    /**
     * Metodo que verifica si alguno de los campos del objeto usuario está vacio
     * @param usuario , usuario que vamos a verificar que sus campos no estén vacios
     * @return, "false" si ninguno de sus campos está vacio, si alguno de los campos está vacio retorna "True".
     */
    private boolean verifyEmptyFields (Usuario usuario){
        if(usuario.getNombreUsuario().isEmpty() || 
        usuario.getContrasenia().isEmpty() || usuario.getNombre().isEmpty() 
        || usuario.getNombre().isEmpty()){
            return true;
        }
        return false;
    }
}
