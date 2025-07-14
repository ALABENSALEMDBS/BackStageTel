package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.Renseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenseignementRepository extends JpaRepository<Renseignement, Integer> {
}
