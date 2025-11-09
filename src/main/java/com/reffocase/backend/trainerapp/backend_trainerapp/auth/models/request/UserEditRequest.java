package com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.request;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Role;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserEditRequest {

    @NotBlank
    String nickname;
    @NotNull
    Province province;
    @NotNull
    Role role;
    Boolean changePassword;
    String password;

}
