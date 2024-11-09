package com.grupo1.demo.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Services.UserService;


@RestController
@RequestMapping("/sistema")
/**
 * Clase UserController
 */
public class UserController {

    @Autowired
    private UserService userService;

    

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable ("userId") long userId){
        return userService.getUserById(userId);
    }

    @DeleteMapping("/users/delete/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") long studentId){
        return userService.deleteUserById(studentId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario){
        return null; //Falta terminar por falta de token
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario){
        return userService.addUser(usuario);
    }
}
