package com.l19290640.Tudu.controller;

import com.l19290640.Tudu.entities.Users;
import com.l19290640.Tudu.repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UsersRestController {

    @Autowired
    private UsersRepository usersRepository;

    //Consulta general
    @GetMapping
    public Iterable<Users> getAllusers(){return usersRepository.findAll(); }

    //Consulta por id
    @GetMapping(path = "/{idUsuario}")
    public Users getUsersById(@PathVariable long idUsuario){
        Optional<Users> user = usersRepository.findById(idUsuario);

        return user.orElse(null);
    }

    //Insertar un nuevo usuario
    @PostMapping
    public ResponseEntity<Users> registrarUsuario(@RequestBody Users user) {
        //Validaciones
        boolean isOk = true;

        if (user == null)
            return ResponseEntity.badRequest().build(); //Si no envia nada el ingrato dile que nell

        if (user.getNombre() == null || user.getContrasena() == null || user.getCorreo() == null ||
                user.getNombre().isEmpty() || user.getContrasena().isEmpty() || user.getCorreo().isEmpty()
        ) {
            isOk = false;
        }

        if (isOk) {
            Users saved = usersRepository.save(user); //Si si se guardo se debe responder con el registro
            return ResponseEntity.ok(saved);
        }

        return ResponseEntity.badRequest().build(); //Si no ok se responde que fallo

    }

    //Actualizar usuario
    @PutMapping(path = "/{idUsuario}")
    public ResponseEntity<Users> updateUser(@RequestBody Users user, @PathVariable long idUsuario){
        //Primero se valida que el registro exista consultando por su id
        Optional<Users> userInDB = usersRepository.findById(idUsuario);

        if (userInDB.isEmpty())
            return ResponseEntity.notFound().build(); //Si no lo encontro dile que nell

        //Si si existe ya se empiza a actualizar el registro

        user.setIdUsuario(userInDB.get().getIdUsuario());

        //Peroo antes se tiene que revisar si mando todos los datos para que se puedan actializar
        boolean isOk = true;
        if(user == null)
            return ResponseEntity.badRequest().build();

        if (user.getNombre() == null || user.getContrasena() == null || user.getCorreo() == null ||
                user.getNombre().isEmpty() || user.getContrasena().isEmpty() || user.getCorreo().isEmpty()
        ) {
            isOk = false;
        }

        if (isOk) {
            Users saved = usersRepository.save(user); //Si si se guardo se debe responder con el registro
            return ResponseEntity.ok(saved);
        }

        return ResponseEntity.badRequest().build();
    }

    //borrar usuario
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Users> deleteUser(@PathVariable long idUsuario){
        Optional<Users> userInDB = usersRepository.findById(idUsuario); //busca al usuario

        if (userInDB.isEmpty())
            return ResponseEntity.badRequest().build(); //Si no lo encontro dile que nell

        usersRepository.deleteById(idUsuario);
        return ResponseEntity.ok(userInDB.get()); //Si si lo entrontro lo borra y le manda lo q borro
    }

}
