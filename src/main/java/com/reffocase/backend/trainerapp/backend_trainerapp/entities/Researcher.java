package com.reffocase.backend.trainerapp.backend_trainerapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "researchers")
@Data
public class Researcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Valid
    String name;

    @ManyToOne
    @JoinColumn(name="trainer_id",nullable=true)
    Trainer trainer;

}
