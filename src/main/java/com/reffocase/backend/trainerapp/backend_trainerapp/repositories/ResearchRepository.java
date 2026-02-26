package com.reffocase.backend.trainerapp.backend_trainerapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Research;

@Repository
public interface ResearchRepository extends JpaRepository<Research, Long>, JpaSpecificationExecutor<Research> {
}
