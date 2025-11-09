package com.reffocase.backend.trainerapp.backend_trainerapp.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
        // findByNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingAndThematics_NameContainingIgnoreCaseAndProvince_NameContainingIgnoreCaseAndEnabled
        Page<Trainer> findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndThematics_NameContainingIgnoreCaseAndProvince_NameContainingIgnoreCase(
                        Boolean enabled,
                        String name,
                        String lastname,
                        String documentNumber,
                        String thematic,
                        String province,
                        Pageable pageable);

        Page<Trainer> findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndProvince_NameContainingIgnoreCase(
                        Boolean enabled,
                        String name,
                        String lastname,
                        String documentNumber,
                        String province,
                        Pageable pageable);

        List<Trainer> findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndThematics_NameContainingIgnoreCaseAndProvince_NameContainingIgnoreCaseOrderByLastnameAsc(
                        Boolean enabled,
                        String name,
                        String lastname,
                        String documentNumber,
                        String thematic,
                        String province);

        List<Trainer> findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndProvince_NameContainingIgnoreCaseOrderByLastnameAsc(
                        Boolean enabled,
                        String name,
                        String lastname,
                        String documentNumber,
                        String province);

        List<Trainer> findByThematics_NameContainingIgnoreCase(String thematics);
}
