package com.example.backstagetel.Services;

import com.example.backstagetel.Configurations.JwtUtils;
import com.example.backstagetel.Entities.EtatCompte;
import com.example.backstagetel.Entities.EtatRecl;
import com.example.backstagetel.Entities.Reclamation;
import com.example.backstagetel.Entities.Utilisateur;
import com.example.backstagetel.Repositories.ReclamationRepository;
import com.example.backstagetel.Repositories.RoleRepository;
import com.example.backstagetel.Repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

        if(utilisateur.getEtatCompte().equals(EtatCompte.ACTIF) )
        {
           // Création de la réclamation
           reclamation.setEtatRecl(EtatRecl.EN_ATTENTE); // état par défaut
           reclamation.setDateRecl(new Date()); // date de création actuelle
           reclamation.setUtilisateurRecl(utilisateur);
           return reclamationRepository.save(reclamation);
        }
        throw new RuntimeException("compte inactif !!!!!!!");
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

    public Reclamation makeReclamationEnCours(int idReclamation) {
        Reclamation existingReclamation = reclamationRepository.findById(idReclamation)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée avec ID : " + idReclamation));

        existingReclamation.setEtatRecl(EtatRecl.EN_COURS);
        return reclamationRepository.save(existingReclamation);
    }


    public Reclamation repondreReclamation(int idReclamation, Reclamation newReclamation) {
        Reclamation existingReclamation = reclamationRepository.findById(idReclamation)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée avec ID : " + idReclamation));

        existingReclamation.setDescriptionReponRecl(newReclamation.getDescriptionReponRecl());
        existingReclamation.setDateReponRecl(new Date());
        existingReclamation.setEtatRecl(EtatRecl.TRAITEE);

        Reclamation savedReclamation = reclamationRepository.save(existingReclamation);
        if (savedReclamation != null) {
            String subject = "Votre Reponse de la raclamation avec l'id "+ existingReclamation.getIdRecl()+ " a été créé par l'agent";
            String message = "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>" +
                    "<div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +
                    "<h2 style='color: #2e6c80;'>Réponse à votre réclamation n°" + existingReclamation.getIdRecl() + "</h2>" +
                    "<p>Bonjour,</p>" +
                    "<p>Nous vous informons que votre réclamation a été traitée. Voici la réponse apportée par notre équipe :</p>" +
                    "<blockquote style='border-left: 4px solid #2e6c80; margin: 20px 0; padding: 10px 20px; background-color: #f0f8ff;'>" +
                    existingReclamation.getDescriptionReponRecl() +
                    "</blockquote>" +
                    "<p>Date de réponse : <strong>" + new SimpleDateFormat("dd/MM/yyyy").format(existingReclamation.getDateReponRecl()) + "</strong></p>" +
                    "<p>Nous restons à votre disposition pour toute information complémentaire.</p>" +
                    "<br><p>Cordialement,<br><strong>L'équipe de support</strong></p>" +
                    "</div>" +
                    "</body></html>";


            emailService.send(existingReclamation.getUtilisateurRecl().getEmailUser(), subject, message);
        }

        return savedReclamation;
    }

}
