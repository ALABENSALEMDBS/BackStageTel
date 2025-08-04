package com.example.backstagetel.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRecl;

    @Enumerated(EnumType.STRING)
    private TypeRecl typeRecl;

    private String descriptionRecl;
    private String captureRecl;
    private String documentRecl;

    private int numeroConcerne;
    private String sujetRecl;

    @Enumerated(EnumType.STRING)
    private EtatRecl etatRecl;

    private Date dateRecl;

    private String descriptionReponRecl;
    private Date dateReponRecl;

//    @JsonIgnore
    @ManyToOne
    Utilisateur utilisateurRecl;

}
