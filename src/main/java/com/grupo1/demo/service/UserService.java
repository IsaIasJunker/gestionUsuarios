package com.grupo1.demo.service;


import com.grupo1.demo.entity.User;
import com.grupo1.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene todos los usuarios de la base de datos
     * @return
     */
    public List <User> getAllUsers(){
        return userRepository.findAll(); 
    }

    /**
     * Se busca un usuario con un id especifico
     * @param id
     * @return
     */
    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }

    /**
     * Se elimina un usuario utilizando su id 
     * @param userId
     */
    public void deleteUserById(Long userId){
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }else{
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
    }
    /**
     * Se registra un usuario mando un json con los atributos del usuario
     * @param user
     * @return
     */
    public String registerUser(User user){
        userRepository.save(user);
        return "Se ha registrado con exito el usuario"; 
    }

    /**
     * Se busca un usuario usando su nombre como parametro
     * @param nombre
     * @return
     */
    public Optional<User> findUserByName(String nombre){
        return userRepository.findByNombre(nombre);
    }

    public Optional <User> findUserByApellido(String email){
        return userRepository.findByApellido(email);
    }
}
