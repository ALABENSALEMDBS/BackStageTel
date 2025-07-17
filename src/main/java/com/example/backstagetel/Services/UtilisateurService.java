package com.example.backstagetel.Services;

import com.example.backstagetel.Configurations.JwtUtils;
import com.example.backstagetel.DTO.LoginRequest;
import com.example.backstagetel.DTO.LoginResponse;
import com.example.backstagetel.DTO.UserRegistrationRequest;
import com.example.backstagetel.Entities.EtatCompte;
import com.example.backstagetel.Entities.Role;
import com.example.backstagetel.Entities.Utilisateur;
import com.example.backstagetel.Repositories.RoleRepository;
import com.example.backstagetel.Repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UtilisateurService implements UserDetailsService,IUtilisateurService {

    JwtUtils jwtUtils;
    UtilisateurRepository utilisateurRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    private EmailService emailService;

    // Déclare resetCodes pour stocker les codes temporairement
    private final Map<String, CodeInfo> resetCodes = new ConcurrentHashMap<>();

    // record CodeInfo pour contenir code + date d'expiration
    private record CodeInfo(String code, long expirationTime) {}


    public UserDetails loadUserByUsername(String emailuser) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmailUser(emailuser)
                .orElseThrow(() -> new UsernameNotFoundException(emailuser));
    }

    public Utilisateur registerUser(UserRegistrationRequest registrationRequest) {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setEmailUser(registrationRequest.getEmailUser());
        utilisateur.setPasswordUser(passwordEncoder.encode(registrationRequest.getPasswordUser()));
        utilisateur.setNomUser(registrationRequest.getNomUser());
        utilisateur.setPrenomUser(registrationRequest.getPrenomUser());
        utilisateur.setNumeroLigne(registrationRequest.getNumeroLigne());
//        utilisateur.setPhotoUser(registrationRequest.getPhotoUser());
        if (registrationRequest.getPhotoUser() != null && !registrationRequest.getPhotoUser().isEmpty()) {
            utilisateur.setPhotoUser(registrationRequest.getPhotoUser());
        }
        if (registrationRequest.getDocumentContrat() != null && !registrationRequest.getDocumentContrat().isEmpty()) {
            utilisateur.setDocumentContrat(registrationRequest.getDocumentContrat());
        }
        Role roleClient = roleRepository.findByNomRole("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalArgumentException("Role 'CLIENT' not found"));
        utilisateur.setRole(roleClient);

        utilisateur.setCreatedAt(new Date());
        utilisateur.setEtatCompte(EtatCompte.ACTIF);
        return utilisateurRepository.save(utilisateur);

    }


    public LoginResponse loginUser(LoginRequest loginRequest) {
        Utilisateur user = utilisateurRepository.findByEmailUser(loginRequest.getEmailUser())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

        if (!passwordEncoder.matches(loginRequest.getPasswordUser(), user.getPasswordUser())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        String token = jwtUtils.generateToken(user);

        return new LoginResponse(token,user.getRole().getNomRole());
    }





    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }




    public String changePasswordFromToken(String username, String oldPassword, String newPassword) {
        Utilisateur utilisateur = utilisateurRepository.findByEmailUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(oldPassword, utilisateur.getPasswordUser())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        utilisateur.setPasswordUser(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(utilisateur);
        return "Mot de passe mis à jour avec succès";
    }




    public void sendResetCode(String email) {
        Utilisateur user = utilisateurRepository.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Email introuvable"));

        String code = String.format("%06d", new Random().nextInt(999999));
        long expirationTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes

        resetCodes.put(email, new CodeInfo(code, expirationTime));

        // ✅ Envoyer le mail réellement
        String subject = "Code de réinitialisation de mot de passe";
        String message = "Bonjour,\n\nVoici votre code de réinitialisation : " + code + "\nIl est valable 5 minutes.";
        emailService.send(email, subject, message);
    }

}
