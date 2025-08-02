package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.Renseignement;
import com.example.backstagetel.Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RenseignementRepository extends JpaRepository<Renseignement, Integer> {

    int countByUtilisateurRensAndDateRensBetween(Utilisateur utilisateur, Date start, Date end);

}
