package com.example.backstagetel.Configurations;

import com.example.backstagetel.Entities.EtatCompte;
import com.example.backstagetel.Entities.Role;
import com.example.backstagetel.Entities.Utilisateur;
import com.example.backstagetel.Repositories.RoleRepository;
import com.example.backstagetel.Repositories.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {


        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    new Role("ROLE_ADMIN","L’administrateur de l’application est responsable d’activer ou désactiver les comptes utilisateurs, et gérer leurs informations. Il dispose également d’un tableau de bord pour consulter les statistiques, suivre la performance du service"),
                    new Role("ROLE_AGENT","Agent de support est responsable du traitement des réclamations. Il peut répondre aux demandes de renseignement"),
                    new Role("ROLE_CLIENT", "L'utilisateur simple de l'application est un client de Tunisie Telecom qui dépose une réclamation, suit l'état de la réclamation et demande également des informations.")
            ));
        }

        if (utilisateurRepository.count() == 0) {
            Role adminRole = roleRepository.findByNomRole("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Role admin not found"));
            Role agentRole = roleRepository.findByNomRole("ROLE_AGENT").orElseThrow(() -> new RuntimeException("Role agent not found"));

            Utilisateur admin = new Utilisateur();
            Utilisateur agent = new Utilisateur();

            admin.setNomUser("Admin");
            admin.setPrenomUser("BenAdmin");
            admin.setPasswordUser(passwordEncoder.encode("admin123"));
            admin.setPhotoUser("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTul6F3Ak5Mb-qnsUamAvEsxpoxEmuQx6KjEx2HE6oDzVeU5KfZuXlrNoHO_f-55f9ceCs&usqp=CAU");
            admin.setEmailUser("alabensalem.iset@gmail.com");
            admin.setCreatedAt(new Date());
            admin.setRole(adminRole);
            admin.setEtatCompte(EtatCompte.ACTIF);

            agent.setNomUser("Agent");
            agent.setPrenomUser("BenAgent");
            agent.setPasswordUser(passwordEncoder.encode("agent123"));
            agent.setPhotoUser("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTul6F3Ak5Mb-qnsUamAvEsxpoxEmuQx6KjEx2HE6oDzVeU5KfZuXlrNoHO_f-55f9ceCs&usqp=CAU");
            agent.setEmailUser("ala.bensalem144@gmail.com");
            agent.setCreatedAt(new Date());
            agent.setRole(agentRole);
            agent.setEtatCompte(EtatCompte.ACTIF);

            utilisateurRepository.save(admin);
            utilisateurRepository.save(agent);
        }

    }
}
