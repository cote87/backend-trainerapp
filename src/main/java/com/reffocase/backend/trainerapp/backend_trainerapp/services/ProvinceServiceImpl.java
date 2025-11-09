package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.ProvinceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    @Override
    public Province save(Province province) {
        return provinceRepository.save(province);
    }

    @Override
    @Transactional
    public Province update(Long id, Province provinceDetails) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Province no encontrada con ID: " + id));

        province.setName(provinceDetails.getName()); // Suponiendo que tiene un campo "name"
        
        return provinceRepository.save(province);
    }

    @Override
    public Optional<Province> findById(Long id) {
        return provinceRepository.findById(id);
    }

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        if (!provinceRepository.existsById(id)) {
            throw new RuntimeException("Province no encontrada con ID: " + id);
        }
        provinceRepository.deleteById(id);
    }

    @Override
    public List<Province> saveAll(List<Province> provinces) {
        return provinceRepository.saveAll(provinces);
    }
}

