package com.l19290640.Tudu.controller;

import com.l19290640.Tudu.entities.Tarea;
import com.l19290640.Tudu.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/tareas")
public class TareaRestController {

    @Autowired
    private TareaRepository tareaRepository;

    @GetMapping
    public Iterable<Tarea> getAlltareas() {
        return tareaRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Tarea getTareaById(@PathVariable long id){
        Optional<Tarea> tarea = tareaRepository.findById(id);

        //return tarea.orElse(null);

        if(tarea.isPresent())
            return tarea.get();

        return null;
     }

     @PostMapping
    public ResponseEntity<Tarea> registrarTarea(@RequestBody Tarea tarea){

        //Validaciones
        boolean isOk = true;
        if(tarea == null)
            return ResponseEntity.badRequest().build();

         if(     tarea.getTitulo() == null || tarea.getDescripcion() == null ||
                 tarea.getTitulo().isEmpty() || tarea.getDescripcion().isEmpty()){
             isOk = false;
         }

        if(tarea.getStatus() == null || tarea.getStatus().isEmpty())
            tarea.setStatus("Pendiente");

        if(isOk){
            Tarea saved = tareaRepository.save(tarea);
            return ResponseEntity.ok(saved);
        }

         return ResponseEntity.badRequest().build();

     }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Tarea> updateTarea(@RequestBody Tarea tarea, @PathVariable long id){

        //Primero se valida que el registro exista consultando por si id
        Optional<Tarea> tareaIndb = tareaRepository.findById(id);

        if(tareaIndb.isEmpty())
            return ResponseEntity.notFound().build();

        tarea.setId(tareaIndb.get().getId());

        boolean isOk = true;
        if(tarea == null)
            return ResponseEntity.badRequest().build();

        if(tarea.getTitulo() == null || tarea.getDescripcion() == null ||
                tarea.getTitulo().isEmpty() || tarea.getDescripcion().isEmpty()){
            isOk = false;
        }

        if(isOk){
            Tarea saved = tareaRepository.save(tarea);
            return ResponseEntity.ok(saved);
        }

        return ResponseEntity.badRequest().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tarea> deleteTarea(@PathVariable long id){
        Optional<Tarea> tareaIndb = tareaRepository.findById(id);
        if (tareaIndb.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        tareaRepository.deleteById(id);

        return ResponseEntity.ok(tareaIndb.get());
    }



}
