package com.reffocase.backend.trainerapp.backend_trainerapp.entities;

import com.reffocase.backend.trainerapp.backend_trainerapp.audit.AuditEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "thematics")
@EntityListeners(AuditEntityListener.class)
@Data
public class Thematic {

    public Thematic() {
    }

    public Thematic(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

}
