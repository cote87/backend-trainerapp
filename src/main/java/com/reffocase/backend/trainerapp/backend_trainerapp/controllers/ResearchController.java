package com.reffocase.backend.trainerapp.backend_trainerapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Research;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.ResearchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/investigaciones")
@CrossOrigin(origins = "*")
public class ResearchController {

    @Autowired
    private ResearchService researchService;

    @GetMapping
    public ResponseEntity<Page<Research>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long thematicId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(researchService.findAll(name, thematicId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Research> getById(@PathVariable Long id) {
        return ResponseEntity.ok(researchService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Research> create(@Valid @RequestBody Research research) {
        return new ResponseEntity<>(researchService.save(research), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Research> update(@PathVariable Long id, @Valid @RequestBody Research research) {
        return ResponseEntity.ok(researchService.update(id, research));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        researchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
