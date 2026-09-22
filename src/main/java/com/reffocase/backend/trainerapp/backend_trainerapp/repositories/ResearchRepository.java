package com.reffocase.backend.trainerapp.backend_trainerapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Research;

public interface ResearchRepository extends JpaRepository<Research, Long>, JpaSpecificationExecutor<Research> {
}
