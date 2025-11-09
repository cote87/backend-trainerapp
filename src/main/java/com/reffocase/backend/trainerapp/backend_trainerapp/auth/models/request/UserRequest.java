package com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.request;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class UserRequest {
    @Valid
    private User user;
}
