package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Thematic;

public interface ThematicService {

    Page<Thematic> findThematicsPage(String name,Pageable pageable);
    List<Thematic> findAll();
    Optional<Thematic> findById(Long id);

    Thematic save(Thematic thematic);
    Thematic update(Long id, Thematic thematicDetails);

    void delete(Long id);
}
