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

    @PutMapping("/modifierRecl/{id}")
    public ResponseEntity<?> modifierReclamation(@PathVariable int id, @RequestBody Reclamation newReclamation) {
        try {
            Reclamation updatedReclamation = reclamationService.modifierReclamation(id, newReclamation);
            return ResponseEntity.ok(updatedReclamation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
        }
    }

}
