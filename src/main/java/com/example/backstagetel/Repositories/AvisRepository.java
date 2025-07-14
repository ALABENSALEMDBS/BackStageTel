package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Integer> {
}
