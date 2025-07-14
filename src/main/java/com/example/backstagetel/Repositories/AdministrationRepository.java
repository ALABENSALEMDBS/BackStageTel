package com.example.backstagetel.Repositories;

import com.example.backstagetel.Entities.Administration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrationRepository extends JpaRepository<Administration, Integer> {
}
