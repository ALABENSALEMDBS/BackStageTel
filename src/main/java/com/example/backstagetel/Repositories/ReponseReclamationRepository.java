package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.ReponseReclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseReclamationRepository extends JpaRepository<ReponseReclamation, Integer> {
}
