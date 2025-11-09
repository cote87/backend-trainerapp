package com.reffocase.backend.trainerapp.backend_trainerapp.auth.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.dto.UserDto;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.request.UserEditRequest;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.request.UserRequest;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(originPatterns = "*")
public class UserController {
    @Autowired
    private UserService service;

    // Lista de usuarios paginados
    @GetMapping
    public Page<UserDto> userList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size,
            @RequestParam(name = "username", defaultValue = "") String username) {

        String currentRole = getCurrentRole();

        switch (currentRole) {
            case "sadmin":
                return service.findUsersPage(page, size, null, username, false);
            case "admin":
                return service.findUsersPage(page, size, getProvinceId(), username, true);
            default:
                return null;
        }
    }

    // Guardar un User
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) {

        if (result.hasErrors()) {
            return validation(result);
        }

        String currentRole = getCurrentRole();

        switch (currentRole) {
            case "admin":
                if (userRequest.getUser().getRole().getName().equals("ROLE_ADMIN"))
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No está autorizado para realizar esta operación");
            case "sadmin":
                if (userRequest.getUser().getRole().getName().equals("ROLE_SADMIN"))
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No está autorizado para realizar esta operación");
                break;
            default:
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No está autorizado para realizar esta operación");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userRequest.getUser()));
    }

    // Editar un User

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @Valid @RequestBody UserEditRequest userRequest,
            BindingResult result) {

        if (result.hasErrors()) {
            return validation(result);
        }

        User userDB = service.findById(id).orElseThrow();

        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank() && userRequest.getChangePassword()) {
            if (!userRequest.getPassword().matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                return ResponseEntity.badRequest()
                        .body("La contraseña debe tener al menos 8 caracteres, una mayúscula y un número");
            }
            userDB.setPassword(userRequest.getPassword());
        }

        userDB.setNickname(userRequest.getNickname());
        userDB.setProvince(userRequest.getProvince());
        userDB.setRole(userRequest.getRole());
    
        return ResponseEntity.status(HttpStatus.OK).body(service.save(userDB));

    }

    // Eliminar un User

    @DeleteMapping("/{id}")
    public Boolean deleteUser(
            @PathVariable Long id) {
        try {
            service.remove(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Funcionalidades

    private String getCurrentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SADMIN"))) {
            // Si es Super Admin
            return "sadmin";
        } else {
            if (auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                // Si es Admin
                return "admin";
            }
        }
        // Si no es admin ni super admin no deberia ver nada.
        return "other";
    }

    private Long getProvinceId() {
        return service.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get()
                .getProvince().getId();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
