package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.DocumentType;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Thematic;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Trainer;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.DocumentTypeRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.ProvinceRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.ThematicRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.TrainerRepository;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final ProvinceRepository provinceRepository;
    private final ThematicRepository thematicRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository,
            DocumentTypeRepository documentTypeRepository,
            ProvinceRepository provinceRepository,
            ThematicRepository thematicRepository) {
        this.trainerRepository = trainerRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.provinceRepository = provinceRepository;
        this.thematicRepository = thematicRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAll(String thematics) {
        return (List<Trainer>) trainerRepository.findByThematics_NameContainingIgnoreCase(thematics);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    @Override
    @Transactional
    public Trainer save(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public Trainer update(Long id, Trainer trainerDetails) {
        // Buscar el Trainer en la base de datos
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer no encontrado con ID: " + id));

        // Actualizar los campos básicos
        trainer.setName(trainerDetails.getName());
        trainer.setLastname(trainerDetails.getLastname());
        trainer.setEmail(trainerDetails.getEmail());
        trainer.setCv(trainerDetails.getCv());
        trainer.setDocumentNumber(trainerDetails.getDocumentNumber());
        trainer.setPhone(trainerDetails.getPhone());
        trainer.setEnabled(trainerDetails.getEnabled());

        // Actualizar la relación con DocumentType (si viene un ID válido)
        if (trainerDetails.getDocumentType() != null) {
            DocumentType documentType = documentTypeRepository.findById(trainerDetails.getDocumentType().getId())
                    .orElseThrow(() -> new RuntimeException("DocumentType no encontrado"));
            trainer.setDocumentType(documentType);
        }

        // Actualizar la relación con Province (si viene un ID válido)
        if (trainerDetails.getProvince() != null) {
            Province province = provinceRepository.findById(trainerDetails.getProvince().getId())
                    .orElseThrow(() -> new RuntimeException("Province no encontrada"));
            trainer.setProvince(province);
        }

        // Actualizar la relación Many-to-Many con Thematics
        if (trainerDetails.getThematics() != null) {
            Set<Thematic> thematics = thematicRepository.findAllById(trainerDetails.getThematics().stream()
                    .map(Thematic::getId).toList()).stream().collect(Collectors.toSet());
            trainer.setThematics(thematics);
        }

        // Guardar y devolver el trainer actualizado
        return trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!trainerRepository.existsById(id)) {
            throw new RuntimeException("Formador no encontrado con ID: " + id);
        }
        trainerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Trainer> findAll(String name, String lastname, String documentNumber, String thematic, String province,
            String enabled, int page, int size, String sortBy, String asc) {

        // Se determinan las condiciones de ordenación de los resultados
        Pageable pageable;
        Boolean ascFilter = true;
        if (asc.contains("false")) {
            ascFilter = false;
        }

        List<String> options = new ArrayList<>();
        options.add("name");
        options.add("lastname");
        options.add("province");

        if (!options.contains(sortBy)) {
            sortBy = "lastname";
        }

        if ("province".equals(sortBy)) {
            sortBy = "provinceName"; // Cambia "province" por el campo virtual "provinceName"
        }

        if (ascFilter) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortBy)));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        }

        // Se determina si se muestran los resultados habilitados o deshabilitados
        // La razón es que si son formadores deshabilitados, no poseen tematicas
        // asociadas
        // Esto produce que no devuelva ningun resultado si se filtra por temática

        Boolean enabledFilter = true;
        if (enabled.contains("false")) {
            enabledFilter = false;
        }

        if (enabledFilter) {
            return trainerRepository
                    .findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndThematics_NameContainingIgnoreCaseAndProvince_NameContainingIgnoreCase(
                            enabledFilter,
                            name,
                            lastname,
                            documentNumber,
                            thematic,
                            province,
                            pageable);
        }

        return trainerRepository
                .findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndProvince_NameContainingIgnoreCase(
                        enabledFilter,
                        name,
                        lastname,
                        documentNumber,
                        province,
                        pageable);
    }

    @Override
    public List<Trainer> findAll(String name, String lastname, String documentNumber, String thematic, String province,
            String enabled) {

        // Se determina si se muestran los resultados habilitados o deshabilitados
        // La razón es que si son formadores deshabilitados, no poseen tematicas
        // asociadas
        // Esto produce que no devuelva ningun resultado si se filtra por temática

        Boolean enabledFilter = true;
        if (enabled.contains("false")) {
            enabledFilter = false;
        }

        if (enabledFilter) {
            return trainerRepository
                    .findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndThematics_NameContainingIgnoreCaseAndProvince_NameContainingIgnoreCaseOrderByLastnameAsc(
                            true,
                            name,
                            lastname,
                            documentNumber,
                            thematic,
                            province);
        }

        return trainerRepository
                .findDistinctByEnabledAndNameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndDocumentNumberContainingIgnoreCaseAndProvince_NameContainingIgnoreCaseOrderByLastnameAsc(
                        false,
                        name,
                        lastname,
                        documentNumber,
                        province);
    }

}
