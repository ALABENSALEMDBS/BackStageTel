package com.example.backstagetel.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;

    private String nomUser;
    private String prenomUser;
    private String photoUser;
    private String emailUser;
    private Date createdAt;
    private int numeroLigne;
    private String documentContrat;

    @Enumerated(EnumType.STRING)
    private EtatCompte etatCompte;

    @ManyToOne
    private Role role;

    @OneToMany (cascade = CascadeType.ALL)
    Set<Avis> avis= new HashSet<>();

    @OneToMany(mappedBy = "utilisateurRens", cascade = CascadeType.ALL)
    Set<Renseignement> renseignements= new HashSet<>();

    @OneToMany(mappedBy = "utilisateurRecl", cascade = CascadeType.ALL)
    Set<Reclamation> reclamations= new HashSet<>();
}
