package com.reffocase.backend.trainerapp.backend_trainerapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name="provinces")
@Data
public class Province {

    public Province(){}
    
    public Province(@NotBlank String code, @NotBlank String name) {
        this.code = code;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique=true)
    private String code;

    @NotBlank
    @Column(unique = true)
    private String name;

}
