package com.reffocase.backend.trainerapp.backend_trainerapp.repositories;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TrainingRepository extends JpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {
}
