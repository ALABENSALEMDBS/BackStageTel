package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.Reclamation;
import com.example.backstagetel.Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {

    List<Reclamation> findByUtilisateurRecl(Utilisateur utilisateur);

    boolean existsByUtilisateurReclAndDateReclBetween(Utilisateur utilisateur, Date start, Date end);

    List<Reclamation> findByUtilisateurRecl_EmailUserAndNumeroConcerne(String emailUser, int numeroConcerne);


}
