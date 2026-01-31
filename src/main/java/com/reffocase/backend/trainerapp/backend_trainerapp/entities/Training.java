package com.reffocase.backend.trainerapp.backend_trainerapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import org.hibernate.annotations.JdbcTypeCode;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Types;
import java.time.LocalDate;

@Entity
@Table(name = "trainings")
@Data
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    private String title;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    @JdbcTypeCode(Types.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Argentina/Buenos_Aires")
    private LocalDate startDate;

    @NotNull(message = "El modo de capacitación es obligatorio")
    @Enumerated(EnumType.STRING)
    private Mode mode; // Presencial (ONSITE) / A distancia (ONLINE) / Presencial y a distancia (HYBRID)

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @NotBlank(message = "El organizador es obligatorio")
    @Size(max = 255, message = "El nombre del organizador no puede superar los 255 caracteres")
    private String organizer;

    // Relación con Thematic
    @ManyToOne
    @JoinColumn(name = "thematic_id")
    @NotNull(message="La temática es obligatoria")
    private Thematic thematic;

    // Relación con Province
    @ManyToOne
    @JoinColumn(name= "province_id")
    @NotNull(message = "La provincia es obligatoria")
    private Province province;

    private Long userId;
}

