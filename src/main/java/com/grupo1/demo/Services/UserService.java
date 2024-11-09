package com.grupo1.demo.Services;


import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        if(usuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }else{
            return ResponseEntity.ok(usuario);
        }
        
        
    }

    /**
     * Metodo que elimina un usuario utilizando su id
     * @param id , del usuario que vamos a eliminar
     * @return , un mensaje indicando que se borró correctamente el usuario
     */
    public ResponseEntity deleteUserById(long id){
        Optional<Usuario> usuario = userRepository.findById(id);
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
        userRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
