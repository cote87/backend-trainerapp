package com.reffocase.backend.trainerapp.backend_trainerapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Thematic;

public interface ThematicRepository extends JpaRepository<Thematic,Long>{

    Page<Thematic> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
