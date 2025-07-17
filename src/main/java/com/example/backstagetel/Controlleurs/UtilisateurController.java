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

import java.util.List;

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

}
