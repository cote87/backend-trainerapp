package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.DocumentType;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.DocumentTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @SuppressWarnings("null")
    @Override
    public DocumentType save(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }

    @Override
    @Transactional
    public DocumentType update(Long id, DocumentType documentTypeDetails) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DocumentType no encontrado con ID: " + id));

        documentType.setName(documentTypeDetails.getName()); // Suponiendo que tiene un campo "name"
        
        return documentTypeRepository.save(documentType);
    }

    @Override
    public Optional<DocumentType> findById(Long id) {
        return documentTypeRepository.findById(id);
    }

    @Override
    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        if (!documentTypeRepository.existsById(id)) {
            throw new RuntimeException("DocumentType no encontrado con ID: " + id);
        }
        documentTypeRepository.deleteById(id);
    }
}
