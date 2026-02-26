package com.reffocase.backend.trainerapp.backend_trainerapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Researcher;

@Repository
public interface ResearcherRepository extends JpaRepository<Researcher,Long>{

}
