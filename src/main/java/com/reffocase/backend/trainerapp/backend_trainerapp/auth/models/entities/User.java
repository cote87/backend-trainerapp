package com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String nickname;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "contraseña no tiene al menos 8 caracteres, una mayúscula y un número")
    @NotBlank
    private String password;

    @NotNull
    @Valid
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Province province;

    @NotNull(message = "El rol es obligatorio")
    @Valid
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean enabled;
}
