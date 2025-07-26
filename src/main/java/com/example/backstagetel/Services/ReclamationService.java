package com.example.backstagetel.Services;

import com.example.backstagetel.Configurations.JwtUtils;
import com.example.backstagetel.Entities.EtatRecl;
import com.example.backstagetel.Entities.Reclamation;
import com.example.backstagetel.Entities.Utilisateur;
import com.example.backstagetel.Repositories.ReclamationRepository;
import com.example.backstagetel.Repositories.RoleRepository;
import com.example.backstagetel.Repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReclamationService implements IReclamationService {


    private final ReclamationRepository reclamationRepository;
    UtilisateurRepository utilisateurRepository;
    private EmailService emailService;


    public Reclamation ajouterReclamation(Reclamation reclamation,int idUser) {

        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));


        // Obtenir l'heure actuelle et l'heure une heure avant
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, -1);
        //    cal.add(Calendar.MINUTE, -15);
        Date oneHourAgo = cal.getTime();

        // Vérifier s'il existe une réclamation de cet utilisateur dans la dernière heure
        boolean hasRecentReclamation = reclamationRepository.existsByUtilisateurReclAndDateReclBetween(utilisateur, oneHourAgo, now);

        if (hasRecentReclamation) {
            throw new RuntimeException("Vous ne pouvez soumettre qu'une seule réclamation par heure.");
        }

        // Création de la réclamation
        reclamation.setEtatRecl(EtatRecl.EN_ATTENTE); // état par défaut
        reclamation.setDateRecl(new Date()); // date de création actuelle
        reclamation.setUtilisateurRecl(utilisateur);
        return reclamationRepository.save(reclamation);
    }

    public List<Reclamation> getReclamations() {
        return reclamationRepository.findAll();
    }

    public Reclamation getReclamation(int idReclamation) {
        return reclamationRepository.findById(idReclamation)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée avec ID : " + idReclamation));

    }

    public List<Reclamation> getReclamationsByUser(int idUser) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ID : " + idUser));

        return reclamationRepository.findByUtilisateurRecl(utilisateur);
    }


    public void deletereclamation(int idReclamation) {
        Reclamation reclamation = reclamationRepository.findById(idReclamation)
                .orElseThrow(() -> new RuntimeException("Reclamation non trouvé avec ID : " + idReclamation));
        reclamationRepository.deleteById(idReclamation);
    }


    public Reclamation modifierReclamation(int idReclamation, Reclamation newReclamation) {
        Reclamation existingReclamation = reclamationRepository.findById(idReclamation)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée avec ID : " + idReclamation));

        existingReclamation.setTypeRecl(newReclamation.getTypeRecl());
        existingReclamation.setDescriptionRecl(newReclamation.getDescriptionRecl());
        existingReclamation.setCaptureRecl(newReclamation.getCaptureRecl());
        existingReclamation.setDocumentRecl(newReclamation.getDocumentRecl());

        return reclamationRepository.save(existingReclamation);
    }

}
