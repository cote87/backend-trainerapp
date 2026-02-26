package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Research;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Researcher;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.ResearchRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.ResearcherRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ResearchServiceImpl implements ResearchService {

    @Autowired
    private ResearchRepository researchRepository;

    @Autowired
    private ResearcherRepository researcherRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Research> findAll(String name, Long thematicId, Pageable pageable) {
        Specification<Research> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (thematicId != null) {
                // Join para filtrar por ID de temática en la relación ManyToMany
                predicates.add(cb.equal(root.join("thematics").get("id"), thematicId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return researchRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Research findById(Long id) {
        return researchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Investigación no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public Research save(Research research) {

        Set<Researcher> researchers = research.getResearchers();
        for (Researcher res : researchers) {
            if (res.getId() == 0)
                res.setId(null);
        }

        return researchRepository.save(research);
    }

    @Override
    @Transactional
    public Research update(Long id, Research newData) {
        Research existing = findById(id); // Trae el objeto de la DB

        existing.setName(newData.getName());
        existing.setDescription(newData.getDescription());
        existing.setUserId(newData.getUserId());

        // Actualizar Temáticas (asumiendo que vienen con ID correcto)
        existing.getThematics().clear();
        if (newData.getThematics() != null) {
            existing.getThematics().addAll(newData.getThematics());
        }

        // Actualizar Researchers

        Set<Long> deleteResearchersId = new HashSet<>();
        for(Researcher res: existing.getResearchers()){
            boolean delete = true; 
            for(Researcher newRes : newData.getResearchers()){
                if(newRes.getId() == res.getId()) delete = false;
            }
            if(delete){
                deleteResearchersId.add(res.getId());
            }
        }

        existing.getResearchers().clear(); // Limpiamos la relación vieja
        if (newData.getResearchers() != null) {
            for (Researcher res : newData.getResearchers()) {
                // Normalizamos el ID 0 a null para nuevos registros
                if (res.getId() != null && res.getId() <= 0) {
                    res.setId(null);
                }

                // Si el investigador es nuevo, el Cascade PERSIST lo creará.
                // Si tiene ID, el Cascade MERGE lo actualizará si cambió algo.
                existing.getResearchers().add(res);
            }
        }

        for(Long deleteId : deleteResearchersId){
            Researcher researcher = researcherRepository.getById(deleteId);
            researcherRepository.delete(researcher);
        }

        return researchRepository.save(existing);
    }   

    @Override
    public void delete(Long id) {
        if (!researchRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar: Investigación no existe.");
        }
        // Al borrar la entidad, JPA se encarga de limpiar las tablas intermedias
        // automágicamente
        researchRepository.deleteById(id);
    }

}
