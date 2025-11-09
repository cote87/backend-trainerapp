package com.reffocase.backend.trainerapp.backend_trainerapp.controllers;

import org.springframework.web.bind.annotation.*;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.ProvinceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/provincias") // Ruta base para Province
@CrossOrigin(origins = "*")
public class ProvinceController {

    private final ProvinceService provinceService;

    // Constructor para inyección de dependencias
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    // Crear una nueva provincia
    @PostMapping
    public Province save(@RequestBody Province province) {
        return provinceService.save(province);
    }

    // Actualizar una provincia existente
    @PutMapping("/{id}")
    public Province update(@PathVariable Long id, @RequestBody Province province) {
        return provinceService.update(id, province);
    }

    // Eliminar una provincia por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        provinceService.delete(id);
    }

    // Obtener todas las provincias
    @GetMapping
    public List<Province> findAll() {
        return provinceService.findAll();
    }

    // Obtener una provincia por ID
    @GetMapping("/{id}")
    public Optional<Province> findById(@PathVariable Long id) {
        return provinceService.findById(id);
    }

    @PostMapping("/massive")
    public List<Province> saveAll(@RequestBody List<Province> provinces) {
        return provinceService.saveAll(provinces);
    }
}
