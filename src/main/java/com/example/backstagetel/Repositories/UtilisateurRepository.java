package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmailUser(String email);
    Optional<Utilisateur> findByNomUser(String nom);

    List<Utilisateur> findByRole_NomRole(String nomRole);
    boolean existsByEmailUser(String email);

}
