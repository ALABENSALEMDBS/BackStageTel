package com.example.backstagetel.Services;

import com.example.backstagetel.Entities.*;
import com.example.backstagetel.Repositories.RenseignementRepository;
import com.example.backstagetel.Repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class RenseignementService implements IRenseignementService{

    RenseignementRepository renseignementRepository;
    UtilisateurRepository utilisateurRepository;
    private EmailService emailService;

    public Renseignement ajouterRenseignement(Renseignement renseignement, int idUser) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!utilisateur.getEtatCompte().equals(EtatCompte.ACTIF)) {
            throw new RuntimeException("Compte inactif !");
        }

        // Obtenir la date d'aujourd'hui (sans heure)
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        Date startDate = java.sql.Timestamp.valueOf(startOfDay);
        Date endDate = java.sql.Timestamp.valueOf(endOfDay);

        // Compter le nombre de renseignements créés aujourd'hui par cet utilisateur
        int nombreRenseignementsAujourdHui = renseignementRepository
                .countByUtilisateurRensAndDateRensBetween(utilisateur, startDate, endDate);

        if (nombreRenseignementsAujourdHui >= 3) {
            throw new RuntimeException("Limite de 3 ajouts par jour atteinte.");
        }

        renseignement.setDateRens(new Date());
        renseignement.setUtilisateurRens(utilisateur);
        return renseignementRepository.save(renseignement);
    }

    public List<Renseignement> findAllRenseignements() {
        return renseignementRepository.findAll();
    }




    public Renseignement repondreRenseignement(int idRenseignement, Renseignement newRenseignement) {
        Renseignement existingRenseignement = renseignementRepository.findById(idRenseignement)
                .orElseThrow(() -> new RuntimeException("Renseignement non trouvée avec ID : " + idRenseignement));

        existingRenseignement.setDescriptionReponRens(newRenseignement.getDescriptionReponRens());
        existingRenseignement.setDateReponRens(new Date());

        Renseignement savedRenseignement = renseignementRepository.save(existingRenseignement);
        if (savedRenseignement != null) {
            String subject = "Votre Reponse de la renseignement avec l'id "+ existingRenseignement.getIdRens()+ " a été créé par l'agent";
            String message = "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>" +
                    "<div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +
                    "<h2 style='color: #2e6c80;'>Réponse à votre renseignement n°" + existingRenseignement.getIdRens() + ": "+ existingRenseignement.getDescriptionRens() +"</h2>" +
                    "<p>Bonjour,</p>" +
                    "<p>Nous vous informons que votre renseignement a été traitée. Voici la réponse apportée par notre équipe :</p>" +
                    "<blockquote style='border-left: 4px solid #2e6c80; margin: 20px 0; padding: 10px 20px; background-color: #f0f8ff;'>" +
                    existingRenseignement.getDescriptionReponRens() +
                    "</blockquote>" +
                    "<p>Date de réponse : <strong>" + new SimpleDateFormat("dd/MM/yyyy").format(existingRenseignement.getDateReponRens()) + "</strong></p>" +
                    "<p>Nous restons à votre disposition pour toute information complémentaire.</p>" +
                    "<br><p>Cordialement,<br><strong>L'équipe de support</strong></p>" +
                    "</div>" +
                    "</body></html>";


            emailService.send(existingRenseignement.getUtilisateurRens().getEmailUser(), subject, message);
        }

        return savedRenseignement;
    }

}
