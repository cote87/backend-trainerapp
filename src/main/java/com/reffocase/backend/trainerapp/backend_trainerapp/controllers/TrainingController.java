package com.reffocase.backend.trainerapp.backend_trainerapp.controllers;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Mode;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Training;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.TrainingService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @GetMapping("/capacitaciones")
    public Page<Training> getCapacitaciones(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String trainer,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @RequestParam(required = false) Mode mode,
            @RequestParam(required = false) Long provinceId,
            @RequestParam(required = false) Long thematicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
            ) {

        if (startDateFrom == null) {
            startDateFrom = LocalDate.now(); // por defecto desde hoy
        }

        return trainingService.getFilteredTrainings(
                title, trainer, startDateFrom, startDateTo,
                provinceId, thematicId, mode,
                page, size, sortBy, sortDir);
    }

    @GetMapping("/capacitaciones-list")
    public List<Training> getCapacitacionesList(
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String trainer,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @RequestParam(required = false) Mode mode,
            @RequestParam(required = false) Long provinceId,
            @RequestParam(required = false) Long thematicId) {

        if (startDateFrom == null) {
            startDateFrom = LocalDate.now();
        }

        return trainingService.getFilteredTrainingsList(
                title, trainer, startDateFrom, startDateTo, provinceId, thematicId, mode, sortBy, sortDir);
    }

    @GetMapping("/capacitaciones/{id}")
    public ResponseEntity<Training> getTraining(@PathVariable Long id) {
        try {
            Training training = trainingService.getTrainingById(id);
            return ResponseEntity.ok(training);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/capacitaciones")
    public ResponseEntity<Training> createCapacitacion(@Valid @RequestBody Training training) {
        Training saved = trainingService.saveTraining(training);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/capacitaciones/{id}")
    public ResponseEntity<Training> updateCapacitacion(@PathVariable Long id, @Valid @RequestBody Training training) {
        Training saved = trainingService.updateTraining(id, training);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/capacitaciones/{id}")
    public void deleteCapacitacion(@PathVariable Long id) {
        trainingService.deleteTraining(id);
    }
}
