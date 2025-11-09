package com.reffocase.backend.trainerapp.backend_trainerapp.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Role;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.RoleService;

@RestController
@RequestMapping("/api/roles") 
@CrossOrigin(origins = "*")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> findAll(){
        List<Role> roles = roleService.findAll();
        roles.removeIf(r -> r.getName().equals("ROLE_SADMIN"));
        return roles;
    } 

}
