package com.reffocase.backend.trainerapp.backend_trainerapp.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Research;

public interface ResearchService {
    Page<Research> findAll(String name, Long thematicId, Pageable pageable);
    Research findById(Long id);
    Research save(Research research);
    Research update(Long id, Research research);
    void delete(Long id);
}
