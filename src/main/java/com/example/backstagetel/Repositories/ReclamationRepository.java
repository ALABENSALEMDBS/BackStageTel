package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
}
