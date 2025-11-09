package com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long>{

}
