package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.util.List;
import java.util.Optional;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.DocumentType;

public interface DocumentTypeService {
    DocumentType save(DocumentType documentType);
    DocumentType update(Long id, DocumentType documentTypeDetails);
    Optional<DocumentType> findById(Long id);
    List<DocumentType> findAll();
    void delete(Long id);
}
