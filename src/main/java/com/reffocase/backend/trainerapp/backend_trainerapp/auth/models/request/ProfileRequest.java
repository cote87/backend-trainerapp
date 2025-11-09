package com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfileRequest {

    @NotBlank
    String username;
    
    @NotBlank
    String nickname;

    @NotBlank
    private String currentPassword;
    
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número")
    @NotBlank
    private String newPassword;
}
