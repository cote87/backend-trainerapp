package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Thematic;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.ThematicRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ThematicServiceImpl implements ThematicService {

    private final ThematicRepository thematicRepository;

    public ThematicServiceImpl(ThematicRepository thematicRepository) {
        this.thematicRepository = thematicRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Thematic> findThematicsPage( String name ,Pageable pageable) {
        return thematicRepository.findByNameContainingIgnoreCase(name,pageable);
    }

    @Override
    @Transactional
    public Thematic save(Thematic thematic) {
        return thematicRepository.save(thematic);
    }

    @Override
    @Transactional
    public Thematic update(Long id, Thematic thematicDetails) {
        Thematic thematic = thematicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thematic no encontrado con ID: " + id));

        // Aquí se pueden actualizar los campos del objeto "thematic" con los detalles
        // nuevos
        thematic.setName(thematicDetails.getName()); // Ejemplo de campo "name"

        return thematicRepository.save(thematic);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!thematicRepository.existsById(id)) {
            throw new RuntimeException("Thematic no encontrado con ID: " + id);
        }
        thematicRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Thematic> findById(Long id) {
        return thematicRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Thematic> findAll() {
        return thematicRepository.findAll();
    }
}
