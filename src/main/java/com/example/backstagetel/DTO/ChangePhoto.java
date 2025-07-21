package com.example.backstagetel.DTO;

import com.example.backstagetel.Entities.EtatCompte;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ChangePhoto {
    private String role;

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String photoUser;
    private String documentContrat;
    private Date createdAt;
    private int numeroLigne;

    @Enumerated(EnumType.STRING)
    private EtatCompte etatCompte;
}
