package com.example.backstagetel.Services;

import com.example.backstagetel.Entities.Reclamation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReclamationService {

    public Reclamation ajouterReclamation(Reclamation reclamation, int idUser);

    public List<Reclamation> getReclamations();

    public Reclamation getReclamation(int idReclamation);

    public List<Reclamation> getReclamationsByUser(int idUser);

    public void deletereclamation(int idReclamation);

    public ResponseEntity<?> modifierReclamation(int idReclamation, Reclamation newReclamation);

    public Reclamation makeReclamationEnCours(int idReclamation);

    public Reclamation makeReclamationRejetee(int idReclamation);

    public Reclamation repondreReclamation(int idReclamation, Reclamation newReclamation);

    public List <Reclamation> getReclamationsByUserAndNumeroconcerne(String email, int numeroconcerne);


}
