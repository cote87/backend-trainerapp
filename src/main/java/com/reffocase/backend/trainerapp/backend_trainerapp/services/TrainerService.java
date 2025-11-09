package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Trainer;

public interface TrainerService {

    List<Trainer> findAll(String thematics);

    Page<Trainer> findAll(String name, String lastname, String documentNumber, String thematic, String province,
            String enabled, int page, int size, String sortBy, String asc);

    List<Trainer> findAll(String name, String lastname, String documentNumber, String thematic, String province,
            String enabled);

    Optional<Trainer> findById(Long id);

    Trainer save(Trainer trainer);

    Trainer update(Long id, Trainer trainer);

    void delete(Long id);
}
