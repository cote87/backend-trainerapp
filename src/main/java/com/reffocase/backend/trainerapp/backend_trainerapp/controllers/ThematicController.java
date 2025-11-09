package com.reffocase.backend.trainerapp.backend_trainerapp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Thematic;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.ThematicService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tematicas") // Ruta base para Thematic
@CrossOrigin(origins = "*")
public class ThematicController {

    private final ThematicService thematicService;

    // Constructor para inyección de dependencias
    public ThematicController(ThematicService thematicService) {
        this.thematicService = thematicService;
    }

    // Obtener todas las temáticas sin paginación
    // Uso en los formularios donde tienen que aparecer todas si o si

    @GetMapping("/")
    public List<Thematic> findList() {
        return thematicService.findAll();
    }

    // Obtener las temáticas listadas con paginador
    // Uso en busquedas donde solo queremos ver los resultados a fracciones
    @GetMapping
    public Page<Thematic> findPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size,
            @RequestParam(name = "name", defaultValue = "") String name) {
        Pageable pageable = PageRequest.of(page, size);
        return thematicService.findThematicsPage(name,pageable);
    }

    // Obtener una temática por ID
    @GetMapping("/{id}")
    public Optional<Thematic> findById(@PathVariable Long id) {
        return thematicService.findById(id);
    }

    // Crear una nueva temática
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Thematic thematic, BindingResult result) {
        if (result.hasErrors()) {
            return UtilController.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(thematicService.save(thematic));
    }

    // Actualizar una temática existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Thematic thematic, BindingResult result,
            @PathVariable Long id) {
        if (result.hasErrors()) {
            return UtilController.validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(thematicService.update(id, thematic));
    }

    // Eliminar una temática por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        thematicService.delete(id);
    }

}
