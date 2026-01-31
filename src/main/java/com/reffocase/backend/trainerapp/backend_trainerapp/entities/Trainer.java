package com.reffocase.backend.trainerapp.backend_trainerapp.entities;

import java.util.HashSet;
import java.util.Set;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "trainers")
@Data
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Column(unique = true)
    private String documentNumber;

    @NotBlank
    private String areaCode;
    
    @NotBlank
    private String phone;

    private String cv;

    @NotNull
    private Boolean enabled;

    @Size(max = 255, message = "El nombre de la institución y/o fuerza no puede superar los 255 caracteres.")
    private String institution;

    @NotNull
    @Valid
    @ManyToOne
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

    @NotNull
    @Valid
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    private Long userId;

    @ManyToMany
    @JoinTable(
        name = "trainer_thematics",
        joinColumns = @JoinColumn(name = "trainer_id"),
        inverseJoinColumns = @JoinColumn(name = "thematic_id")
    )
    private Set<Thematic> thematics = new HashSet<>();

    @Transient  // Este campo NO se almacena en la base de datos
    private String provinceName;

    public String getProvinceName() {
        return province != null ? province.getName() : null;
    }

}
