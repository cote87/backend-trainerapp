package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Role;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

}
