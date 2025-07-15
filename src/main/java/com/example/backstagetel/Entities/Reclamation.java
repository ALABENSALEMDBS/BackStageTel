package com.example.backstagetel.Entities;

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

    @Enumerated(EnumType.STRING)
    private EtatRecl etatRecl;

    private Date dateRecl;

    private String descriptionReponRecl;
    private Date dateReponRecl;

    @ManyToOne
    Utilisateur utilisateurRecl;

}
