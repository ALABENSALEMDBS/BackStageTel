package com.example.backstagetel.Controlleurs;


import com.example.backstagetel.Entities.Avis;
import com.example.backstagetel.Services.AvisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestion Avis")
@RestController
@AllArgsConstructor
@RequestMapping("/avis")
@CrossOrigin(origins = "http://localhost:4200")
public class AvisController {

    AvisService avisService;


    // Add a new Avis
    @PostMapping("/add")
    public ResponseEntity<?> addAvis(@RequestBody Avis avis) {
        try {
            Avis newAvis = avisService.addAvis(avis);
            return ResponseEntity.ok(newAvis);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add Avis: " + e.getMessage());
        }
    }

    // Retrieve all Avis
    @GetMapping("/all")
    public ResponseEntity<?> getAllAvis() {
        try {
            List<Avis> avisList = avisService.retrieveAvis();
            return ResponseEntity.ok(avisList);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve Avis list: " + e.getMessage());
        }
    }
}
