package com.reffocase.backend.trainerapp.backend_trainerapp.controllers;

import org.springframework.web.bind.annotation.*;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.DocumentType;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.DocumentTypeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipo-documento") // Ruta base para DocumentType
@CrossOrigin(origins = "*")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    // Constructor para inyección de dependencias
    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    // Crear un nuevo tipo de documento
    @PostMapping
    public DocumentType save(@RequestBody DocumentType documentType) {
        return documentTypeService.save(documentType);
    }

    // Actualizar un tipo de documento existente
    @PutMapping("/{id}")
    public DocumentType update(@PathVariable Long id, @RequestBody DocumentType documentType) {
        return documentTypeService.update(id, documentType);
    }

    // Eliminar un tipo de documento por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        documentTypeService.delete(id);
    }

    // Obtener todos los tipos de documento
    @GetMapping
    public List<DocumentType> findAll() {
        return documentTypeService.findAll();
    }

    // Obtener un tipo de documento por ID
    @GetMapping("/{id}")
    public Optional<DocumentType> findById(@PathVariable Long id) {
        return documentTypeService.findById(id);
    }
}
