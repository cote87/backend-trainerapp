package com.reffocase.backend.trainerapp.backend_trainerapp.auth.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.request.ProfileRequest;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.request.ValidationRequest;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = "*")
public class AuthController {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/validacion")
    public ResponseEntity<?> verifyPassword(@RequestBody ValidationRequest validationRequest, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();

        boolean valid = passwordEncoder.matches(validationRequest.getPassword(), user.getPassword());

        return ResponseEntity.ok(Map.of("success", valid));
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ProfileRequest profileRequest, BindingResult result) {

        if (result.hasErrors()) {
            return validation(result);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userService.findByUsername(auth.getName()).orElseThrow();

        if (!passwordEncoder.matches(profileRequest.getCurrentPassword(), loggedUser.getPassword())) {
            return ResponseEntity.badRequest().body("La contraseña actual es incorrecta.");
        }

        loggedUser.setUsername(profileRequest.getUsername());
        loggedUser.setNickname(profileRequest.getNickname());
        loggedUser.setPassword(profileRequest.getNewPassword());

        return ResponseEntity.status(HttpStatus.OK).body(userService.save(loggedUser));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
