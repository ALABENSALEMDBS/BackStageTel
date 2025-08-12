package com.example.backstagetel.Services;

import com.example.backstagetel.Entities.Avis;
import com.example.backstagetel.Entities.Reclamation;
import com.example.backstagetel.Repositories.AvisRepository;
import com.example.backstagetel.Repositories.ReclamationRepository;
import com.example.backstagetel.Repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AvisService {

    AvisRepository avisRepository;
    UtilisateurRepository utilisateurRepository;
    ReclamationRepository reclamationRepository;


    public Avis addAvis(Avis avis, int idReclamation) {
        Reclamation reclamation = reclamationRepository.findById(idReclamation)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée avec ID : " + idReclamation));

        // Vérifier si un avis est déjà associé
        if (reclamation.getAvisRecl() != null) {
            throw new RuntimeException("Un avis existe déjà pour cette réclamation (ID : " + idReclamation + ")");
        }
        reclamation.setAvisRecl(avis);
        return avisRepository.save(avis);
    }

    public List <Avis> retrieveAvis() {
        return avisRepository.findAll();
    }
}
