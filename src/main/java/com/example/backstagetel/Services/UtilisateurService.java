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

@Service
@AllArgsConstructor
public class UtilisateurService implements UserDetailsService,IUtilisateurService {

    JwtUtils jwtUtils;
    UtilisateurRepository utilisateurRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String nomUser) throws UsernameNotFoundException {
        return utilisateurRepository.findByNomUser(nomUser)
                .orElseThrow(() -> new UsernameNotFoundException(nomUser));
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
}
