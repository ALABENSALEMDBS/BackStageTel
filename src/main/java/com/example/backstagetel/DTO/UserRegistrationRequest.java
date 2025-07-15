package com.example.backstagetel.DTO;

import com.example.backstagetel.Entities.EtatCompte;
import com.example.backstagetel.Entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRegistrationRequest {

    String nomUser;
    String prenomUser;
    String photoUser;
    String emailUser;
    String passwordUser;
//    Date createdAt;
    int numeroLigne;
    String documentContrat;

//    String etatCompte;
//
//    String role;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(String nomUser,String prenomUser, String photoUser, String emailUser, String passwordUser, int numeroLigne, String documentContrat) {
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.photoUser = photoUser;
        this.emailUser = emailUser;
        this.passwordUser = passwordUser;
       //       this.createdAt = createdAt;
        this.numeroLigne = numeroLigne;
        this.documentContrat = documentContrat;
//        this.role = role;
//        this.etatCompte = etatCompte;
    }

}
