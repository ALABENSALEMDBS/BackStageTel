package com.example.backstagetel.Services;

import com.example.backstagetel.Entities.Avis;
import com.example.backstagetel.Repositories.AvisRepository;
import com.example.backstagetel.Repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AvisService {

    AvisRepository avisRepository;
    UtilisateurRepository utilisateurRepository;


    public Avis addAvis(Avis avis) {
        return avisRepository.save(avis);
    }

    public List <Avis> retrieveAvis() {
        return avisRepository.findAll();
    }
}
