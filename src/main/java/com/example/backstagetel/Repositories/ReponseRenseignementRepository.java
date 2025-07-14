package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.ReponseRenseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseRenseignementRepository extends JpaRepository<ReponseRenseignement, Integer> {
}
