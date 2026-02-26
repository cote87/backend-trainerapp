package com.reffocase.backend.trainerapp.backend_trainerapp.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "researchs")
@Data
public class Research {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @NotEmpty(message = "Debe seleccionar al menos una temática")
    @ManyToMany
    @JoinTable(name = "research_thematics", joinColumns = @JoinColumn(name = "research_id"), inverseJoinColumns = @JoinColumn(name = "thematic_id"))
    private Set<Thematic> thematics = new HashSet<>();

    @NotEmpty(message = "Debe incluir al menos un investigador asociado")
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "research_researchers", joinColumns = @JoinColumn(name = "research_id"), inverseJoinColumns = @JoinColumn(name = "researcher_id"))
    private Set<Researcher> researchers = new HashSet<>();

    private Long userId;
}
