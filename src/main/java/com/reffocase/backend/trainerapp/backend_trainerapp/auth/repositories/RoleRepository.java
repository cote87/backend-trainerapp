package com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{

}
