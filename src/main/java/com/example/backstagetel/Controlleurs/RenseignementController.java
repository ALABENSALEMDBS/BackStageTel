package com.example.backstagetel.Controlleurs;

import com.example.backstagetel.Entities.Reclamation;
import com.example.backstagetel.Entities.Renseignement;
import com.example.backstagetel.Services.RenseignementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestion Renseignement")
@RestController
@AllArgsConstructor
@RequestMapping("/renseignement")
@CrossOrigin(origins = "http://localhost:4200")
public class RenseignementController {

    RenseignementService renseignementService;


    @PostMapping("/ajouter/{idUser}")
    public ResponseEntity<Renseignement> ajouterRenseignement(@RequestBody Renseignement renseignement, @PathVariable int idUser) {
        try {
            Renseignement nouveauRenseignement = renseignementService.ajouterRenseignement(renseignement, idUser);
            return ResponseEntity.ok(nouveauRenseignement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/listeRens")
    public ResponseEntity<List<Renseignement>> getAllRenseignements() {
        List<Renseignement> renseignements = renseignementService.findAllRenseignements();
        return ResponseEntity.ok(renseignements);
    }


    @PutMapping("/repondre/{idRenseignement}")
        public ResponseEntity<Renseignement> repondreRenseignement(
            @PathVariable int idRenseignement,
            @RequestBody Renseignement newRenseignement) {

        Renseignement updatedRenseignement = renseignementService.repondreRenseignement(idRenseignement, newRenseignement);
        return ResponseEntity.ok(updatedRenseignement);
    }
}
