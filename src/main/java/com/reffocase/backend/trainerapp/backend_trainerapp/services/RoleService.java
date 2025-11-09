package com.reffocase.backend.trainerapp.backend_trainerapp.services;

import java.util.List;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Role;

public interface RoleService {
    List<Role> findAll();
}
