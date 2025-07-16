package com.example.backstagetel.Controlleurs;

import com.example.backstagetel.DTO.LoginRequest;
import com.example.backstagetel.DTO.LoginResponse;
import com.example.backstagetel.DTO.UserRegistrationRequest;
import com.example.backstagetel.Entities.Utilisateur;
import com.example.backstagetel.Services.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
}
