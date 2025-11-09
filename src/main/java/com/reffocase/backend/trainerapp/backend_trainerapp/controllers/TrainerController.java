package com.reffocase.backend.trainerapp.backend_trainerapp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Trainer;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.TrainerService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/formadores") // Usamos "formadores" como nombre en las URLs
@CrossOrigin(origins = "*")
public class TrainerController {

    private final TrainerService trainerService;

    // Constructor para inyección de dependencias
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping
    public Page<Trainer> findAllPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "lastname", defaultValue = "") String lastname,
            @RequestParam(name = "documentNumber", defaultValue = "") String documentNumber,
            @RequestParam(name = "thematic", defaultValue = "") String thematic,
            @RequestParam(name = "province", defaultValue = "") String province,
            @RequestParam(name = "enabled", defaultValue = "") String enabled,
            @RequestParam(name = "orderBy", defaultValue = "") String orderBy,
            @RequestParam(name = "asc", defaultValue = "") String asc) {

        return trainerService.findAll(name, lastname, documentNumber, thematic, province, enabled, page, size,
                orderBy, asc);
    }

    @GetMapping("/list")
    public List<Trainer> findAll(
            @RequestParam(name = "thematics", defaultValue = "") String thematics) {

        return trainerService.findAll(thematics);
    }

    @GetMapping("/pdf")
    public List<Trainer> getPDF( 
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "lastname", defaultValue = "") String lastname,
            @RequestParam(name = "documentNumber", defaultValue = "") String documentNumber,
            @RequestParam(name = "thematic", defaultValue = "") String thematic,
            @RequestParam(name = "province", defaultValue = "") String province,
            @RequestParam(name = "enabled", defaultValue = "") String enabled) {


         return trainerService.findAll(name, lastname, documentNumber, thematic, province, enabled);
    }
    

    // Obtener un trainer por ID
    @GetMapping("/{id}")
    public Optional<Trainer> findById(@PathVariable Long id) {
        return trainerService.findById(id);
    }

    // Crear un nuevo trainer
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Trainer trainer, BindingResult result) {
        if (result.hasErrors() || trainer.getThematics().isEmpty()) {
            if (trainer.getThematics().isEmpty()) {
                result.addError(new ObjectError("thematics", "Debe seleccionar al menos una temática"));
            }
            return UtilController.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.save(trainer));
    }

    // Actualizar un trainer existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Trainer trainer, BindingResult result, @PathVariable Long id) {

        if ((trainer.getThematics().isEmpty() && trainer.getEnabled()) || result.hasErrors()) {
            if (trainer.getThematics().isEmpty()) {
                result.addError(new ObjectError("thematics", "Debe seleccionar al menos una temática"));
            }
            return UtilController.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.update(id, trainer));

    }

    // Eliminar un trainer por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        trainerService.delete(id);
    }
}
