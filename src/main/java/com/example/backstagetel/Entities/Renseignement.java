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
public class Renseignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRens;

    private String sujetRens;
    private String descriptionRens;
    private Date dateRens;
    private String descriptionReponRens;
    private Date dateReponRens;

    @ManyToOne
    Utilisateur utilisateurRens;
}
