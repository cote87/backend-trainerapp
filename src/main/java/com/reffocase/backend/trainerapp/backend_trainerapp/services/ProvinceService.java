package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.util.List;
import java.util.Optional;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;

public interface ProvinceService {
    Province save(Province province);
    Province update(Long id, Province provinceDetails);
    Optional<Province> findById(Long id);
    List<Province> findAll();
    void delete(Long id);
    List<Province> saveAll(List<Province> provinces);
}

