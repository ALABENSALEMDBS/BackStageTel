package com.example.backstagetel.Controlleurs;

import com.example.backstagetel.DTO.*;
import com.example.backstagetel.Entities.Utilisateur;
import com.example.backstagetel.Services.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Gestion Utilisateur")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")

public class UtilisateurController {
    UtilisateurService utilisateurService;

    @Operation(description = "registration utilisateur")
    @PostMapping("/auth/register")
    public Utilisateur registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        return utilisateurService.registerUser(registrationRequest);
    }


    @PostMapping("/auth/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {
        return utilisateurService.loginUser(loginRequest);
    }

    @GetMapping("/getAllUsers")
    public List<Utilisateur> getAllUsers() {
        return utilisateurService.getAllUsers();
    }

    @GetMapping("/admin-only")
    public String adminOnlyRoute() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return String.format("Hello %s! This route is accessible by ADMIN only.", username);
    }





    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        try {
            String username = authentication.getName(); // récupéré depuis le token
            String result = utilisateurService.changePasswordFromToken(username, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();
        try {
            utilisateurService.sendResetCode(email);
            return ResponseEntity.ok("Code envoyé par email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            utilisateurService.resetPassword(
                    request.getEmail(),
                    request.getCode(),
                    request.getNewPassword()
            );
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PutMapping("/update-photo/{id}")
    public ResponseEntity<ChangePhoto> updatePhoto( @PathVariable int id, @RequestBody ChangePhoto changePhoto) {
        ChangePhoto updatedUser = utilisateurService.updatephoto(id, changePhoto);
        return ResponseEntity.ok(updatedUser);
    }





    private final String uploadDir = "C:/xampp/htdocs/document/";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        // Check if the file is null or empty
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "No file was uploaded"), HttpStatus.BAD_REQUEST);
        }

        try {

            String fileName = file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir, fileName); // Corrected path

            // Create the upload directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory including any necessary parent directories.
            }

            // Save the file
            Files.write(filePath, file.getBytes());

            // Create the file URL (IMPORTANT: Adjust this based on your webserver setup)
            String fileUrl = "http://localhost/document/" + fileName;  // IMPORTANT

            // Create a success response
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            // Handle the exception (log it, etc.)
            System.err.println("Error uploading file: " + e.getMessage());  // Log the full error message
            e.printStackTrace();  // VERY IMPORTANT:  Log the stack trace for debugging
            return new ResponseEntity<>(Map.of("error", "Failed to upload file: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
