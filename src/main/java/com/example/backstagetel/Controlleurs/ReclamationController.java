package com.example.backstagetel.Controlleurs;

import com.example.backstagetel.Entities.Reclamation;
import com.example.backstagetel.Services.ReclamationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestion Reclamation")
@RestController
@AllArgsConstructor
@RequestMapping("/reclamation")
@CrossOrigin(origins = "http://localhost:4200")
public class ReclamationController {

     ReclamationService reclamationService;


    @PostMapping("/ajouter/{idUser}")
    public ResponseEntity<Reclamation> ajouterReclamation( @RequestBody Reclamation reclamation, @PathVariable int idUser) {

        Reclamation saved = reclamationService.ajouterReclamation(reclamation, idUser);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/getReclByUser/{idUser}")
    public ResponseEntity<List<Reclamation>> getReclamationsByUser(@PathVariable int idUser) {
        List<Reclamation> reclamations = reclamationService.getReclamationsByUser(idUser);
        return ResponseEntity.ok(reclamations);
    }

    @GetMapping("/reclamationBYId/{idReclamation}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable int idReclamation) {
        Reclamation reclamation = reclamationService.getReclamation(idReclamation);
        return ResponseEntity.ok(reclamation);
    }

    @GetMapping("/getReclamations")
    public ResponseEntity<List<Reclamation>> getReclamations() {
        List<Reclamation> reclamations = reclamationService.getReclamations();
        return ResponseEntity.ok(reclamations);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReclamation(@PathVariable int id) {
        try {
            reclamationService.deletereclamation(id);
            return ResponseEntity.ok("Reclamation supprimé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    @PutMapping("/modifierRecl/{id}")
//    public ResponseEntity<?> modifierReclamation(@PathVariable int id, @RequestBody Reclamation newReclamation) {
//        try {
//            Reclamation updatedReclamation = reclamationService.modifierReclamation(id, newReclamation);
//            return ResponseEntity.ok(updatedReclamation);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
//        }
//    }

    @PutMapping("/modifierRecl/{id}")
    public ResponseEntity<?> modifierReclamation(@PathVariable int id, @RequestBody Reclamation newReclamation) {
        try {
            ResponseEntity<?> updatedReclamation = reclamationService.modifierReclamation(id, newReclamation);
            return ResponseEntity.ok(updatedReclamation);
        }
        catch (IllegalStateException e) {
            // Erreur de logique métier (ex: état != EN_ATTENTE)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
        catch (RuntimeException e) {
            // Réclamation non trouvée ou autre erreur inattendue
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
        }
    }



    @PutMapping("/en-cours/{idReclamation}")
    public ResponseEntity<Reclamation> mettreReclamationEnCours(@PathVariable int idReclamation) {
        Reclamation updatedReclamation = reclamationService.makeReclamationEnCours(idReclamation);
        return ResponseEntity.ok(updatedReclamation);
    }

    @PutMapping("/rejetee/{idReclamation}")
    public ResponseEntity<Reclamation> mettreReclamationRejetee(@PathVariable int idReclamation) {
        Reclamation updatedReclamation = reclamationService.makeReclamationRejetee(idReclamation);
        return ResponseEntity.ok(updatedReclamation);
    }

    @PutMapping("/repondre/{idReclamation}")
    public ResponseEntity<Reclamation> repondreReclamation(
            @PathVariable int idReclamation,
            @RequestBody Reclamation newReclamation) {

        Reclamation updatedReclamation = reclamationService.repondreReclamation(idReclamation, newReclamation);
        return ResponseEntity.ok(updatedReclamation);
    }


    @GetMapping("/by-user/{email}/{numeroConcerne}")
    public ResponseEntity<List<Reclamation>> getReclamationsByUserAndNumero(
            @PathVariable String email,
            @PathVariable int numeroConcerne) {

        List<Reclamation> reclamations = reclamationService.getReclamationsByUserAndNumeroconcerne(email, numeroConcerne);
        return ResponseEntity.ok(reclamations);
    }

}
